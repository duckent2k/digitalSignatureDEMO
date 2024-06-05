package com.example.digitalsignature.generate

import java.math.BigInteger
import java.security.SecureRandom

class RSAAlgorithm(bigLength: Int) {

    var e: BigInteger
    val d: BigInteger
    val n: BigInteger
    val p: BigInteger
    val q: BigInteger
    private val random: SecureRandom = SecureRandom()

    init {
//    Chọn 2 số p,q bất kì
        p = BigInteger.probablePrime(bigLength / 2, random)
        q = BigInteger.probablePrime(bigLength / 2, random)

//    Tính n = p * q
        n = p.multiply(q)

//    Tính số phi(n) = (p - 1) * (q - 1)
        val phi = q.subtract(BigInteger.ONE).multiply(p.subtract(BigInteger.ONE))

//    Kiểm tra phi thoả mãn điều kiện 1 < e < phi(n) và gcd(e, phi(n)) = 1
        do {
            e = BigInteger(phi.bitLength(), random) // Khoá công khai
        } while (e <= BigInteger.ONE || e >= phi || e.gcd(phi) != BigInteger.ONE)

//    Tính d = e ^ -1 mod phi
        d = e.modInverse(phi) // Khoá bí mật

    }

    //    Hàm mã hoá chữ ký số
    fun encryptSignature(message: String, privateKey: BigInteger, n: BigInteger): BigInteger {
        val messageBytes = message.toByteArray()
        val messageBigInt = BigInteger(1, messageBytes)
        return messageBigInt.modPow(privateKey, n)
    }

    //    Hàm giải mã chữ ký số
    fun decryptSignature(
        encryptedSignature: BigInteger,
        publicKey: BigInteger,
        n: BigInteger
    ): String {
        val decryptedSignature = encryptedSignature.modPow(publicKey, n)
        val decryptedMessageBytes = decryptedSignature.toByteArray()
        return String(decryptedMessageBytes)
    }

}
