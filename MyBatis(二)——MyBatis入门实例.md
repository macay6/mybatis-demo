@[TOC]
### 一、创建student表（id，name，email，age）
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210502124528579.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)
### 二、新建maven项目、修改pom.xml文件
#### 1、设置编码方式
#### 2、加入依赖 mybatis依赖， mysql驱动， junit单元测试
#### 3、加入资源插件

```xml
  <!--设置源代码的编码方式-->
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <dependencies>
    <!--mybatis框架-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.6</version>
        </dependency>
    <!--mysql驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.27</version>
        </dependency>
    <!--junit驱动-->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <!--资源插件： 处理src/main/java目录中的xml-->
        <resources>
            <resource>
                <directory>src/main/java</directory><!--所在的目录-->
                <includes><!--包括目录下的.properties,.xml 文件都会扫描到-->
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>false</filtering>
            </resource>
        </resources>
    </build>
```
如果不配置资源插件，后面xml文件是找不到的：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210502202042898.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)

#### 3、创建实体类Student。定义属性， 属性名和列名保持一致

```java
public class Student {
    private Integer id;
    private String name;
    private String email;
    private Integer age;
}
```
#### 4、创建Dao接口， 定义操作数据库的方法

```java
public interface StudentDao {

    Student selectStudentById(Integer id);
}
```
#### 5、创建xml文件（mapper文件）， 写sql语句
mybatis官方文档：[https://mybatis.org/mybatis-3/zh/configuration.html](https://mybatis.org/mybatis-3/zh/configuration.html)

```java
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.macay.dao.StudentDao">

<!--
  1.约束文件
    http://mybatis.org/dtd/mybatis-3-mapper.dtd
    约束文件作用： 定义和限制当前文件中可以使用的标签和属性，以及标签出现的顺序。

  2.mapper是根标签
    namespace： 命名空间，必须有值，不能为空。唯一值。
                推荐使用Dao接口的全限定名称(全路径)。
    作用： 参与识别sql语句的作用。

  3.在mapper里面可以写 <insert>,<update>,<delete>,<select>等标签。
    <insert>里面是 insert语句，表示执行的insert操作
    <update>里面是 update语句
    <delete>里面是 delete语句
    <select> 里面是 select语句
-->

    <!--查询一个学生Student

    <select>：表示查询操作， 里面是select 语句
    id: 要执行的sql语句的唯一标识， 是一个自定义字符串。
        推荐使用dao接口中的方法名称
    resultType:告诉mybatis，执行sql语句，把数据赋值给那个类型的java对象。
          resultType的值现在使用的java对象的全限定名称


    #{studentId}:占位符， 表示从java程序中传入过来的数据
-->


    <select id="selectStudentById" resultType="com.macay.entity.Student">
        select id, name, email, age from student where id =100
    </select>
</mapper>

```
#### 6、创建mybatis的主配置文件（xml文件）：仅有一个， 放在resources目录下
1.定义创建连接实例的数据源（DataSource）对象

2.指定其他mapper文件的位置

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--设置日志-->
    <settings>
        <setting name="logImpl" value="STDOUT_LOGGING"/>
    </settings>

    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <!--配置数据源： 创建Connection对象。-->
            <dataSource type="POOLED">
                <!--driver:驱动的内容-->
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <!--连接数据库的url-->
                <property name="url"
                          value="jdbc:mysql://localhost:3306/mybatis?useUnicode=true&amp;characterEncoding=utf-8"/>
                <!--用户名-->
                <property name="username" value="root"/>
                <!--密码-->
                <property name="password" value="123456"/>
            </dataSource>
        </environment>
    </environments>

    <!--指定其他mapper文件的位置：
        其他其他mapper文件目的是找到其他文件的sql语句
    -->
    <mappers>
        <!--
           使用mapper的resource属性指定mapper文件的路径。
           这个路径是从target/classes路径开启的

           使用注意：
              resource=“mapper文件的路径，使用 / 分割路径”
              一个mapper resource 指定一个mapper文件,若有多个，需要些多个mapper标签
        -->
        <mapper resource="com/macay/dao/StudentDao.xml"/>
        <!--<mapper resource="com/bjpowernode/dao/OrderDao.xml"/>
        <mapper resource="com/bjpowernode/dao/UserDao.xml"/>-->
    </mappers>
</configuration>

```
#### 7、创建测试的内容

```java
public class MyTest {
    @Test
    public void testSelectById() throws IOException {
        //调用mybatis某个对象的方法，执行mapper文件中的sql语句
        //mybatis核心类： SqlSessionFactory

        //1.定义mybatis主配置文件的位置, 从类路径开始的相对路径
        String config="mybatis.xml";
        //2.读取主配置文件。使用mybatis框架中的Resources类
        InputStream inputStream = Resources.getResourceAsStream(config);
        //3.创建SqlSessionFactory对象， 使用SqlSessionFactoryBuidler类
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);

        //4.获取SqlSession对象。
        SqlSession session = factory.openSession();

        //5.指定要执行的sql语句的 id
        //  sql的id = namespace+"."+ select|update|insert|delete标签的id属性值
        String sqlId = "com.macay.dao.StudentDao"+"."+"selectStudentById";

        // 6.通过SqlSession的方法，执行sql语句
        Student student = session.selectOne(sqlId);
        System.out.println("使用mybatis查询一个学生："+student);

        // 7.关闭SqlSession对象
        session.close();
    }
}
```
结果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210502202530567.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)
#### 8、MyBatis的一些重要对象
（1）、 Resources ： mybatis框架中的对象， 一个作用读取 主配置信息。

```java
InputStream inputStream = Resources.getResourceAsStream("mybatis.xml");
```
（2）、SqlSessionFactoryBuilder：负责创建SqlSessionFactory对象

```java
SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
```
（3）、SqlSessionFactory: 重要对象

SqlSessionFactory是重量级对象：创建此对象需要使用更多的资源和时间。 在项目中有一个就可以了。
DefaultSqlSessionFactory实现类

```java
public class DefaultSqlSessionFactory implements SqlSessionFactory { } 
```

SqlSessionFactory接口：作用是SqlSession的工厂， 就是创建SqlSession对象。

SqlSessionFactory接口中的方法

openSession(): 获取一个默认的SqlSession对象， 默认是需要手工提交事务的。
openSession(boolean): boolean参数表示是否自动提交事务。
​ true： 创建一个自动提交事务的SqlSession
​ false: 等同于没有参数的openSession

（4）、SqlSession对象

SqlSession对象是通过SqlSessionFactory获取的。 SqlSession本身是接口
DefaultSqlSession: 实现类

```java
public class DefaultSqlSession implements SqlSession { }
```
**SqlSession作用是提供了大量的执行sql语句的方法：**

> selectOne：执行sql语句，最多得到一行记录，多余1行是错误。
selectList：执行sql语句，返回多行数据
selectMap：执行sql语句的，得到一个Map结果
insert：执行insert语句
update：执行update语句
delete：执行delete语句
commit：提交事务
rollback：回顾事务


注意SqlSession对象不是线程安全的， 使用的步骤：

①：在方法的内部，执行sql语句之前，先获取SqlSession对象

②：调用SqlSession的方法，执行sql语句

③：关闭SqlSession对象，执行SqlSession.close()
