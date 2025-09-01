// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.oslp.util

import com.gxf.utilities.oslp.message.signing.SigningUtil
import com.gxf.utilities.oslp.message.signing.configuration.SigningProperties

class OslpSigningUtil {

    companion object {
        private var instance: SigningUtil? = null
        private const val SECURITY_ALGORITHM = "SHA256withECDSA"

        fun getInstance(): SigningUtil =
            instance
                ?: SigningUtil(
                        SigningProperties(OslpKeyProvider.SECURITY_PROVIDER, SECURITY_ALGORITHM),
                        OslpKeyProvider(),
                    )
                    .also { instance = it }
    }
}
