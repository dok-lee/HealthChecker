# HealthChecker
SpringBoot toy project : Host management tool for register &amp; reachability check

[ 주제 ]
‘호스트들의 Alive 상태 체크 및 모니터링 API 서버 개발'
다수의 서버들을(호스트) 관리하는 네트워크 관제 프로그램이 있다고 가정합니다.
이 프로그램에서 등록된 호스트들이 다운되었을 경우를 UI 화면에 표시하려고 합니다.
BackEnd 서버에서 UI에서 필요한 정보를 REST API로 제공해야 합니다.
(호스트들의 등록 관리, 상태 확인, 모니터링 결과 조회)

[ 요구 사항 ]
1. 호스트 등록 관리 REST API
  - 호스트 조회/등록/수정/삭제 API 제공
  - 호스트 등록 시 필드는 name, ip 이다.
  - name, ip 중복되면 안 된다.
  - 조회 결과 필드에는 등록/수정 시간을 포함해야 한다.
  - 서버가 재시작되어도 등록된 호스트들은 유지되어야 한다.
  - 호스트 등록은 100개로 제한한다.

2. 특정 호스트의 현재 Alive 상태 조회 REST API
  - 조회의 단위는 한 호스트만 가능해야 한다.
  - Alive 상태 확인은 InetAddress.isReachable() 사용을 권장한다.

3. 호스트들의 Alive 모니터링 결과 조회 REST API
  - 조회 결과 필드에는 Alive 상태와 마지막 Alive 시간을 포함해야 한다.
  - 조회의 단위는 한 호스트, 전체 호스트를 제공해야 한다.
  - 전체 조회 시 100개의 호스트가 모두 Unreachable 상태여도 조회는 1초 이내에 응답해야 한다.

4. Readme 작성
  - REST API 설명을 Readme에 작성한다.
  - 제약 사항이 있는 경우 Readme에 작성한다.
  - 시험 결과 리스트가 있는 경우 Readme에 작성한다.

[ 사용 기술스택 ]
  - Java
  - MariaDB
  - Gradle
  - Spring-Boot
  - Swagger (http://localhost:8080/swagger-ui/index.html#)

[ 제약 사항 ]
  - resources/application.properties
   - spring.jpa.hibernate.ddl-auto=**update**
    - 실사용 시 none으로 설정, 각 테이블의 id를 db에서 auto로 할 것 (DDLSQL.md 파일 참고).
   - spring.datasource.username=root
   - spring.datasource.password=root
   - spring.datasource.url=jdbc:mariadb://localhost:3306/mysql
  - 각 테이블의 id는 Long이며, insert 시마다 시퀀셜하게 증가함. 혹시라도 Long Max까지 찬다면 리셋하여 사용할 것.
  - 호스트가 모두 연결에 실패할 경우 최대 100초까지 기다려야 연결 상태가 업데이트 될 수 있음.
  - TODO: host 테이블에 값이 있는데 health 테이블에 값이 없는 경우 에러가 발생함.

[ REST API ]
 - (GET) /api/host
 - (POST) /api/host
 - (PUT) /api/host
 - (DELETE) /api/host
 - (GET) /api/host/name/{name}
 - (DELETE) /api/host/name/{name}
 - (GET) /api/host/ip/{ip}
 - (DELETE) /api/host/ip/{ip}
 
 - (GET) /api/health
 - (GET) /api/health/name/{name}
 - (GET) /api/health/ip/{ip}
