package io.github.rain.persecution.data.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.ktorm.database.Database
import java.sql.Connection
import java.sql.DriverManager
import javax.sql.DataSource
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
        database = Database.connect(
            crateDataSource(
                "jdbc:mysql://localhost:3306/persecution",
                "root",
                "!Ghy030608"
            )
        )
    }

    // create hikari data source
    private fun crateDataSource(
        url: String,
        user: String,
        password: String,
    ): DataSource {
        val config = HikariConfig()
        config.apply {
            jdbcUrl = url
            driverClassName = "com.mysql.cj.jdbc.Driver"
            username = user
            setPassword(password)
        }
        return HikariDataSource(config)
    }
}