@[TOC]
封装输出结果： 即MyBatis执行sql语句，得到ResultSet, 转为java对象。
常用的有两个：resultType, resultMap。
这里我们先说**resultType**。
#### 基本介绍
resultType属性： 在执行select时使用， 作为~<select>~标签的属性出现的。
resultType:表示结果类型 ，  mysql执行sql语句，得到java对象的类型， 规则是**同名列赋值给同名属性，匹配不到的属性会是空值**。注意如果返回的是集合，那应该设置为集合包含的类型，而不是集合本身。

它的值有两种：

- java类型的全限定名称
- 使用别名

#### 1、使用java类型的全限定名称（推荐使用）
接口、方法定义：

```java
Student selectById(Integer id);
```
mapper文件：

```xml
<select id="selectById"  resultType="com.macay.entity.Student">
    select id,name,email,age from student where id=#{studentId}
</select>
```
resultType使用java类型的全限定名称。 表示的意思 mybatis执行sql，把ResultSet中的数据转为Student类型的对象。  mybatis会做以下操作：

 1. 调用com.macay.entity.Student的无参数构造方法，创建对象。Student student = new Student(); //使用反射创建对象。
 2. 同名的列赋值给同名的属性。    
     student.setId( rs.getInt("id"));
     student.setName(rs.getString("name"));
  
 3. 得到java对象， 如果d**ao接口返回值是List集合， mybatis把student对象放入到List集合。**

所以执行 Student mystudent = dao.selectById(1001); 得到 数据库中 id=1001这行数据， 这行数据的列值， 赋给了mystudent对象的属性。 你能得到mystudent对象。 就相当于是 id=1001这行数据。

#### 2、使用别名（不常用）
基本类型使用mybaits自带的别名机制，可以参考前面一篇文章。对象累着使用别名的形式需要我们在mybatis主配置文件进行配置，使用typeAliases标签。
typeAliases标签使用时要注意标签的位置，在setting标签之后，点击configuration标签可以查看标签位置：

```xml
<!ELEMENT configuration (properties?, settings?, typeAliases?, typeHandlers?, objectFactory?, objectWrapperFactory?, reflectorFactory?, plugins?, environments?, databaseIdProvider?, mappers?)>
```

##### 1、第一种语法格式：自定义别名
主配置文件mybatis.xml：
```xml
<!--声明别名-->
<typeAliases>
        <!--第一种语法格式
            type:java类型的全限定名称（自定义类型）
            alias:自定义别名
            优点： 别名可以自定义
            缺点： 每个类型必须单独定义
        -->
        <typeAlias type="com.macay.entity.Student" alias="stu" />
        <typeAlias type="com.macay.entity.QueryParam" alias="qp" />
 </typeAliases>
```
mapper文件：

```xml
<select id="selectById"  resultType="stu">
    select id,name,email,age from student where id=#{studentId}
</select>
```
##### 2、第一种语法格式：类名
主配置文件：

```xml
    <!--声明别名-->
    <typeAliases>
        <!--第二种方式
            name:包名， mybatis会把这个包中所有类名作为别名（不用区分大小写）
            优点：使用方便，一次给多个类定义别名
            缺点: 别名不能自定义，必须是类名。若存在不同包的同名类，程序无法区分会报错。
        -->
        <package name="ccom.macay.entity" />
        <package name="com.macay.vo" />
    </typeAliases>
```
mapper文件：

```xml
<select id="selectById"  resultType="student">
    select id,name,email,age from student where id=#{studentId}
</select>
```
#### 3、resultType表示简单类型
简单类型比较简单：

接口、方法定义：

```java
int countStudent();
```
mapper文件：

```xml
<select id="countStudent" resultType="int">
    select count(*) from student
</select>
```
#### 4、resultType表示对象类型
其实我们前面说别名的时候就一直使用的是对象类型，如果dao接口返回值是一个对象类型，我们使用如下：

接口、方法定义：

```java
Student selectStudentById(Integer id);
```
mapper文件：

```xml
<select id="selectStudentById"  resultType="com.macay.entity.Student">
    select id, name, email, age from student where id = #{id}
</select>
```
如果dao接口**返回的是一个集合类型，需要指定集合中的类型**，不是集合本身：

```java
List<Student> selectStudents();
```
mapper文件：

```xml
<select id="selectStudents" resultType="com.macay.entity.Student">
    select id, name, email, age from student
</select>
```
#### 5、resultType表示Map
我们可以将sql 的查询结果作为 Map 的 key 和 value。推荐使用Map<Object,Object>。
##### 1、查询结果只有一条
当sql 语句的查询结果最多只能有一条记录，resultType为map，Map 作为dao接口返回值。

接口、定义方法：

```java
Map<String, Object> selectStudentById2(Integer id);
```
mapper文件：

```xml
<select id="selectStudentById2" resultType="map">
    select id, name, email, age from student where id = #{id}
</select>
```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210504184247847.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)
可以看到，打印出来的就是一个key,value形式的map集合。

##### 2.1、查询结果有多条——List<Map<String, Object>>形式
当sql 语句的查询结果有多条记录，resultType为map，List<Map<String, Object>>作为dao接口返回值。

接口、定义方法：

```java
List<Map<String, Object>> selectStudentByAge(Integer id);
```
mapper文件：

```xml
<select id="selectStudentByAge" resultType="map">
    select id, name, email, age from student where age = #{id}
</select>
```
测试类如下：

```java
    @Test
    public void testselectStudentByAge() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        List<Map<String, Object>> list = mapper.selectStudentByAge(20);
        for (Map<String, Object> map : list) {
            System.out.println(map);
        }
        sqlSession.close();
    }
```
结果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210504185132275.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)
可以看到，打印出来的就是一个List<Map<String, Object>>集合。

##### 2.2、查询结果有多条——Map<Integer, Student>形式
当我们有这样的需求，希望查询结果是Map<Integer, Student>这样的形式，比如key是一个学生的ID（student任何字段都可以），value是这个学生对象。这时候我们这样来使用：

接口、定义方法：

```java
@MapKey("id")
Map<Integer, Student> selectStudentByAge2(Integer id);
```
@MapKey这个注解用于告诉mybatis封装map集合的时候key使用对象的那个属性。

mapper文件：

```xml
<select id="selectStudentByAge2" resultType="map">
    select id, name, email, age from student where age = #{id}
</select>
```
测试类：

```java
    @Test
    public void testselectStudentByAge2() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Map<Integer, Student> map = mapper.selectStudentByAge2(20);
        System.out.println(map);
        sqlSession.close();
    }
```
结果：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210504211053763.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)
