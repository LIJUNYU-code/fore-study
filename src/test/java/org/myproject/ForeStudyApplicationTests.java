package org.myproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class ForeStudyApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void generatePassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("123456");
        System.out.println("========== 123456的BCrypt哈希值 ==========");
        System.out.println(hash);
        System.out.println("验证: " + encoder.matches("123456", hash));
    }

}