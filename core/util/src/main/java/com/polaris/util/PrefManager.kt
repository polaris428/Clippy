package com.polaris.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

@SuppressLint("StaticFieldLeak")
object PrefManager {
    private lateinit var context: Context
    private lateinit var pref: SharedPreferences


    private const val KEY_INIT_PREF_MANAGER = "KEY_INIT_PREF_MANAGER"
    fun init(mContext: Context) {
        context = mContext
        pref = context.getSharedPreferences(KEY_INIT_PREF_MANAGER, Context.MODE_PRIVATE)
    }
    var userSignInSkip: Boolean
        get() = pref.getBoolean(PreferenceConstants.USER_SIGN_IN_CHECK, false)
        set(v) {
            pref.edit().putBoolean(PreferenceConstants.USER_SIGN_IN_CHECK, v).apply()
        }
    var userSignInCheck: Boolean
        get() = pref.getBoolean(PreferenceConstants.USER_SIGN_IN_CHECK, false)
        set(v) {
            pref.edit().putBoolean(PreferenceConstants.USER_SIGN_IN_CHECK, v).apply()
        }

    var userUid:String
        get() = pref.getString(PreferenceConstants.USER_UID, "").toString()
        set(v) {
            pref.edit().putString(PreferenceConstants.USER_UID, v).apply()
        }
    var userName: String
        get() = pref.getString(PreferenceConstants.USER_NAME, "")!!
        set(v) {
            pref.edit().putString(PreferenceConstants.USER_NAME, v).apply()
        }

}