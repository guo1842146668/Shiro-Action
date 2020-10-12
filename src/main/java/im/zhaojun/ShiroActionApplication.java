package im.zhaojun;

import im.zhaojun.socket.StartServerSocket9000;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class ShiroActionApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShiroActionApplication.class, args);
    }
}