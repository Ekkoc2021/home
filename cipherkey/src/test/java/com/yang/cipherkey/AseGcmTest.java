package com.yang.cipherkey;

import org.junit.jupiter.api.Test;

public class AseGcmTest {


    @Test
    public void test() throws Exception {
       String token="VAIh7gYt62V9vHemac6+BysZWEQbmLt2n9ypdhtfn52WlDpupLg0AX5P1BY=";

       String decrypt = AesGcmCompat.decrypt(token, "asd456");
       System.out.println(decrypt);

    }
}
