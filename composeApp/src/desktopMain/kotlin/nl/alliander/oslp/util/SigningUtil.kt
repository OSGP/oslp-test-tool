package nl.alliander.oslp.util

import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.Signature

object SigningUtil {

    fun createSignature(
        message: ByteArray,
        privateKey: PrivateKey
    ): ByteArray =
        Signature.getInstance(SECURITY_ALGORITHM, SECURITY_PROVIDER).apply {
            initSign(privateKey, SecureRandom())
            update(message)
        }.sign()

    fun verifySignature(
        message: ByteArray,
        securityKey: ByteArray,
        publicKey: PublicKey,
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

    private const val SECURITY_PROVIDER = "SunEC"
    private const val SECURITY_ALGORITHM = "SHA256withECDSA"
}
