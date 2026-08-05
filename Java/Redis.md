# Redis

`更新时间：2026-8-5`

注释解释：

- `<>`必填项，必须在当前位置填写相应数据

- `{}`必选项，必须在当前位置选择一个给出的选项

- `[]`可选项，可以选择填写或忽略

*注：该笔记内的可选项和参数均不完整，如有需要，请查询相关手册*

---

## NoSQL

NoSQL相对SQL，SQL全程Structured Query Language，结构化查询语言，是主流关系型数据库使用的查询语言，使用近似自然语言的命令来构建查询语句，如

```sql
SELET id, username FROM users;
```

NoSQL目前没有官方的名称，一般认为是非结构化查询语言，这并不指一种语言，而是除SQL以外其他数据库及其数据库查询语言的统称，常见的NoSQL数据库包括Redis、MongoDB、ES等等，均使用不同的查询语言与数据结构

常见的NoSQL数据结构有键值对型，如Redis中通过键确定每一条数据值；还有文档型，如ES中，每条数据由文档构成，多个文档构成一个索引；还有如图表型，将每条数据及其数据间的关系以节点的形式来表示，构成复杂的关系网络

而且SQL具有强事务性，SQL数据库必须满足事务的ACID特性，而NoSQL一般不作要求，进满足BASE理论即可，即基本可用、软状态、最终一致性。对于存储方式来说，SQL大多采用磁盘存储，保证数据结构稳定及其数据安全性，避免数据丢失；而NoSQL大多采用内存存储，虽然会使用磁盘作为持久化手段，但是核心数据通常是存储在内存中，一旦服务宕机，很可能造成数据丢失

## Redis

Redis诞生于2009年，全程 Remote Dictionary Server，远程词典服务器，是一个基于内存的键值对型NoSQL数据库，其明显的特征是键值型，value支持多种不同的数据结构，功能丰富。此外，Redis还具备了单线程，每个命令具备原子性，并发安全；延迟极低，速度快，基于内存、IO多路复用和良好的编码；且支持数据持久化，支持主从集群，分片集群、哨兵集群，支持多语言客户端，多个编程语言都可以操作Redis

### Redis数据结构

Redis有很多种数据结构，但是基本数据类型只有五种，分别是String字符串、Hash哈希表、List列表、Set集合、SortedSet排序集合；其余的被称为特殊类型，如GEO地理坐标、BitMap位图、HyperLog超文档等，都是基于基本数据类型构造的特殊数据类型

### Redis通用命令

通用命令是指无需考虑数据类型，所有数据类型下都可以使用的命令，使用命令

```redis
HELP @generic
```

就可以查看所有的通用命令

> ![](javaweb2/301.png)

下面我们罗列几个常用的通用命令，其余的命令可以通过官方文档进行了解学习

#### KEYS

**标准语法**

```redis
KEYS <PATTERN>
```

KEYS命令根据PATTERN来查询匹配的键名，PATTERN不是正则表达式，而是Redis自己的匹配模式，如下

| 匹配符 | 含义                                                     | 示例                                          |
| ------ | -------------------------------------------------------- | --------------------------------------------- |
| ?      | 单字符匹配，该位置可以为任意字符                         | h?llo = hallo、hbllo、hcllo...                |
| *      | 多字符匹配，即通配符                                     | h*llo = hallo、hbllo、haallo、haaallo...      |
| [xy]   | 单字符枚举匹配，该位置可以为[xy]枚举集合中的任意一个字符 | h[ae]llo = hallo、hello                       |
| [^x]   | 前缀匹配，该位置可以为字符x之前的任意一个字符            | h[\^e]llo = hallo、hbllo...hdllo，不包含hello |
| [x-y]  | 单字符范围匹配，改位置可以为x到y之间的任意一个字符       | h[b-e]llo = hbllo、hcllo、hdllo、hello        |

> ![](javaweb2/302.png)

KEYS会执行搜索，而Redis是单线程执行的，所以一旦Redis数据库中的数据量足够庞大，执行KEYS就会造成一定的进程阻塞，无法执行其他命令。因此一般不推荐在开发中使用KEYS命令，或者应当在主从集群的从节点上执行该命令，不影响主节点写命令

#### DEL

**标准语法**

```redis
DEL <KEY> [KEYS...]
```

删除一个或者多个KEY，返回值为删除的KEY数量，当KEY不存在时返回0

> ![](javaweb2/303.png)

#### EXISTS

**标准语法**

```redis
EXISTS <KEY> [KEYS...]
```

判断一个或多个KEY是否存在，返回值为存在的KEY数量

> ![](javaweb2/304.png)

#### EXPIRE

**标准语法**

```redis
EXPIRE <KEY> <SECONDS>
```

为一个KEY设置有效期，单位为秒，有效期到期时，KEY会被自动删除以释放内存，返回值为修改的KEY数量，返回值为0表示KEY不存在

> ![](javaweb2/305.png)

#### TTL

**标准语法**

```redis
TTL <KEY>
```

与EXPIRE相对，查看一个KEY的有效时间，当有效期为-2时表示KEY已经过期

> ![](javaweb2/306.png)

为什么是-2呢，因为Redis将永久有效的KEY的TTL设置为了-1

> ![](javaweb2/307.png)

*注：一般来说，Redis中的KEY都需要设置一个TTL，以避免长期占用内存空间，而每个KEY设置的TTL也不应该相同，避免缓存雪崩*

### String

String类型，也就是字符串类型，是Redis中最简单的存储类型，其value为字符串，根据字符串格式不同，可以将String分为三类

- string：普通字符串
- int：整数类型，可以进行自增自减操作
- float：浮点类型，也可以进行自增自减操作

String底层通过字节数组存储，但是不同的String类型的编码方式不同。String类型的最大空间不能超过512M

#### 常用命令

| 命令        | 标准语法                                                     | 说明                                                         |
| ----------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| SET         | SET \<KEY\> \<VALUE> [EX SECONDS \| PX MILLISECONDS] [NX \| XX] | 添加或修改一个String类型的KEY，可以携带EX或PX设置过期时间，EX单位为秒，PX单位为毫秒，NX和XX用于指定执行前提，NX表示不存在则执行，XX表示存在则执行，NX和XX一般用于锁 |
| GET         | GET \<KEY>                                                   | 获取一个String类型的KEY的VALUE                               |
| MSET        | MSET \<KEY> \<VALUE> [\<KEYS> \<VALUES>...]                  | 批量添加多个String类型的KEY                                  |
| MGET        | MGET \<KEY> [\<KEYS>...]                                     | 批量获取多个String类型的KEY的VALUE                           |
| INCR        | INCR \<KEY>                                                  | 让一个整型KEY自增1                                           |
| INCRBY      | INCRBY \<KEY> \<INCREMENT>                                   | 让一个整型KEY自增并设置自增步长                              |
| INCRBYFLOAT | INCRBYFLOAT \<KEY> \<INCREMENT>                              | 让一个浮点型KEY自增并设置自增步长                            |
| SETNX       | SETNX \<KEY> \<VALUE>                                        | 如果KEY不存在，则添加一个String类型的KEY                     |
| SETEX       | SETEX \<KEY> \<SECONDS> \<VALUE>                             | 添加一个String类型的KEY，并同时设置TTL                       |

### KEY的分级结构

在Redis中，并不存在类似于SQL中的表结构，所有的KEY统一放置在一个数据库中，这很容易造成KEY的混乱，为了不同的业务使用相同的KEY，Redis支持使用特殊符号作为KEY，其中以分号作为分隔符的KEY会被Redis认为是分级KEY。假设有一个OOEZ语音聊天项目，需要存储语音频道中所有用户的信息，KEY结构如下

```redis
ooez:channel:user:id
```

用户信息的KEY-VALUE结构示例为

| KEY                     | VALUE                          |
| ----------------------- | ------------------------------ |
| ooez:channel:user:20549 | {"id": 20549, "name": "kiiz"}  |
| ooez:channel:user:31160 | {"id": 31160, "name": "ocean"} |

然后再定义一个KEY用于存储频道中的房间信息

```redis
ooez:channel:room:id
```

房间信息KEY-VALUE结构示例为

| KEY                  | VALUE                     |
| -------------------- | ------------------------- |
| ooez:channel:room:14 | {"id": 14, "maxUsers": 5} |
| ooez:channel:room:16 | {"id": 16, "maxUsers": 2} |

通常是使用id作为KEY，而前缀就可以表明KEY所处的业务逻辑，避免KEY重复

### Hash

哈希类型，也叫散列，其VALUE是一个无序字典，类似于Java中的HashMap，如果加上本来的KEY，其数据结构类似于Java中的Map\<String, Map\<String, String>>。哈希的VALUE也是一个键值对，其键被称为Field字段，键值才称为VALUE

#### 常用命令

| 命令    | 标准语法                                                | 说明                                              |
| ------- | ------------------------------------------------------- | ------------------------------------------------- |
| HSET    | HSET \<KEY> \<FIELD> \<VALUE>                           | 添加或修改Hash类型的KEY的FIELD值                  |
| HGT     | HGET \<KEY> \<FIELD>                                    | 获取一个Hash类型的KEY的FIELD值                    |
| HMSET   | HMSET \<KEY> \<FIELD> \<VALUE> [\<FIELDS> \<VALUES>...] | 批量添加多个Hash类型的KEY的FIELD值                |
| HMGET   | HMGET \<KEY> \<FIELD> [\<FIELDS>...]                    | 批量获取多个Hash类型的KEY的FIELD值                |
| HGETALL | HGETALL \<KEY>                                          | 获取一个Hash类型的KEY中的所有FIELD和VALUE         |
| HKEYS   | HKEYS \<KEY>                                            | 获取一个Hash类型的KEY中的所有FIELD                |
| HVALS   | HVALS \<KEY>                                            | 获取一个Hash类型的KEY中的所有VALUE                |
| HINCRBY | HINCRBY \<KEY> \<FIELD> \<INCREMENT>                    | 让一个Hash类型KEY的字段值自增并指定步长           |
| HSETNX  | HSETNX \<KEY> \<FIELD> \<VALUE>                         | 如果FIELD不存在，则添加一个Hash类型的KEY的FIELD值 |

### List

Redis中的List类型与Java中的LinkedList类似，可以看作是一个双向链表结构，既可以正向索引也可以反向索引。其特征也符合双向链表，有序、元素可以重复、插入和删除速度快、查询速度一般。List类型常用来存储一个有序数据，如点赞列表、评论列表等等

#### 常用命令

| 命令   | 标准语法                             | 说明                                                      |
| ------ | ------------------------------------ | --------------------------------------------------------- |
| LPUSH  | LPUSH \<KEY> \<VALUE> [\<VALUES>...] | 向列表左侧插入一个或多个元素                              |
| LPOP   | LPOP \<KEY>                          | 弹出并返回列表左侧的一个元素，没有则返回nil               |
| RPUSH  | RPUSH \<KEY> \<VALUE> [\<VALUES>...] | 向列表右侧插入一个或多个元素                              |
| RPOP   | RPOP \<KEY>                          | 弹出并返回列表右侧的一个元素，没有则返回nil               |
| LRANGE | LRANGE \<KEY> \<START> \<STOP>       | 返回一段角标范围内的所有元素                              |
| BLPOP  | BLPOP \<KEY> [\<KEYS>...] \<TIMEOUT> | 与LPOP类似，但是没有元素时等待指定时间，而不是直接返回nil |

### Set

Redis的Set结构与Java中的HashSet类似，可以看作是一个VALUE为null的HashMap，Set结构底层也是一个哈希表。Set具有的特征有无序、元素不可重复、查找快、支持交集、并集、差集等功能

哈希表底层是通过哈希函数计算元素来获取下标，所以可以保证数据不重复，而相同的数据哈希结果相同，因此查询速度也更快

#### 常用命令

| 命令      | 标准语法                              | 说明                      |
| --------- | ------------------------------------- | ------------------------- |
| SADD      | SADD \<KEY> \<MEMBER> [\<MEMBERS>...] | 向SET中添加一个或多个元素 |
| SREM      | SREM \<KEY> \<MEMBER> [\<MEMBERS>...] | 移除SET中的指定元素       |
| SCARD     | SCARD \<KEY>                          | 返回SET中元素的个数       |
| SISMEMBER | SISMEMBER \<KEY> \<MEMBER>            | 判断一个元素是否存在SET中 |
| SMEMBERS  | SMEMBERS \<KEY>                       | 获取SET中的所有元素       |
| SINTER    | SINTER \<KEY> [\<KEYS>...]            | 求SET集合的交集           |
| SDIFF     | SDIFF \<KEY> [\<KEYS>...]             | 求SET集合的差集           |
| SUNION    | SUNION \<KEY> [\<KEYS>...]            | 求SET集合的并集           |

### SortedSet

Redis的SortedSet是一个可排序的Set集合，与Java中的TreeSet类似，但是底层数据结构差别很大。SortedSet中每一个元素都带有一个score属性，可以基于score属性对元素排序，底层实现是一个跳表加哈希表，详情可见[SortedSet](./JavaWebEnhance.md#SortedSet)。SortedSet的特征有可排序、元素不重复、查询速度快

#### 常用命令

| 命令          | 标准语法                                                     | 说明                                               |
| ------------- | ------------------------------------------------------------ | -------------------------------------------------- |
| ZADD          | ZADD \<KEY> [NX \| XX] [CH] [INCR] \<SCORE> \<MEMBER> [\<SCORES> \<MEMBERS>...] | 添加一个或多个元素到ZSET，如果已经存在则更新其分数 |
| ZREM          | ZREM \<KEY> \<MEMBER> [\<MEMBERS>...]                        | 删除ZSET中的一个指定元素                           |
| ZSCORE        | ZSCORE \<KEY> \<MEMBER>                                      | 获取ZSET指定元素的分数                             |
| ZRANK         | ZRANK \<KEY> \<MEMBER>                                       | 获取ZSET中指定元素的排名                           |
| ZCARD         | ZCARD \<KEY>                                                 | 获取ZSET中元素个数                                 |
| ZCOUNT        | ZCOUNT \<KEY> \<MIN> \<MAX>                                  | 统计分数在指定范围内的所有元素个数                 |
| ZINCRBY       | ZINCRBY \<KEY> \<INCREMENT> \<MEMBER>                        | 让ZSET指定元素自增                                 |
| ZRANGE        | ZRANGE \<KEY> \<START> \<STOP> [WITHSCORES]                  | 按照分数排序，然后获取指定排名范围内的元素         |
| ZRANGEBYSCORE | ZRANGEBYSCORE \<KEY> \<MIN> \<MAX> [WITHSCORES] [LIMIT OFFSET COUNT] | 按照分数排序后，获取指定分数范围内的元素           |
| ZDIFF         | ZDIFF \<NUMKEYS> \<KEY> [\<KEYS>...] [WITHSCORES]            | 求ZSET的差集                                       |
| ZINTER        | ZINTER \<NUMKEYS> \<KEY> [\<KEYS>...] [WEIGHTS \<WEIGHT> [\<WEIGHTS>...]] [AGGREGATE SUM \| MIN \| MAX] [WITHSCORES] | 求ZSET的交集                                       |
| ZUNION        | ZUNION\<NUMKEYS> \<KEY> [\<KEYS>...] [WEIGHTS \<WEIGHT> [\<WEIGHTS>...]] [AGGREGATE SUM | MIN                                                |

*注：ZDIFF、ZINTER、ZUNION都是Redis6.2.0+加入的新命令，如果想要使用，服务端和客户端都需要升级到6.2.0+*

## Redis Java Client

作为Java后端开发人员，我们就需要通过Redis的Java客户端来操作Redis，而Redis官方也罗列了几种Redis的Java客户端供我们选择

| 客户端   | 说明                                                         |
| -------- | ------------------------------------------------------------ |
| Jedis    | 以Redis命令作为方法名称，学习成本低，简单实用。但是Jedis实例是线程不安全的，多线程环境下需要基于连接池来使用 |
| Lettuce  | Lettuce基于Netty实现，支持同步、异步和响应式编程方式，并且是线程安全的，支持Redis哨兵模式、集群模式和管道模式 |
| Redisson | Redisson是一个基于Redis实现的分布式、可伸缩的Java数据结构集合，包含了诸如Map、Queue、Lock、Semaphor、AtomicLong等强大功能 |

### Jedis

#### 快速入门

Jedis上手非常方便，开发人员可以通过几行代码就快速构建一个可用的Resposiroty。首先引入依赖

```xml
<!-- Jedis-->
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>4.3.1</version>
</dependency>
```

然后声明一个Jedis实例

```java
// 声明一个Jedis连接
Jedis jedis = new Jedis("localhost", 6379);
// 选择0号数据库
jedis.select(0);
```

接下来就可以执行Redis命令了，我们打印Redis服务端信息

```java
String server = jedis.info("SERVER");
System.out.println(server);
```

> ![](javaweb2/308.png)

#### Jedis连接池

Jedis本身是线程不安全的，并且频繁地创建和销毁连接会造成性能损耗，因此更推荐使用Jedis连接池代替Jedis直连。我们创建一个工厂类，工厂类中构建Jedis连接池，然后暴露get方法即可

```java
package com.itheima.mp.util;

import lombok.Getter;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

public class JedisConnectionFactory {

    @Getter
    private static final JedisPool jedisPool;
    private static final String HOST = "localhost";
    private static final Integer PORT = 6379;

    // 初始化连接池
    static {
        // 声明配置对象
        JedisPoolConfig config = new JedisPoolConfig();
        // 配置参数
        config.setMaxTotal(10);
        config.setMaxIdle(10);
        config.setMinIdle(2);
        config.setMaxWait(Duration.ofSeconds(5));
        // 创建连接池对象
        jedisPool = new JedisPool(config, HOST, PORT);
    }
}
```

首先定义私有成员JedisPool，然后通过静态代码块来为其进行初始化赋值，JedisPool需要JedisPoolConfig作为配置，因此需要先声明一个JedisPoolConfig，然后填充参数，如最大连接数、最大空闲连接数、最小空闲连接数、连接等待时间等等，然后将配置传入JedisPool的构造器，构造一个连接池，最后为私有成员添加@Getter注解添加getter方法

### Spring Data Redis

Spring Data是Spring中数据操作的模块，包含对各种数据库的集成，其中对Redis的集成模块叫做Spring Data Redis。此外，Spring Data还提供了如Spring Data JDBC、Spring Data JPA、Spring Data REST、Spring Data Elasticsearch等等

Spring Data Redis提供了对不同Redis客户端的整合，如Lettuce和Jedis，支持Redis的发布订阅模型，支持Redis哨兵和Redis集群，支持基于Lettuce的响应式编程模型，支持基于JDK、JSON、字符串、Spring对象的数据序列化及其反序列化，支持基于Redis的JDK Collection实现

Spring Data Redis提供了RedisTemplate统一API来操作Redis，RedisTemplate将不同的数据类型的操作API封装到了不同的类型中

| API                         | 返回值类型      | 说明                  |
| --------------------------- | --------------- | --------------------- |
| redisTemplate.opsForValue() | ValueOperations | 操作String类型数据    |
| redisTemplate.opsForHash()  | HashOperations  | 操作Hash类型数据      |
| redisTemplate.opsForList()  | ListOperations  | 操作List类型数据      |
| redisTemplate.opsForSet()   | SetOperations   | 操作Set类型数据       |
| redisTemplate.opsForZset()  | ZSetOperations  | 操作SortedSet类型数据 |
| redisTemplate               |                 | 通用命令              |

#### 快速入门

首先引入依赖

```xml
<!-- spring data redis-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<!-- apache 连接池-->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>
```

Spring Data Redis的版本号依赖Spring Boot父工程的版本，所以不需要手动指定。而Apache Pool则负责构建连接池

在引入依赖完成后，在配置文件中配置Redis

```yml
spring:
  redis:
    host: localhost
    port: 6379
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 2
        max-wait: 1000
```

然后就按照Spring的标准方式，注入RedisTemplate，获取Redis数据

```java
@Autowired
private RedisTemplate redisTemplate;

@Test
void test() {
    redisTemplate.opsForValue().set("name", "Ocean");
    Object name = redisTemplate.opsForValue().get("name");
    System.out.println(name);
}
```

但是当我们尝试通过redis-cli访问name时，却发现name并不存在

> ![](javaweb2/309.png)

查看当前所有KEY，只有一个以name结尾的KEY，\xac\xed\x00\x05t\x00\x04name

> ![](javaweb2/310.png)

#### 序列化器

这源于Spring Data Redis默认选择的序列化器，Spring Data Redis默认选择了JDK的序列化器，而JDK序列化器会将所有的KEY和VALUE直接转换为字节数组，同时加入一些额外的标识符，因此会生成这样的字节数据

如果希望Spring Data Redis直接输出可读的KEY和VALUE，就需要更改其序列化器，默认的序列化器实现其实有很多种

> ![](javaweb2/311.png)

而常用的就是StringRedisSerializer和Jackson2JsonRedisSerializer。修改的方式则是定义一个配置类，主动声明一个Bean返回RedisTemplate，而这个RedisTemplate就是已经修改好序列化器的RedisTemplate

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 创建RedisTemplate对象
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 设置连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置KEY序列化器
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        // 设置VALUE序列化器
        redisTemplate.setValueSerializer(RedisSerializer.json());
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        // 返回
        return redisTemplate;
    }
}
```

需要注意的是这里直接使用了RedisSerializer的string和json方法来快捷获取序列化器实例，就不需要我们手动声明了

```java
/**
 * Obtain a {@link RedisSerializer} that can read and write JSON using
 * <a href="https://github.com/FasterXML/jackson-core">Jackson</a>.
 *
 * @return never {@literal null}.
 * @since 2.1
 */
static RedisSerializer<Object> json() {
    return new GenericJackson2JsonRedisSerializer();
}

/**
 * Obtain a simple {@link java.lang.String} to {@literal byte[]} (and back) serializer using
 * {@link java.nio.charset.StandardCharsets#UTF_8 UTF-8} as the default {@link java.nio.charset.Charset}.
 *
 * @return never {@literal null}.
 * @since 2.1
 */
static RedisSerializer<String> string() {
    return StringRedisSerializer.UTF_8;
}
```

然后我们通过redis-cli创建name，值设置为Ki1z，再通过Spring Data Redis读取

> ![](javaweb2/312.png)

> ![](javaweb2/313.png)

可以看到出现了报错，但其实并不是无法读取的报错，而是序列化器无法对其反序列化的报错，我们直接写入的`Ki1z`并不是标准JSON，所以无法进行转换。下面我们更改为标准的`"Ki1z"`

> ![](javaweb2/314.png)

> ![](javaweb2/315.png)

#### StringRedisTemplate

刚才我们尝试了读取String类型，而Jackson2Json还支持直接的对象插入

```java
@Test
void test() {
    User user = new User("Ki1z", "123456");
    redisTemplate.opsForValue().set("user", user);
    System.out.println(redisTemplate.opsForValue().get("user"));
}

@AllArgsConstructor
@NoArgsConstructor
@Data
static class User {
    private String username;
    private String password;
}
```

> ![](javaweb2/316.png)

然后我们通过DataGrip来观察实际存入Redis的数据

> ![](javaweb2/317.png)

不难发现，Jackson为了保证反序列化的正确性，在JSON插入一了一条类信息，标识该对象所属的实体类。但另一方面，这些多余的数据会造成大量的额外内存占用，如图中的@class字段，占用空间比原本的User还长，所以我们不能使用Jackson的自动反序列化

而作为开发人员，我们知道Redis中存储的数据对应的实体类，因此我们只用进行手动反序列化即可。而在Redis中，只需要保存原本的JSON字符串即可，而JSON字符串的本质是String，所以只需要将Redis的序列化器都设置为StringRedisSerializer。而Spring Data Redis其实已经预先提供好了KEY和VALUE的序列化器都是String的模板，这就是StringRedisTemplate

下面我们利用StringRedisTemplate来传入User对象

```java
@Autowired
private StringRedisTemplate redisTemplate;

@Test
void test() throws JsonProcessingException {
    User user = new User("Ki1z", "123456");
    ObjectMapper objectMapper = new ObjectMapper();
    // 将对象转为JSON字符串
    String json = objectMapper.writeValueAsString(user);

    redisTemplate.opsForValue().set("user", json);
    json = (String) redisTemplate.opsForValue().get("user");
    User user1 = objectMapper.readValue(json, User.class);
    System.out.println(user1);
}

@AllArgsConstructor
@NoArgsConstructor
@Data
static class User {
    private String username;
    private String password;
}
```

注入StringRedisTemplate，然后声明ObjectMapper用于转换JSON，转换完成后传入Redis，最后再通过ObjectMapper反序列化为User

> ![](javaweb2/318.png)

## 黑马点评

下面我们将依据黑马点评项目，通过Redis来学习并解决实际开发中可能遇到的各种问题，如短信登录、点赞列表、点赞排行榜、好友关注、用户签到、UV统计、附近商户、优惠券秒杀、查询缓存等，从实际开发中逐步深入了解Redis

