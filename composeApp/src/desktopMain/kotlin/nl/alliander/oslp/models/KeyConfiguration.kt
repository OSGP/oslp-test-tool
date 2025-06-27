/*
 * Copyright 2025 Alliander N.V.
 */
package nl.alliander.oslp.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.swing.JOptionPane

object KeyConfiguration {
    private val SECURITY_PROVIDER = "SunEC"
    private val SECURITY_KEYTYPE = "EC"

    private var _privateKeyBytes by mutableStateOf<ByteArray?>(null)
    private var _publicKeyBytes by mutableStateOf<ByteArray?>(null)

    var privateKeyBytes: ByteArray?
        get() = _privateKeyBytes
        set(value) {
            _privateKeyBytes = null
            privateKey = value?.let { createPrivateKey(it) }
            privateKey?.let { _privateKeyBytes = value }
        }

    var publicKeyBytes: ByteArray?
        get() = _publicKeyBytes
        set(value) {
            _publicKeyBytes = null
            publicKey = value?.let { createPublicKey(it) }
            publicKey?.let { _publicKeyBytes = value }
        }

    var privateKey: PrivateKey? = null
        private set

    var publicKey: PublicKey? = null
        private set

    fun validKeys() = publicKey != null && privateKey != null

    private fun createPrivateKey(key: ByteArray): PrivateKey? =
        runCatching {
                KeyFactory.getInstance(SECURITY_KEYTYPE, SECURITY_PROVIDER).generatePrivate(PKCS8EncodedKeySpec(key))
            }
            .onFailure { showErrorDialog("Failed to create private key: ${it.message}") }
            .getOrNull()

    private fun createPublicKey(key: ByteArray): PublicKey? =
        runCatching {
                KeyFactory.getInstance(SECURITY_KEYTYPE, SECURITY_PROVIDER).generatePublic(X509EncodedKeySpec(key))
            }
            .onFailure { showErrorDialog("Failed to create public key: ${it.message}") }
            .getOrNull()

    private fun showErrorDialog(message: String) {
        JOptionPane.showMessageDialog(null, message, "Invalid key", JOptionPane.ERROR_MESSAGE)
    }
}
