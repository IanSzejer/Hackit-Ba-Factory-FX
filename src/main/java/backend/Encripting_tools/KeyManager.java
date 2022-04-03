package backend.Encripting_tools;

//Guardamos la mitad de la key de desencriptado de serpent
// y la encryptamos, usaremos la mitad original como seed phrase para poder mover el autenticador
// hashear la mitad de la key


import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.SecretGenerator;
import com.bastiaanjansen.otp.TOTP;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.time.Duration;
import java.util.Date;

public class KeyManager {
    private TOTP totp;
    private PrivateKey key;

    public KeyManager(){

    }

    public PrivateKey getKey(String code) {           //Se le pide a la app que genere un String code con totp.now()
        if(!totp.verify(code))
            return null;
        return key;

    }

    public void addKey(PrivateKey key){
        this.key=key;
    }

    public byte[] generateSeed(){
        byte[] seed=ShortSecretGenerator.generate();
        TOTP.Builder builder = new TOTP.Builder(seed)
                .withPeriod(Duration.ofSeconds(30))
                .withPasswordLength(6)
                .withAlgorithm(HMACAlgorithm.SHA1);
        totp = builder.build();
        return seed;
    }

    public void resetKeyManager(){
        key=null;
        totp=null;
    }





}


