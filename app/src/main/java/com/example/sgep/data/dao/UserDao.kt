package com.example.sgep.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.sgep.data.entity.UserEntity

@Dao
interface UserDao {

    @Insert
    suspend fun registerUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    fun getUserFlowByEmail(email: String): Flow<UserEntity?>
}