// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.oslp.util

import com.gxf.utilities.oslp.message.signing.KeyProvider
import java.io.File
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.swing.JOptionPane
import org.lfenergy.gxf.oslp.models.ApplicationConfiguration

class OslpKeyProvider : KeyProvider {
    companion object {
        const val SECURITY_PROVIDER = "SunEC"
        private const val SECURITY_KEYTYPE = "EC"
    }

    private lateinit var publicKey: PublicKey
    private lateinit var privateKey: PrivateKey

    private val config = ApplicationConfiguration.get()

    init {
        try {
            privateKey =
                KeyFactory.getInstance(SECURITY_KEYTYPE, SECURITY_PROVIDER)
                    .generatePrivate(PKCS8EncodedKeySpec(File(config.privateKeyPath).readBytes()))
            publicKey =
                KeyFactory.getInstance(SECURITY_KEYTYPE, SECURITY_PROVIDER)
                    .generatePublic(X509EncodedKeySpec(File(config.publicKeyPath).readBytes()))
        } catch (e: Exception) {
            showErrorDialog("Invalid key: ${e.message}")
        }
    }

    override fun getPublicKey(): PublicKey {
        return publicKey
    }

    override fun getPrivateKey(): PrivateKey {
        return privateKey
    }

    private fun showErrorDialog(message: String) {
        JOptionPane.showMessageDialog(null, message, "Invalid key", JOptionPane.ERROR_MESSAGE)
    }
}
