## 미니프로젝트(개인)

### 프로젝트 명세서
- 프로젝트 명 : FMS(Football Management System)
- 프로젝트 기간 : 2024.04.09~2024.04.17
- 프로젝트 인원 : 1명
- 운영체제 : Mac OS(Sonoma 14.4.1)
- 프로그래밍 언어 : Java(Oracle OpenJDK 17.0.9)
- 빌드도구 : IntelliJ 제공
- 데이터베이스 : Oracle Database(Docker)
- 외부 라이브러리 및 모듈
    - Oracle Jdbc Driver 11
    - jbcrypt 0.4
- IDE : IntelliJ Ultimate Edition
- 형상관리 도구 : Git & Github
- 프로젝트 간단 설명
  - 사용자는 Player 와 Admin 중 1개로 회원가입을 하여 기능을 사용할 수 있다.
  - Player 는 원하는 선수를 생성하여 팀에 등록,수정,조회, 삭제를 할 수있다. 원하는 팀을 조회하고 리그도 조회할 수 있다.
  - Admin 은 관리자 계정으로 사용자의 계정을 삭제할 수 있으며 리그와 팀을 추가,수정,삭제를 할 수 있는 권한을 가진다.
- 구현 기능
  - 회원가입(Users테이블) -> `Admin`(관리자)/`Player`(선수)을 컬럼으로 구분하여 회원 저장
    - `ID`(USER_NAME 컬럼) 중복체크
    - 비밀번호 암호화해서 DB 저장 -> `jbcrypt` 라이브러리 사용
  - 로그인/로그아웃
    - 로그인 성공 시 `Users` 의 `ROLE` 이 `Admin` 인지 `Player` 인지 나누어서 기능 제공
    - Player 인 경우
      - 선수등록, 선수정보 수정, 선수조회, 팀 조회, 리그 조회, 선수 삭제 기능 구현 완료
      - 선수, 팀, 리그 조회시 필요한 컬럼만 뽑아내기 위해 별도의 DTO에 각 컬럼을 받아 List에 담아서 출력
    - Admin 인 경우
      - 팀 삭제,수정,추가 리그 삭제,수정,추가 기능 
      - Users(Player)의 회원탈퇴 기능

### DB Diagram
![db.png](src/resources/DB_Diagram.png)

> [더미 데이터 및 테이블 세팅 쿼리](src/sql/table.sql)