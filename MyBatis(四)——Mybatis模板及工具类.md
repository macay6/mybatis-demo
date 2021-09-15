@[TOC]
#### 1、使用IDEA创建的mybatis通过mapper接口加载映射文件不生效问题
mybatis最终加载的是编译后target目录下面的配置文件，若此目录下没有执行会报错：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210503103133420.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)

[https://blog.csdn.net/Doctor_LY/article/details/83000745](https://blog.csdn.net/Doctor_LY/article/details/83000745)

#### 2、创建mapper.xml模板和主配置文件模板
创建模版的步骤：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210503111214213.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)
创建模版文件：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210503111235314.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)
创建文件选择使用的模版：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210503111257868.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDA3NTk2Mw==,size_16,color_FFFFFF,t_70)

#### 3、使用工具类获取sqlsession对象

```java
public class SqlSessionUtils {
    private static SqlSessionFactory sessionFactory = null;

    // 初始化sqlsessionFactory
    static {
        String config = "mybatis.xml";
        try {
            InputStream resourceAsStream = Resources.getResourceAsStream(config);
            sessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 创建方法，获取SqlSession对象
    public static SqlSession getSqlSession() {
        SqlSession sqlSession = null;
        if (null != sessionFactory) {
            sqlSession = sessionFactory.openSession();
        }
        return sqlSession;
    }
}
```
使用帮助类：

```java
    /**
     * 工具类批量查询
     */
    @Test
    public void testSelectStudentsByUtils() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        String sqlId = "com.macay.dao.StudentDao"+"."+"selectStudents";
        List<Student> students = sqlSession.selectList(sqlId);
        for (Student student : students) {
            System.out.println(student);
        }
        sqlSession.close();
    }
```
