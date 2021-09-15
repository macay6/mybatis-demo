@[TOC]

### 一、框架简介
&ensp;&ensp;&ensp;&ensp;Mybatis是一款持久层框架，它对JDBC操作数据库的的过程进行了封装，使用者只需要关注sql本身，而不需要去花费精力去处理比如加载驱动、创建connect连接对象、创建statement语句对象、手动设置参数、结果集检索等一系列繁杂的过程。
&ensp;&ensp;&ensp;&ensp;Mybatis通过XML或者注解的方式进行配置和映射，将参数映射到配置的SQL形成最终执行的SQL语句，最后将执行的sql结果映射成java对象返回给业务层。

### 二、MyBatis与JDBC的对比

 #### 1、优化获取和释放
 
一般在访问数据库时都是通过数据库连接池来操作数据库，耦合性比较高，我们可以通过DataSource 进行隔离解耦，我们统一从 DataSource 里面获取数据库连接。

**C3P0 xml配置：**  

```xml
<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource"       
      destroy-method="close">      
   <property name="driverClass" value=" oracle.jdbc.driver.OracleDriver "/>      
   <property name="jdbcUrl" value=" jdbc:oracle:thin:@localhost:1521:ora9i "/>      
   <property name="user" value="admin"/>      
   <property name="password" value="1234"/>      
</bean>
```
**DBCP xml配置：**

```xml
<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource"       
       destroy-method="close">       
    <property name="driverClassName" value="com.mysql.jdbc.Driver" />      
    <property name="url" value="jdbc:mysql://localhost:3309/sampledb" />      
    <property name="username" value="root" />      
    <property name="password" value="1234" />      
</bean>
```
**DURID xml配置：**

```xml

<bean name="dataSource" class="com.alibaba.druid.pool.DruidDataSource">  
　　<property name="driverClassName">  
　　　　<value>com.mysql.jdbc.Driver</value>  
　　</property>  
　　<property name="url">  
　　　　<value>${jdbc_url_gx}</value>  
　　</property>  
　　<property name="username">  
　　　　<value>${jdbc_username_gx}</value>  
　　</property>  
　　<property name="password">  
　　　　<value>${jdbc_password_gx}</value>  git
　　</property> 
</bean>
```
#### 2、SQL 统一管理，对数据库进行存取操作
我们使用JDBC对数据库进行操作时，SQL查询语句与业务逻辑代码混在一起，这样可读性差，不利于维护，当我们修改Java类中的SQL语句时要重新进行编译。

Mybatis可以把SQL语句放在配置文件中统一进行管理，以后修改配置文件，也不需要重新就行编译部署。

#### 3、生成动态SQL语句
**我们在查询中可能需要根据一些属性进行组合查询**，比如我们进行商品查询，我们可以根据商品名称进行查询，也可以根据发货地进行查询，或者两者组合查询。**如果使用JDBC进行查询，这样就需要写多条SQL语句。**

Mybatis可以在配置文件中通过使用<if test=””></if>标签进行SQL语句的拼接，生成动态SQL语句。比如下面这个例子：

```xml

<select id="getCountByInfo" parameterType="User" resultType="int">
        select count(*) from user
        <where>
            <if test="nickname!=null">
                and nickname = #{nickname}
            </if>
            <if test="email!=null">
                and email = #{email}
            </if>
        </where>
</select>
```
就是通过昵称或email或者二者的组合查找用户数。

#### 4、能够对结果集进行映射

我们在使用JDBC进行查询时，返回一个结果集ResultSet,我们要从结果集中取出结果封装为需要的类型

在Mybatis中我们可以设置将结果直接映射为自己需要的类型，比如：JavaBean对象、一个Map、一个List等等。像上个例子中就是将结果映射为int类型。
