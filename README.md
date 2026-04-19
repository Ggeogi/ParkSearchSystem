# 🅿️ 대구시 통합 주차 정보 시스템 (ParkSearchSystem)

대구광역시의 공영주차장 데이터를 수집하여 DB화하고, 특정 조건(차량 부제 등)에 맞는 주차장을 분류하는 백엔드 시스템입니다.

### 🛠️ 주요 기능
- **공공데이터 API 연동**: `odcloud` 표준 데이터셋 API를 활용한 실시간 데이터 수집
- **데이터 정밀 매핑**: 지번 주소, 위도, 경도 등 핵심 정보 추출 및 mysqlDB 저장
- **5부제 대상 분류**: 국채보상, 경상감영 등 주요 공영주차장 필터링 로직 구현 (`is_rotation_target`)
- **자동 종료 시스템**: 데이터 수집 및 저장 완료 후 애플리케이션 자동 종료 기능

### ⚙️ 기술 스택
- **Language**: Java 17
- **Framework**: Spring Boot 3.x
- **Database**: MariaDB (MySQL)
- **Library**: Spring Data JPA, Lombok, RestTemplate
