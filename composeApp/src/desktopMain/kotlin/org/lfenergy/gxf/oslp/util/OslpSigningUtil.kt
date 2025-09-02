// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.oslp.util

import com.gxf.utilities.oslp.message.signing.SigningUtil
import com.gxf.utilities.oslp.message.signing.configuration.SigningProperties

class OslpSigningUtil(signingProperties: SigningProperties, val oslpKeyProvider: OslpKeyProvider) :
    SigningUtil(signingProperties, oslpKeyProvider) {

    companion object {
        private var instance: OslpSigningUtil? = null
        private const val SECURITY_ALGORITHM = "SHA256withECDSA"

        fun getInstance(): OslpSigningUtil =
            instance
                ?: OslpSigningUtil(
                        SigningProperties(OslpKeyProvider.SECURITY_PROVIDER, SECURITY_ALGORITHM),
                        OslpKeyProvider(),
                    )
                    .also { instance = it }
    }
}
