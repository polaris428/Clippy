package com.polaris.model.model

import com.polaris.model.dto.UserDTO

data class User(val id:String, val folderList: ArrayList<String> )

fun User.toDTO():UserDTO{
    return UserDTO(id=id,  folderList= folderList )

}
