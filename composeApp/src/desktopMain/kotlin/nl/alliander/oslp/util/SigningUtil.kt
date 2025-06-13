package nl.alliander.oslp.util

import kotlinx.coroutines.runBlocking
import oslp_test_tool.composeapp.generated.resources.Res
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

object SigningUtil {

    private val publicKey: PublicKey by lazy {
        createPublicKey("files/oslp_sim_ecdsa_public.der")
    }

    private val privateKey: PrivateKey by lazy {
        createPrivateKey("files/oslp_test_ecdsa_private.der")
    }

    fun createSignature(
        message: ByteArray,
    ): ByteArray =
        Signature.getInstance(SECURITY_ALGORITHM, SECURITY_PROVIDER).apply {
            initSign(privateKey, SecureRandom())
            update(message)
        }.sign()

    fun verifySignature(
        message: ByteArray,
        securityKey: ByteArray,
    ): Boolean {
        val builder = Signature.getInstance(SECURITY_ALGORITHM, SECURITY_PROVIDER).apply {
            initVerify(publicKey)
            update(message)
        }

        // Truncation needed for some signature types, including the used SHA256withECDSA
        val len = securityKey[1] + 2 and 0xff
        val truncatedKey = securityKey.copyOf(len)

        return builder.verify(truncatedKey)
    }

    private fun createPrivateKey(keyPath: String): PrivateKey {
        val key = runBlocking { Res.readBytes(keyPath) }
        val keyFactory = KeyFactory.getInstance(SECURITY_KEYTYPE, SECURITY_PROVIDER)

        return keyFactory.generatePrivate(PKCS8EncodedKeySpec(key)) ?: throw Exception("No Key")
    }

    private fun createPublicKey(keyPath: String): PublicKey {
        val key = runBlocking { Res.readBytes(keyPath) }
        val keyFactory = KeyFactory.getInstance(SECURITY_KEYTYPE, SECURITY_PROVIDER)

        return keyFactory.generatePublic(X509EncodedKeySpec(key)) ?: throw Exception("No Key")
    }

    const val SECURITY_PROVIDER = "SunEC"
    const val SECURITY_KEYTYPE = "EC"
    const val SECURITY_ALGORITHM = "SHA256withECDSA"
}
