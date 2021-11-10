package top.binaryx.garen.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("top.binaryx.garen.server.mapper")
@SpringBootApplication
public class GarenApplication {

	public static void main(String[] args) {
		SpringApplication.run(GarenApplication.class, args);
	}

}
