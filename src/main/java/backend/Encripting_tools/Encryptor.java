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

    private static void recursiveFileFinder(File currentFile,Cipher cip,Key publicKey,Cryptography function){
        if (currentFile==null)
            return;
        if(!currentFile.isDirectory()){              //Si no es un directorio, encriptar y terminar la rama
            try {
                function.cryptographyMethod(currentFile, cip);
            }catch (Exception e){
                e.printStackTrace();
            }
            return;
        }

        if (currentFile.listFiles()==null)          //Si es un directorio vacio, terminar la rama
            return;
        for (File file: List.of(currentFile.listFiles())){
            recursiveFileFinder(file,cip,publicKey,function);
        }
     }







     public static KeyPair encrypt(File importantFiles) throws Exception {
        Signature sign = Signature.getInstance(SIGN_ENCRYPTION);
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_GEN_ENCRYPTION);
        keyPairGen.initialize(2048);
        KeyPair pair = keyPairGen.generateKeyPair();
        PublicKey publicKey = pair.getPublic();
        Cipher cip = Cipher.getInstance(CIP_ENCRYPTION);
        cip.init(Cipher.ENCRYPT_MODE, publicKey);
        recursiveFileFinder(importantFiles,cip,publicKey, (File files,Cipher c)-> {


            byte[] toEncrypt = files.toString().getBytes(StandardCharsets.UTF_8);
            c.update(toEncrypt);
            byte[] cipherText = c.doFinal();
            // System.out.println(new String(cipherText, "UTF8"));
            FileWriter Alteration = new FileWriter(files);
            Alteration.write(new String(cipherText, "UTF-8"));
            Alteration.close();


        });
        return pair;
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



