@[TOC]
### 一、# 占位符
#### 1、使用
语法： #{字符}

使用方式如下：

```xml
<select id="selectById"  parameterType="integer"
             resultType="com.bjpowernode.domain.Student">
    select id,name,email,age from student where id=#{studentId}
</select>
```
mybatis处理#{} 使用jdbc对象是 PrepareStatment对象：

```java
// mybatis出创建PrepareStatement对象，执行sql语句
String sql=" select id,name,email,age from student where id=?";
PrepareStatement pst = conn.prepareStatement(sql);
pst.setInt(1,1001);  //传递参数
ResultSet rs  = pst.executeQuery(); //执行sql语句
```
#### 2、#特点：

 1. 使用的PrepareStatement对象，执行sql语句，效率高。
 2. 使用的PrepareStatement对象，能预编译，避免sql注入， sql语句执行更安全。
 3. #{} 常常作为 列值使用的， 位于等号的右侧， #{}位置的值和数据类型有关的。#将传入的数   据都当成一个字符串，会对自动传入的数据加一个双引号。
 
### 二、$ 占位符
#### 1、使用
语法 : ${字符}

```xml
<select id="selectById"  parameterType="integer"
             resultType="com.bjpowernode.domain.Student">
    select id,name,email,age from student where name=${studentName}
</select>
```

mybatis处理$ 占位符使用jdbc对象是 Statment对象，${} 表示字符串连接， 把sql语句的其他内容和 ${}内容使用 字符串（+） 连接的方式连在一起

```java
String sql="select id,name,email,age from student where name=" + "lisi";
// mybatis创建Statement对象， 执行sql语句。
Statement stmt  = conn.createStatement(sql);
ResultSet rs  = stmt.executeQuery();
```
如果我们使用之前的传值方式：

```java
Student students = mapper.selectByName("lisi");
```
可以看到lisi将原样拼接到SQL中，缺失引号，执行也会报错：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210504092539200.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)
我们需要自行拼接引号才能使用：

```java
Student students = mapper.selectByName("‘lisi’");
```


#### 2、$特点

 1. 使用Statement对象，执行sql语句，效率低。
 2. ${}占位符的值，使用的字符串拼接方式， 有sql注入的风险。 有代码安全的问题。
 3. ${} 数据是原样使用的， 不会区分数据类型。
 4. 常用作 表名或者列名， 在能保证数据安全的情况下使用。

####  3、$的正确使用场景（表名和列名）
实现下面这个需求：

从不同的表中通过某个字段查询数据，并且通过某个字段排序。

接口、方法定义：

```java
List<Student> selectStudentOrderByColumn(@Param("name") String name, @Param("column") String column,
@Param("tableName") String tableName);
```
mapper文件：

```xml
    <!--
       ${}常用作表名或列名
    -->
    <select id="selectStudentOrderByColumn" resultType="com.macay.entity.Student">
       select id, name, email, age from ${tableName} where name = #{name} order by ${column}
    </select>
```
测试类：

```java
    @Test
    public void selectStudentOrderByColumn() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        List<Student> students = mapper.selectStudentOrderByColumn("macay","age","student");
        students.forEach(student -> System.out.println(student));
        sqlSession.close();
    }
```
### 三、区别总结

 1、 #使用的PrepareStatement对象，执行sql语句，效率高。能预编译，避免sql注入， sql语句执行更安全。$使用Statement对象，执行sql语句，效率低，相当于字符串拼接方式， 有sql注入的风险。 有代码安全的问题。

2、#{} 常常作为 列值使用的， 位于等号的右侧，传入的数   据都当成一个字符串。而$常用作 表名或者列名。

3、#{} 如果传递的参数是简单类型，里面的内容任意。$如果传递的是简单类型，里面的值必须是value。


### 四、使用#或者说PrepareStatement如何防止SQL攻击？
比如说根据学生姓名查学生信息，会传入一个name的参数，假设学生姓名是方方，那么Sql就是：

```java
SELECT id,name,age FROM student WHERE name = '方方';
```
在没有做防Sql注入的时候，我们的Sql语句可能是这么写的：

```html
<select id="selectStudentByName" resultType="entity.Student"> 
   SELECT id,name,age FROM student WHERE name = '${value}' 
</select>
```
但如果我们对传入的姓名参数做一些更改，比如改成anything' OR 'x'='x，那么拼接而成的Sql就变成了：

```html
SELECT id,name,age FROM student WHERE name = 'anything' OR 'x'='x'
```
库里面所有的学生信息都被拉了出来，是不是很可怕。原因就是传入的anything’ OR ‘x’='x和原有的单引号，正好组成了 ‘anything’ OR ‘x’='x’，而OR后面恒等于1，所以等于对这个库执行了查所有的操作。

防范Sql注入的话，就是要把整个anything’ OR ‘x’='x中的单引号作为参数的一部分，而不是和Sql中的单引号进行拼接.

使用了#即可在Mybatis中对参数进行**转义**:

```html
<select id="selectStudentByName" resultType="entity.Student"> 
   SELECT id,name,age FROM student WHERE name = #{name} 
</select>
```
我们看一下发送到数据库端的Sql语句长什么样子。

```html
SELECT id,name,age FROM student WHERE name = 'anything\' OR \'x\'=\'x'
```
从上述代码中我们可以看到参数中的所有单引号统统被转义了，这都是JDBC中PrepareStatement的功劳，如果在数据库服务端开启了预编译，则是服务端来做了这件事情。
