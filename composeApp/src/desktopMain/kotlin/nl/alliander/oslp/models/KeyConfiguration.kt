package nl.alliander.oslp.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

class KeyConfiguration {
    private val SECURITY_PROVIDER = "SunEC"
    private val SECURITY_KEYTYPE = "EC"

    private var _privateKeyBytes by mutableStateOf<ByteArray?>(null)
    private var _publicKeyBytes by mutableStateOf<ByteArray?>(null)

    var privateKeyBytes: ByteArray?
        get() = _privateKeyBytes
        set(value) {
            _privateKeyBytes = value
            privateKey = value?.let { createPrivateKey(it) }
        }

    var publicKeyBytes: ByteArray?
        get() = _publicKeyBytes
        set(value) {
            _publicKeyBytes = value
            publicKey = value?.let { createPublicKey(it) }
        }

    var privateKey: PrivateKey? = null
        private set

    var publicKey: PublicKey? = null
        private set

    //TODO: maybe for later, if you upload something which is not a valid key, the app crashes, might want to handle that more gracefully.
    private fun createPrivateKey(key: ByteArray): PrivateKey {
        val keyFactory = KeyFactory.getInstance(SECURITY_KEYTYPE, SECURITY_PROVIDER)

        return keyFactory.generatePrivate(PKCS8EncodedKeySpec(key)) ?: throw Exception("No Key")
    }

    private fun createPublicKey(key: ByteArray): PublicKey {
        val keyFactory = KeyFactory.getInstance(SECURITY_KEYTYPE, SECURITY_PROVIDER)
        return keyFactory.generatePublic(X509EncodedKeySpec(key)) ?: throw Exception("No Key")
    }
}
