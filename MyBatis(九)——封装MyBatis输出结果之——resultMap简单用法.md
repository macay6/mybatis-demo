@[TOC]

#### 1、实体类属性名和列名不同现象
前面我们看的例子都是实体类属性名和列名相同的情况，我们来构造一个不一样的场景，另外一个实体类的属性名和数据库表列名不一致：

实体类：

```java
public class NewStudent {
    private Integer sid;
    private String sName;
    private String email;
    private Integer age;

    // get/set方法
```
数据库student表：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210504224431628.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)
可以看到，只有email和age两个属性是一致的，其他两个属性不一致。我们使用mybatis查询看下效果：

mapper文件：

```xml
<select id="selectById" resultType="com.macay.entity.NewStudent">
   select id, name, email, age from student where id = #{id}
</select>
```
这里要注意，新增了mapper文件要在主配置文件里面配置：

```xml
<mappers>
    <mapper resource="com/macay/dao/StudentDao.xml"/>
    <mapper resource="com/macay/dao/NewStudentDao.xml"/>
</mappers>
```

测试类：

```java
@Test
public void testSelectById() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    NewStudentDao mapper = sqlSession.getMapper(NewStudentDao.class);
    NewStudent newStudent = mapper.selectById(1);
    System.out.println(newStudent);
    sqlSession.close();
}
```
执行结果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210504224912589.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)
可以看到，虽然sql执行不会报错，但是实体类未匹配到数据库字段的属性都是空值。如何解决这个问题 呢，有下面两种方法：

####  2、处理方式一： 使用列别名
我们可以给要查询的字段起别名，使列别名与对象字段一致：

```xml
<select id="selectById" resultType="com.macay.entity.NewStudent">
   select id sid, name sName, email, age from student where id = #{id}
</select>
```
执行结果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210504225742435.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)
可以看到所有字段都被赋值。

#### 3、处理方式二：使用resultMap
resultMap 元素是 MyBatis 中最重要最强大的元素，可以自定义 sql 的结果和 java 对象属性的映射关系。它可以将查询到的复杂数据（比如查询到几个表中数据）映射到一个结果集当中。**常用在列名和 java 对象属性名不一样的情况**。

使用方式：

1.先定义 resultMap,指定列名和属性的对应关系。
2.在<select>中把 resultType 替换为 resultMap。

还是上面这个例子，mapper文件定义：

```xml
<resultMap id="newStuMap" type="com.macay.entity.NewStudent">
    <!--定义列名和属性名的对应-->
    <!--column的值为数据库字段，property的值为对象的属性-->
    <!--主键类型使用id标签-->
    <id column="id" property="sid" />
    
    <!--非主键类型使用result标签-->
    <result column="name" property="sName"/>
    
    <!--列名和属性名相同可以不用定义，但一般建议只要使用了resultMap,所有属性都加上-->
    <result column="email" property="email"/>
    <result column="age" property="age"/>
</resultMap>

<!--使用resultMap属性，指定映射关系的id
    resultMap和resultType 不能同时使用， 二选一。
-->
<select id="selectById2" resultMap="newStuMap">
    select id, name, email, age from student where id = #{id}
</select>
```
测试类：

```java
@Test
public void testSelectById2() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    NewStudentDao mapper = sqlSession.getMapper(NewStudentDao.class);
    NewStudent newStudent = mapper.selectById2(1);
    System.out.println(newStudent);
    sqlSession.close();
}
```
结果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210504232959221.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)
