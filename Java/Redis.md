# Redis

`更新时间：2026-8-7`

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

> ![](javaweb2/319.png)

### 基于Session实现登录

在项目搭建完成后，我们开始逐步实现项目中未完成的功能，首先是基于Session实现登录功能。不过并不是真的只通过Session，而是通过短信登录验证，在发送验证码后，通过Session在服务端保存验证码，并在校验时取出验证，这就是基于Session实现登录

在编写代码之前，我们先来分析一下基于Session实现登录的步骤。基于Session实现登录可以分为三个模块，发送验证码、接收验证码进行登录、校验登录状态。在发送验证码模块中，首先由用户提交手机号，然后后端生成一个验证码，将验证码保存在Session中以供下游调用，然后再向用户发送验证码；在接收验证码模块中，后端获取用户提交的手机号和验证码，然后从Session中取出验证码进行校验，如果校验通过，则从数据库中查询该用户信息，查询通过则保存用户登录信息到Session，而不存在的用户则直接进行快捷注册，然后保存用户信息到Session；在校验登录模块，从Session中获取用户信息，判断用户信息是否合法，再将用户信息保存在ThreadLocal中，供下文直接调用

#### 发送短信验证码

在黑马点评项目中，点击“我的”就会跳转到个人页面，未登录的情况下重定向到登录页面

> ![](javaweb2/320.png)

输入手机号，点击发送验证码，前端就会向后端发送一条请求

> ![](javaweb2/321.png)

然后我们基于这个请求路径来编写业务逻辑

```java
@PostMapping("code")
public Result sendCode(@RequestParam("phone") @NotBlank String phone, HttpSession session) {
    log.debug("发送验证码，手机号：{}", phone);
    return userService.sendCode(phone, session);
}
```

```java
@Override
public Result sendCode(String phone, HttpSession session) {
    // 检查手机号
    if (RegexUtils.isPhoneInvalid(phone)) {
        return Result.fail("手机号格式错误");
    }
    // 生成验证码
    String code = RandomUtil.randomNumbers(4);
    // 保存验证码
    session.setAttribute("code", code);
    // 发送验证码
    log.debug("发送验证码成功，验证码: {}", code);
    // 返回结果
    return Result.ok("发送成功，验证码5分钟内有效");
}
```

在Service中，首先通过RegexUtils工具类检查手机号格式，然后通过随机数生成器生成一个四位数验证码，再将验证码保存到Session中，发送验证码，并返回结果。如果要发送真实的短信验证码需要调用第三方服务，还会造成短信资费，因此我们选择使用log模拟发送

> ![](javaweb2/322.png)

#### 接收验证码与登录校验

```java
@PostMapping("/login")
public Result login(@RequestBody @Valid LoginFormDTO loginForm, HttpSession session){
    log.debug("登录，参数：{}", loginForm);
    return userService.login(loginForm, session);
}
```

```java
@Transactional
@Override
public Result login(LoginFormDTO loginForm, HttpSession session) {
    String phone = loginForm.getPhone();
    if (RegexUtils.isPhoneInvalid(phone)) {
        return Result.fail("手机号格式错误");
    }
    String code = loginForm.getCode();
    String password = loginForm.getPassword();
    if (code != null && !code.isEmpty()) {
        // 验证码登录
        // 从Session中获取验证码
        String cacheCode = (String) session.getAttribute("code");
        // 判断验证码是否一致
        if (cacheCode == null || !cacheCode.equals(code)) {
            return Result.fail("验证码错误");
        }
        // 验证通过，删除验证码
        session.removeAttribute("code");
        // 查询用户
        User user = query().eq("phone", phone).one();
        if (user == null) {
            // 用户不存在，注册用户
            user = registerByPhone(phone);
        }
        // 登录成功，保存用户信息到Session中
        session.setAttribute("user", user);
        return Result.ok("登录成功");

    } else if (password != null && !password.isEmpty()) {
        return Result.fail("密码登录功能未完成");
    } else {
        return Result.fail("密码或验证码不能为空");
    }
}

@Transactional
public User registerByPhone(String phone) {
    User user = User.builder()
            .id(IdWorker.getId())
            .phone(phone)
            .nickName("用户" + RandomUtil.randomString(10))
            .build();
    int insert = userMapper.insert(user);
    if (insert <= 0) {
        throw new RuntimeException("注册失败");
    }
    return user;
}
```

由于密码登录与验证码登录使用同一个接口，为了方便开发，这里就只做验证码登录的逻辑。首先验证登录的额表单信息，然后从Session中获取验证码，并判断与用户提交的是否一致，验证成功后，删除验证码，并从数据库中查询用户信息，如果用户不存在，则注册一个新用户，最后将用户信息保存到Session中

#### 校验登录状态

登录状态的校验不能部署在接口处，因为这会导致每个接口占用大量的代码去编写校验逻辑，而Spring中则提供了Interceptor来统一拦截请求，所以我们将登录校验逻辑安排在Interceptor中

```java
package com.hmdp.interceptor;

import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import com.hmdp.utils.UserHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取用户信息
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.setStatus(401);
            return false;
        }
        // 保存到上下文中
        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .icon(user.getIcon())
                .nickName(user.getNickName())
                .build();
        UserHolder.saveUser(userDTO);
        // 放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除用户
        UserHolder.removeUser();
    }
}
```

定义LoginInterceptor实现HandlerInterceptor，并实现方法preHandle和afterCompletion，在preHandle中从Session中获取用户信，然后保存到上下文中，而afterCompletion负责在登录校验完成后清除上下文

```java
package com.hmdp.config;

import com.hmdp.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        "/user/code",
                        "/user/login",
                        "/shop/**",
                        "/voucher/**",
                        "/blog/hot",
                        "/shop-type/**",
                        "/upload/**"
                );
    }
}
```

然后定义MvcConfig，实现WebMvcConfigurer，拦截器想要生效必须由registry通过addInterceptor方法注册，并指定排除路径。这样就可以实现登录校验了，我们登录一个账号，然后访问个人中心，即可查看个人信息

> ![](javaweb2/323.png)

### 基于Redis实现登录

Session有一个很明显的缺点，不支持分布式或者集群。简单来说，如果部署了多台Tomcat服务器，服务器中运行相同的项目实例，但是每台Tomcat的Session是不共享的，虽然Tomcat支持启用某些功能以共享数据，但是从性能和安全性上来说都不太合适。而登录校验是一项频繁访问的业务，服务器通常需要满足延迟低、高并发等要求，Redis则刚好符合这些要求

简单分析一下基于Redis实现登录的步骤，在发送验证码时，后端直接将验证码与手机号信息保存在Redis中，用户登录时从Redis中获取验证码进行验证，验证通过后，再将用户信息保存在Redis中，供接口调用。但这里会出现一个问题，Redis中用户信息的KEY该如何确定，这个KEY会保存在用户浏览器中，浏览器访问接口时携带这个KEY，拦截器拦截请求后，通过KEY访问并获取用户信息，再填入ThreadLocal中。因此KEY不能包含任何用户信息，KEY也需要保证唯一性，那么一串随机的字符串刚好可以作为KEY，这也让KEY伪造的难度大大提高。而目前主流的随机字符串中，UUID的使用相当广泛

#### 代码改造

在改造业务代码之前，我们先定以一个Redis的操作类，Redis属于数据库，所以Redis操作类一般属于DAO层，我们定义为RedisRepository

```java
package com.hmdp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final StringRedisTemplate stringRedisTemplate;

    public void set(String key, String value, Long timeout) {
        stringRedisTemplate.opsForValue().set(key, value);
        stringRedisTemplate.expire(key, Duration.ofSeconds(timeout));
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void hashSet(String key, String field, String value, Long timeout) {
        stringRedisTemplate.opsForHash().put(key, field, value);
        stringRedisTemplate.expire(key, Duration.ofSeconds(timeout));
    }

    public String hashGet(String key, String field) {
        return (String) stringRedisTemplate.opsForHash().get(key, field);
    }

    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    public void expire(String key, Long timout) {
        stringRedisTemplate.expire(key, Duration.ofSeconds(timout));
    }
}
```

在RedisRepository中，注入StringRedisTemplate，封装一些常用方法，并同时设置过期时间，避免KEY永远存在Redis中。然后就可以开始改造原始代码了

```java
@Override
public Result sendCode(String phone, HttpSession session) {
    // 检查手机号
    if (RegexUtils.isPhoneInvalid(phone)) {
        return Result.fail("手机号格式错误");
    }
    // 生成验证码
    String code = RandomUtil.randomNumbers(4);
    // 保存验证码
    redisRepository.set(RedisKeyConstant.LOGIN_CODE_KEY + phone, code, RedisKeyConstant.LOGIN_USER_TTL);
    // 发送验证码
    log.debug("发送验证码成功，验证码: {}", code);
    // 返回结果
    return Result.ok("发送成功，验证码5分钟内有效");
}
```

在发送验证码方法中，原来是将验证码保存在Session中，现在改为通过redisRepository保存在Redis中，以用户手机号为键，TTL规范为一个常量

```java
package com.hmdp.constants;

import cn.hutool.core.util.RandomUtil;

public class RedisKeyConstant {
    public static final String LOGIN_CODE_KEY = "login:code:";          // 登录验证码
    public static final Long LOGIN_USER_TTL = 5 * 60L + RandomUtil.randomLong(0, 60);                  // 登录验证码有效期

    public static final String USER_INFO_KEY = "user:info:";            // 用户信息
    public static final Long USER_INFO_TTL = 30 * 60L + RandomUtil.randomLong(0, 60);         // 用户信息保存有效期
}
```

常量中定义需要的键名和TTL，同时保证TTL包含随机值，避免同一时间失效大量KEY，造成雪崩

```java
@Transactional
@Override
public Result login(LoginFormDTO loginForm, HttpSession session) throws JsonProcessingException {
    String phone = loginForm.getPhone();
    if (RegexUtils.isPhoneInvalid(phone)) {
        return Result.fail("手机号格式错误");
    }
    String code = loginForm.getCode();
    String password = loginForm.getPassword();
    if (code != null && !code.isEmpty()) {
        // 验证码登录
        // 从Redis中获取验证码
        String cacheCode = redisRepository.get(RedisKeyConstant.LOGIN_CODE_KEY + phone);
        // 判断验证码是否一致
        if (cacheCode == null || !cacheCode.equals(code)) {
            return Result.fail("验证码错误");
        }

        // 验证通过，删除验证码
        redisRepository.delete(RedisKeyConstant.LOGIN_CODE_KEY + phone);
        // 查询用户
        User user = query().eq("phone", phone).one();
        if (user == null) {
            // 用户不存在，注册用户
            user = registerByPhone(phone);
        }

        // 登录成功，保存用户信息到Redis中
        // 生成token
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        // 将用户信息转换为JSON
        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .icon(user.getIcon())
                .nickName(user.getNickName())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userDTO);
        // 保存用户信息到Redis中
        redisRepository.set(RedisKeyConstant.USER_INFO_KEY + token, userJson, RedisKeyConstant.USER_INFO_TTL);
        // 返回token
        return Result.ok(token);

    } else if (password != null && !password.isEmpty()) {
        return Result.fail("密码登录功能未完成");
    } else {
        return Result.fail("密码或验证码不能为空");
    }
}
```

然后是登录方法，登录方法中验证通过，删除Redis中的验证码，然后将用户信息保存到Redis中，键为UUID随机数，最后返回token，这个token前端会作为请求头authorization的值，在后续请求中每次携带

> ![](javaweb2/324.png)

```java
@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    // 从请求头中获取token
    String token = request.getHeader("authorization");
    if (token == null) {
        response.setStatus(401);
        return false;
    }
    // 查询用户
    String userJson = redisRepository.get(RedisKeyConstant.USER_INFO_KEY + token);
    if (userJson == null) {
        response.setStatus(401);
        return false;
    }
    UserDTO user = new ObjectMapper().readValue(userJson, UserDTO.class);
    // 保存到上下文中
    UserHolder.saveUser(user);
    // 更新Redis中的用户信息TTL
    redisRepository.expire(RedisKeyConstant.USER_INFO_KEY + token, RedisKeyConstant.USER_INFO_TTL);
    // 放行
    return true;
}
```

最后是拦截器，拦截器不再拦截Session，而是获取请求头中的authorization头，通过authorization作为键名访问键值，如果没有则表示authorization过期或者伪造，从而返回401。最后将用户信息反序列化为UserDTO实体，最后放行

### 商户查询缓存

#### 代码改造

在黑马点评项目中，商户信息是一个高频访问的接口，所有的用户在选择商户时肯定都会访问商户信息的页面，因此我们需要为商户信息接口添加缓存

> ![](javaweb2/325.png)

```java
/**
 * 根据id查询商铺信息
 * @param id 商铺id
 * @return 商铺详情数据
 */
@GetMapping("/{id}")
public Result queryShopById(@PathVariable("id") Long id) {
    return Result.ok(shopService.getById(id));
}
```

追踪到原始代码，可以发现后端是直接调用IServer提供的getById方法，通过数据库返回数据，高并发情况下，这会对数据库服务造成很大的性能影响，因此我们将其改造为Redis缓存架构

首先来分析如何改造，Redis基于内存，拥有强大的高并发读写能力，因此在请求直达数据库之前，可以先在Redis处进行分流，我们将数据库中的数据缓存到Redis中，如果Redis拥有缓存，则直接返回，这就可以避免大量请求直接抵达数据库。即使Redis中没有数据，也可以先放行一个请求到达数据库，然后由后端将数据库中的数据缓存到Redis中，后续的请求在Redis处就可以直接返回

```java
@Override
public Result queryById(@NotNull Long id) {
    // 查询Redis中是否存在缓存
    String shopJson = redisRepository.get(RedisKeyConstant.CACHE_SHOP_INFO_KEY + id);
    // 缓存命中
    if (shopJson != null) {
        Shop shop = JSONUtil.toBean(shopJson, Shop.class);
        return Result.ok(shop);
    }
    // 缓存未命中，查询数据库
    Shop shop = this.getById(id);
    // 数据库中不存在，返回错误
    if (shop == null) {
        return Result.fail("店铺不存在");
    }
    // 数据库中存在，写入Redis缓存
    shopJson = JSONUtil.toJsonStr(shop);
    redisRepository.set(RedisKeyConstant.CACHE_SHOP_INFO_KEY + id, shopJson, RedisKeyConstant.CACHE_SHOP_INFO_TTL);
    // 返回结果
    return Result.ok(shop);
}
```

改造的代码与我们预先的设计基本相同，首先通过redisRepository获取Redis中对应KEY，如果缓存命中，则直接封装为shop实体然后返回，这里注意不要使用Jackson，该项目自然的Jackson版本可能过于老旧，不对LocalDateTime适配，这会导致序列化的数据中将LocalDateTime识别为一个额外的对象，从而进一步序列化LocalDateTime中的各种属性。缓存未命中，则从数据库中查询，如果数据库中也不存在数据，则认为该店铺不存在，返回错误。最后将数据库中的数据序列化为JSON，写入Redis，然后返回到前端

#### 缓存更新策略

缓存更新策略大致可以分为三种，内存淘汰、超时剔除和主动更新

| 更新策略 | 说明                                                         | 数据一致性 | 维护成本 |
| -------- | ------------------------------------------------------------ | ---------- | -------- |
| 内存淘汰 | 不用自己维护，利用Redis的内存淘汰机制，当内存不足时自动淘汰部分数据，下次查询时更新缓存 | 差         | 无       |
| 超时剔除 | 给缓存数据添加TTL时间，到期后自动删除缓存，下次查询时更新缓存 | 一般       | 低       |
| 主动更新 | 编写业务逻辑，在修改数据库的同时更新缓存                     | 好         | 高       |

对于低一致性要求的业务，可以使用内存淘汰机制，缓存不一致对业务几乎不产生影响；而对于高一致性要求的业务，则应当根据实际要求制定适当的缓存更新策略

主动更新一般可以分为三类，这在[缓存一致性](./JavaWebEnhance.md#缓存一致性)中也有阐述，这里简单概述一下。主动更新在企业中一般有三种模式，Cache Aside、Read/Write Through和Write Behind，Cache Aside要求开发人员独立完成缓存和数据库业务，而另外两种方式则不需要开发人员兼顾缓存和数据库，Read/Write Through侧重于无感知开发，将数据存储服务独立出来，业务开发人员不需要关心到底是缓存还是数据库，所有的存储细节由存储服务实现；Write Behind侧重于缓存存储，开发人员只操作缓存，数据库的存储由独立线程周期异步执行，强调最终一致性

一般来说，企业通常会选择Cache Aside模式，小部分业务选择Write Behind，而Read/Write Through基本不考虑

```java
@Override
@Transactional
public Result updateByIdWithCache(@NotNull Shop shop) {
    if (shop == null || shop.getId() == null) {
        return Result.fail("店铺不存在");
    }
    // 更新数据库
    boolean update = this.updateById(shop);
    if (!update) {
        throw new RuntimeException("更新店铺信息失败");
    }
    // 删除Redis缓存
    redisRepository.delete(RedisKeyConstant.CACHE_SHOP_INFO_KEY + shop.getId());
    return Result.ok();
}
```

我们为店铺信息缓存构建更新策略，在更新数据库后立即删除缓存，以保证数据一致性

### 缓存穿透

理论详见[缓存穿透](./JavaWebEnhance.md#缓存穿透)

#### 缓存空对象

对店铺信息缓存建立缓存穿透防御措施，采用缓存空对象方案

```java
@Override
public Result queryById(@NotNull Long id) {
    // 查询Redis中是否存在缓存
    String shopJson = redisRepository.get(RedisKeyConstant.CACHE_SHOP_INFO_KEY + id);
    // 缓存命中
    if (shopJson != null && !StrUtil.isBlank(shopJson)) {
        Shop shop = JSONUtil.toBean(shopJson, Shop.class);
        return Result.ok(shop);
    }
    // 缓存命中，但为空对象，返回错误
    if (StrUtil.isBlank(shopJson)) {
        return Result.fail("店铺不存在");
    }
    // 缓存未命中，查询数据库
    Shop shop = this.getById(id);
    // 数据库中不存在，建立缓存空对象，并返回
    if (shop == null) {
        redisRepository.set(RedisKeyConstant.CACHE_SHOP_INFO_KEY + id, "", RedisKeyConstant.CACHE_NONE_TTL);
        return Result.fail("店铺不存在");
    }
    // 数据库中存在，写入Redis缓存
    shopJson = JSONUtil.toJsonStr(shop);
    redisRepository.set(RedisKeyConstant.CACHE_SHOP_INFO_KEY + id, shopJson, RedisKeyConstant.CACHE_SHOP_INFO_TTL);
    // 返回结果
    return Result.ok(shop);
}
```

先前我们判断缓存是否命中是通过shopJson != null，一旦引入缓存空对象，就必须再判断字符串本身是否是空对象，然后再决定是否拦截。而对于缓存和数据都未命中的数据，先前是直接返回错误，现在需要建立一个该KEY的缓存空对象，TTL设置为空对象特定的短TTL，避免长时间占用内存，然后再返回错误

#### 布隆过滤

这里详细讲解一下布隆过滤，布隆过滤本质上是一种用 位数组 + 多个哈希函数 实现的空间高效、概率型数据结构，用于快速判断元素是否属于某个集合。它的特点是判断不存在绝对准确，判断存在可能有误判，但绝不会出现漏判
$$
\begin{array}{l}

\large \textbf{概念} \\

布隆过滤器核心由两部分构成 \\

\bullet \quad 一个二进制，也就是位数组，长度为m，初始值全为0 \\

\bullet \quad k个独立的哈希函数，分别记为h_1，h_2，h_3...，h_k，每个函数都可以将输入映射到[0,m)范围内的某一个位置 \\

\\

\large \textbf{插入} \\

当需要插入元素x时，使用k个哈希函数分别计算h_1(x)，h_2(x)，h_3(x)，...，h_k(x) \\

然后将数组中对应位置全部置为1 \\

例如，插入苹果apple，假设k=3，哈希结果为2，5，8，则bit[2]、bit[5]、bit[8]都置为1 \\

\\

\large \textbf{查询} \\

当查询元素y是否存在时，也按照这个顺序 \\

首先计算h_1(y)，h_2(y)，h_3(y)，...，h_k(y) \\

检查对应位置是否全部为1 \\

\bull \quad 如果任何一个位置为0 \Rightarrow 元素y一定不存在 \\

\bull \quad 如果全部为1 \Rightarrow 元素y可能存在，这些1可能是其他元素造成的碰撞 \\

\\

\large \textbf{关键数学公式} \\

误判率(False \ Positive \ Rate)的近似公式为 \\

\hfill p \approx (1-e^{-kn/m})^{k} \hfill \\ 

其中m为数组长度，n表示已插入的元素数量，k表示哈希函数个数 \\

从这个公式出发，我们可以得到最优哈希函数个数公式 \\

\hfill k = \frac{m}{n}\ln{2} \hfill \\

如果给定误判率p和元素数量n，所需数组长度公式 \\

\hfill m = -\frac{n\ln{p}}{(\ln{2})^2} \hfill
\end{array}
$$
当然，布隆过滤器并不是万能的，也有自己的一些优缺点

优点：布隆过滤器空间效率极高，同样存储一百万条数据仅占用约1.2MiB空间，而Redis的HashSet需要约50MiB；其次，查询速度快，布隆过滤器底层数据结构为数组，布隆过滤器通过数据下标查询元素，k个哈希函数的情况下，每个哈希函数进行一次查找，每个哈希函数的查询时间复杂度为O(1)，总计时间复杂度为O(k)；布隆过滤器可以绝对判定元素不存在，不存在漏判，而且布隆过滤器隐私性好，位数组中不包含任何原始数据

缺点：布隆过滤器存在误判风险，可能把不存在的元素判断为存在，从数学模型中可以知道误判率p随m增大而指数级下降，随n增大指数级上升，核心则取决于比值n/m；布隆过滤器本身不支持删除，因为多个元素的哈希结果可能相同，如果因为某一个元素而删除布隆数组，那么会直接导致其他元素也同样被认为删除；布隆过滤器不支持扩容，位数组长度在创建时就已经确定，元素超出预期会导致误判率大幅提高；哈希强依赖性，布隆过滤器的安全性直接受哈希函数安全性的约束，如果哈希函数本身不安全，生成大量重复结果，就会导致误判率大幅提高

**示例**

那么如何降低布隆过滤器的误判率呢？我们用一个例子来分析。假设Redis中需要存储的KEY最大值为100万个，，要求误判率不超过5%，经过公式可以计算得到最佳的数组长度为
$$
m = -\frac{1000000\ln{0.05}}{(\ln{2})^2} \approx 6235035
$$
然后计算最佳的哈希函数数量
$$
k = \frac{6235035}{1000000}\ln{2} \approx 4.32193
$$
也就是说，在误判率不超过5%，KEY最大值为100万的情况下，需要准备6235035位的数组以及5个哈希函数。然后我们将误判率降低到1%，计算得到m约等于9585058，k约等于7。可以看出，误判率的提升伴随着哈希函数与位数组的增长，其中位数组的增长幅度相当大，在误判率降低4%的情况下，位数组的长度需要增长约54%。总结为数学模型为
$$
m \approx 1,442,695 \times log_2(\frac{1}{p}) \\ k=log_2(\frac{1}{p})
$$
这是一种半衰结构，因此在实际业务中，不能过度追求误判率，需要考虑实际的机器性能与业务需求

