package service;

import javax.ejb.Singleton;
import java.util.UUID;

@Singleton
public class TokenGenerator {

   public String generateToken(){
       return UUID.randomUUID().toString();
   }

}
