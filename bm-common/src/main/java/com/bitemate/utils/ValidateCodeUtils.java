package com.bitemate.utils;

import java.util.Random;

/**
 * Utils to generate the validate code
 */
public class ValidateCodeUtils {

    /**
     * Generate a random verification code
     * @param length Code length (must be 4 or 6 digits)
     * @return Verification code
     */

    public static Integer generateValidateCode(int length){
        Integer code = 0;
        if(length == 4){
            code = new Random().nextInt(9999);
            if(code < 1000){
                code += 1000;
            }
        }
        else if(length == 6){
            code = new Random().nextInt(999999);
            if(code < 100000){
                code += 100000;
            }
        }
        else {
            throw new RuntimeException("Only 4-digit or 6-digit numeric verification codes can be generated");
        }
        return code;
    }

}
