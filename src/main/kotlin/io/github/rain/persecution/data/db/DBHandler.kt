package io.github.rain.persecution.data.db

import org.ktorm.database.Database
import java.sql.Connection
import java.sql.DriverManager
import kotlin.concurrent.thread

/**
 * io.github.rain.persecution.data.db.SQLHandler
 * persecution
 *
 * @author 寒雨
 * @since 2022/3/5 18:21
 **/
object DBHandler {

    lateinit var database: Database

    fun init() {
        val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123456")

        Runtime.getRuntime().addShutdownHook(
            thread(start = false) {
                // 进程退出时，关闭连接
                conn.close()
            }
        )

        database = Database.connect {
            object : Connection by conn {
                override fun close() {
                    // 重写 close 方法，保持连接不关闭
                }
            }
        }
    }

}