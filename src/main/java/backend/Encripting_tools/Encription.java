package backend.Encripting_tools;



import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;


public class Encription {

    private static final int MAX_ENCRYPT_BLOCK = 117;


    private static final int MAX_DECRYPT_BLOCK = 128;


    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        return generator.generateKeyPair();
    }


    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.getDecoder().decode(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }


    public static PublicKey getPublicKey(String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.getDecoder().decode(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    //Encriptamos los archivos de forma recursiva
    public static void recursiveFileEncryptor(File currentFile,PublicKey key) throws Exception{
        if (currentFile==null)
            return;
        if (!currentFile.exists())
            throw new IllegalArgumentException("File doesnt exist");
        if(!currentFile.isDirectory()){              //Si no es un directorio, encriptar y terminar la rama
            encrypt(currentFile,key);
            return;
        }

        if (currentFile.listFiles()==null)          //Si es un directorio vacio, terminar la rama
            return;
        for (File file: List.of(currentFile.listFiles())){
            recursiveFileEncryptor(file,key);
        }
    }


    public static void recursiveFileDecryptor(File currentFile,PrivateKey privateKey) throws Exception{
        if (currentFile==null)
            return;
        if (!currentFile.exists())
            throw new IllegalArgumentException("File doesnt exist");
        if(!currentFile.isDirectory()){              //Si no es un directorio, encriptar y terminar la rama
            decrypt(currentFile,privateKey);
            return;
        }

        if (currentFile.listFiles()==null)          //Si es un directorio vacio, terminar la rama
            return;
        for (File file: List.of(currentFile.listFiles())){
            recursiveFileDecryptor(file,privateKey);
        }
    }


    private static void encrypt(File files,PublicKey publicKey) throws Exception {
        // Selecting a Image for operation
        FileInputStream fis = new FileInputStream(
                files);
        byte data[] = new byte[fis.available()];

        // Read the array
        fis.read(data);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // Cifrar segmentos de datos
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();

        // Opening a file for writing purpose
        FileOutputStream fos = new FileOutputStream(
                files);

        // Writing new byte array value to image which
        // will Encrypt it.c

        fos.write(encryptedData);

        // Closing file
        fos.close();
        fis.close();


    }


    private static void decrypt(File file,PrivateKey privateKey) throws Exception {
        FileInputStream fis = new FileInputStream(
                file);

        // Converting image into byte array,it will
        // Create a array of same size as image.
        byte data[] = new byte[fis.available()];

        // Read the array

        fis.read(data);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // Descifrar segmentos de datos
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(data, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();



        // Opening file for writting purpose
        FileOutputStream fos = new FileOutputStream(
                file);

        // Writting Decrypted data on Image
        fos.write(decryptedData);
        fos.close();
        fis.close();
    }


    public static String sign(String data, PrivateKey privateKey) throws Exception {
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(key);
        signature.update(data.getBytes());
        return new String(Base64.getEncoder().encodeToString(signature.sign()));
    }


    public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        return signature.verify(Base64.getDecoder().decode(sign.getBytes()));
    }

}

