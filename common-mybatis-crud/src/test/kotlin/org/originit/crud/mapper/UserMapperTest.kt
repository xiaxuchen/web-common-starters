package org.originit.crud.mapper

import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.originit.crud.BaseDataTest
import org.originit.crud.pojo.User
import org.springframework.context.support.ClassPathXmlApplicationContext

class UserMapperTest: BaseDataTest() {

    @Before
    fun setUp() {
        createBlogDataSource()
    }

    @Test
    fun testSelectById() {
        val app = ClassPathXmlApplicationContext("spring-mybatis.xml")
        val userMapper = app.getBean(UserMapper::class.java)
        // 从SqlSessionFactory中获取UserMapper
        userMapper.delete(User(1))
        Assert.assertNull(userMapper.getById(1))
        userMapper.insert(User(3, "gj", 26))
        Assert.assertNotNull(userMapper.getById(3))
        Assert.assertEquals(userMapper.getById(3), User(3, "gj", 26))
        userMapper.updateById(User(2, "xxc"))
        val gj = userMapper.getById(2)
        Assert.assertEquals(gj, User(2, "xxc",  gj!!.age))
        val user = userMapper.list(null)
        Assert.assertEquals(user.size, 2)
    }
}