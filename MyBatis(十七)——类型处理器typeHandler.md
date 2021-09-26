@[TOC]
#### 一、什么是类型处理器TypeHandler
我们在项目中经常会遇到这样的场景：

我们java有javabean的数据类型，数据库有数据库的数据类型。但是在数据库中存储时往往需要转换成数据库对应的类型，并且在从数据库中取出来时也需要将数据库类型转换为javabean中的对应类型。比如：javabean中字段类型为Date，数据库中存储的是varchar类型；javabean中字段类型是Enum，数据库中存储的是String或者Integer。

因为有大量类似数据的转换，手动转换类型进行存储和查询已经过于麻烦。MyBatis为我们提供了解决办法：TypeHandler类型处理器。

MyBatis 中的 TypeHandler 类型处理器用于 JavaType 与 JdbcType 之间的转换，用于 PreparedStatement 设置参数值和从 ResultSet 或 CallableStatement 中取出一个值。


#### 二、内置类型处理器
MyBatis 内置了大部分基本类型的类型处理器，所以对于基本类型可以直接处理。在 MyBatis 的 TypeHandlerRegistry 类型中，可以看到内置的类型处理器。内置处理器比较多，这里整理常见的一些。
```java
Java代码  收藏代码
register(Boolean.class, new BooleanTypeHandler());  
register(boolean.class, new BooleanTypeHandler());  
register(Byte.class, new ByteTypeHandler());  
register(byte.class, new ByteTypeHandler());  
register(Short.class, new ShortTypeHandler());  
register(short.class, new ShortTypeHandler());  
register(Integer.class, new IntegerTypeHandler());  
register(int.class, new IntegerTypeHandler());  
register(Long.class, new LongTypeHandler());  
register(long.class, new LongTypeHandler());  
register(Float.class, new FloatTypeHandler());  
register(float.class, new FloatTypeHandler());  
register(Double.class, new DoubleTypeHandler());  
register(double.class, new DoubleTypeHandler());  
register(String.class, new StringTypeHandler());  
register(String.class, JdbcType.CHAR, new StringTypeHandler());  
register(String.class, JdbcType.CLOB, new ClobTypeHandler());  
register(String.class, JdbcType.VARCHAR, new StringTypeHandler());  
register(String.class, JdbcType.LONGVARCHAR, new ClobTypeHandler());  
register(String.class, JdbcType.NVARCHAR, new NStringTypeHandler());  
register(String.class, JdbcType.NCHAR, new NStringTypeHandler());  
register(String.class, JdbcType.NCLOB, new NClobTypeHandler());  
register(Object.class, JdbcType.ARRAY, new ArrayTypeHandler());  
register(BigInteger.class, new BigIntegerTypeHandler());  
register(BigDecimal.class, new BigDecimalTypeHandler());  
register(Byte[].class, new ByteObjectArrayTypeHandler());  
register(Byte[].class, JdbcType.BLOB, new BlobByteObjectArrayTypeHandler());  
register(Byte[].class, JdbcType.LONGVARBINARY, new BlobByteObjectArrayTypeHandler());  
register(byte[].class, new ByteArrayTypeHandler());  
register(byte[].class, JdbcType.BLOB, new BlobTypeHandler());  
register(byte[].class, JdbcType.LONGVARBINARY, new BlobTypeHandler());  
register(Object.class, UNKNOWN_TYPE_HANDLER);  
register(Object.class, JdbcType.OTHER, UNKNOWN_TYPE_HANDLER);  
register(Date.class, new DateTypeHandler());  
register(Date.class, JdbcType.DATE, new DateOnlyTypeHandler());  
register(Date.class, JdbcType.TIME, new TimeOnlyTypeHandler());  
register(java.sql.Date.class, new SqlDateTypeHandler());  
register(java.sql.Time.class, new SqlTimeTypeHandler());  
register(java.sql.Timestamp.class, new SqlTimestampTypeHandler());  
register(Character.class, new CharacterTypeHandler());  
register(char.class, new CharacterTypeHandler());  

```
整理如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/8906ee1cfbd04484b97200df8566c471.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBATWFjYXk=,size_20,color_FFFFFF,t_70,g_se,x_16)
#### 三、自定义类型处理器
##### 1、自定义类型处理器创建
除了内置的处理器，业务中我们需要处理Java实体类中的类型和数据库中的类型不对应时就可以自定义类型处理器。

自定义类型处理器是通过实现 org.apache.ibatis.type.TypeHandler 接口实现的。这个接口定义了类型处理器的基本功能，接口定义如下所示。

```java
public interface TypeHandler<T> {  
   
    /** 
     * 用于定义在Mybatis设置参数时该如何把Java类型的参数转换为对应的数据库类型 
     * @param ps 当前的PreparedStatement对象 
     * @param i 当前参数的位置 
     * @param parameter 当前参数的Java对象 
     * @param jdbcType 当前参数的数据库类型 
     * @throws SQLException 
     */  
    void setParameter(PreparedStatement ps, int i, T parameter,  
           JdbcType jdbcType) throws SQLException;  
   
    /** 
     * 用于在Mybatis获取数据结果集时如何把数据库类型转换为对应的Java类型 
     * @param rs 当前的结果集 
     * @param columnName 当前的字段名称 
     * @return 转换后的Java对象 
     * @throws SQLException 
     */  
    T getResult(ResultSet rs, String columnName) throws SQLException;  
   
    /** 
     * 用于在Mybatis通过字段位置获取字段数据时把数据库类型转换为对应的Java类型 
     * @param rs 当前的结果集 
     * @param columnIndex 当前字段的位置 
     * @return 转换后的Java对象 
     * @throws SQLException 
     */  
    T getResult(ResultSet rs, int columnIndex) throws SQLException;  
   
    /** 
     * 用于Mybatis在调用存储过程后把数据库类型的数据转换为对应的Java类型 
     * @param cs 当前的CallableStatement执行后的CallableStatement 
     * @param columnIndex 当前输出参数的位置 
     * @return 
     * @throws SQLException 
     */  
    T getResult(CallableStatement cs, int columnIndex) throws SQLException;  
   
}  

```
其中 setParameter 方法用于把 java 对象设置到 PreparedStatement 的参数中，getResult 方法用于从 ResultSet（根据列名或者索引位置获取） 或 CallableStatement（根据存储过程获取） 中取出数据转换为 java 对象。

**实际开发中，我们可以继承 org.apache.ibatis.type.BaseTypeHandler 类型来实现自定义类型处理器。**这个类型是抽象类型，实现了 TypeHandler 的方法进行通用流程的封装，做了异常处理，并定义了几个类似的抽象方法，如下所示。继承 BaseTypeHandler 类型可以极大地降低开发难度。

```java
public abstract class BaseTypeHandler<T> extends TypeReference<T> implements TypeHandler<T> {
    /** @deprecated */
    @Deprecated
    protected Configuration configuration;

    public BaseTypeHandler() {
    }

    /** @deprecated */
    @Deprecated
    public void setConfiguration(Configuration c) {
        this.configuration = c;
    }
    
   //由于BaseTypeHandler中已经把parameter为null的情况做了处理，所以使用时我们就不用再判断parameter是否为空了，直接用就可以了  

    public void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            if (jdbcType == null) {
                throw new TypeException("JDBC requires that the JdbcType must be specified for all nullable parameters.");
            }

            try {
                ps.setNull(i, jdbcType.TYPE_CODE);
            } catch (SQLException var7) {
                throw new TypeException("Error setting null for parameter #" + i + " with JdbcType " + jdbcType + " . Try setting a different JdbcType for this parameter or a different jdbcTypeForNull configuration property. Cause: " + var7, var7);
            }
        } else {
            try {
                this.setNonNullParameter(ps, i, parameter, jdbcType);
            } catch (Exception var6) {
                throw new TypeException("Error setting non null for parameter #" + i + " with JdbcType " + jdbcType + " . Try setting a different JdbcType for this parameter or a different configuration property. Cause: " + var6, var6);
            }
        }

    }

    public T getResult(ResultSet rs, String columnName) throws SQLException {
        try {
            return this.getNullableResult(rs, columnName);
        } catch (Exception var4) {
            throw new ResultMapException("Error attempting to get column '" + columnName + "' from result set.  Cause: " + var4, var4);
        }
    }

    public T getResult(ResultSet rs, int columnIndex) throws SQLException {
        try {
            return this.getNullableResult(rs, columnIndex);
        } catch (Exception var4) {
            throw new ResultMapException("Error attempting to get column #" + columnIndex + " from result set.  Cause: " + var4, var4);
        }
    }

    public T getResult(CallableStatement cs, int columnIndex) throws SQLException {
        try {
            return this.getNullableResult(cs, columnIndex);
        } catch (Exception var4) {
            throw new ResultMapException("Error attempting to get column #" + columnIndex + " from callable statement.  Cause: " + var4, var4);
        }
    }

    // 使用这些方法：

    public abstract void setNonNullParameter(PreparedStatement var1, int var2, T var3, JdbcType var4) throws SQLException;

    public abstract T getNullableResult(ResultSet var1, String var2) throws SQLException;

    public abstract T getNullableResult(ResultSet var1, int var2) throws SQLException;

    public abstract T getNullableResult(CallableStatement var1, int var2) throws SQLException;
}
```
##### 2、自定义类型转换器的注册
参考文章：[https://blog.csdn.net/weixin_42030357/article/details/103967699](https://blog.csdn.net/weixin_42030357/article/details/103967699)

建立了自己的TypeHandler之后就需要把它注册到Mybatis的配置文件中，让Mybatis能够识别并使用它。注册TypeHandler主要有两种方式，全局注册和局部注册。

###### 1、全局注册
全局注册又分为两种:

(1)、单独注册：

通过在Mybatis配置文件中定义typeHandlers元素的子元素typeHandler来注册，例如：

```xml
<typeHandlers>
    <typeHandler handler="com.macay.handler.BooleanCharTypeHandler" javaType="Boolean"
                 jdbcType="CHAR"/>
</typeHandlers>
```
其中，javaType和jdbcType还可以通过在TypeHandler类上使用了注解@MappedTypes和@MappedJdbcTypes来指定，效果是一样的。

> @MappedTypes：注解配置 java 类型
@MappedJdbcTypes：注解配置 jdbc 类型

如下：

```java
@MappedTypes(Boolean.class)
@MappedJdbcTypes(JdbcType.CHAR)
public class BooleanCharTypeHandler extends BaseTypeHandler {
```
(2)、通过package包扫描注册

使用typeHandler单独注册时一次只能注册一个TypeHandler，而使用package元素注册时，Mybatis会把指定包里面的所有TypeHandler都注册为TypeHandler。注册时需要我们通过它的name属性来指定要扫描的包，如果这个时候我们也需要指定对应TypeHandler的javaType和jdbcType的话就需要我们在TypeHandler类上使用注解来定义了。

```xml
<typeHandlers>
    <package name="com.macay.handler"/>
</typeHandlers>
```

上面这两种配置的作用域是全局的，也就是说所有的我们书写的Mapper中凡是满足Java类型是Boolean数据库类型是Number或者是int时（满足这个条件 javaType=“Boolean” jdbcType=“NUMERIC” ），都会执行这个TypeHandler。

这个需要我们在查询resultMap中和插入Insert中手动定义javaType和jdbcType，如下，否则即使定义了全局注册，仍然不会生效。

```xml
    <resultMap id="studentMap" type="com.macay.entity.Student">
        <!--主键类型使用id标签-->
        <id column="id" property="id" />
        <!--非主键类型使用result标签-->
        <result column="name" property="name"/>
        <result column="email" property="email"/>
        <result column="age" property="age"/>
        <result column="party_member" property="partyMember" javaType="boolean" jdbcType="NUMERIC"/>
    </resultMap>
```

###### 2、局部注册
对于查询，需要我们在对应的Mapper文件中添加resultMap标签：

```xml
<resultMap id="studentMap" type="com.macay.entity.Student">
    <!--主键类型使用id标签-->
    <id column="id" property="id" />
    <!--非主键类型使用result标签-->
    <result column="name" property="name"/>
    <result column="email" property="email"/>
    <result column="age" property="age"/>
    <result column="party_member" property="partyMember"
            typeHandler="com.macay.handler.BooleanIntTypeHandler"></result>
</resultMap>
```
如图，我们可以直接在mapper要用到的地方声明需要使用的类型转换器，仅在resultMap里指定属性装配,不在mybatis的配置文件中装配,就可以仅对此属性生效。

但是这种方式有一个缺点那就是只适用于查询操作，即在查询的过程中系统会启用我们自定义的typeHandler，会将boolean转为int对象，但是在插入的时候却不会启用我们自定义的typeHandler，想要在插入的时候启用自定义的typeHandler，需要我们在insert节点中简单配置一下，如下：

```xml
<insert id="insertStudent">
    insert into student (id, name, email, age, party_member)
    values (#{id},#{name},#{email},#{age},#{partyMember, jdbcType=CHAR, javaType=Boole, typeHandler=com.macay.handler.BooleanIntTypeHandler})
</insert>
```
也可以只配置javaType和jdbcType，如下：
```xml
<insert id="insertStudent">
    insert into student (id, name, email, age, party_member)
    values (#{id},#{name},#{email},#{age},#{partyMember, jdbcType=CHAR, javaType=Boolean})
</insert>
```
或者只配置typeHandler：

```xml
<insert id="insertStudent">
    insert into student (id, name, email, age, party_member)
    values (#{id},#{name},#{email},#{age},#{partyMember, typeHandler=com.macay.handler.BooleanIntTypeHandler})
</insert>
```
这三种效果都是一样的。

#### 四、自定义类型转换器的常见使用场景
##### 1、boolean与int
比如：Java实体类中有一个Boolean类型的字段flag，对应到数据库flag字段中类型是int。

使用场景：比如我们Student表中有一个属性是是否是党员，Java中使用一个boolean类型partyMember来定义，但是在MySQL表中我们使用int类型的party_member来表示。使用方式如下：
Java类：

```java
public class Student {
    private Integer id;
    private String name;
    private String email;
    private Integer age;
    private Boolean partyMember;
    private Classess cla
```

定义TypeHandler：

```java
public class BooleanIntTypeHandler extends BaseTypeHandler<Boolean> {
    @Override
    // 用于定义在Mybatis设置参数时该如何把Java类型的参数转换为对应的数据库类型
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Boolean aBoolean, JdbcType jdbcType) throws SQLException {
        if (aBoolean) {
            preparedStatement.setInt(i, 1);
        } else {
            preparedStatement.setInt(i, 0);
        }
    }

    @Override
    // 用于在Mybatis获取数据结果集时如何把数据库类型转换为对应的Java类型
    public Boolean getNullableResult(ResultSet resultSet, String str) throws SQLException {
        int anInt = resultSet.getInt(str);
        if (anInt == 1) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return null;
    }

    @Override
    public Boolean getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return null;
    }
}
```
全局注册：

```xml
<typeHandlers>
    <typeHandler handler="com.macay.handler.BooleanIntTypeHandler" javaType="Boolean"
                 jdbcType="NUMERIC"/>
</typeHandlers>
```
接口：

```java
int insertStudent(Student student);
Student getStudnetById(int id);
```
mapper文件：

```xml
<resultMap id="studentMap" type="com.macay.entity.Student">
    <!--主键类型使用id标签-->
    <id column="id" property="id" />
    <!--非主键类型使用result标签-->
    <result column="name" property="name"/>
    <result column="email" property="email"/>
    <result column="age" property="age"/>
    <result column="party_member" property="partyMember"
            typeHandler="com.macay.handler.BooleanIntTypeHandler"></result>
</resultMap>
<select id="getStudnetById" resultMap="studentMap">
    select * from student where id = #{id}
</select>
```
测试类：

插入的时候值为boolean,数据库存入时为int:
```java
@Test
public void testInsertStudent() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    StudentDao mapper = sqlSession.getMapper(StudentDao.class);
    Student student = new Student();
    student.setId(31);
    student.setName("test30");
    student.setEmail("123@qq.com");
    student.setAge(30);
    student.setPartyMember(true);
    int i = mapper.insertStudent(student);
    System.out.println(i);
    sqlSession.commit();
    sqlSession.close();
}
```
效果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/70098e690f184e5badc56eb076861b5f.png)
查询时得到的又是boolean类型：
![在这里插入图片描述](https://img-blog.csdnimg.cn/2e5d1411bfe848a1a9dc3c01ea1c246c.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBATWFjYXk=,size_20,color_FFFFFF,t_70,g_se,x_16)
**特别注意：**

mysql数据库不提供boolean类型的数据存储，但是可以用tinyint代替，改该字段对应的javabean的那个变量定义为boolean类型即可，当存入true时，自动转换为1，false为0，取的时候也一样。

这个比较特殊，所有boolean与int存入的时候是可以不用类型转换器的，但是取出来的时候还是需要的。

##### 2、boolean与char
场景：还是上面这个例子，Java中使用一个boolean类型partyMember来定义，但是在MySQL表中我们使用char类型的party_member来表示,是党员用”Y“表示，不是用”N“表示。

定义TypeHandler：

```java
@MappedTypes(Boolean.class)
@MappedJdbcTypes(JdbcType.CHAR)
public class BooleanCharTypeHandler extends BaseTypeHandler<Boolean> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Boolean aBoolean, JdbcType jdbcType) throws SQLException {
        if (aBoolean) {
            preparedStatement.setString(i, "Y");
        } else {
            preparedStatement.setString(i, "N");
        }
    }

    @Override
    public Boolean getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String string = resultSet.getString(s);
        if ("Y".equals(string)) {
            return true;
        } else if ("N".equals(string)) {
            return false;
        }
        return null;
    }

    @Override
    public Boolean getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return null;
    }

    @Override
    public Boolean getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return null;
    }
}
```

mapper文件：

```xml
<insert id="insertStudent">
    insert into student (id, name, email, age, party_member)
    values (#{id},#{name},#{email},#{age},#{partyMember, jdbcType=CHAR, javaType=Boole
</insert>

<!--    也可以使用typeHandler来指定-->
<!--    <insert id="insertStudent">-->
<!--        insert into student (id, name, email, age, party_member)-->
<!--        values (#{id},#{name},#{email},#{age},#{partyMember, typeHandler=com.macay.handler.BooleanCharTypeHandler})-->
<!--    </insert>-->


<resultMap id="studentMap" type="com.macay.entity.Student">
    <!--主键类型使用id标签-->
    <id column="id" property="id" />
    <!--非主键类型使用result标签-->
    <result column="name" property="name"/>
    <result column="email" property="email"/>
    <result column="age" property="age"/>
    <result column="party_member" property="partyMember"
            typeHandler="com.macay.handler.BooleanCharTypeHandler"></result>
</resultMap>
```

全局注册：

```xml
<typeHandlers>
    <typeHandler handler="com.macay.handler.BooleanCharTypeHandler" javaType="Boolean"
                 jdbcType="CHAR"/>
    <typeHandler handler="com.macay.handler.BooleanIntTypeHandler" javaType="Boolean"
                 jdbcType="NUMERIC"/>
</typeHandlers>
```
测试类：

```java
    @Test
    public void testInsertStudent() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Student student = new Student();
        student.setId(33);
        student.setName("test30");
        student.setEmail("123@qq.com");
        student.setAge(30);
        student.setPartyMember(true);
        int i = mapper.insertStudent(student);
        System.out.println(i);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void testGetStudentById() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Student student = mapper.getStudnetById(33);
        System.out.println(student);
        sqlSession.commit();
        sqlSession.close();
    }
```
效果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/833e430525f449af8fe8a651809b6f0a.png)
![在这里插入图片描述](https://img-blog.csdnimg.cn/9917485ecbf8479b9a4c453b0ddad203.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBATWFjYXk=,size_20,color_FFFFFF,t_70,g_se,x_16)
##### 3、list与string
场景：一个学生，在登记的时候需要选择他的特长爱好。比如1运动，2歌唱，3书法，4乐器，在界面上是一个复选框(checkbox)。
在数据库保存的是用逗号分隔的字符串，例如“1,3,4”，而返回给程序的时候是整形集合List {1,3,4}。

java对象：

```java
public class Student {
    private Integer id;
    private String name;
    private String email;
    private Integer age;
    private Boolean partyMember;
    private Classess cla;
    private List<Integer> hobbies;
```


typeHandler类型处理器：

```java
public class ListStringTypeHandler extends BaseTypeHandler<List<Integer>> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<Integer> list,
                                    JdbcType jdbcType) throws SQLException {
        StringBuffer sb = new StringBuffer();
        list.forEach(item -> {
            sb.append(String.valueOf(item)).append(",");
        });
        preparedStatement.setString(i, sb.substring(0, sb.length() -1));

    }

    @Override
    public List<Integer> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        if(null == resultSet.getString(s) || 0 == resultSet.getString(s).trim().length()){
            return null;
        }
        String string = resultSet.getString(s);
        String[] split = string.split(",");
        List<Integer> list = new ArrayList<>();
        for (String str : split) {
            list.add(Integer.valueOf(str));
        }
        return list;
    }

    @Override
    public List<Integer> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return null;
    }

    @Override
    public List<Integer> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return null;
    }
}
```
mapper文件：

```xml
<insert id="insertStudent">
    insert into student (id, name, email, age, party_member,hobbies)
    values (#{id},#{name},#{email},#{age},#{partyMember, jdbcType=CHAR, javaType=Boolean}, #{hobbies, typeHan
</insert>
<resultMap id="studentMap" type="com.macay.entity.Student">
    <!--主键类型使用id标签-->
    <id column="id" property="id" />
    <!--非主键类型使用result标签-->
    <result column="name" property="name"/>
    <result column="email" property="email"/>
    <result column="age" property="age"/>
    <result column="party_member" property="partyMember"
            typeHandler="com.macay.handler.BooleanCharTypeHandler"></result>
    <result column="hobbies" property="hobbies"
            typeHandler="com.macay.handler.ListStringTypeHandler"></result>
</resultMap>
<select id="getStudnetById" resultMap="studentMap">
    select * from student where id = #{id}
</select>
```
测试类：

```java
@Test
public void testInsertStudent() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    StudentDao mapper = sqlSession.getMapper(StudentDao.class);
    Student student = new Student();
    student.setId(34);
    student.setName("test30");
    student.setEmail("123@qq.com");
    student.setAge(30);
    student.setPartyMember(true);
    List<Integer> list = Arrays.asList(new Integer[]{1, 2, 3, 4});
    student.setHobbies(list);
    int i = mapper.insertStudent(student);
    System.out.println(i);
    sqlSession.commit();
    sqlSession.close();
}
@Test
public void testGetStudentById() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    StudentDao mapper = sqlSession.getMapper(StudentDao.class);
    Student student = mapper.getStudnetById(34);
    System.out.println(student);
    sqlSession.commit();
    sqlSession.close();
}
```
结果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/785e4b2095014a6f9ade88bb0b9f7075.png)
![在这里插入图片描述](https://img-blog.csdnimg.cn/07c4b6b0749944b6810f95af2ed1876a.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBATWFjYXk=,size_20,color_FFFFFF,t_70,g_se,x_16)
##### 4、JavaBean与json
场景：在项目开发中有些字段需要使用json格式进行存储，mysql从5.7开始已经支持JSON类型的字段。使用mybatis自定义实现类型转换为json格式存储在数据库中。比如我们将学生的班级信息用josn存储。

java对象：

```java
public class Student {
    private Integer id;
    private String name;
    private String email;
    private Integer age;
    private Boolean partyMember;
    private Classess cla;
    private List<Integer> hobbies;
```
typeHandler类型转换器：

```java
@MappedTypes(Classess.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class JavabeenJsonTypeHandler extends BaseTypeHandler<Classess> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Classess classess, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, JSON.toJSONString(classess));
    }

    @Override
    public Classess getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return JSON.parseObject(resultSet.getString(s), Classess.class);
    }

    @Override
    public Classess getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return null;
    }

    @Override
    public Classess getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return null;
    }
}

```
mapper文件：

```xml
<insert id="insertStudent">
    insert into student (id, name, email, age, party_member,hobbies,cla)
    values (#{id},#{name},#{email},#{age},#{partyMember, jdbcType=CHAR, javaType=B
    #{hobbies, typeHandler=com.macay.handler.ListStringTypeHandler}, #{cla, typeHa
</insert>
<resultMap id="studentMap" type="com.macay.entity.Student">
    <!--主键类型使用id标签-->
    <id column="id" property="id" />
    <!--非主键类型使用result标签-->
    <result column="name" property="name"/>
    <result column="email" property="email"/>
    <result column="age" property="age"/>
    <result column="party_member" property="partyMember"
            typeHandler="com.macay.handler.BooleanCharTypeHandler"></result>
    <result column="hobbies" property="hobbies"
            typeHandler="com.macay.handler.ListStringTypeHandler"></result>
    <result column="cla" property="cla"
            typeHandler="com.macay.handler.JavabeenJsonTypeHandler"></result>
</resultMap>
<select id="getStudnetById" resultMap="studentMap">
    select * from student where id = #{id}
</select>
```
测试类：

```java
@Test
public void testInsertStudent() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    StudentDao mapper = sqlSession.getMapper(StudentDao.class);
    Student student = new Student();
    student.setId(36);
    student.setName("test30");
    student.setEmail("123@qq.com");
    student.setAge(30);
    student.setPartyMember(true);
    List<Integer> list = Arrays.asList(new Integer[]{1, 2, 3, 4});
    Classess classess = new Classess();
    classess.setId(2);
    classess.setName("sannianyuban");
    student.setCla(classess);
    student.setHobbies(list);
    int i = mapper.insertStudent(student);
    System.out.println(i);
    sqlSession.commit();
    sqlSession.close();
}
@Test
public void testGetStudentById() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    StudentDao mapper = sqlSession.getMapper(StudentDao.class);
    Student student = mapper.getStudnetById(36);
    System.out.println(student);
    sqlSession.commit();
    sqlSession.close();
}
```
结果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/dbec7a7739bd43f7afd9ece030b00d4a.png)
![在这里插入图片描述](https://img-blog.csdnimg.cn/906ed5308f024ce398aebcab17690228.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBATWFjYXk=,size_20,color_FFFFFF,t_70,g_se,x_16)
##### 5、枚举类
场景：某些时候我们需要使用枚举类，比如性别枚举类如下，我们存入数据库中的值为int类型的属性code，实现如下：

```java
public enum GenderEnum {

    MALE(1, "男性"),
    FEMALE(2, "女性");

    private int code;

    private String description;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    GenderEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static GenderEnum getGenderEnum(Integer val) {
        for (GenderEnum genderEnum : GenderEnum.values()) {
            if (genderEnum.getCode() == val) {
                return genderEnum;
            }
        }
        return null;
    }
}
```
Java类：

```java
public class Student {
    private Integer id;
    private String name;
    private String email;
    private Integer age;
    private Boolean partyMember;
    private Classess cla;
    private List<Integer> hobbies;
    private GenderEnum gender;
```
typeHandler处理器：

```java
public class MenuIntTypeHandler extends BaseTypeHandler<GenderEnum> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, GenderEnum genderEnum, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, genderEnum.getCode());
    }

    @Override
    public GenderEnum getNullableResult(ResultSet resultSet, String s) throws SQLException {
        int anInt = resultSet.getInt(s);
        if (anInt == 1) {
            return GenderEnum.getGenderEnum(1);
        } else if (anInt == 2) {
            return GenderEnum.getGenderEnum(2);
        }
        return null;
    }

    @Override
    public GenderEnum getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return null;
    }

    @Override
    public GenderEnum getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return null;
    }
}
```
mapper文件：

```xml
<insert id="insertStudent">
    insert into student (id, name, email, age, party_member,hobbies,cla,gender)
    values (#{id},#{name},#{email},#{age},#{partyMember, jdbcType=CHAR, javaType=Boolean},
    #{hobbies, typeHandler=com.macay.handler.ListStringTypeHandler},
    #{cla, typeHandler=com.macay.handler.JavabeenJsonTypeHandler},
    #{gender, typeHandler=com.macay.handler.MenuIntTypeHandler})
</insert>
<resultMap id="studentMap" type="com.macay.entity.Student">
    <!--主键类型使用id标签-->
    <id column="id" property="id" />
    <!--非主键类型使用result标签-->
    <result column="name" property="name"/>
    <result column="email" property="email"/>
    <result column="age" property="age"/>
    <result column="party_member" property="partyMember"
            typeHandler="com.macay.handler.BooleanCharTypeHandler"></result>
    <result column="hobbies" property="hobbies"
            typeHandler="com.macay.handler.ListStringTypeHandler"></result>
    <result column="cla" property="cla"
            typeHandler="com.macay.handler.JavabeenJsonTypeHandler"></result>
    <result column="gender" property="gender"
            typeHandler="com.macay.handler.MenuIntTypeHandler"/>
</resultMap>
<select id="getStudnetById" resultMap="studentMap">
    select * from student where id = #{id}
</select>
```
测试类：

```java
@Test
public void testInsertStudent() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    StudentDao mapper = sqlSession.getMapper(StudentDao.class);
    Student student = new Student();
    student.setId(37);
    student.setName("test30");
    student.setEmail("123@qq.com");
    student.setAge(30);
    student.setPartyMember(true);
    List<Integer> list = Arrays.asList(new Integer[]{1, 2, 3, 4});
    Classess classess = new Classess();
    classess.setId(2);
    classess.setName("sannianyuban");
    student.setCla(classess);
    student.setHobbies(list);
    student.setGender(GenderEnum.MALE);
    int i = mapper.insertStudent(student);
    System.out.println(i);
    sqlSession.commit();
    sqlSession.close();
}
@Test
public void testGetStudentById() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    StudentDao mapper = sqlSession.getMapper(StudentDao.class);
    Student student = mapper.getStudnetById(37);
    System.out.println(student);
    sqlSession.commit();
    sqlSession.close();
}
```
结果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/862f543012bd423a8ef952c62a162c34.png)
![在这里插入图片描述](https://img-blog.csdnimg.cn/e31cc4abbe4e4d9e9dd2bfbaa63a3604.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBATWFjYXk=,size_20,color_FFFFFF,t_70,g_se,x_16)
##### 6、日期类处理
场景：比如student中有一个Date数据类型，我想将之存到数据库的时候存成一个1970年至今的毫秒数。

java类：

```java
public class Student {
    private Integer id;
    private String name;
    private String email;
    private Integer age;
    private Boolean partyMember;
    private Classess cla;
    private List<Integer> hobbies;
    private GenderEnum gender;
    private Date regDate;
```
typeHandler处理类：

```java
public class DateVarCharTypeHandler extends BaseTypeHandler<Date> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Date date, JdbcType jdbcType) throws SQLException {
      preparedStatement.setString(i, String.valueOf(date.getTime()));
    }

    @Override
    public Date getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return new Date(resultSet.getLong(s));
    }

    @Override
    public Date getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return null;
    }

    @Override
    public Date getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return null;
    }
}

```
mapper文件：

```xml
<insert id="insertStudent">
    insert into student (id, name, email, age, party_member,hobbies,cla,gender, regDate)
    values (#{id},#{name},#{email},#{age},#{partyMember, jdbcType=CHAR, javaType=Boolean},
    #{hobbies, typeHandler=com.macay.handler.ListStringTypeHandler},
    #{cla, typeHandler=com.macay.handler.JavabeenJsonTypeHandler},
    #{gender, typeHandler=com.macay.handler.MenuIntTypeHandler},
    #{regDate, typeHandler=com.macay.handler.DateVarCharTypeHandler})
</insert>
<resultMap id="studentMap" type="com.macay.entity.Student">
    <!--主键类型使用id标签-->
    <id column="id" property="id" />
    <!--非主键类型使用result标签-->
    <result column="name" property="name"/>
    <result column="email" property="email"/>
    <result column="age" property="age"/>
    <result column="party_member" property="partyMember"
            typeHandler="com.macay.handler.BooleanCharTypeHandler"></result>
    <result column="hobbies" property="hobbies"
            typeHandler="com.macay.handler.ListStringTypeHandler"></result>
    <result column="cla" property="cla"
            typeHandler="com.macay.handler.JavabeenJsonTypeHandler"></result>
    <result column="gender" property="gender"
            typeHandler="com.macay.handler.MenuIntTypeHandler"/>
    <result column="regDate" property="regDate"
            typeHandler="com.macay.handler.DateVarCharTypeHandler"/>
</resultMap>
<select id="getStudnetById" resultMap="studentMap">
    select * from student where id = #{id}
</select>
```
测试类：

```java
@Test
public void testInsertStudent() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    StudentDao mapper = sqlSession.getMapper(StudentDao.class);
    Student student = new Student();
    student.setId(38);
    student.setName("test30");
    student.setEmail("123@qq.com");
    student.setAge(30);
    student.setPartyMember(true);
    List<Integer> list = Arrays.asList(new Integer[]{1, 2, 3, 4});
    Classess classess = new Classess();
    classess.setId(2);
    classess.setName("sannianyuban");
    student.setCla(classess);
    student.setHobbies(list);
    student.setGender(GenderEnum.MALE);
    student.setRegDate(new Date());
    int i = mapper.insertStudent(student);
    System.out.println(i);
    sqlSession.commit();
    sqlSession.close();
}
@Test
public void testGetStudentById() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    StudentDao mapper = sqlSession.getMapper(StudentDao.class);
    Student student = mapper.getStudnetById(38);
    System.out.println(student);
    sqlSession.commit();
    sqlSession.close();
}
```
效果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/654f11a62ff944f7abf4d03171d68d93.png)
![在这里插入图片描述](https://img-blog.csdnimg.cn/b64e1b5cd6fb423684460c5d01662782.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBATWFjYXk=,size_20,color_FFFFFF,t_70,g_se,x_16)
