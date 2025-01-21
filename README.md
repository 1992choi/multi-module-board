# 프로젝트

## 프로젝트 설명
- 인프런 > 스프링부트로 직접 만들면서 배우는 대규모 시스템 설계 - 게시판

## 프로젝트 설정
- MySQL 설정
  - 이미지 다운로드
    - docker pull mysql:8.0.38
  - 이미지 조회
    - docker images
  - 실행
    - docker run --name demo-board-mysql -e MYSQL_ROOT_PASSWORD=root -d -p 3306:3306 mysql:8.0.38
  - 컨테이너 접속
    - docker exec -it demo-board-mysql bash
  - 데이터베이스 접속
    - mysql -u root -p
      - 위 명령어 입력한 이후 패스워드로 'root' 입력

## 테이블 설계
- article 
  - 데이터베이스 생성
    - create database article;
  - article 데이터베이스 접속
    - use article;
  - 테이블 생성 스크립트
    - /service/article-read/src/main/resources/db/rdb-schema.sql