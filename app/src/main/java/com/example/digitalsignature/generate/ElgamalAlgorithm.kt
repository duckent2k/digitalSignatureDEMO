package com.example.digitalsignature.generate

import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.SecureRandom

class ElgamalAlgorithm(bitLength: Int) {

    private val publicKey: PublicKey
    private val privateKey: PrivateKey
    val p: BigInteger
    val g: BigInteger
    val x: BigInteger
    val y: BigInteger
    private val random: SecureRandom = SecureRandom()

    data class PublicKey(val p: BigInteger, val g: BigInteger, val y: BigInteger)
    data class PrivateKey(val x: BigInteger)
    data class CipherText(val c1: BigInteger, val c2: BigInteger)


    init {
        p = BigInteger.probablePrime(bitLength, random)
        g = BigInteger.probablePrime(bitLength, random).mod(p.subtract(BigInteger.ONE))
            .add(BigInteger.ONE)
        x = BigInteger(bitLength - 1, random)
        y = g.modPow(x, p)

        publicKey = PublicKey(p, g, y)
        privateKey = PrivateKey(x)
    }

    //    Hàm mã hoá
    fun encrypt(message: String): CipherText {
        val m = BigInteger(1, message.toByteArray())
        val k = BigInteger(
            publicKey.p.bitLength(),
            SecureRandom()
        ).mod(publicKey.p.subtract(BigInteger.ONE)).add(BigInteger.ONE)
        val c1 = publicKey.g.modPow(k, publicKey.p)
        val c2 = m.multiply(publicKey.y.modPow(k, publicKey.p)).mod(publicKey.p)
        return CipherText(c1, c2)
    }

    //    Hàm giải mã
    fun decrypt(cipherText: CipherText): String {
        val s = cipherText.c1.modPow(privateKey.x, publicKey.p)
        val sInverse = s.modInverse(publicKey.p)
        val m = cipherText.c2.multiply(sInverse).mod(publicKey.p)
        return String(m.toByteArray(), StandardCharsets.UTF_8)
    }
}