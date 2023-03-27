package org.ac.cst8277.islam.moin.util;

import org.springframework.stereotype.Component;

@Component
public class Util {


    public static String getToken(String token){
        return token.replace("Bearer ","");
    }
}
