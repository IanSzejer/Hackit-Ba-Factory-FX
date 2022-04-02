package backend.Encripting_tools;

import com.bastiaanjansen.otp.SecretGenerator;
import org.bouncycastle.crypto.engines.SerpentEngine;
import org.bouncycastle.jcajce.provider.symmetric.Serpent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class runner {
   /* public static void main(String[] args) throws Exception {
        final int keyLength = 256;
        try {
            File fil = new File("/Users/john/Desktop/demo2.txt");
            //Asumo que Encryptor.encrypt encripta el archivo y me da la key que uso como respuesta, va encriptar y devolverme la key
            byte[] secret = SecretGenerator.generate();

            KeyManager manager = new KeyManager(Encryptor.encrypt(fil), secret);

            //Se quiere vincular una app con el manager
            App app = new App();

            //Quiero vincular la app con mi keyManager
            app.connectKey(secret);
            //Intengo desencriptar
            try {
                Encryptor.unencrypt(fil, manager.getKey(app.obtainCode()));
            } catch (Exception e) {
                e.printStackTrace();
            }





        }
        catch (Exception e) {

        }


        }

    */
}

