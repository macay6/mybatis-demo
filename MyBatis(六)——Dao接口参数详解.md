### 一、parameterType
parameterType:表示参数的类型， 指定dao方法的形参数据类型。 在mybatis映射接口的配置中,有select,insert,update,delete等元素都涉及了parameterType的用法，parameterType为输入参数，在配置的时候，配置相应的输入参数类型即可。parameterType有基本数据类型和复杂的数据类型配置。

**注意：parameterType：mybatis通过反射机制可以获取 dao接口方法参数的类型， 可以不写。**

#### 1、两种用法
1、第一个用法： java类型的全限定类型名称   parameterType="java.lang.Integer"

```xml
<select id="selectStudentById" parameterType="java.lang.Integer" resultType="com.macay.entity.Student">
        select id, name, email, age from student where id = #{id}
    </select>
```
  2、第二个用法： mybatis定义的java类型的别名  parameterType="int"
 

```xml
<select id="selectStudentById" parameterType="integer" resultType="com.macay.entity.Student">
        select id, name, email, age from student where id = #{id}
    </select>
```
常见别名如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210503192943542.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)
### 二、dao接口方法是一个简单类型的参数
 当dao接口方法参数是一个简单类型的参数，简单类型包括java基本数据类型和String，mapper文件获取这个参数值，使用#{任意字符}

接口、方法定义：

```java
// dao接口的方法形参是一个简单类型的
Student selectByEmail(String email);
```
mapper文件:

```xml
 <!--
    dao接口是一个简单类型的参数
    mapper文件，获取这个参数值，使用#{任意字符}
  -->
<select id="selectByEmail"  resultType="com.macay.entity.Student">
    select id, name, email, age from student where email = #{studentemail}
 </select>
```
### 三、dao接口方法有多个简单类型的参数，使用@Param
当dao接口方法参数是多个简单类型的参数时，我们要使用@Param注解区分。
@Param: 命名参数， 在方法的形参前面使用的， 定义参数名。 这个名称可以用在mapper文件中。

接口、方法定义：

```java
/*
多个简单类型的参数
使用@Param命名参数， 注解是mybatis提供的
位置：在形参定义的前面
属性：value 自定义的参数名称
 */
List<Student> selectByNameOrAge(@Param("myName") String name, @Param("myAge") Integer age);
```
mapper文件：

```xml
<!-- 多个简单类型的参数.
     当使用了@Param命名后，例如@Param("myname").
     在mapper中，使用#{命名的参数}， 例如 #{myname}
     -->
<select id="selectByNameOrAge" resultType="com.macay.entity.Student">
    select id, name, email, age from student where name = #{myName} or age = #{myAge}
</select>
```
测试类：

```java
@Test
    public void testSelectByNameOrAge() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        List<Student> students = mapper.selectByNameOrAge("macay", 20);
        students.forEach(student -> System.out.println(student));
        sqlSession.close();
    }
```

### 四、dao接口方法有多个简单类型的参数，使用位置（不推荐）
参数位置： dao接口中方法的形参列表，从左往右，参数位置是 0 ， 1， 2......
语法格式：#{arg0} ,#{arg1}

**注意：此方法虽然也能用，但是可读性差，所有不推荐使用。**

接口、方法定义：

```java
/*
 * 使用位置，获取参数
 */
List<Student> selectByPosition(String name,Integer age);
```

mapper文件：

```xml
<!--
  mybatis版本是 3.5.1
  使用位置获取参数值， dao接口方法是多个简单类型的参数
  语法： #{arg0}, #{arg1}....
 -->
<select id="selectByPosition" resultType="com.macay.entity.Student">
    select id, name, email, age from student where name = #{arg0} or age = #{arg1}
</select>
```

### 五、dao接口方法使用一个对象作为参数
若dao接口方法的形参有多个参数，我们也可以使用java对象封装参数。使用对象的属性值作为参数使用。

java对象：
```java
// 实体类
public class Student {
    private Integer id;
    private String name;
    private String email;
    private Integer age;

    // get/set方法
}
// 也可以是一个任意的对象
public class QueryParam {
    private Object p1;
    private Object p2;
    //set|get方法
}
```
接口、方法定义：

```java
/*
 * 一个java对象作为参数( 对象由属性， 每个属性有set，get方法)
 */
List<Student> selectByObject(Student student);

List<Student> selectByQueryParam(QueryParam param);
```
mapper文件：

```xml
<!--
   一个java对象作为方法的参数，使用对象的属性作为参数值使用
   简单的语法： #{属性名} ， mybatis调用此属性的getXXX()方法获取属性值
-->
<select id="selectByObject" resultType="com.macay.entity.Student">
    select id,name,email,age from student where name=#{name} or age=#{age}
</select>

<select id="selectByQueryParam" resultType="com.macay.entity.Student">
     select id,name,email,age from student where name=#{p1} or age=#{p2}
</select>
```
**注意**：mapper中，一般我们都使用简写后的语法： #{属性名}，完整的语法应该是：

>  #{属性名,javaType=java类型的全限定名称,jdbcType=mybatis中定义列的数据类型}

上面的那个例子也可以写成这样:

```xml
<select id="selectByObject" resultType="com.macay.entity.Student">
        select id,name,email,age from student where
        name=#{name,javaType=java.lang.String,jdbcType=VARCHAR}
        or
        age=#{age,javaType=java.lang.Integer,jdbcType=INTEGER}
</select>
```
使用工具生成的xml文件经常是这种写法，我们要能够理解。

### 六、dao接口方法使用一个map作为参数
dao接口参数如果有多个，可以封装一个map作为dao接口的参数， 使用 key 获取参数值，mapper文件中，语法格式 #{key}

接口、方法定义：

```java
/*
 * 使用一个map作为参数
 */
List<Student> selectByMap(Map<String, Object> data);
```
mapper文件：

```xml
<!--
   使用Map传递参数，
   在mapper文件中，获取map的值，是通过key获取的，语法：#{key}
-->
<select id="selectByMap" resultType="com.macay.entity.Student">
   select id, name, email, age from student where name = #{myName} or age = #{myAge}
</select>
```
测试类：

```java
    @Test
    public void testselectByMap() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Map<String, Object> data = new HashMap<>();
        data.put("myName", "macay");
        data.put("myAge", 20);
        List<Student> students = mapper.selectByMap(data);
        students.forEach(student -> System.out.println(student));
        sqlSession.close();
    }
```

