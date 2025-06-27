// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package nl.alliander.oslp.util

import java.security.SecureRandom
import java.security.Signature
import nl.alliander.oslp.models.KeyConfiguration

object SigningUtil {

    fun createSignature(message: ByteArray): ByteArray =
        Signature.getInstance(SECURITY_ALGORITHM, SECURITY_PROVIDER)
            .apply {
                initSign(KeyConfiguration.privateKey, SecureRandom())
                update(message)
            }
            .sign()

    fun verifySignature(message: ByteArray, securityKey: ByteArray): Boolean {
        val builder =
            Signature.getInstance(SECURITY_ALGORITHM, SECURITY_PROVIDER).apply {
                initVerify(KeyConfiguration.publicKey)
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
