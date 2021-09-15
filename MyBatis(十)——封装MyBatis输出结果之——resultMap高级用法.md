@[TOC]
### 前言
前面我们初次体验了一下resultMap结果映射的使用，貌似挺简单啊，其实不然，真正的企业开发中我们需要将各种复杂的sql复杂数据（比如查询到几个表中数据）映射到一个结果集，下面我们看几个使用场景：

### 一、一对一映射
这次我们除了有student表之外，再添加一个classes班级表，查询学生信息时还要查出对应的班级信息。

**场景：查询学生信息时还要查出对应的班级信息**

classes表：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210505105222571.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)
student表，新增cid字段用来关联classes表：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210505105259457.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)
Classess实体类：
(注意实体类名称不要太简单，否则会有重复，比如class,classes等)

```java
public class Classess {
    private Integer id;
    private String name;
    // get/set
    }
```
Student实体类：

```java
public class Student {
    private Integer id;
    private String name;
    private String email;
    private Integer age;
    private Classess cla;

   // get/set
   }
```
#### 1、使用级联属性封装结果集
接口、方法定义：

```groovy
Student selectStudentById(Integer id);
```

mapper文件：

```xml
<!--
 联合查询，使用级联属性封装结果集
-->
<resultMap id="studentMap" type="com.macay.entity.Student">
    <!--主键类型使用id标签-->
    <id column="id" property="id" />
    <!--非主键类型使用result标签-->
    <result column="name" property="name"/>
    <result column="email" property="email"/>
    <result column="age" property="age"/>
    <!-- 级联属性
       cla: student对象的对象属性，大小写保持一致
    -->
    <result column="cid" property="cla.id"/>
    <result column="cname" property="cla.name"/>
</resultMap>
<select id="selectStudentById"  resultMap="studentMap">
    select s.id, s.name, s.email, s.age, c.id cid, c.name cname from student s, classes c where s.cid = c.id and
    s.id = #{id}
</select>
```
测试类：

```java
@Test
public void testSelectStudentsById() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    StudentDao mapper = sqlSession.getMapper(StudentDao.class);
    Student student = mapper.selectStudentById(1);
    System.out.println(student);
    sqlSession.close();
}
```
结果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210505110715360.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)
#### 2、使用association标签指定联合的javabean对象
mapper文件：

```xml
<!--
   使用association指定联合的javabean对象，封装结果
-->
<resultMap id="studentMap2" type="com.macay.entity.Student">
    <id column="id" property="id" />
    <result column="name" property="name"/>
    <result column="email" property="email"/>
    <result column="age" property="age"/>
    <!-- association可以指定联合的javabean对象
        property="cla"： 指定那个属性是联合的对象
        javaType：指定这个属性对象的类型（不能省略）
    -->
    <association property="cla" javaType="com.macay.entity.Classess">
        <!--
          column：数据库字段
          property：关联对象的属性
        -->
        <id column="cid" property="id" />
        <result column="cname" property="name"/>
    </association>
</resultMap>

<select id="selectStudentById2"  resultMap="studentMap">
    select s.id, s.name, s.email, s.age, c.id cid, c.name cname from student s, classes c where s.cid = c.id and
    s.id = #{id}
</select>
```
#### 3、使用association标签实现分步查询
分步查询的思路就是：我们先通过学生ID从学生表查出学生信息，获得班级的cid，然后使用cid去班级表中查询班级信息，然后将两次查询的结果封装。

使用前首先需要查询班级信息的接口、方法及mapper文件：

接口：

```java
Classess selectById(Integer id);
```
mapper文件：

```xml
<select id="selectById" resultType="com.macay.entity.Classess">
   select id, name from classes
</select>
```
下面我们定义student接口中的方法和mapper文件:

接口：

```java
Student selectStudentByStep(Integer id);
```
mapper文件：

```xml
<!-- 使用association分步查询：
  1、先按照学生ID查询学生信息。
  2、根据查询学生信息中的cid去班级表查询班级信息。
  3、把查出的班级设置到学生信息中。
-->
<resultMap id="studentMapByStep" type="com.macay.entity.Student">
    <id column="id" property="id" />
    <result column="name" property="name"/>
    <result column="email" property="email"/>
    <result column="age" property="age"/>
    <!--association定义关联对象的封装规则
      select:表示cla这属性对象是通过调用select里面的这个方法查询得到的。
      column:表示指定将哪一列的值传给上面这个方法。
      流程：使用select指定的方法（传入column指定的这列参数的值）查出对象，并指定给property指定的属性。
    -->
    <association property="cla" select="com.macay.dao.ClassessDao.selectById" column="cid">
    </association>
</resultMap>
<select id="selectStudentByStep" resultMap="studentMapByStep">
    select * from student where id = #{id}
</select>
```
测试类：

```java
@Test
public void testSelectStudentsByStep() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    StudentDao mapper = sqlSession.getMapper(StudentDao.class);
    Student student = mapper.selectStudentByStep(1);
    System.out.println(student);
    sqlSession.close();
}
```
结果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210505123021401.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)
可以看到执行了两次查询。


### 二、一对多映射
#### 1、基本使用
上面的这个例子中，一个学生关联一个班级信息，有些场景，一条数据会对应关联表中的多条数据，此时查询会有多个结果。比如查询某个班级信息的时候把班级中的学生信息也查出来，那如何处理这种一对多隐射的场景呢？

**场景：查询某个班级信息的时候把班级中的学生信息也查出来**

首先，为了能够存储一对多的数据，我们先对Classess类进行改造，如下：

```java
public class Classess {
    private Integer id;
    private String name;
    private List<Student> students;
    // get/set
}
```
接口、方法定义：

```java
Classess selectClassWithStudentById(Integer id);
```
mapper文件定义：

```xml
<resultMap id="classMap" type="com.macay.entity.Classess">
    <id column="id" property="id"/>
    <result column="name" property="name"/>
    <!--collection定义集合类型的属性的封装规则
       ofType：指定集合里面元素的类型
    -->
    <collection property="students" ofType="com.macay.entity.Student">
        <!--定义集合中元素的封装规则-->
        <id column="sid" property="id"/>
        <result column="sname" property="name"/>
        <result column="semail" property="email"/>
        <result column="sage" property="age"/>
    </collection>
</resultMap>
<select id="selectClassWithStudentById" resultMap="classMap" resultType="com.macay.entity.Classess">
   select c.id, c.name, s.id sid,s.name sname, s.age sage, s.email semail from classes c left join student s on
 c.id = s.cid where c.id = #{id}
</select>
```
测试类：

```java
@Test
public void testSelectStudentsById() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    ClassessDao mapper = sqlSession.getMapper(ClassessDao.class);
    Classess classess = mapper.selectClassWithStudentById(302);
    System.out.println(classess);
    sqlSession.close();
}
```
结果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210506215929905.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)
和一对一映射对比就很容易发现，此处就是把association改成了collection，然后将property设置成了集合属性students，其他的id和result的配置都是一样的。仔细想想其实也不难理解，collection用于配置一对多关系，对应的属性必须是对象的集合类型，因此是students。另外，resultMap只是为了配置数据库字段和实体属性的映射关系，因此其他都一样。同时能存储一对多的数据结构肯定也能存储一对一关系，所以一对一像是一对多的一种特例。collection支持的属性以及属性的作用和association完全相同，这里不再赘述。

#### 2、一对多的结果合并原理
我们先看一下查询所以班级信息和班级中的学生信息的例子：

SQL：

```java
SELECT c.id,c.NAME,s.id sid,s.NAME sname,s.age sage,s.email semail FROM
 classes c LEFT JOIN student s ON c.id=s.cid order by c.id
```
sql执行结果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210506224007368.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)
可以看到数据是有5条的。然后我们用mybatis 在处理结果：

接口、方法定义：

```java
List<Classess> selectAllClassWithStudent();
```
mapper文件，resultMap和上面的定义一致：

```xml
<select id="selectAllClassWithStudent" resultMap="classMap" >
    select c.id, c.name, s.id sid,s.name sname, s.age sage, s.email semail from classes c left join student s on
 c.id = s.cid order by c.id
</select>
```
测试类：

```java
@Test
public void testselectAllClassWithStudent() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    ClassessDao mapper = sqlSession.getMapper(ClassessDao.class);
    List<Classess> classess = mapper.selectAllClassWithStudent();
    System.out.println("classess size is :" + classess.size());
    for (Classess classs : classess) {
        System.out.println(classs);
    }
    sqlSession.close();
}
```
结果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/2021050622523597.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)
可以看到，最终mybatis执行的结果将数据封装成了3条。原来mybatis 在处理结果时， 会判断对象是否相同， 如果相同则会将结果与之前的结果进行合并。

那么， 结果是否相同是如何判断的呢？

**首先是通过 id的比较， 如果 id 没有配置则比较每一个字段（此时， 只要有一个字段不同的对象不同）。**

在映射配置中我们配置了id标签，：

```java
<id column="id" property="id"/>
```

而 id 属性就可以用来判断获取到的数据是否属于同一个对象。

在以上的例子中， 数据库中查询出来有两个 id=302， 则 mybatis 在处理结果时就可以知道这两条数据对应相同的对象， 从而将他们合并。

所有，**建议尽量配置 id 的属性， 如果没有这个属性， 则 mybatis 在进行结果合并时效率会低很多。**

### 三、多对多隐射
