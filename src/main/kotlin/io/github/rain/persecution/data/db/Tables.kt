package io.github.rain.persecution.data.db

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * io.github.rain.persecution.data.db.Tables
 * persecution
 *
 * @author 寒雨
 * @since 2022/3/5 18:38
 **/
object TableClassificationInfo : Table<Nothing>("classification_info") {
    val id = int("id").primaryKey()
    val name = varchar("name")
    val avatar = varchar("avatar")
    val description = varchar("description")
}

object TableClassificationContent : Table<Nothing>("classification_content") {
    val id = int("id").primaryKey()
    val cid = int("cid")
    val image = varchar("image")
    val cosKey = varchar("cos_key")
}