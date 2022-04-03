package backend.Encripting_tools;

import com.bastiaanjansen.otp.SecretGenerator;

public class ShortSecretGenerator  {

    public static byte[] generate() {
        byte[] seed = SecretGenerator.generate() ;
        byte[] shortSeed={0,0,0,0,0,0,0,0};
        for(int i=0;i<8;i++){
            shortSeed[i]=seed[seed.length-i-1];
        }
        return shortSeed;
    }
}
