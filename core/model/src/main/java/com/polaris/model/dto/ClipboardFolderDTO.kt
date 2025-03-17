package com.polaris.model.dto

data class ClipboardFolderDTO(
    var id: String ,
    var timestamp: Long = System.currentTimeMillis(),
    var name: String ,
    var owner: String ,
)