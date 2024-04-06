package com.example.employeeportal.encrypter

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionHelper {
    private const val AES_MODE = "AES/CBC/PKCS5Padding"
    private const val SECRET_KEY = "OregonState159!@"
    private const val IV = "StateOregon159@!"

    fun encrypt(data: String): String {
        val cipher = Cipher.getInstance(AES_MODE)
        val secretKey = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
        val ivParameterSpec = IvParameterSpec(IV.toByteArray())
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)
        val encryptedBytes = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun decrypt(data: String): String {
        val cipher = Cipher.getInstance(AES_MODE)
        val secretKey = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
        val ivParameterSpec = IvParameterSpec(IV.toByteArray())
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec)
        val decodedBytes = Base64.decode(data, Base64.DEFAULT)
        val decryptedBytes = cipher.doFinal(decodedBytes)
        return String(decryptedBytes, Charsets.UTF_8)
    }
}