package com.example.sgep.data.repository

import com.example.sgep.data.dao.UserDao
import com.example.sgep.data.entity.UserEntity

class UserRepository(private val userDao: UserDao) {
    suspend fun registerUser(email: String, password: String) {
        val user = UserEntity(email = email, password = password)
        userDao.registerUser(user)
    }

    suspend fun login(email: String, password: String): UserEntity? {
        return userDao.login(email, password)
    }
}