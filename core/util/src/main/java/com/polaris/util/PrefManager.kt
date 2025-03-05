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
    var userSignInCheck: Boolean
        get() = pref.getBoolean(PreferenceConstants.USER_SIGN_IN_CHECK, false)
        set(v) {
            pref.edit().putBoolean(PreferenceConstants.USER_SIGN_IN_CHECK, v).apply()
        }

}