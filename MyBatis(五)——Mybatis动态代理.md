@[TOC]

### 一、原始Dao层开发
#### 1、原理
前面的例子我们是将SQL的执行写在了测试方法中，在实际的开发中我们肯定是调用Dao接口的方法去执行sql的，也就是我们所说的**原始Dao层开发**，通过Dao接口的实现类去关联执行对应的sql，步骤如下：

 1. 需要我们手动编写dao接口和dao接口的实现类。
 2. 在实现类中使用一些对象，由SqlSessionFactoryBuilder对象获得SqlSessionFactory对象，由SqlSessionFactory对象获得SqlSession对象，再由SqlSession对象里面封装的一系列方法实现对数据库的操作。

#### 2、代码
创建dao接口的实现类（其实就是将Test测试里面的逻辑移到了这里）：
```java
public class StudentDaoImpl implements StudentDao {
    @Override
    public Student selectStudentById(Integer id) {
      // 一些代码已封装
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        String sqlId = "com.macay.dao.StudentDao"+"."+"selectStudentById";
        Student students = sqlSession.selectOne(sqlId);
        sqlSession.close();
        return students;
    }

    @Override
    public List<Student> selectStudents() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        String sqlId = "com.macay.dao.StudentDao"+"."+"selectStudents";
        List<Student> students = sqlSession.selectList(sqlId);
        sqlSession.close();
        return students;
    }

    @Override
    public void insertStudent(Student student) {
    }
}
```
测试类中直接调用dao对象中的方法完成对sql语句的执行：

```java
    @Test
    public void testSelectStudentsByUtils() {
        StudentDao studentDao = new StudentDaoImpl();
        Student student = studentDao.selectStudentById(2);
        System.out.println(student);
    }

    @Test
    public void testInsert() {
        StudentDao studentDao = new StudentDaoImpl();
        List<Student> students = studentDao.selectStudents();
        students.forEach(student -> System.out.println(student));
    }
```
虽然这种方式是可行的，但是如果有多个dao接口我们就要创建多个实现类，不但有大量重复的代码，而且存在硬编码不利于维护。

### 二、Mapper动态代理
Mybatis使用了动态代理机制，我们可以只编写数据交互的接口及方法定义，和对应的Mapper映射文件，具体的交互方法实现由MyBatis来完成。这样大大节省了开发DAO层的时间。


实现Mapper动态代理的方法并不难，但是使用要**遵守一定的开发规范**，如下：

 1. 创建一个interface接口，接口名称保持与某个mapper.xml配置文件相同。
 2. mapper的namespace指定interface全路径名。
 3. 接口中方法定义的方法和方法参数，以及方法返回类型，都与mapper.xml配置文件中的SQL映射的id及输入/输出映射类型相同。

满足这几点，就可以使用SqlSession类获取Mapper代理（即一个interface接口类型的对象）来执行SQL映射配置。

#### 1、Dao（Mapper）接口的工作原理：
Dao接口的工作原理实际上就是**动态代理**，在执行Dao接口的方法时，实际上执行的是mybatis的代理对象，代理对象通过**反射机制**获取Dao接口的类全名和方法，再通过类全名和方法找到Xml中要执行的sql语句，然后使用SqlSession对象执行sql语句并将sql执行结果返回。

源码解析：[https://blog.csdn.net/xiaokang123456kao/article/details/76228684](https://blog.csdn.net/xiaokang123456kao/article/details/76228684)
#### 2、mybatis代理实现方式
**使用SqlSession对象的方法 getMapper(dao.class)**

代码如下：

```java
public class MyTest {

    /**
     * 动态代理开发
     */
    @Test
    public void testSelectStudentsByUtils() {
        // 1、获得sqlSession对象
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        // 2、获取Mapper代理对象
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        // 3、执行Mapper代理对象的查询方法
        Student student = mapper.selectStudentById(2);
        System.out.println(student);
        // 4、关闭session
        sqlSession.close();
    }

    @Test
    public void testInsert() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        List<Student> students = mapper.selectStudents();
        students.forEach(student -> System.out.println(student));
        sqlSession.close();
    }
}
```
打印mapper对象也可以看到这是一个代理对象：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210503180218783.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)
