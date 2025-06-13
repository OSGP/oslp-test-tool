package nl.alliander.oslp.util

fun ByteArray.toInt(): Int {
    return this.fold(0) { acc, byte -> (acc shl 8) or (byte.toInt() and 0xFF) }
}

fun Int.toByteArray(numBytes: Int): ByteArray {
    require(numBytes in 1..4) { "numBytes must be between 1 and 4" }
    return ByteArray(numBytes) { i ->
        (this shr (8 * (numBytes - i - 1)) and 0xFF).toByte()
    }
}
