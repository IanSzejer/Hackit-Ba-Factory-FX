package backend.Encripting_tools;
//Tenemos que importar el archivo de Serpent que pongo en external libraries
//generador estilo google auth
//problema como hacer una generacion continua de keys y poder recuperar la key original para desencryptar

//Generar una encryptacion de serpent e iterar sobre la key generada

//Recordar que hay que eliminar la key anterior una vez se crea una nueva
import java.io.File;
import javax.crypto.Cipher;
import javax.crypto.*;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.security.Signature;

public class Encryptor {
    private static final String encryption = "AES";

    public Encryptor (){

    }

     public static SecretKey encrypt(File importantFiles) throws Exception {

        Signature sign = Signature.getInstance("SHA256withRSA");
        KeyGenerator keyGen = KeyGenerator.getInstance(encryption);
        SecretKey currentKey = keyGen.generateKey();
        Cipher cip = Cipher.getInstance("AES/GCM/NoPadding ");
        cip.init(Cipher.ENCRYPT_MODE, currentKey);
        byte[] toEncrypt = importantFiles.toString().getBytes(StandardCharsets.UTF_8);
        cip.update(toEncrypt);
        byte[] cipherText = cip.doFinal();
         System.out.println(new String(cipherText, "UTF8"));
         FileWriter Alteration = new FileWriter(importantFiles);
         Alteration.write(new String(cipherText, "UTF8"));
         Alteration.close();
        System.out.println(currentKey);
        return currentKey;
    }

    public static void unencrypt(File encrypt, SecretKey key) throws Exception{
        Cipher cip = Cipher.getInstance("AES/GCM/NoPadding");
        cip.init(Cipher.DECRYPT_MODE,key);
        byte[] unEncrypt = encrypt.toString().getBytes(StandardCharsets.UTF_8);
        cip.update(unEncrypt);
        byte[] cipherText = cip.doFinal();
        System.out.println(cipherText);
        FileWriter Alteration = new FileWriter(encrypt);
        Alteration.write(new String(cipherText, "UTF8"));
        Alteration.close();

    }


    public static void main(String[] Args) throws Exception{

        File toEncrypt = new File("src/main/resources/hola.txt");

        SecretKey Key = Encryptor.encrypt(toEncrypt);
        //Encryptor.unencrypt(toEncrypt,Key);


    }
}



