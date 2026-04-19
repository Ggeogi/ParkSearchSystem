package com.example.parkserchsystem.service;

import com.example.parkserchsystem.entity.ParkingEntity;
import com.example.parkserchsystem.repository.ParkingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ParkingService {

    private final ParkingRepository parkingRepository;

    // 상민 님이 선정한 대구 5부제 대상 주차장 키워드 리스트
    private static final List<String> TARGETS = List.of(
            "국채보상", "경상감영", "달서교하부", "상인공영", "서변1공영", "서변2공영", "서촌공영",
            "성서1공영", "성서2공영", "성서3공영", "신천둔치", "안심역공영", "앞산공영", "어린이세상"
    );

    public void fetchDataAndSave() {
        // 공공데이터 포털 표준 데이터셋 전용 주소와 인증키
        String baseUrl = "https://api.odcloud.kr/api/15151311/v1/uddi:ddad927f-4d89-4eee-b6e1-a43c58e63195";
        String serviceKey = "a7cf681345f0cfb041c4ad45d04e043a1d98f1e3b2cb472f025ead7963ad7823";

        // URI 빌드 (perPage를 2000으로 잡아 한 번에 다 가져옵니다)
        URI uri = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("page", 1)
                .queryParam("perPage", 2000)
                .queryParam("serviceKey", serviceKey)
                .build(true) // 인증키 특수문자 변형 방지
                .toUri();

        RestTemplate restTemplate = new RestTemplate();

        try {
            System.out.println("🔗 대구 공영주차장 데이터 수집 중... (URI: " + uri + ")");

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(uri, Map.class);

            if (response != null && response.get("data") instanceof List) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> dataList = (List<Map<String, Object>>) response.get("data");

                // 중복 방지를 위해 기존 데이터를 싹 지우고 새로 받습니다.
                parkingRepository.deleteAll();

                for (Map<String, Object> item : dataList) {
                    // 1. 주차장명 가져오기
                    String name = String.valueOf(item.get("주차장명"));
                    if (name == null || name.equals("null") || name.isEmpty()) continue;

                    // 2. 주소 가져오기 (상민님이 찾으신 '지번주소' 키값 적용)
                    String address = String.valueOf(item.get("지번주소"));

                    // 3. 5부제 대상 여부 판단 (이름에 키워드가 포함되면 true)
                    boolean isTarget = TARGETS.stream().anyMatch(name::contains);

                    // 4. 위도 및 경도 추출 (문자열로 들어오므로 Double로 변환)
                    double lat = 0.0;
                    double lng = 0.0;
                    try {
                        if (item.get("위도") != null) lat = Double.parseDouble(String.valueOf(item.get("위도")));
                        if (item.get("경도") != null) lng = Double.parseDouble(String.valueOf(item.get("경도")));
                    } catch (Exception e) {
                        // 변환 실패 시 기본값 0.0 유지
                    }

                    // 5. Entity 생성 및 DB 저장
                    ParkingEntity entity = ParkingEntity.builder()
                            .parkingName(name)
                            .address(address)
                            .latitude(lat)
                            .longitude(lng)
                            .isRotationTarget(isTarget)
                            .build();

                    parkingRepository.save(entity);
                }
                System.out.println("📦 총 " + dataList.size() + "건의 주차장 정보를 DB에 저장했습니다.");
            }
        } catch (Exception e) {
            System.err.println("❌ 데이터 수집 중 오류 발생: " + e.getMessage());
        }
    }
}