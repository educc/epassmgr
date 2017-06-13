package com.epassmgr

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.crypto.BadPaddingException

/**
 * Created by USER on 12/06/2017.
 */


class PassManager {
    var key:String = ""
    var content:Array<ItemPass> = arrayOf()

    val INIT_VECTOR = "HD7UiIAY5y1rqZQu"

    constructor(key:String, filename:String){
        this.key = key
        if ( key.length != 16){
            throw AppException("The key should has 16 characters")
        }

        try {
            val strContent:String? = this.decryptFile(filename, key)
            if( strContent != null){
                this.content =  Utils.parse(strContent)
            }else{
                throw AppException("The key is invalid")
            }
        }catch (ex: JsonSyntaxException){
            throw AppException("The key is invalid")
        }catch (ex: Exception){
            throw AppException(ex.message)
        }
    }

    fun decryptFile(filename:String, key:String):String? {
        val bytes = Files.readAllBytes(Paths.get(filename))
        return Encryptor.decrypt(this.key, INIT_VECTOR, bytes)
    }

    fun find(key:String):ItemPass? {
        for (item in this.content){
            if ( key.equals(item.name,true)){
                return item
            }
        }
        return null
    }

    fun listAll():Array<ItemPass>{
        return content
    }

}