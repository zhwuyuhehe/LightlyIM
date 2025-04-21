package top.zspaces.lightlyim;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAsync
@SpringBootApplication
@MapperScan("top.zspaces.lightlyim.mapper")
public class LightlyImApplication {

    public static void main(String[] args) {
        SpringApplication.run(LightlyImApplication.class, args);
    }

}
