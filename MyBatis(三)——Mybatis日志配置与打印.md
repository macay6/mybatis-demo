### 前言

> 我们在测试SQL的时候，要是能够在控制台输出 SQL 的话，是不是就能够有更快的排错效率？
如果一个数据库相关的操作出现了问题，我们可以根据输出的SQL语句快速排查问题。
对于以往的开发过程，我们会经常使用到debug模式来调节，跟踪我们的代码执行过程。但是现在使用Mybatis是基于接口，配置文件的源代码执行过程。因此，我们必须选择日志工具来作为我们开发，调节程序的工具。

Mybatis 通过使用内置的日志工厂提供日志功能。内置日志工厂将会把日志工作委托给下面的实现之一：
- SLF4J
- Apache Commons Logging
- Log4j 2
- Log4j
- JDK logging

MyBatis 内置日志工厂会基于运行时检测信息选择日志委托实现。它会（按上面罗列的顺序）使用第一个查找到的实现。当没有找到这些实现时，将会禁用日志功能。

我们暂时需要知道的有两个：STDOUT_LOGGING和LOG4J。


### 一、标准日志实现（STDOUT_LOGGING）
STDOUT_LOGGING是属于比较标准的那种，打开的方式也是比较简单的，在mybatis-config.xml中配置settings标签。

```xml
 <settings>
        <setting name="logImpl" value="STDOUT_LOGGING"/>
  </settings>
```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210502215237689.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)
测试，可以看到控制台有大量的输出！我们可以通过这些输出来判断程序到底哪里出了Bug。
![在这里插入图片描述](https://img-blog.csdnimg.cn/2021050221542388.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)

### 二、LOG4J
相较于比较标准的日志输出，但很多时候我们用的是LOG4J，这个可以进行一些配置。
- Log4j是Apache的一个开源项目
- 通过使用Log4j，我们可以控制日志信息输送的目的地：控制台，文本，GUI组件…
- 我们也可以控制每一条日志的输出格式；
- 通过定义每一条日志信息的级别，我们能够更加细致地控制日志的生成过程。最令人感兴趣的就是，这些可以通过一个配置文件来灵活地进行配置，而不需要修改应用的代码。

**使用步骤**
#### 1、导入log4j的包

```xml
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.12</version>
        </dependency>
```
#### 2、配置文件编写
在resources中新建一个log4j.properties文件，内容如下：

```yaml
#将等级为DEBUG的日志信息输出到console和file这两个目的地，console和file的定义在下面的代码
log4j.rootLogger=DEBUG,console,file

#控制台输出的相关设置
log4j.appender.console = org.apache.log4j.ConsoleAppender
log4j.appender.console.Target = System.out
log4j.appender.console.Threshold=DEBUG
log4j.appender.console.layout = org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern=[%c]-%m%n

#文件输出的相关设置
log4j.appender.file = org.apache.log4j.RollingFileAppender
# 日志输出的文件
log4j.appender.file.File=./log/mybatis.log
log4j.appender.file.MaxFileSize=10mb
log4j.appender.file.Threshold=DEBUG
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=[%p][%d{yy-MM-dd}][%c]%m%n

#日志输出级别
log4j.logger.org.mybatis=DEBUG
log4j.logger.java.sql=DEBUG
log4j.logger.java.sql.Statement=DEBUG
log4j.logger.java.sql.ResultSet=DEBUG
log4j.logger.java.sql.PreparedStatement=DEBUG
```

#### 3、setting设置日志实现

```yaml
    <!--设置日志-->
    <settings>
        <setting name="logImpl" value="LOG4J"/>
    </settings>
```

执行效果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210502221753152.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)
我们可以看到：这个控制台的输出和那个标准的导师没多大区别，本质上都是jdbc的操作，只是封装了起来，并且根据我们的配置这个还生成了日志文件。

#### 4、在程序中使用Log4j进行输出！
我们配置的log4j日志还可以用来在java代码中打印日志：

```java
//注意导包：org.apache.log4j.Logger
static Logger logger = Logger.getLogger(MyTest.class);

@Test
public void selectUser() {
   logger.info("info：进入selectUser方法");
   logger.debug("debug：进入selectUser方法");
   logger.error("error: 进入selectUser方法");
   SqlSession session = MybatisUtils.getSession();
   UserMapper mapper = session.getMapper(UserMapper.class);
   List<User> users = mapper.selectUser();
   for (User user: users){
       System.out.println(user);
  }
   session.close();
}

```
