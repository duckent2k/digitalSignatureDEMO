package com.example.digitalsignature.generate

import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.SecureRandom

class RSA {
    private val bitLength = 1024
    private val random = SecureRandom()

    val pubKey: Pair<BigInteger, BigInteger>
    val priKey: Pair<BigInteger, BigInteger>

    init {
        val p = BigInteger(bitLength, 100, random)
        val q = BigInteger(bitLength, 100, random)
        val n = p.multiply(q)
        val phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE))
        var e: BigInteger
        do {
            e = BigInteger(bitLength / 2, random)
        } while (e.gcd(phi) != BigInteger.ONE || e >= phi)
        val d = e.modInverse(phi)

        pubKey = Pair(e, n)
        priKey = Pair(d, n)
    }

    fun encrypt(message: String, publicKey: Pair<BigInteger, BigInteger>): BigInteger {
        val messageBytes = message.toByteArray(StandardCharsets.UTF_8)
        val messageBigInt = BigInteger(1, messageBytes)
        return messageBigInt.modPow(publicKey.first, publicKey.second)
    }

    fun decrypt(ciphertext: BigInteger, privateKey: Pair<BigInteger, BigInteger>): String {
        val decryptedBigInt = ciphertext.modPow(privateKey.first, privateKey.second)
        val decryptedBytes = decryptedBigInt.toByteArray()
        return String(decryptedBytes, StandardCharsets.UTF_8).trim()
    }

}