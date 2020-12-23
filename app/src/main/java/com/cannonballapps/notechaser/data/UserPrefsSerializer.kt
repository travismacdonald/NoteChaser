package com.cannonballapps.notechaser.data


import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import com.cannonballapps.notechaser.UserPrefs
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import kotlin.coroutines.coroutineContext

object UserPrefsSerializer : Serializer<UserPrefs> {

    override val defaultValue: UserPrefs = UserPrefs.getDefaultInstance()

    override fun readFrom(input: InputStream): UserPrefs {
        try {
            return UserPrefs.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override fun writeTo(t: UserPrefs, output: OutputStream) = t.writeTo(output)

}