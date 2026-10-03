# Redis Advance

`更新时间：2026-10-03`

注释解释：

- `<>`必填项，必须在当前位置填写相应数据

- `{}`必选项，必须在当前位置选择一个给出的选项

- `[]`可选项，可以选择填写或忽略

*注：该笔记内的可选项和参数均不完整，如有需要，请查询相关手册*

---

## 分布式缓存

在[Redis](./Redis.md)基础篇中我们提到的Redis缓存都是单节点缓存，而由于Redis是内存存储，服务重启或者宕机时很有可能导致数据丢失；虽然Redis以高性能著称，但是单节点的性能仍受限于服务器本身，如果服务器本身性能较差，并发请求又非常高，很容易出现服务崩溃；同时，如果单节点Redis出现故障，也无法自主恢复，这不仅仅会造成Redis本身数据丢失，还会造成一系列的相关业务异常；以及内存存储虽然高性能，但是存储容量与硬盘基本不在一个数量级，单节点Redis很容易出现内存不足，需要频繁清除Key的情况

### Redis持久化

#### RDB持久化

RDB全称Redis Database Backup file，Redis数据备份文件，或者叫做Redis数据快照。简单来说就是将内存中所有的数据都写入到磁盘中，当Redis实例故障重启后，就可以从硬盘中读取快照文件，恢复数据。RDB文件默认保存在Redis运行目录，通过客户端的SAVE命令可以调起一次备份

> ![](img4/26.png)

不过SAVE命令是由Redis主进程来执行的，会阻塞当前所有命令，如果Key比较多的情况下会消耗一定时间，所以在实际开发中应当选择另一个异步备份命令BGSAVE，BGSAVE会启动一个子进程来执行SAVE，不会阻塞主进程命令执行

Redis在运行时和正常终止时也会自动执行SAVE

> ![](img4/27.png)

在Redis.conf配置文件中，可以针对RDP文件进行一些配置，如下

```conf
# 在指定时间t内，如果至少有k个Key被修改，则执行一次bgsave
save 900 1
# save 300 10
# save 60 10000

# 是否压缩RDB，一般不开启，压缩会额外占用CPU资源
rdbcompression yes

# RDB文件名
dbfilename dump.rdb

# Redis数据文件目录
dir ./
```

RDB本身不能完全解决服务突然宕机的问题，如果将BGSAVE的时间设置过长，在保存时间窗口内的数据就会丢失，如果设置过短，如果Redis数据量很大，就会占用大量的CPU资源来执行备份，降低服务性能

##### RDB fork原理

fork是指，当Redis执行BGSAVE时，需要从主进程中调起一个子进程，这个过程就是fork。fork过程中需要主进程向子进程同步内存数据，所以其实需要主进程阻塞，在fork完成后，子进程才将内存中的数据写入硬盘

不难发现，fork的过程需要尽快完成，因为需要尽最大可能避免主进程阻塞。如果直接将内存数据拷贝一份，就会额外占用一倍的物理内存，对于服务本身来说是完全没有必要的，因此Redis设计了一个特别的拷贝方式。在主流操作系统中，系统会为每个进程划分一块属于进程自己的虚拟内存空间，每个进程的虚拟内存相互独立，而操作系统则维护一份虚拟内存空间到物理内存的映射表，这个表就是页表。在Redis进行fork时，直接将页表拷贝给了子进程，对于子进程来说，页表中映射的地址与主进程完全相同，因此其中存储的属于也就是同一份，直接实现了内存共享

不过还需要考虑一个问题，如果子进程在读取内存中数据时，主进程需要写入新的数据呢？为了避免同时读写冲突，Redis采用了copy-on-write模式，当主进程读数据时，直接读取共享数据，而当主进程写数据时，拷贝一份需要写的数据，然后基于拷贝执行操作，后续的读操作也基于这个拷贝。共享数据永远属于只读模式，防止数据被更改

在极端情况下，如果主进程需要同时写入当前共享数据中的所有数据，那么就需要将所有数据全部拷贝一份，造成额外一倍的内存空间占用，因此RDB无法从根本上解决数据持久化问题，还需要其他技术进行弥补

#### AOF 持久化

AOF全称为Append Only File追加文件，Redis会将每一个写命令记录在AOF文件中，也就相当于命令日志文件，当需要数据恢复时，只需要将AOF文件中的所有命令执行一次即可

AOF默认是关闭的，需要通过Redis.conf配置文件打开，同时AOF记录命令的频率也可以进行配置

```conf
# 开启AOF功能，默认为no
appendonly yes
# AOF文件名
appendfilename "appendonly.aof"

# 每次执行命令，立即写入到AOF文件
appendfsync always
# 写入命令先存入内存缓冲区，然后每秒将缓冲区数据写入硬盘
appendfsync everysec
# 写入命令先存入内存缓冲区，然后由操作系统自己决定如何写入硬盘
appendfsync no
```

| 写入频率 | 刷盘时机     | 优点                       | 缺点                                 |
| -------- | ------------ | -------------------------- | ------------------------------------ |
| always   | 同步         | 高可靠性，几乎不会丢失数据 | 性能影响大，每次写入需要额外的硬盘IO |
| everysec | 每秒         | 性能较好                   | 理论上会丢失一秒内的数据             |
| no       | 操作系统控制 | 性能最佳                   | 可靠性极低，甚至不如RDB              |

*注：AOF默认采用everysec频率*

AOF文件格式如下

```aof
*2
$6
SELECT
$1
0
*3
$3
SET
$3
num
$3
123
```

上文的AOF文件执行了两条命令

```redis
SELECT 0
SET num 123
```

我们以此来解析AOF文件结构，每条命令通过`*`来指定命令参数个数，第一行的`*2`表示第一条命令有两个参数，第一个参数是命令，第二个参数是命令参数；然后是`$`标识参数字符长度，所以第二行`$6`表示命令长度为6，第三行即是命令本身，第四行是第二个参数，长度为1，第五行为参数本身，以此类推

观察下面的AOF文件

```aof
*3
$3
SET
$3
num
$3
123
*3
$3
SET
$3
num
$3
456
*3
$3
SET
$3
num
$3
789
```

这次我们对num进行了三次写操作，所以AOF也对三次操作分别进行了记录，但是从最终结果来看，只有最后一次写操作是有效。相比RDB，AOF文件的体积会大得多，因为会包含大量的重复Key写操作，在进行数据备份时，这些重复的写操作也会占用额外的资源

因此Redis设计了BGREWRITEAOF命令，可以让AOF文件执行重写功能，用最少的命令达到最终数据一致。而且命令重写不仅针对重复Key，对于相同类型的写操作，Redis会自动重写为批量写入命令，例如

```redis
SET name jack
SET num 123
```

执行BGREWRITEAOF，重写为

```redis
MSET name jack num 123
```

不过重写后的文件内容格式就不是原本可读的AOF格式，而是经过压缩编码的格式

> ![](img4/28.png)

除了手动执行BGREWRITEAOF命令，Redis也会在一定条件下自动重写，触发条件可以由Redis.conf进行配置

```conf
# AOF文件最大增长率，超过该比例将触发重写。默认为100%，即超过一倍大小将触发重写
auto-aof-rewrite-percentage 100
# AOF文件最大体积，超过该体积则触发重写
auto-aof-rewrite-min-size 64mb
```

**综合对比**

| 持久化方案   | RDB                                | AOF                                                |
| ------------ | ---------------------------------- | -------------------------------------------------- |
| 实现方式     | 定时对内存做快照                   | 记录每次执行的写命令                               |
| 数据完整性   | 不完整，两次备份之间的数据会丢失   | 相对完整，完整性取决于刷盘策略                     |
| 文件大小     | 会自动压缩，文件体积较小           | 相对较大，可以采用命令重写                         |
| 宕机恢复速度 | 很快，直接覆盖数据文件即可         | 慢，恢复速度取决于命令长度                         |
| 系统资源占用 | 高，需要大量CPU和内存执行备份      | 低，主要是硬盘IO资源，不过在重写时需要消耗大量资源 |
| 使用场景     | 数据完整性需求较低，追求更高的性能 | 数据完整性与安全性要求较高                         |

### Redis集群

单节点Redis的并发能力是有上限的，因此要进一步提高Redis的并发能力，就需要搭建主从集群，实现读写分离。而主从集群是指，一台Web服务器连接Redis时，可以提供由多个Redis服务器组成的集群，而在这个集群中每个Redis服务器的角色是不同的， 分为master和slave/replica，master一般负责写，布置较少，slave/replic负责读，布置较多，这样可以大大提升Redis的读性能

而为了保证数据一致，需要master节点将数据同步给slave节点，以保证在任何slave节点都能够读取到正确的数据

#### 建立主从集群

建立Redis主从集群有两种方式，第一种是在Redis命令台中输入命令来配置主从关系，这种方式是临时生效的

```bash
# Redis 5.x之前
slaveof <masterIp> <masterPort>
# Redis 5.x之后
replicaof <masterIp> <masterPort>
```

*注：slaveof在5.x之后同样可用，master节点不需要命令，默认所有节点均为master*

第二种则是在配置文件redis.conf中使用slaveof指定master节点，注册为其slave节点，这种方式是永久生效的

```conf
slaveof <masterIp> <masterPort>
```

*注：一般不采用配置方式，因为主从关系很可能会变动*

在启用Redis后，可以通过命令info replication来查看当前服务的主从关系

> ![](javaweb2/187.png)

现在我们准备三个Redis服务，端口分别为7001、7002、7003，7001为master，其余的为slave。需要注意的是，不要使用windows的Redis5.x版本，windows的Redis5.x版本存在已知问题，主从集群无法正确连接

> ![](javaweb2/189.png)

在7002中输入

```bash
SLAVEOF localhost 7001
```

然后检查关系状态，确认role为slave，并且master_link_status为up，保证主从连接成功

> ![](javaweb2/190.png)

7003执行同样的步骤

> ![](javaweb2/191.png)

登录7001，查看7001的slave状态

> ![](javaweb2/192.png)

Redis主从集群默认是同步的，而且仅有master节点拥有写权限。我们先尝试在7002中写数据

> ![](javaweb2/193.png)

提示仅有读权限，然后我们在7001中写入一个num，再使用7002读取

> ![](javaweb2/194.png)
>
> ![](javaweb2/195.png)

#### 主从同步原理

当主从第一次同步连接，或者由于某些原因断开重连时，从节点都会发送一个psync请求，尝试数据同步。主节点在接收到psync请求后，先判断从节点是否为第一次同步，如果是第一次同步，则向从节点发送自己的全部数据，这种同步被称为全量同步；如果从节点不是第一次同步，而是断开重连，则主节点没必要进行全量同步，而是将从节点缺少的数据发送给从节点，这种同步被称为增量同步。在连接保持时，每当主节点写数据，都会将命令发送给所有从节点，保持实时同步

> ![](javaweb2/196.png)

##### 首次连接判断

在建立主从集群之前，每个Redis服务都有一个属于自己的replicationID，简称replid，并且每个服务的id都不同

> ![](javaweb2/197.png)

而在主从集群建立之后，master节点会重新生成一个replid，并将自己的id分享给所有的slave节点，简单来说，主从集群中所有的redis服务共用一个replid

> ![](javaweb2/198.png)
>
> ![](javaweb2/199.png)

而判断是否为第一次同步就是依据replid，如果replid与当前主从集群的id一致，则说明该节点先前与master有过同步记录，因此下一步进行增量同步；而如果replid与当前主从集群的id不一致，则说明是第一次同步，需要全量同步

##### 同步机制

###### 全量同步

上文提到，RDB文件会保存Redis中所有的数据，因此全量同步就是基于RDB实现的。在从节点第一次连接后，主节点触发一次BGSAVE，生成一个RDB文件，在生成完成后，将RDB文件发送给从节点，从而完成同步。不过RDB文件并不包含主节点所有的数据，因为BGSAVE是由子进程进行的，在文件生成途中可能还有写操作，因此主节点会将新的写操作保存在内存缓冲区repl_baklog中，在RDB文件生成完成后，将repl_baklog一并发送给从节点，这样才实现全量同步

> ![](javaweb2/200.png)

###### 增量同步

假设从节点因为网络故障异常断开，网络恢复后又重新连接，这时就需要进行增量同步。但检查数据增量是非常麻烦的，所以Redis在进行增量同步时并不是发送数据增量，而是从节点缺失的命令增量。这个命令增量就是repl_baklog，repl_baklog的创建时机是第一个从节点连接成功的时刻立即创建，然后开始记录命令，这样当任意从节点在任意时刻断开连接，主节点都能拥有完整的命令记录。而增量同步的核心思想是利用一个offset偏移值，offset记录了repl_baklog文件的长度，保持连接时，主节点与从节点的offset保持一致，而断开连接后，主节点offset继续增大，从节点offset保持不变。一旦从节点重新连接，只要获取了从节点的offset值，就可以得知从节点是何时断开连接，并且也能得知从节点缺少哪些命令，然后主节点再将缺失的命令发送给从节点

> ![](javaweb2/201.png)
>
> ![](javaweb2/202.png)

##### 同步优化

上文我们提到，增量同步的重点在于repl_baklog文件记录主节点命令，但是当主节点命令越来越多，repl_baklog文件也越来越大，repl_baklog默认位于内存中，如果容量太大，会直接影响Redis的运行安全，因此Redis针对repl_baklog文件的大小进行了限制，默认最大仅为1Mib。而一旦对repl_baklog文件容量进行限制，就又会出现新的问题，假设repl_baklog文件被写满，又该如何记录命令？这里Redis为repl_baklog设计了一个特殊的数据结构，repl_baklog是一个环形数组，当数组填满时，新的数据会从0开始覆盖旧的数据，避免repl_baklog被写满。

但这又会引出新的问题，假设环形数据的下标最大值为1000，目前主从节点的offset位于127，此时某个从节点因为网络原因断开连接，而主节点继续执行命令，由于命令较多，repl_baklog文件被写满，只能从0下标开始覆盖，直到超过127。一段时间后，网络恢复，从节点重新连接主节点，但此时从节点的offset已经被新的值覆盖，增量同步完全失效，所以就只能进行全量同步。

而全量同步应当尽量避免，因为dump.rdb文件在长期使用后可能达到Gib级别，从内存写入磁盘，再从磁盘由网络发送到从节点会非常消耗主节点服务器资源，导致严重的性能问题

针对以上问题，我们可以实施下列的几种优化方案：

- 在master中配置repl-diskless-sync yes启用无磁盘复制，在全量同步时直接从内存中传输数据，避免磁盘IO
- 设置Redis单节点内存占用限制，减少rdb文件导致的过多磁盘IO
- 适当提高repl_baklog文件大小，尽快修复从节点故障，避免从节点offset被覆盖，从而避免全量同步
- 限制一个master上的从节点数量，或是采用主从从链式结构，减少master压力

#### 哨兵

上文我们提到的问题都是基于从节点故障的情况，但如果是主节点故障，就会产生非常严重的影响。因此Redis提供了哨兵机制来实现主从集群的自动故障恢复。

哨兵是独立于Redis主从集群之外的服务，并且为了避免哨兵本身存在异常，通常会布置多个哨兵服务形成哨兵集群。哨兵的主要工作内容是监控主从集群中各个节点的运行情况，如果此时主节点发生异常断开连接，哨兵也能将某个从节点提升为新主节点，从而保持业务的正常运行。当原来的主节点恢复后，将会被降级为从节点。当主从集群发生故障时，哨兵也需要将新的主节点与从节点的信息推送到Redis客户端

##### 服务监控

哨兵的服务状态监控基于心跳机制，即每隔1秒向集群的所有节点发送一个PING命令，Redis默认在接收到PING命令后会返回一个PONG

> ![](javaweb2/203.png)

如果某个哨兵发现某个节点在规定时间内未响应PONG，则该实例对于哨兵主观下线。当超过指定数量 quorum 的哨兵都认为某节点主观下线时，则认为该实例客观下线，从而认定服务失效。quorum的值应当超过哨兵数量的一半

##### master选举

一旦master故障，哨兵就需要在slave中重新挑选一个新的master，选择依据如下：

- 判断slave节点与master节点断开时间长短，如果超过指定值，则会排除该slave节点。这个值由 down-after-millisecondes \* 10来得到
- 判断slave节点的slave-priority值，也就是优先级，值越小优先级越高，如果值为0则表示永不参与选举。默认值为1
- 判断slave节点的offset值，越大说明数值越新，优先级越高
- 判断slave节点的运行id，id越小优先级越高。但实际上运行id是随机生成的，也就等同于随机选举

这四条选举规则是顺序进行的，只有先满足上面的，再对下一条进行匹配

当新的master选举成功后，哨兵会向其发送一条SLAVEOF NO ONE命令，让该节点提升为master，然后向其他所有节点发送SLAVEOF \<NEW MASTER HOST\> \<NEW PORT\>命令，让其余的所有节点成为新master的slave节点，开始从新的master节点中同步数据。而旧master会被标记为slave，当节点修复后，自动成为新的master的slave节点

##### 哨兵集群

###### 搭建哨兵集群

首先需要关闭当前的主从集群

> ![](javaweb2/204.png)

然后编写sentinel.conf配置文件，这里提供了一份最小可用文件

在哨兵的配置文件中，只需要指定master节点的ip以及端口即可，从节点的相关数据能够通过主节点获取

```conf
port 27001
daemonize yes
pidfile "/home/kiiz/redis/sentinel/s1.pid"
logfile "/home/kiiz/redis/sentinel/s1.log"
dir "/home/kiiz/redis/sentinel/data/s1"

# 监控主节点 IP 端口 以及法定票数(quorum)
sentinel monitor mymaster 127.0.0.1 7001 2

# 判定主节点下线的时间阈值（毫秒）
sentinel down-after-milliseconds mymaster 5000

# 故障转移超时时间
sentinel failover-timeout mymaster 60000
```

然后复制多个配置文件，修改端口以及pidfile、logfile和dir位置，启动哨兵集群

哨兵可以通过redis-server和redis-sentinel两种方式来启动，如果通过redis-server，则需要添加参数--mode=sentinel，而redis-sentinel则需要单独安装

> ![](javaweb2/205.png)

通过redis-cli连接其中一个哨兵，输入命令INFO SENTINEL来查看哨兵集群情况

> ![](javaweb2/206.png)

###### 哨兵集群工作流程

现在我们停止master，模拟主节点宕机

> ![](javaweb2/207.png)

在master断开后，s2首先检测到，然后发送了一条`+sdown master mymaster 127.0.0.1 7001`主观下线通知，而后s1检测到，也发送了一条+sdown主观下线通知，最后是s3

> ![](javaweb2/208.png)

我们设置的quorum为2，因此在s1中，当quorum满足2/2时，发送了一条`+odown master mymaster 127.0.0.1 7001 #quorum 2/2`客观下线通知

> ![](javaweb2/209.png)

然后哨兵集群协商故障转移主导，也就是`+vote-for-leader`，一般来说，谁最先发送+odown通知，谁主导故障转移，因此s1为自己投了一票

> ![](javaweb2/211.png)

然后s2和s3也都为s1投票

> ![](javaweb2/212.png)

现在确认由s1主导，因此s1发布了`+elected-leader`担任通知

> ![](javaweb2/213.png)

然后s1开始进行故障转移，首先选举新的master，这里选中了7002

> ![](javaweb2/214.png)

选中7002后，s1下发SLAVEOF ON ONE命令，将7002提升为master

> ![](javaweb2/215.png)

新的master被选举后，s1向其他节点发送通知，告知新的master，以及旧的master被降级

> ![](javaweb2/216.png)

故障转移后，s1随即下发-odown，宣布事实下线事件结束，同时切换master，然后宣布子节点主观下线

> ![](javaweb2/217.png)

此时我们再重启旧master

> ![](javaweb2/218.png)

s1检测到7001上线后，关闭slave sdown主观下线，并将其降级为slave

> ![](javaweb2/219.png)

最后我们通过redis-cli来检查7001是否被降级

> ![](javaweb2/220.png)

### Redis分片集群

主从和哨兵集群可以解决高可用，高并发读的问题，但是依然无法解决海量数据存储以及高并发写的问题。而分片集群则可以解决这些问题，分片集群的特征是集群中有多个master，每个master保存不同的数据，每个master都可以有多个slave节点，而master之间都可以进行类似sentinel的健康状态检测，当检测到某一个master不可用时，也能够从slave中选举出新的master

#### 搭建分片集群

我们部署一个简单的分片集群，准备三个master，每个master只拥有一个slave。首先创建对应的配置项，分片集群的配置项中必须添加cluster相关的配置，以下是一个最小可用配置

```conf
# ========== 网络绑定 ==========
bind 127.0.0.1
port 7001
protected-mode yes

# ========== 后台运行 ==========
daemonize yes
pidfile /home/kiiz/redis/data/r1/r1.pid

# ========== 日志 ==========
loglevel notice
logfile /home/kiiz/redis/data/r1/r1.log

# ========== 持久化（最小 RDB） ==========
save 900 1
save 300 10
save 60 10000
dir /home/kiiz/redis/data/r1/
dbfilename dump.rdb

# ========== 内存控制（推荐） ==========
maxmemory 256mb
maxmemory-policy allkeys-lru

# ========== 关闭 AOF ==========
appendonly no

# ========== Cluster 必须项 ==========
cluster-enabled yes
cluster-config-file nodes.conf
cluster-node-timeout 5000
```

然后依据这个模板，我们创建r2-r6，同时注意data中应包含r1-r6的目录用于存放数据文件

> ![](javaweb2/221.png)

接着启动所有的redis-server，同时创建一个集群，设置三主三从，然后将所有redis-server添加到集群中。这里集群会自动选择主从归属，不需要我们手动指定，默认情况下，会先分配master，再分配slave

```bash
#!/bin/bash

BASE_DIR="/home/kiiz/redis"

# ========== 1. 启动所有 Redis 实例 ==========
echo "🚀 启动 Redis 实例..."

for i in {1..6}; do
  CONF="${BASE_DIR}/r${i}.conf"
  if [ -f "${CONF}" ]; then
    redis-server "${CONF}"
    echo "✅ r${i} 启动成功 (port 700${i})"
  else
    echo "❌ 配置文件不存在: ${CONF}"
    exit 1
  fi
done

# ========== 2. 等待节点就绪 ==========
echo "⏳ 等待 Redis 节点就绪..."
sleep 2

# ========== 3. 创建集群 ==========
echo "🔧 创建 Redis Cluster (replicas = 1)..."

yes yes | redis-cli --cluster create \
  127.0.0.1:7001 \
  127.0.0.1:7002 \
  127.0.0.1:7003 \
  127.0.0.1:7004 \
  127.0.0.1:7005 \
  127.0.0.1:7006 \
  --cluster-replicas 1

echo "✅ Redis Cluster 创建完成"
```

> ![](javaweb2/222.png)

同时也能观察到集群自动选择了master及其对应的slave，如预期所料，r1-r3被选为了master，而r4-r6为slave

> ![](javaweb2/223.png)

然后我们访问r1，从r1处获取集群信息

```bash
kiiz@DESKTOP-T1OASAN:~/redis$ redis-cli -p 7001 cluster nodes
f8107bf464596357b4d31f530db348f6e8bb53b7 127.0.0.1:7006@17006 slave 3be845e6d4dca199306fd97d652a25e9913bbcb3 0 1784105425000 2 connected
ae8dfd55d01a6d55d161f42240b378fb2b4cd34c 127.0.0.1:7001@17001 myself,master - 0 1784105426000 1 connected 0-5460
4253777f9b579523d4353fd9209f2f5b7a84362f 127.0.0.1:7004@17004 slave a25f081ad45b897ea32c4665d00aca7e0232fa7f 0 1784105425529 3 connected
a25f081ad45b897ea32c4665d00aca7e0232fa7f 127.0.0.1:7003@17003 master - 0 1784105425027 3 connected 10923-16383
3be845e6d4dca199306fd97d652a25e9913bbcb3 127.0.0.1:7002@17002 master - 0 1784105426000 2 connected 5461-10922
2078a7cb3ea1c5e95812d86056cddce5f449af6b 127.0.0.1:7005@17005 slave ae8dfd55d01a6d55d161f42240b378fb2b4cd34c 0 1784105426330 1 connected
```

从左往右看，第一列为NodeID，节点唯一标识，用于标识集群中的节点；第二列是节点地址，127.0.0.1:7006是节点所在网络地址，@17006是集群总线端口，这个端口由port + 10000得来，用于节点间gossip通信，因此防火墙必须放行该端口；第三列是集群角色，一般为master和slave，当前节点用myself标识；第四列是主节点NodeID，仅有slave节点才有这个属性，因此所有的master节点第四列为-；第五列为Redis内部的flag位编码，0标识正常，非零值标识可能存在某些异常；第六列为最近一次集群状态变更时间戳，单位毫秒；第七列为集群逻辑时钟，这里暂不做介绍；第八列为链路状态，connected标识正常连接，其余状态还有disconnected不可达，fail?疑似下线，fail确认下线；master节点还有第九行slot分配范围，这里也暂不做介绍

#### 散列插槽

master节点的第九行的slot全称为Hash Slot散列插槽，Redis中共有16384个插槽，在分片集群中，会将所有的插槽分配给集群中的每一个master节点。Redis数据并不与节点绑定，而是与插槽Slot绑定。当需要读写数据时，Redis基于CRC16算法对key进行hash运算，得到的结果与16384模运算，从而得到这个key的slot值，然后到插槽对应的Redis节点进行读写操作

Redis的Hash运算相较常规运算有些不同，当key中包含大括号`{}`时，Redis会取大括号中的字符串来计算hash slot，如果不包含大括号，则会按照原始字符串来进行计算。大括号的一个重要作用是将数据存储在同一个节点上

**示例**

假设我们需要存储用户ocean的信息，先连接7001，然后执行命令SET user ocean

> ![](javaweb2/224.png)

这时却出现了报错，因为定义的key为user，而user在进行slot运算后得到的结果为5474，插槽5474位于7002上，因此Redis不允许我们在7001上插入数据。这里需要在redis-cli后添加一个-c参数，启用cluster模式，这样redis-cli就可以帮我们自动跳转

> ![](javaweb2/225.png)

然后我们再设置用户年龄22，执行命令SET age 22，结果又跳转回了7001

> ![](javaweb2/226.png)

如果此时想要获取用户名GET user，又得跳转到7002，这样就显得非常麻烦，因此可以使用大括号来为key添加一个前缀，例如{user}:name设置用户名，{user}:age设置用户年龄，以保证用户信息存储在一个节点中

> ![](javaweb2/227.png)

#### 故障转移

Redis分片集群不需要哨兵集群，自己就能完成整个故障转移过程。从逻辑上来看，分片集群每个主节点其实就可以担任一个哨兵职位，当发现某一个主节点下线时，其他的主节点位于同一个集群中，就可以立即发现下线节点，然后通过故障转移步骤选举出新的主节点，保证集群的高可用性

##### 数据迁移

Redis分片集群也支持手动故障转移，这又被称为数据迁移，因为这是可控的。数据迁移利用CLUSTER FAILOVER命令，让节点中的某个master节点宕机，然后Redis分片集群会自动将其slave节点提升为master，并完成数据迁移，大致步骤如下

> ![](img4/29.png)

数据迁移有三种模式

- 缺省：默认流程，即上图中的六个步骤
- force：忽略对offset的校验
- takeover：直接将自己标记为master，忽略数据一致性，忽略master状态和其他master意见

## 多级缓存

传统缓存结构中，我们部署的缓存仅有Redis，请求到达Tomcat，先查询Redis，如果未命中则查询数据库，最后返回。而这里其实有个问题，Tomcat本身的并发能力还不如Redis，换句话说，Tomcat并不能发挥出Redis的最佳利用率。而且Redis的Key存在TTL，当缓存过期时，大量请求会直接到达数据库，对数据库造成影响

因此我们需要额外建立多级缓存，在请求的每个环节都添加对应的缓存，减轻Tomcat压力，以及防止请求直达数据库

在一般的开发中，我们会建立如下的缓存结构

> ![](img4/30.png)

首先是浏览器缓存，浏览器可以缓存静态网页资源，而在网页浏览中，几乎绝大多数的资源都是静态资源，因此浏览器缓存可以大幅提升用户体验；而动态资源，如后端请求，浏览器就无法建立缓存，需要通过NGINX反向代理获取数据。而NGINX本身其实支持编程，可以建立NGINX本地缓存，如果用户的请求在NGINX中有缓存，那么就可以直接返回前端，请求根本无法到达后端。对于NGINX本地缓存未命中，就需要在Redis中查询缓存，不过这里就不直接通过Tomcat查询Redis，而是NGINX直接查询Redis，因为NGINX支持直接编程查询Redis，而且Tomcat性能不如Redis，而NGINX性能优于Redis，以保证Web服务器不会影响缓存查询性能。如果Redis缓存未命中，则进入Tomcat后端业务，不过这里仍未直接查询数据库，而是查询Tomcat自己的进程缓存，如果进程缓存中存在对应数据，就直接返回，最后再查询数据库。总的来说，多级缓存就是尽最大可能，在请求路径上每一个可能的节点添加缓存，以避免请求到达数据库

### JVM缓存

缓存在日常开发中起着至关重要的作用，由于是存储在内存中，数据的读取速度非常快，能大量减少对数据库的访问，减少数据库的压力。按照部署位置，我们一般将缓存分为两类

- 分布式缓存：如Redis，优点是存储容量大，可靠性更好，可以在集群间共享缓存；缺点是访问缓存需要网络开销，如果网络异常，缓存直接无法使用；适用场景为缓存数据量较大，可靠性要求较高，需要在集群间共享
- 进程本地缓存：如HashMap、GuavaCache等，优点是读取本地内存，没有网络开销，读取更快；缺点也很明显，存储容量非常有限，可靠性较低，服务异常则缓存异常，无法共享；适用场景为对性能要求较高，缓存数据量较小

#### Caffeine

Caffeine是一款基于Java8开发的，提供了近乎最佳命中率的开源高性能本地缓存解决方案，目前Spring内部的缓存就是基于Caffeine

##### 快速入门

我们利用Caffeine来建立一个本地JVM缓存，实际使用非常简单

```java
@Test
public void test() {
    // 创建缓存对象
    Cache<String, String> cache = Caffeine.newBuilder().build();
    // 存储一个数据
    cache.put("name", "jack");
    // 获取数据，不存在则返回null
    String value = cache.getIfPresent("name");
    System.out.println("value = " + value);
    // 获取数据，不存在则执行数据库查询
    String value2 = cache.get("name1", k -> {
        System.out.println("从数据库查询数据");
        return "tom";
    });
    System.out.println("value2 = " + value2);
}
```

首先创建一个Caffeine缓存对象，然后向缓存对象中插入数据，查询时使用get或者getIfPresent方法尝试获取，不同的是get在获取失败后会执行一个回调方法，而getIfPresent直接返回null

> ![](img4/31.png)

#### 缓存驱逐策略

如同Redis的Key淘汰策略一样，为了减少内存开销，Caffeine也需要有缓存淘汰策略来提供更加的内存利用率，Caffeine默认提供了三种缓存驱逐策略

- 基于容量：设置缓存数量上限，超过缓存数量上限时，前一个缓存会被驱逐
- 基于时间：类似Redis的TTL，为每一个缓存设置一个有效期，有效期截止后不会马上驱逐，而是在下一次读写操作，或者在空间时间完成驱逐
- 基于引用：设置缓存为软引用或者弱引用，利用JVM的GC垃圾回收机制来回收内存，一般不使用

下面是一个基于时间过期驱逐策略的示例

```java
/* 基于时间过期的缓存 */
@Test
public void test2() throws InterruptedException {
    Cache<String, String> cache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofSeconds(2)) // 设置写入后过期时间
            .build();

    cache.put("name", "jack");
    // 立即获取缓存
    System.out.println(cache.getIfPresent("name"));
    // 等待2秒再获取缓存
    Thread.sleep(2000);
    System.out.println(cache.getIfPresent("name"));
}
```

> ![](img4/32.png)

基于容量过期的api也相似，在创建缓存对象时调用maximumSize，并传入最大缓存数量即可

#### 实现商品查询的本地进程缓存

利用Caffeine实现一个本地缓存功能，给根据id查询商品及商品库存的业务添加缓存，缓存未命中时查询数据库。缓存初始大小设置为100，缓存上限为10000

```java
package com.heima.item.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.heima.item.pojo.Item;
import com.heima.item.pojo.ItemStock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaffeineConfig {

    @Bean
    public Cache<Long, Item> itemCache() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(10_000)
                .build();
    }

    @Bean
    public Cache<Long, ItemStock> stockCache() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(10_000)
                .build();
    }
}
```

定义一个CaffeineConfig，声明两个Bean，返回两个缓存对象，一个用于缓存商品信息，一个用户缓存商品库存

```java
@Override
public Item findById(Long id) {
    return itemCache.get(id, this::getById);
}
```

然后在Service中直接调用缓存对象的get方法，回调方法设置为IService的getById即可。这里不需要手动设置缓存逻辑，Caffeine会自动对没有缓存的数据建立缓存

### NGINX本地缓存

NGINX中可以使用Lua语言来建立本地缓存。NGINX由C语言编写，所以有强大的高并发能力，而Lua语言也源自C，所以NGINX开放了对于Lua的脚本功能，使其实现一些业务功能

有关Lua教程可以参考[Lua](../Lua/Lua.md)

#### OpenResty

OpenResty是一个基于NGINX的高性能Web平台，用于方便地搭建能够处理超高并发、扩展性极强的动态Web应用、Web服务以及动态网关，其具备NGINX的完整功能，基于Lua语言进行扩展，集成了大量的Lua库，第三方模块，并且允许使用Lua自定义业务逻辑，自定义库

##### 快速入门

在原版NGINX中，进行反向代理的语法如下

```conf
location /api {
    proxy_pass http://backend;
}
```

通过location关键字，拦截路径为/api的所有url，并转发到http://backend。但是OpenResty需要通过Lua执行脚本，因此需要改用关键字content_by_lua_file，并指定一个Lua脚本，同时设定响应类型

```conf
# 代理/api/item
location /api/item {
    # 设置响应类型，即MIME类型
    default_type application/json;
    # 设置Lua脚本
    content_by_lua_file lua/item.lua;
}
```

lua脚本的返回值即是请求的响应内容，换句话说，NGINX仅仅执行了反向代理，而OpenResty是搭建了一个基于Lua的轻量级后端。不过在此之前，还需要在http块中引入依赖

```conf
# 引入lua模块
lua_package_path "<lualibPath>\?.lua;;";
# 引入c模块
lua_package_cpath "<lualibPath>\?.so;;";
```

*注：这里的\<lualibPath>表示你的lualib目录的位置*

然后就可以开始编写业务逻辑了，这里我们编写一个商品详情页的查询逻辑，先不查询真实数据，而是返回一段测试数据，验证OpenResty是否可用。在Lua中，通过ngx.say()函数返回页面响应，格式为json

```lua
ngx.say([[{
    "id": 10001,
    "name": "SALSA AIR TEST",
    "title": "RIMOWA 21寸托运箱拉杆箱 SALSA AIR TEST系列果绿色 820.70.36.4",
    "price": 99900,
    "image": "https://m.360buyimg.com/mobilecms/s720x720_jfs/t6934/364/1195375010/84676/e9f2c55f/597ece38N0ddcbc77.jpg!q70.jpg.webp",
    "category": "拉杆箱",
    "brand": "RIMOWA",
    "spec": "{\"颜色\": \"红色\", \"尺码\": \"26寸\"}",
    "status": 1,
    "createTime": "2019-04-30T16:00:00.000+00:00",
    "updateTime": "2019-04-30T16:00:00.000+00:00",
    "stock": null,
    "sold": null
}]])
```

我们将10001商品中更改几个数据，例如将价格改为999，然后访问localhost查看内容

> ![](img4/33.png)



可以看到价格更改成功，商品名也新增了TEST字样。总的来说，从JavaWeb的视角来看，OpenResty就是Controller，而Lua脚本就是Service，后面的内容就是通过Lua脚本来访问Redis或者发起远程调用访问Java后端

#### 获取请求参数

OpenResty提供了多个api来获取不同类型的请求参数，下面提供几种常用的参数类型

| 参数         | 示例         | API说明                                                      |
| ------------ | ------------ | ------------------------------------------------------------ |
| 路径占位符   | /item/1001   | 在nginx.conf中利用正则表达式匹配占位符，然后通过ngx.var数组获取 |
| 请求头       | id: 1001     | ngx.req.get_headers()获取所有请求头，返回值类型为table       |
| GET请求参数  | ?id=1001     | ngx.req.get_uri_args()获取GET类型参数，返回值类型为table     |
| POST表单参数 | id=1001      | 首先通过ngx.req.read_body()读取请求体，然后通过ngx.req.get_post_args()获取所有POST表单数据，返回值类型为table |
| JSON请求体   | {"id": 1001} | 首先通过ngx.req.read_body()读取请求体，然后通过ngx.req.get_body_data()获取请求体中的数据，返回值类型为string |

**示例**

路径占位符首先需要在nginx.conf中定义对应的反向代理，使用location \~表示路径中存在正则表达式

```conf
# 反向代理/api/item
location ~ /api/item/(\d+) {
    # 设置响应类型，即MIME类型
    default_type application/json;
    # 设置Lua脚本
    content_by_lua_file lua/item.lua;
}
```

```lua
local id = ngx.var[1]
ngx.say(id)
```

然后通过Postman测试一下

> ![](img4/34.png)

#### 通过OpenResty发起远程调用

在建立NGINX本地缓存之前，OpenResty需要拥有数据才能进行缓存，因此我们需要向Tomcat发起远程调用，获取对应的缓存数据。因此，我们需要获取请求参数中的id，根据id向Tomcat服务发送请求查询商品信息和库存信息，并组装商品信息、库存信息，序列化为JSON格式并返回前端

在OpenResty中，远程调用通过ngx.location.capture，传入两个参数，第一参数为请求路径，参数类型为string，第二个参数为请求内容，参数类型为table

```lua
local res = ngx.location.capture("/path", {
    method = ngx.HTTP_GET,
    args = {
        a = 1,
        b = 2
    },
    body = "a=1&b=2"
})
```

args负责URL传参，body负责请求体传参，两者仅能同时存在一个，返回值res包含三个属性，status响应码、header请求头，类型为table、body响应体。这里需要注意的是，请求的路径仅填写path，而不包含IP以及端口。OpenResty的请求会被自己的NGINX监听，通过NGINX反向代理到Tomcat

```conf
# 反向代理到Tomcat
location /item {
    proxy_pass http://localhost:8081;
}
```

为了方便使用，我们可以将ngx.location.capture再封装为单独的函数，配置在OpenResty的函数库中。床见openresty/lualib/common.lua文件，在common.lua中编写代码

```lua
-- get请求
local function http_get(path, params)

    if not path or path == "" then
        ngx.log(ngx.ERROR, "请求路径为空")
        ngx.exit(400)
    end

    -- 发起请求
    local res = ngx.location.capture(path, {
        method = ngx.HTTP_GET,
        args = params
    })

    -- 获取响应
    if not res then
        ngx.log(ngx.ERROR, "请求路径不存在")
        ngx.exit(404)
    end
    return res.body
end

local _M = {
    http_get = http_get
}

return _M
```

然后在业务lua中调用common库

```lua
-- 导入common.lua
local common = require("common")

-- 获取路径参数
local id = ngx.var[1]

if not id then
    ngx.say()
end

-- 查询商品信息
local item = common.http_get("/item/" .. id, nil)
local stock = common.http_get("/item/stock/" .. id, nil)
```

这里通过http_get查询到的是JSON格式的字符串，但前端要求我们返回一条JSON，而lua本身并不能直接操作JSON，因此需要使用cjson库，将JSON转换为lua的table，然后进行组装。cjson文件位于openresty/lualib/cjson.so，是一个C编写的库，通过C接口来让lua能够访问

```lua
-- 导入common.lua
local common = require("common")
-- 导入cjson
local cjson = require("cjson")

-- 获取路径参数
local id = ngx.var[1]

if not id then
    ngx.say()
end

-- 查询商品信息
local itemJSON = common.http_get("/item/" .. id, nil)
local stockJSON = common.http_get("/item/stock/" .. id, nil)
-- 转换为table
local item = cjson.decode(itemJSON)
local stock = cjson.decode(stockJSON)
-- 组装数据
item.stock = stock.stock
item.sold = stock.sold
-- 返回结果
ngx.say(cjson.encode(item))
```

> ![](img4/35.png)

#### 基于哈希进行负载均衡

如果我们部署了多台Tomcat，形成了一个集群，在轮询负载均衡模式下，对于同一件商品，可能需要所有的Tomcat同时拥有该商品缓存，用户端才能有较好的缓存体验，但对于服务器内存资源来说这就是一种浪费。所以我们可以借鉴Redis的散列插槽的模式，对每个请求参数执行一次哈希运算，相同哈希结果的请求发送到同一台服务器中，保证缓存的最佳利用率

而NGINX其实自带这个哈希负载均衡算法，只需要在upstream集群中设置hash \$request_uri即可

```conf
upstream tomcat-cluster {
	hash $request_uri;
	server http://tomcat1:8080;
	server http://tomcat2:8080;
}
```

#### 通过OpenResty查询Redis缓存

##### Redis缓存预热

在实际开发中，项目上线前，为了避免大量用户请求直接到达数据库，会通过大数据统计手段对热点数据进行缓存，这一步被称为缓存预热，在该项目中，我们也模拟一次缓存预热，因为数据量很小，所以我们缓存全部数据

在Spring中，可以通过实现InitializingBean来定义一个初始化类

```java
package com.heima.item.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisHandler implements InitializingBean {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        
    }
}
```

在afterPropertiesSet方法中完成缓存的预热工作，即查询数据库中所有商品与库存信息，然后在Redis中进行缓存

```java
package com.heima.item.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heima.item.pojo.Item;
import com.heima.item.pojo.ItemStock;
import com.heima.item.service.IItemService;
import com.heima.item.service.IItemStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RedisHandler implements InitializingBean {

    private final StringRedisTemplate redisTemplate;
    private final IItemService itemService;
    private final IItemStockService itemStockService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String ITEM_INFO_KEY_PREFIX = "item:info:";
    private static final String ITEM_STOCK_KEY_PREFIX = "item:stock:";

    @Override
    public void afterPropertiesSet() throws Exception {
        // 查询商品信息
        List<Item> itemList = itemService.list();

        if (itemList == null || itemList.isEmpty())
            return;

        Map<String, String> itemJsonMap = new HashMap<>(itemList.size());
        for (Item item : itemList) {
            Long id = item.getId();
            String json = objectMapper.writeValueAsString(item);
            itemJsonMap.put(ITEM_INFO_KEY_PREFIX + id, json);
        }
        // 存入Redis
        redisTemplate.opsForValue().multiSet(itemJsonMap);

        // 查询商品库存信息
        List<ItemStock> stockList = itemStockService.list();

        if (stockList == null || stockList.isEmpty())
            return;

        Map<String, String> itemStockJsonMap = new HashMap<>(stockList.size());
        for (ItemStock itemStock : stockList) {
            Long id = itemStock.getId();
            String json = objectMapper.writeValueAsString(itemStock);
            itemStockJsonMap.put(ITEM_STOCK_KEY_PREFIX + id, json);
        }
        // 存入Redis
        redisTemplate.opsForValue().multiSet(itemStockJsonMap);
    }
}
```

实现逻辑非常简单，通过Service查询到所有商品数据，然后通过for循环遍历数据，将每个实例转换为JSON，保存在Map中，最后通过MSET缓存到Redis中，使用MSET可以减少网络往返次数，提高预热性能

> ![](img4/36.png)

##### OpenResty查询Redis缓存

类似远程调用，OpenResty也提供了专门针对于Redis的请求API，名为resty.redis，位于openresty/lualib/resty/redis.lua

操作Redis更加复杂，需要先创建lua对象，然后设置超时时间，释放连接池等等

```lua
-- 引入Redis
local redis = require("resty.redis")
-- 创建Redis对象
local r = redis:new()
-- 设置Redis超时时间
r:set_timeouts(1000, 1000, 1000)

-- 释放连接到连接池
local function close_conn(r)
    -- 连接空闲时间，单位ms
    local pool_max_idle_time = 10000
    -- 连接池大小
    local pool_size = 100
    local ok, err = r:set_keepalive(pool_max_idle_time, pool_size)
    
    if not ok then
        ngx.log(ngx.ERR, "创建连接池出错", err)
    end
end
```

初始化完成后，再封装一个读取数据的函数

```lua
local function redis_get(ip, port, key)
    -- 建立连接
    local ok, err = r:connect(ip, port)
    if not ok then
        ngx.log(ngx.ERR, "连接Redis失败", err)
        return nil
    end
    
    -- 执行GET查询
    local res, err = r:get(key)
    -- 释放连接并返回
    close_conn(r)
    return res
end
```

在common.lua中粘贴这些代码，然后暴露redis_get供其他脚本调用

```lua
local _M = {
    http_get = http_get,
    redis_get = redis_get
}
```

然后修改itemInfo.lua中有关查询的逻辑，应该先查询Redis，Redis查询失败后再请求Tomcat

```lua
-- 导入common.lua
local common = require("common")
-- 导入cjson
local cjson = require("cjson")

-- 获取路径参数
local id = ngx.var[1]

if not id then
    ngx.say()
end

local ITEM_INFO_KEY_PREFIX = "cache:item:info:"
local ITEM_STOCK_KEY_PREFIX = "cache:item:stock:"

local REDIS_HOST = "127.0.0.1"
local REDIS_PORT = 6379

-- 查询商品信息
local itemJSON = common.redis_get(REDIS_HOST, REDIS_PORT, ITEM_INFO_KEY_PREFIX .. id)
if not itemJSON then
    itemJSON = common.http_get("/item/" .. id, nil)
end
local stockJSON = common.redis_get(REDIS_HOST, REDIS_PORT, ITEM_STOCK_KEY_PREFIX .. id)
if not stockJSON then
    stockJSON = common.http_get("/item/stock/" .. id, nil)
end

-- 转换为table
local item = cjson.decode(itemJSON)
local stock = cjson.decode(stockJSON)

-- 组装数据
item.stock = stock.stock
item.sold = stock.sold

-- 返回结果
ngx.say(cjson.encode(item))
```

这里额外需要注意的是，redis_get不能解析本地域名，只能填写ip地址，如果是本机请使用127.0.0.1而不是localhost。然后进行测试，我们可以直接关闭Tomcat服务，仅启用Redis，刷新前端页面，依然可以完成请求

> ![](img4/37.png)

#### Nginx本地缓存

OpenResty为NGINX提供了shared dict的功能，可以在多个nginx的worker之间共享数据，worker类似于线程，负责接收用户请求并进行代理转发，而shared dict则可以实现缓存功能

如果需要开启shared dict，则需要在nginx.conf的http栏中添加一条

```conf
# 开启缓存功能，缓存对象命令为item_cache，缓存大小为150Mib
lua_shared_dict item_cache 150m;
```

然后通过专属的api来操作缓存

```lua
-- 获取缓存对象
local item_cache = ngx.shared.item_cache
-- 向缓存中存储数据，可以设置TTL，单位为秒，0表示永不过期
item_cache:set("key", "value", TTL)
-- 读取缓存中的数据
local val = item_cache:get("key")
```

据此，我们继续来改造原始lua脚本，为其添加NGINX本地缓存。这里我们先将查询逻辑封装为一个函数，在函数中统一先查询本地，再查询Redis，最后调用Tomcat

```lua
-- 导入common.lua
local common = require("common")
-- 导入cjson
local cjson = require("cjson")

local REDIS_ITEM_INFO_KEY_PREFIX = "cache:item:info:"
local REDIS_ITEM_STOCK_KEY_PREFIX = "cache:item:stock:"
local REDIS_HOST = "127.0.0.1"
local REDIS_PORT = 6379

local LOCAL_ITEM_INFO_KEY_PREFIX = "cache:item:info:"
local LOCAL_ITEM_STOCK_KEY_PREFIX = "cache:item:stock:"
local LOCAL_ITEM_INFO_KEY_TTL = 30 * 60
local LOCAL_TIEM_STOCK_KEY_TTL = 60

-- 封装查询函数
local function query(local_cache, 
    local_cache_key, 
    redis_key, 
    http_path, 
    http_param,
    local_cache_ttl
)
    -- 查询本地缓存
    ngx.log(ngx.ERR, "开始执行查询，尝试本地缓存")
    local res = local_cache:get(local_cache_key)
    if not res then
        -- 查询Redis
        ngx.log(ngx.ERR, "本地缓存查询失败，尝试Redis")
        res = common.redis_get(REDIS_HOST, REDIS_PORT, redis_key)
        if not res then
            -- 查询Tomcat
            ngx.log(ngx.ERR, "Redis查询失败，尝试Tomcat")
            res = common.http_get(http_path, http_param)
        end

        -- 构建本地缓存
        ngx.log(ngx.ERR, "构建本地缓存", res)
        local_cache:set(local_cache_key, res, local_cache_ttl)
    end

    return res
end

-- 获取路径参数
local id = ngx.var[1]

if not id then
    ngx.say()
end

-- 导入本地缓存
local item_cache = ngx.shared.item_cache

-- 查询商品信息
local itemJSON = query(item_cache, 
    LOCAL_ITEM_INFO_KEY_PREFIX .. id, 
    REDIS_ITEM_INFO_KEY_PREFIX .. id, 
    "/item/" .. id, 
    nil,
    LOCAL_ITEM_INFO_KEY_TTL
)
local stockJSON = query(item_cache, 
    LOCAL_ITEM_STOCK_KEY_PREFIX .. id, 
    REDIS_ITEM_STOCK_KEY_PREFIX .. id, 
    "/item/stock/" .. id, 
    nil,
    LOCAL_TIEM_STOCK_KEY_TTL
)

-- 转换为table
local item = cjson.decode(itemJSON)
local stock = cjson.decode(stockJSON)

-- 组装数据
item.stock = stock.stock
item.sold = stock.sold

-- 返回结果
ngx.say(cjson.encode(item))
```

访问前端，查询错误日志，我们就可以看到缓存是否构建成功

> ![](img4/38.png)

可以看到，在构建本地缓存后，第二次查询就不再通过Redis，而是直接通过本地缓存返回数据

### 缓存同步

缓存同步的常见方式有如下三种

- 设置有效期：给缓存设置有效期或者TTL，到期后自动删除，再次查询时更新，优点是简单方便，缺点是时效性差，缓存过期之前可能数据不一致

- 同步双写：在修改数据库的同时直接修改缓存，优点是时效性强，缓存与数据库强一致性，缺点是有代码侵入，而且代码耦合度高，不利于维护
- 异步通知：修改数据库时发送事件通知，相关服务监听到通知后修改缓存数据，优点是低耦合，可以同时通知多个缓存服务，缺点是时效性一般，可能存在中间不一致状态，而且可靠性性完全依赖消息中间件

#### 基于Canal的异步通知

canal是阿里巴巴旗下一款开源项目，基于Java开发，可以实现基于数据库增量日志解析，提供增量数据订阅与消费。简单来说，canal可以监听Mysql的数据变化，然后发布通知。相比Service在更新数据库时发送消息，可以省去手动发布消息的步骤，进一步提升时效性。canal的监听是基于Mysql主从同步来实现的

Mysql的主从同步类似Redis的增量同步，主节点将数据变更写入而今日文件Binary Log，其中记录的数据叫做Binary Log Events。从节点将主节点的Binary Log Events复制到自己的Relay Log中，然后从节点再通过单独的执行线程读取并重放Relay Log中的事件，达到主从一致

canal的原理就是将自己伪装成Mysql的从节点，监听主节点的binary log变化，再把变化的信息发送到canal客户端，进而完成对其他数据库的通知

