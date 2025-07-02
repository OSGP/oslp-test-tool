// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.oslp.util

import java.io.File
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.swing.JOptionPane
import org.lfenergy.gxf.oslp.models.ApplicationConfiguration

object SigningUtil {
    private const val SECURITY_PROVIDER = "SunEC"
    private const val SECURITY_ALGORITHM = "SHA256withECDSA"
    private const val SECURITY_KEYTYPE = "EC"

    private lateinit var publicKey: PublicKey
    private lateinit var privateKey: PrivateKey

    private val config = ApplicationConfiguration.get()

    fun initializeKeys(): Boolean {
        try {
            privateKey =
                KeyFactory.getInstance(SECURITY_KEYTYPE, SECURITY_PROVIDER)
                    .generatePrivate(PKCS8EncodedKeySpec(File(config.privateKeyPath).readBytes()))
            publicKey =
                KeyFactory.getInstance(SECURITY_KEYTYPE, SECURITY_PROVIDER)
                    .generatePublic(X509EncodedKeySpec(File(config.publicKeyPath).readBytes()))
        } catch (e: Exception) {
            showErrorDialog("Invalid key: ${e.message}")
            return false
        }
        return true
    }

    fun createSignature(message: ByteArray): ByteArray =
        Signature.getInstance(SECURITY_ALGORITHM, SECURITY_PROVIDER)
            .apply {
                initSign(privateKey, SecureRandom())
                update(message)
            }
            .sign()

    fun verifySignature(message: ByteArray, securityKey: ByteArray): Boolean {
        val builder =
            Signature.getInstance(SECURITY_ALGORITHM, SECURITY_PROVIDER).apply {
                initVerify(publicKey)
                update(message)
            }

        // Truncation needed for some signature types, including the used SHA256withECDSA
        val len = securityKey[1] + 2 and 0xff
        val truncatedKey = securityKey.copyOf(len)

        return builder.verify(truncatedKey)
    }

    private fun showErrorDialog(message: String) {
        JOptionPane.showMessageDialog(null, message, "Invalid key", JOptionPane.ERROR_MESSAGE)
    }
}
