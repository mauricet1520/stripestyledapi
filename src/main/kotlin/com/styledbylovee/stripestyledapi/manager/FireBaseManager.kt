package com.styledbylovee.stripestyledapi.manager

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.springframework.stereotype.Component

@Component
class FireBaseManager {

    fun findZipCode(zipCode: Int): Boolean {

        val zipCodeList = arrayListOf<Int>(30002, 30008, 30009, 30021, 30022, 30030, 30032, 30033, 30034, 30035, 30038, 30060, 30062, 30063, 30066, 30067, 30068, 30069, 30071, 30072, 30075, 30076, 30079, 30080, 30082, 30083, 30084, 30088, 30090, 30092, 30093, 30126, 30152, 30288, 30291, 30297, 30303, 30304, 30305, 30306, 30307, 30308, 30309, 30310, 30311, 30312, 30313, 30314, 30315, 30316, 30317, 30318, 30319, 30322, 30324, 30327, 30328, 30329, 30330, 30331, 30336, 30337, 30338, 30339, 30340, 30341, 30342, 30344, 30345, 30346, 30349, 30350, 30354, 30360, 30363)

        zipCodeList.forEach {
            if (it == zipCode) {
                return true
            }
        }
        return false
    }
}