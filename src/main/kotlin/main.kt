/**
 * Basic example of how to generate and check signature of a
 * message using asymmetric cryptography.
 **/
import java.nio.file.Files
import java.nio.file.Path
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*

data class SignedMessage(val data: String, val signature: String)

fun main(args: Array<String>) {
    var signedMessage = sign("key.private.der", "Hello, world")
    println(signedMessage)

    val isSigned = verify("key.public.der", signedMessage)
    println("Correct signed: $isSigned")

    // Intentional corruption
    signedMessage = signedMessage.copy(data = "Hello, world!")
    val isCorruptSigned = verify("key.public.der", signedMessage)
    println("Corrupt signed: $isCorruptSigned")
}

fun sign(privateKey: String, message: String): SignedMessage {
    val bytes = Files.readAllBytes(Path.of(privateKey))
    val spec = PKCS8EncodedKeySpec(bytes)
    val privateKey = KeyFactory.getInstance("RSA").generatePrivate(spec)

    val signature = Signature.getInstance("SHA256withRSA")
    signature.initSign(privateKey)
    signature.update(message.toByteArray())
    val signed = signature.sign()

    return SignedMessage(
        data = message,
        signature = Base64.getEncoder().encodeToString(signed)
    )
}

fun verify(publicKey: String, signedMessage: SignedMessage): Boolean {
    val bytes = Files.readAllBytes(Path.of(publicKey))
    val spec = X509EncodedKeySpec(bytes)
    val publicKey = KeyFactory.getInstance("RSA").generatePublic(spec)

    val signature = Signature.getInstance("SHA256withRSA")
    signature.initVerify(publicKey)
    signature.update(signedMessage.data.toByteArray())

    val messageSignature = Base64.getDecoder().decode(signedMessage.signature)
    return signature.verify(messageSignature)
}
