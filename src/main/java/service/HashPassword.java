package service;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.ejb.Singleton;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Singleton
public class HashPassword {

    private SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );

    public HashPassword() throws NoSuchAlgorithmException {
    }

    /*
    salt = user auto generated token
    * */
    public byte[] hashPassword(String password, String salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(
			//values
			);
            SecretKey key = skf.generateSecret( spec );
            byte[] res = key.getEncoded( );
            System.out.println("HASH");
            System.out.println(password.toCharArray());
            System.out.println(salt.getBytes());
            System.out.println(new String(res));

            return res;
        } catch(InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
    }

}
