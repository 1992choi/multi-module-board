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
    - /service/article/src/main/resources/db/rdb-schema.sql
- comment
  - 데이터베이스 생성
    - create database comment;
  - article 데이터베이스 접속
    - use comment;
  - 테이블 생성 스크립트
    - /service/comment/src/main/resources/db/rdb-schema.sql

## Primary Key 생성 전략
- DB auto_increment
  - 분산 데이터베이스 환경에서 PK가 중복될 수 있기 때문에, 식별자의 유일성이 보장되지 않는다.
    - 여러 샤드에서 동일한 PK를 가지는 상황
  - 클라이언트 측에 노출하면 보안 문제
  - 간단하기 때문에 다음 상황에서 유리할 수 있다.
    - 보안적인 문제를 크게 고려하지 않는 상황
    - 단일 DB를 사용하거나 애플리케이션에서 PK의 중복을 직접 구분하는 상황
- 유니크 문자열 또는 숫자
  - UUID 또는 난수를 생성하여 PK를 지정할 수 있다.
    - 정렬 데이터가 아니라 랜덤 데이터를 삽입하는 것이다.
    - 키 생성 방식이 간단하다.
  - 랜덤 데이터로 인해 성능 저하가 발생할 수 있다.
- 유니크 정렬 문자열
  - 분산 환경에 대한 PK 중복 문제 해결
  - 보안 문제 해결
  - 랜덤 데이터에 의한 성능 문제 해결
  - UUID v7, ULID 등의 알고리즘
  - 데이터 크기에 따라, 공간 및 성능 효율이 달라진다.
    - 일반적으로 알려진 알고리즘은 128비트를 사용한다.
- 유니크 정렬 숫자 (현재 프로젝트에서 채택한 방식)
  - 분산 환경에 대한 PK 중복 문제 해결
  - 보안 문제 해결
  - 랜덤 데이터에 의한 성능 문제 해결
  - Snowflake, TSID 등의 알고리즘
  - 앞서 살펴본 문자열 방식보다 적은 공간을 사용한다.
    - 64비트를 사용한다. (BIGINT)