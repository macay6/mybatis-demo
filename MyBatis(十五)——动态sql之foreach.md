@[TOC]
#### 前言
mybatis的foreach标签经常用于遍历集合，构建in条件语句或者批量操作语句。


#### 一、foreach构建in条件
foreach实现in集合或者数组是最简单常用的一种情况，语法如下：

```xml
< foreach collection="集合类型" open="开始的字符" close="结束的字符"
          item="集合中的成员" separator="集合成员之间的分隔符">
              #{item 的值}
</ foreach>
```
标签属性：
collection： 表示，循环的对象是 数组， 还是list集合。  **如果dao接口方法的形参是 数组， 
             collection="array" ,如果dao接口形参是List， collection="list"。**
open:循环开始时的字符。 
close：循环结束时字符。
item：集合成员， 自定义的变量。  
separator：集合成员之间的分隔符。 
#{item 的值}：获取集合成员的值。

##### 1、参数是一个历 List<简单类型>
场景：通过传入的一个用户id集合来查询出所有符合条件的用户。

接口：

```java
List<Student> selectByIdList(List<Integer> idList);
```

mapper文件：

```xml
<select id="selectByIdList" resultType="com.macay.entity.Student">
    select id, name, email, age from student
    where id in
    <foreach collection="list" item="ids" open="(" close=")" separator=",">
        #{ids}
    </foreach>
</select>
```
测试类：

```java
@Test
public void selectByIdList() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    StudentDao mapper = sqlSession.getMapper(StudentDao.class);
    List<Integer> list = new ArrayList<>();
    list.add(1);
    list.add(2);
    list.add(3);
    List<Student> students = mapper.selectByIdList(list);
    System.out.println(students);
    sqlSession.commit();
    sqlSession.close();
}
```
结果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/f1f6c2703e8a44feb55db11ff58350e2.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBATWFjYXk=,size_20,color_FFFFFF,t_70,g_se,x_16)
这里一定要注意，如果入参是集合类型，mapper文件里面的collection="list"是必须的，不能想当然的写成接口入参collection="idList"，否则会报下面的错误：
![在这里插入图片描述](https://img-blog.csdnimg.cn/a0e39746f839433384430f23801bb8cc.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBATWFjYXk=,size_20,color_FFFFFF,t_70,g_se,x_16)
另外，假如程序中没有对集合做判空处理，前面我们这种写法还是有问题的，在集合为null或者是集合为空集合时，会有语法错误，所已为了使程序更加健壮，我们还需要加上判空操作，mapper代码如下：

```xml
<select id="selectByIdList" resultType="com.macay.entity.Student">
    select id, name, email, age from student
    <if test="list !=null and list.size > 0">
        where id in
        <foreach collection="list" item="ids" open="(" close=")" separator=",">
            #{ids}
        </foreach>
    </if>
</select>
```
![在这里插入图片描述](https://img-blog.csdnimg.cn/2cbf53cbcb314d95b714d4f2f7d0790c.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBATWFjYXk=,size_20,color_FFFFFF,t_70,g_se,x_16)
可以看到，即使集合为空，也不会有语法错误。

##### 2、参数是一个 List<对象类型>
接口：

```java
List<Student> selectByIdList2(List<Student> students);
```
mapper文件：

```xml
<select id="selectByIdList2" resultType="com.macay.entity.Student">
    select id, name, email, age from student
    <if test="list !=null and list.size > 0">
        where id in
        <foreach collection="list" item="stu" open="(" close=")" separator=",">
            #{stu.id}
        </foreach>
    </if>
</select>
```
**注意：如果list里面是对象类型，在引用的时候使用的是”属性.属性“的方式，如stu.id。**

测试类：

```java
@Test
public void selectByIdList2() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    StudentDao mapper = sqlSession.getMapper(StudentDao.class);
    List<Student> list = new ArrayList<>();
    Student student = new Student();
    student.setId(1);
    Student student1 = new Student();
    student1.setId(2);
    list.add(student);
    list.add(student1);
    List<Student> students = mapper.selectByIdList2(list);
    System.out.println(students);
    sqlSession.commit();
    sqlSession.close();
}
```
结果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/530e88360b2e44cfa5796fa71c2a6a8b.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBATWFjYXk=,size_20,color_FFFFFF,t_70,g_se,x_16)

##### 3、参数是一个数组类型
接口：

```java
List<Student> selectByIdArray(Integer[] idArray);
```
mapper文件：

```xml
<select id="selectByIdArray" resultType="com.macay.entity.Student">
    select id, name, email, age from student
    where id in
    <foreach collection="array" item="ids" open="(" close=")" separator=",">
        #{ids}
    </foreach>
</select>
```
加上数组判空的处理：

```xml
<select id="selectByIdArray" resultType="com.macay.entity.Student">
    select id, name, email, age from student
    <if test="array !=null">
        where id in
        <foreach collection="array" item="ids" open="(" close=")" separator=",">
            #{ids}
        </foreach>
    </if>
</select>
```

测试类：

```java
@Test
public void selectByIdArray() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    StudentDao mapper = sqlSession.getMapper(StudentDao.class);
    List<Integer> list = new ArrayList<>();
    Integer[] ids = new Integer[3];
    list.add(1);
    list.add(2);
    list.add(3);
    Integer[] integers = list.toArray(ids);
    List<Student> students = mapper.selectByIdArray(integers);
    System.out.println(students);
    sqlSession.commit();
    sqlSession.close();
}
```
结果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/0e1df8dbb13a4c46a8753bf8e76f152b.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBATWFjYXk=,size_20,color_FFFFFF,t_70,g_se,x_16)


#### 二、foreach用于批量操作
##### 1、foreach实现批量插入
在mysql中，批量插入的语法如下：

```xml
insert into table_name (c1, c2, c3 ...) values (v1a, v2a, v3a), (v1b, v2b, v3b),(v1c, v2c, v3c)....
```
从待处理的部分可以看到，后面的值是一个循环体，因此可以通过foreach实现循环插入。

接口：

```java
int insertStudentBatch(List<Student> students);
```
mapper文件：

```xml
<insert id="insertStudentBatch">
    insert into student (id, name, email, age)
    values
    <foreach collection="list" separator="," item="stu">
        (#{stu.id}, #{stu.name}, #{stu.email}, #{stu.age})
    </foreach>
</insert>
```
测试类：

```java
@Test
public void insertStudentBatch() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    StudentDao mapper = sqlSession.getMapper(StudentDao.class);
    List<Student> list = new ArrayList<>();
    Student student = new Student();
    student.setId(22);
    student.setName("test20");
    student.setAge(20);
    student.setEmail("test20@qq.com");
    Student student1 = new Student();
    student1.setId(23);
    student1.setName("test21");
    student1.setAge(21);
    student1.setEmail("test21@qq.com");
    list.add(student);
    list.add(student1);
    if (CollectionUtils.isNotEmpty(list)) {
        int count = mapper.insertStudentBatch(list);
        System.out.println(count);
    }
    sqlSession.commit();
    sqlSession.close();
}
```
##### 2、foreach实现动态update
这里主要介绍，当参数是map时，foreach如何实现动态update。

当参数是Map类型的时候，foreach标签的index属性值对应的不是索引值，而是Map中的key,利用这个key可以实现动态update。

接口：
```java
int updateStudentByMap(Map<String, Object> map);
```
mapper文件：

```xml
<update id="updateStudentByMap" >
    update student set
    <foreach collection="_parameter" item="val" index="key" separator=",">
        ${key} = #{val}
    </foreach>
    where id = #{id}
</update>
```
注意，这里可以通过@param注解来指定参数名，也可以使用mybatis内部默认的_parameter作为该参数的key,所有XML中也使用了_parameter。

测试类：

```java
@Test
public void updateStudentByMap() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    StudentDao mapper = sqlSession.getMapper(StudentDao.class);
    Map<String, Object> map = new HashMap<>();
    map.put("id", 3);
    map.put("name", "test3");
    map.put("email", "test3@qq.com");
    if (MapUtils.isNotEmpty(map)) {
        int count = mapper.updateStudentByMap(map);
        System.out.println(count);
    }
    System.out.println(mapper.selectById(3));
    sqlSession.commit();
    sqlSession.close();
}
```
结果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/6166861ccd3645038acc5eb87de5981f.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBATWFjYXk=,size_20,color_FFFFFF,t_70,g_se,x_16)
