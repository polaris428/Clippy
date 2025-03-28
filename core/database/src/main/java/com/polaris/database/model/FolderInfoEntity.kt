package com.polaris.database.model

import com.polaris.model.model.FolderInfo

data class FolderInfoEntity(
    val id: String,
    val name: String
)

fun FolderInfoEntity.toModel(): FolderInfo {
    return FolderInfo(id = id , name = name)
}
fun List<FolderInfoEntity>.toModelList(): List<FolderInfo> {
    return this.map { it.toModel() }
}
