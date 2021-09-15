@[TOC]

关于模糊查询，我们介绍三种方式，像使用$ {} 这种形式虽然也可以实现功能，但是会引起SQL注入，这里就不再赘述了，推荐使用第三种形式。

#### 1、在程序中拼接
在java程序中，把like的内容组装好。 把这个内容传入到sql语句。

接口。方法定义：

```java
List<Student> selectStudentLikeName(String name);
```
mapper文件：

```xml
<select id="selectStudentLikeName" resultType="com.macay.entity.Student">
    select * from student where name like #{name}
</select>
```
测试类：

```java
@Test
public void selectStudentLikeName() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    StudentDao mapper = sqlSession.getMapper(StudentDao.class);
    String str = "m";
    // String name = "%" + str + "%";
    String name = new StringBuffer("%").append(str).append("%").toString();
    // name = "%m%"
    List<Student> students = mapper.selectStudentLikeName(name);
    System.out.println(students);
    sqlSession.close();
}
```
#### 2、sql语句中使用空格拼接

sql语句like的格式：  where name like "%"空格#{name}空格"%"

接口。方法定义：

```java
List<Student> selectStudentLikeName2(String name);
```
mapper文件：

```xml
<select id="selectStudentLikeName2" resultType="com.macay.entity.Student">
    select * from student where name like "%" #{name} "%"
</select>
```
测试类：

```java
@Test
public void selectStudentLikeName2() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    StudentDao mapper = sqlSession.getMapper(StudentDao.class);
    List<Student> students = mapper.selectStudentLikeName2("m");
    System.out.println(students);
    sqlSession.close();
}
```
#### 3、sql语句中使用CONCAT()函数连接参数形式
在MySQL中我们也可以使用CONCAT()拼接字符串：

sql语句like的格式：  where name like CONCAT('%',#{name},'%')
接口。方法定义：

```java
List<Student> selectStudentLikeName3(String name);
```
mapper文件：

```xml
<select id="selectStudentLikeName3" resultType="com.macay.entity.Student">
    select * from student where name like CONCAT('%',#{name},'%')
</select>
```
测试类：

```java
@Test
public void selectStudentLikeName3() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    StudentDao mapper = sqlSession.getMapper(StudentDao.class);
    List<Student> students = mapper.selectStudentLikeName3("m");
    System.out.println(students);
    sqlSession.close();
}
}
```
#### 注意：
MySQL的concat支持多个参数，但是Oracle的concat只支持两个参数，所有要是数据库是Oracle，相应的SQL也就要改变成：CONCAT(CONCAT(a,b), c) 这种形式。

```xml
<select id="selectStudentLikeName3" resultType="com.macay.entity.Student">
    select * from student where name like CONCAT(CONCAT('%', #{name}), '%')
</select>
```
