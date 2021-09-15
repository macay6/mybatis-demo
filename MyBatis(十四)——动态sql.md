### 前言
1、动态 SQL，即通过 MyBatis 提供的各种标签对条件作出判断以实现动态拼接
SQL 语句。 这里的条件判断使用的表达式为 OGNL 表达式。 常用的动态 SQL
标签有if、 where、 choose、 foreach等。
2、动态 SQL，主要用于解决查询条件不确定的情况：在程序运行期间，根据
用户提交的查询条件进行查询。提交的查询条件不同，执行的 SQL 语句不同。
若将每种可能的情况均逐一列出，对所有条件进行排列组合，将会出现大量的
SQL 语句。此时，可使用动态 SQL 来解决这样的问题。
3、使用动态sql的时候， dao方法的形参使用java对象。 

#### 一、if标签
if标签通常用于WHERE语句中，通过判断参数值来决定是否使用某个查询条件，它也经常用于UPDATE语句中判断是否更新某一个字段，还可以在INSERT语句中用来判断是否插入某个字段的值。
语法：

```xml
语法： <if test=” 条件”> sql 语句的部分 </if>
```


#### 1、在WHERE条件中使用if

假设现在有这么一个场景：实现一个用户管理的高级查询功能，根据输入的条件去检索用户信息，这个功能需要支持下面三种情况：当只输入用户名时，需要根据用户名进行模糊查询，当只输入email时，需要根据email完全匹配，当同时输入用户名和email时，用这两个条件去查询匹配的用户。

按照我们之前的写法我们可能会这么写：

```xml
<select id="selectStudentIfInWhere" resultType="com.macay.entity.Student">
    select * from student where name like CONCAT('%',#{name},'%') and email = #{email}
</select>
```
当name和email都有值的时候，我们可以查出结果，但当只提供email的值时，name默认为null，这就会导致name = null 也作为查询条件，因而查不到正确的结果。这个时候我们就可以使用if标签来解决这个问题，代码如下：

```xml
<select id="selectStudentIfInWhere" resultType="com.macay.entity.Student">
    select * from student where 1 = 1
     /*
     test: 使用对象的属性值作为条件，建议只用true或false作为结果
     */
     <if test="name != null and name != ''">
         and name like CONCAT('%',#{name},'%')
     </if>
     <if test="email != null and email != ''">
         and email = #{email}
     </if>
</select>
```
需要注意下面几点：

1、判断条件name != null或者name == null；适用于**任何类型**的字段，用于判断属性值是否为空。

2、判断条件name != ''或者name == ''；仅适用于String类型的字段，用于判断属性值是否为空。

3、上面两个属性类型都是String，对字符串的判断与Java中类似，最好是先判断字段是否为null，然后再去判断是否为空。

4、加上where 1 = 1 这个条件是为了避免SQL语法错误，但这种写法并不美观，后面介绍where标签的时候会改进。

#### 2、在UPDATE更新列中使用if 
现在有这么一个场景：只更新有变化的字段。需要注意的是，更新的时候不能将原来有值但没有发生变化的字段更新为空或者null。通过if标签可以实现这种动态更新列。

如果仅使用if标签，为了保证SQL语句的正确性，我们需要在SQL语句中拼接许多额外的语句，稍显麻烦，所有我们使用where和set标签来解决这个问题。

**set标签的作用**：

- 如果该标签包含的元素有返回值，就插入一个set;
- 如果set后面的字符串是以逗号结尾的，就将这个逗号删除。

接口、方法定义：

```html
int updateStudentByIdSelective(Student student);
```
mapper文件：
```xml
<update id="updateStudentByIdSelective" >
    update student
    <set>
        <if test="name != null and name != ''">
            name = #{name},
        </if>
        <if test="email != null and email != ''">
            email = #{email},
        </if>
        <if test="age > 0">
            age = #{age},
        </if>
        id = #{id}
    </set>
    where id = #{id}
</update>
```
这里要注意的是，如果在set里面没有id = #{id}这个标签，假如set里面没有内容（也就是name，email，age不符合条件），这时候SQL语句就变成了update student where id = ? ，所有加上id = #{id}也是为了避免出错，从这一点看来，set标签并没有解决全部的问题，使用时仍然要注意。

测试类：

```java
@Test
public void updateStudentByIdSelective() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    StudentDao mapper = sqlSession.getMapper(StudentDao.class);
    Student student = new Student();
    student.setId(2);
    student.setName("hhh");
    student.setEmail(null);
    int count = mapper.updateStudentByIdSelective(student);
    System.out.println(count);
    sqlSession.commit();
    sqlSession.close();
}
```
#### 3、在INSERT动态插入列中使用if
在数据库表中插入数据的时候，如果某一列的参数值不为空，就使用传入的值，如果传入的参数为空，就使用数据库中的默认值（通常是空），而不使用传入的值。使用if就可以实现这种动态插入列的功能。






