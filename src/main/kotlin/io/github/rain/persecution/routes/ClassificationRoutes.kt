package io.github.rain.persecution.routes

import io.github.rain.persecution.data.bean.BaseResponse
import io.github.rain.persecution.data.bean.ClassificationData
import io.github.rain.persecution.data.bean.Pager
import io.github.rain.persecution.data.bean.SingleImageData
import io.github.rain.persecution.data.db.DBHandler
import io.github.rain.persecution.data.db.TableClassificationContent
import io.github.rain.persecution.data.db.TableClassificationInfo
import io.github.rain.persecution.utils.ErrorCode
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import org.ktorm.dsl.*

/**
 * io.github.rain.persecution.routes.ClassificationRoutes
 * persecution
 *
 * @author 寒雨
 * @since 2022/3/5 14:05
 **/
fun Routing.setupClassificationRoutes() {
    // 上传图片
    // 将图片上传到一个储存桶中，再储存url
    post("/upload") {

    }

    // 通过id或name获取分类
    get("/classification") {
        var id = call.request.queryParameters["id"]?.toIntOrNull()
        if (id == null) {
            val name = call.request.queryParameters["name"] ?: return@get let {
                call.respond(
                    BaseResponse(
                        ErrorCode.MISSING_PARAMS,
                        "参数缺失",
                        null
                    )
                )
            }
            id = DBHandler.database
                .from(TableClassificationInfo)
                .select(TableClassificationInfo.id)
                .where { TableClassificationInfo.name eq name }
                .limit(1)
                .map { it[TableClassificationInfo.id] }
                .firstOrNull() ?: return@get let {
                call.respond(
                    BaseResponse(
                        ErrorCode.WRONG_PARAMS,
                        "不存在与该名称匹配的参数",
                        null
                    )
                )
            }
        }
        val result = DBHandler.database
            .from(TableClassificationInfo)
            .select()
            .where { TableClassificationInfo.id eq id }
            .map { ClassificationData(it[TableClassificationInfo.id]!!, it[TableClassificationInfo.name]!!, it[TableClassificationInfo.avatar]!!, it[TableClassificationInfo.description]!!) }
            .firstOrNull() ?: return@get let {
                call.respond(
                    BaseResponse(
                        ErrorCode.WRONG_PARAMS,
                        "参数错误",
                        null
                    )
                )
        }
        call.respond(
            BaseResponse(
                ErrorCode.OK,
                "",
                result
            )
        )
    }

    // 获取分类图片
    // 做下分页处理
    get("/classification/images") {
        val id = call.request.queryParameters["id"]?.toIntOrNull() ?: return@get let {
            call.respond(
                BaseResponse(
                    ErrorCode.MISSING_PARAMS,
                    "参数错误或不完整",
                    null
                )
            )
        }
        val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 20
        val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0
        val results = DBHandler.database
            .from(TableClassificationContent)
            .select()
            .where { TableClassificationContent.cid eq id }
            .limit(limit, offset)
            .map {
                SingleImageData(
                    it[TableClassificationContent.id]!!,
                    it[TableClassificationContent.cid]!!,
                    it[TableClassificationContent.image]!!
                )
            }
        call.respond(
            BaseResponse(
                ErrorCode.OK,
                "",
                Pager(
                    size = results.size,
                    offset = offset,
                    data = results
                )
            )
        )
    }

    // 创建分类
    post("/classification/create") {
        val params = call.receiveParameters()
        if (!assertArgsNonNull(params, "name")) return@post
        DBHandler.database
            .insert(TableClassificationInfo) {
                set(it.name, params["name"])
                set(it.avatar, params["avatar"] ?: "https://tse2-mm.cn.bing.net/th/id/OIP-C.CN79D_9Jx71T6Ugg0Tpx2AHaHa?pid=ImgDet&rs=1")
                set(it.description, params["description"] ?: "没有介绍喵~")
            }
        call.respond(
            BaseResponse(
                ErrorCode.OK,
                "操作成功",
                null
            )
        )
    }

    // 删除分类
    post("/classification/remove") {
        val params = call.receiveParameters()
        val id = params["id"]?.toIntOrNull() ?: return@post let {
            call.respond(
                BaseResponse(
                    ErrorCode.MISSING_PARAMS,
                    "参数错误或不完整",
                    null
                )
            )
        }
        DBHandler.database
            .delete(TableClassificationInfo) { it.id eq id }
        call.respond(
            BaseResponse(
                ErrorCode.OK,
                "操作成功",
                null
            )
        )
    }

    // 获取全部分类
    get("/classification/all") {
        val list = DBHandler.database
            .from(TableClassificationInfo)
            .select()
            .map { ClassificationData(it[TableClassificationInfo.id]!!, it[TableClassificationInfo.name]!!, it[TableClassificationInfo.avatar]!!, it[TableClassificationInfo.description]!!) }
        call.respond(
            BaseResponse(
                ErrorCode.OK,
                "",
                list
            )
        )
    }

    // 搜索
    get("/classification/query") {
        val query = call.request.queryParameters["query"] ?: return@get let {
            call.respond(
                BaseResponse(
                    ErrorCode.MISSING_PARAMS,
                    "参数不完整",
                    null
                )
            )
        }
        val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
        val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0
        val list = DBHandler.database
            .from(TableClassificationInfo)
            .select()
            .where { TableClassificationInfo.name like query }
            .limit(limit, offset)
            .map { ClassificationData(it[TableClassificationInfo.id]!!, it[TableClassificationInfo.name]!!, it[TableClassificationInfo.avatar]!!, it[TableClassificationInfo.description]!!) }
        call.respond(
            BaseResponse(
                ErrorCode.OK,
                "",
                Pager(
                    size = list.size,
                    offset = offset,
                    data = list
                )
            )
        )
    }
}

suspend fun PipelineContext<Unit, ApplicationCall>.assertArgsNonNull(params: Parameters, vararg args: String): Boolean {
    return args
        .all { params[it] != null }
        .also {
            if (!it) {
                call.respond(
                    BaseResponse(
                        ErrorCode.MISSING_PARAMS,
                        "参数不完整",
                        null
                    )
                )
            }
        }
}