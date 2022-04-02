package backend.Encripting_tools;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.SecretGenerator;
import com.bastiaanjansen.otp.TOTP;

import javax.crypto.SecretKey;
import java.time.Duration;

public class App {
    private TOTP totpReplic=null;

    public App(){

    }
    //Falta agregar el period al TOTP
    public void connectKey(byte[] seed){
        TOTP.Builder builder = new TOTP.Builder(seed)               //Replicamos el TOTP del Encryprot
                .withPeriod(Duration.ofSeconds(30))
                .withPasswordLength(6)
                .withAlgorithm(HMACAlgorithm.SHA1);
        totpReplic = builder.build();
    }

    public String obtainCode(){
        return totpReplic.now();
    }




}
