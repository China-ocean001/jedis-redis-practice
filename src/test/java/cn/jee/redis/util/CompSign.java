package cn.jee.redis.util;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class CompSign {
  public static void main(String[] args) throws Exception {
    doVerify();
  }

  public static boolean doVerify() throws Exception {
    byte[] publicKeyBytes = loadKeyFromFile("public.key");
    PublicKey loadedPublicKey = bytesToPublicKey(publicKeyBytes);
    String message = Files.readString(toPath("StuMisTest.java"));
    String signature = Files.readString(toPath(".sign"));
    boolean verifier = verify(message, signature, loadedPublicKey);
    System.out.println(verifier);
    return verifier;
  }

  private static boolean verify(String message, String signature, PublicKey publicKey) throws Exception {
    Signature verifier = Signature.getInstance("SHA256withRSA");
    verifier.initVerify(publicKey);
    verifier.update(message.getBytes(StandardCharsets.UTF_8));
    return verifier.verify(Base64.getDecoder().decode(signature));
  }

  private static Path toPath(String fileName) throws Exception {
    URI path = CompSign.class.getClassLoader().getResource(fileName).toURI();
    System.out.println(path);
    return Paths.get(path);
  }

  public static PublicKey bytesToPublicKey(byte[] publicKeyBytes) throws Exception {
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
    return keyFactory.generatePublic(keySpec);
  }


  private static byte[] loadKeyFromFile(String fileName) throws Exception {
    String keyBase64 = new String(Files.readAllBytes(toPath(fileName)));
    return Base64.getDecoder().decode(keyBase64);
  }
}
