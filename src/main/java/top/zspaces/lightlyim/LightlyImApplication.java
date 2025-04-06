package top.zspaces.lightlyim;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@MapperScan("top.zspaces.lightlyim.mapper")
@EnableAsync
@SpringBootApplication
public class LightlyImApplication {

    public static void main(String[] args) {
        SpringApplication.run(LightlyImApplication.class, args);
    }

}
