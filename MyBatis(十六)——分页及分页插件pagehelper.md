@[TOC]

#### 前言
mybatis框架分页实现，有几种方式，最简单的就是利用原生的sql关键字limit来实现，还有一种就是利用interceptor来拼接sql，实现和limit一样的功能，再一个就是利用PageHelper来实现。这里以MySQL为例讲解这两种常见的实现方式。

无论哪种实现方式，我们返回的结果，不能再使用List了，需要一个自定义对象Pager：

```java
package com.macay.entity;

import java.util.List;

/**
 * @ClassName: Pager,分页对象
 * @Description:
 * @Author: Macay
 * @Date: 2021/9/22 11:58 下午
 */
public class Pager<T> {
    private int page;//分页起始页
    private int size;//每页记录数
    private List<T> rows;//返回的记录集合
    private long total;//总记录条数

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Pager{" +
                "page=" + page +
                ", size=" + size +
                ", rows=" + rows +
                ", total=" + total +
                '}';
    }
}

```


#### 一、使用limit的普通分页
使用limit分页的核心就是SQL，不用任何插件或者工具就能够很方便的实现的方法，我们在mapper文件中拼接这个SQL：

```xml
SELECT * from user limit startIndex,pageSize 
```
startIndex：分页的起始位置，从0开始；
pageSize：查询的数量；

接口：

```java
List<Student> getStudentByPage(Map<String, Object> params);
long getCount();
```
mapper文件：

```xml
<select id="getStudentByPage" resultType="com.macay.entity.Student">
    select * from student limit #{startIndex}, #{pageSize}
</select>
<select id="getCount" resultType="long">
    select count(*) from student
</select>
```
测试类：

```java
@Test
public void testGetStudentByPage() {
    Pager<Student> studnentByPage = getStudnentByPage(2, 5);
    System.out.println(studnentByPage);
}
private Pager<Student> getStudnentByPage(int page, int size) {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    StudentDao mapper = sqlSession.getMapper(StudentDao.class);
    Map<String, Object> params = new HashMap<>();
    params.put("startIndex", (page-1)*size);
    params.put("pageSize", size);
    List<Student> students = mapper.getStudentByPage(params);
    Pager<Student> studentPager = new Pager<>();
    studentPager.setPage(page);
    studentPager.setSize(size);
    studentPager.setRows(students);
    studentPager.setTotal(mapper.getCount());
    return studentPager;
}
```
测试结果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/40dcd19bbfc846f19c690eb6c0b54d54.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBATWFjYXk=,size_20,color_FFFFFF,t_70,g_se,x_16)
#### 二、使用Mybatis分页拦截器实现
使用Mybatis分页拦截器，我们可以不用在Mapper配置文件中写分页查询语句，我们只需要写非分页查询语句就行，然后通过分页拦截器，拦截到需要分页查询的普通sql,将普通的sql替换成分页sql，非常巧妙的实现分页查询。

这里暂不做深入讲解，具体可以搜索一下网上的讲解：

[https://blog.csdn.net/u014292162/article/details/52089808](https://blog.csdn.net/u014292162/article/details/52089808)

[https://blog.csdn.net/feinifi/article/details/88769101](https://blog.csdn.net/feinifi/article/details/88769101)
#### 三、使用分页插件pagehelper
此插件支持的数据库版本很多，如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/8e45804c7e69499fbdf3f0a28d0b676d.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBATWFjYXk=,size_20,color_FFFFFF,t_70,g_se,x_16)
github地址如下：[https://github.com/pagehelper/Mybatis-PageHelper](https://github.com/pagehelper/Mybatis-PageHelper)


其实PageHelper方法也是第二种使用Interceptor拦截器方式的一种三方实现，它内部帮助我们实现了Interceptor的功能。基本步骤如下：

##### 1、引入maven依赖

```xml
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>5.2.0</version>
</dependency>
```
##### 2、加入 plugin 配置
在 mybatis 核心配置文件中配置 PageHelper 插件(注意target目录）：

```xml
<!--在<environments>之前加入-->
<plugins>
    <plugin interceptor="com.github.pagehelper.PageInterceptor"/>
</plugins>
```
注意：从4.0.0版本以后已经可以自动识别数据库了，不需要再去指定数据库
##### 3、PageHelper 对象
**查询语句之前调用 PageHelper.startPage 静态方法。**
在你需要进行分页的 MyBatis 查询方法前调用 PageHelper.startPage 静态方法即可，紧跟
在这个方法后的第一个 MyBatis 查询方法会被进行分页。

接口如下：

```java
List<Student> getStudentByPageHelper();
```
mapper文件：

```xml
<select id="getStudentByPageHelper" resultType="com.macay.entity.Student">
    select * from student
</select>
```
测试类：

```java
@Test
public void getStudentByPageHelper() {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    StudentDao mapper = sqlSession.getMapper(StudentDao.class);
    PageHelper.startPage(2, 5);
    List<Student> students = mapper.getStudentByPageHelper();
    System.out.println(students);
}
```
![在这里插入图片描述](https://img-blog.csdnimg.cn/92151d645f19474b9ea1aa781057adf5.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBATWFjYXk=,size_20,color_FFFFFF,t_70,g_se,x_16)
如图所示，查询得到的是第二页的数据，每页5条。

不仅如此，我们还可以获取分页相关的其他参数：

```java
@Test
public void getStudentByPageHelper() {
    // 集成分页助手插件，测试分页
    SqlSession sqlSession = SqlSessionUtils.getSqlSession();
    StudentDao mapper = sqlSession.getMapper(StudentDao.class);
    // 设置分页相关参数 当前页+每页显示的条数
    PageHelper.startPage(2, 5);
    List<Student> students = mapper.getStudentByPageHelper();
    // System.out.println(students);
    //获取与分页相关的参数
    PageInfo<Student> pageInfo = new PageInfo<>(students);
    System.out.println("当前页："+pageInfo.getPageNum());
    System.out.println("每页显示条数："+pageInfo.getPageSize());
    System.out.println("总条数："+pageInfo.getTotal());
    System.out.println("总页数："+pageInfo.getPages());
    System.out.println("上一页："+pageInfo.getPrePage());
    System.out.println("下一页："+pageInfo.getNextPage());
    System.out.println("是否是第一页："+pageInfo.isIsFirstPage());
    System.out.println("是否是最后一页："+pageInfo.isIsLastPage());
}
```
结果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/18b58ea683a34aea89f65d55c7eb6462.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBATWFjYXk=,size_20,color_FFFFFF,t_70,g_se,x_16)
