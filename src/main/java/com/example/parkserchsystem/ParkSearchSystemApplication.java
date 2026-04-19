package com.example.parkserchsystem;

import com.example.parkserchsystem.service.ParkingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ParkSearchSystemApplication {

    public static void main(String[] args) {
        // 1. 스프링 컨텍스트를 변수에 담습니다.
        ConfigurableApplicationContext context = SpringApplication.run(ParkSearchSystemApplication.class, args);

        try {
            // 2. ParkingService를 가져와서 데이터 수집 실행
            ParkingService parkingService = context.getBean(ParkingService.class);
            parkingService.fetchDataAndSave();

            System.out.println("🚀 [성공] 모든 데이터가 저장되었습니다. 프로그램을 종료합니다.");
        } catch (Exception e) {
            System.err.println("❌ [에러] 실행 중 문제가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 3. 작업이 끝나면 수동 종료할 필요 없이 자동으로 닫습니다.
            context.close();
        }
    }
}