package backend.Encripting_tools;

import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Base64;

public class EncriptionHandler {
    KeyManager keyManager= new KeyManager();

    public String encrypt(File file, boolean generateNewSeed){
        try {
            KeyPair keyPair = Encription.getKeyPair();
            Encription.recursiveFileEncryptor(file, keyPair.getPublic());
            keyManager.addKey(keyPair.getPrivate());
            if (generateNewSeed== true){
                return Base64.getEncoder().encodeToString(keyManager.generateSeed());
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public boolean decrypt(File file,String code){

        PrivateKey privateKey = keyManager.getKey(code);
        if (privateKey==null)
            return false;
        try {
            Encription.recursiveFileDecryptor(file, privateKey);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        keyManager.resetKeyManager();
        return true;                //Devuelve true para confirmar que el code fue correcto
    }
}
