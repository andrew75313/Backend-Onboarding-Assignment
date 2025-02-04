### January 19, 2025
# API 명세서

| **API**                     | **Method** | **Request URL**     | **Request Body**                                                                 | **Response Body**                                                                                                                       | **Status Code**   | **Description**                                       |
|-----------------------------|------------|---------------------|-----------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------|-------------------|-------------------------------------------------------|
| **회원가입**                 | POST       | `/signup`           | ```json<br>{<br>"username": "JIN HO",<br>"password": "12341234",<br>"nickname": "Mentos"<br>}``` | ```json<br>{<br>"username": "JIN HO",<br>"nickname": "Mentos",<br>"authorities": [{ "authorityName": "ROLE_USER" }]<br>}```            | 200 OK            | 사용자 회원가입 처리                                |
| **로그인**                   | POST       | `/sign`             | ```json<br>{<br>"username": "JIN HO",<br>"password": "12341234"<br>}```             | ```json<br>{<br>"token": "eKDIkdfjoakIdkfjpekdkcjdkoIOdjOKJDFOlLDKFJKL"<br>}```                                                        | 200 OK            | 로그인 후 JWT 토큰 발급                             |
| **로그인 실패**               | POST       | `/sign`             | ```json<br>{<br>"username": "incorrect",<br>"password": "wrong"<br>}```             | ```json<br>{<br>"message": "Invalid username or password"<br>}```                                                                      | 401 Unauthorized  | 잘못된 아이디 또는 비밀번호로 로그인 실패 시 응답  |
| **Access Token 재발급**      | POST       | `/token/refresh`    | ```json<br>{<br>"refreshToken": "validRefreshToken"<br>}```                         | ```json<br>{<br>"token": "newAccessToken"<br>}```                                                                                         | 200 OK            | 유효한 refresh token으로 access token 재발급       |
| **Access Token 재발급 실패** | POST       | `/token/refresh`    | ```json<br>{<br>"refreshToken": "invalidRefreshToken"<br>}```                       | ```json<br>{<br>"message": "Please login again"<br>}```                                                                                  | 400 Bad Request   | 만료된 또는 잘못된 refresh token으로 재발급 실패   |

---
###  January 19, 2025
# 한 달 인턴 8기 - (Java) 백엔드 온보딩 과제

## Overview

- Github에 Repo를 생성해서 코드를 작성하기
    - 과제 요구사항에 따라 코드를 작성하여, Public 리포지토리 생성 후 링크를 제출해 주세요.

## Requirements

- [x] Junit을 이용한 테스트 코드 작성법 이해
- [x] Spring Security를 이용한 Filter에 대한 이해
- [x] JWT와 구체적인 알고리즘의 이해
- [x] PR 날려보기
- [x] 리뷰 바탕으로 개선하기
- [x] EC2에 배포해보기

## 시나리오 설계 및 코딩 시작!

### Spring Security 기본 이해

#### Filter란 무엇인가? (with Interceptor, AOP)

##### Filter & Interceptor & AOP
- **공통관심사를 가지고 실제 비지니스로직과 분리한 컴포넌트**
- **재사용성**과 유지보수성을 향상

#### Filter
- **Servlet**이 제공하는 요청, 응답을 **중간에서 처리(전처리, 후처리)하는 컴포넌트**
    - **Servlet** : 클라이언트 요청처리/응답 생성의 컴포넌트, HTTP 요청 처리 등 담당, 결과 반환 담당
    - **Servlet API**를 사용하여 `javax.servlet.Filter` 인터페이스를 구현하여 사용
    - **Filter Chain**을 활용해서 순차적으로 사용 가능
- **Example**
    - Servlet API 단에서 **인증/인가 Filter**: 로그인 요청 시 Servlet으로 도달하기 전, Filter단에서 이미 인증/인가를 처리
    - **HTTP 요청/응답 전처리 후처리**: 보안, 인코딩, CORS 처리 등
- **장점**
    - **모듈화**: 비지니스로직과 분리가 되어있기 때문에 기능을 재사용 가능
    - **빠른 처리**: 요청을 가장 먼저 빠르게 처리가 가능
- **단점**
    - **사용범위 제한**: **Servlet API에서만** 사용 가능
    - **단순 구조**: 비지니스로직과 분리가 되어있기 때문에 Filter단에서 해결할 수 있는 간단한 로직만 가능

#### Interceptor
- 요청/응답을 중간에 처리하는 Filter와 유사하나, Spring이 제공하는 **Spring MVC 구조에서 Controller에 적용되는 컴포넌트**
    - Spring MVC와 통합된 `HandlerInterceptor`를 통해 구현하여 Spring Interceptor를 사용 가능
- Request → WAS → Filters → Servlet → **Spring Interceptor** → Controller 순서로 진행
    - DispatcherServlet과 Controller 사이에 호출, 작동될 수 있음
- **Example**
    - Controller 앞단에서의 **인증/인가 Interceptor**: 인증 인가 Filter와 똑같지만 그 적용 단계별 위치가 다를 수 있음

#### AOP (Aspect-Oriented Programming), 관점지향 프로그래밍
- Java의 비지니스 로직을 관심사 기준으로 분리하나, Spring AOP를 통해서 메서드 기준으로 호출 전후 처리에 실행하는 컴포넌트
- 코드의 공통 관심사를 분리하는 요소
    - 로깅, 트랜잭션 관리 등
- **구성**
    - **Pointcut**: AOP 부가기능을 적용할 메서드의 위치를 지정
    - **Advice**: 메서드에서 호출된 정확한 지점을 정의
- **Example**
    - 로깅, 트랜잭션
- **장점**
    - Pointcut, Advice로 부가기능을 메서드에 쉽게 적용 가능
- **단점**
    - 복잡한 흐름이 될 경우, 디버깅에 있어서 혼잡하거나 어려울 수 있음
    - AOP로 인한 성능 이슈가 발생할 수 있음

| 특성           | **Filter**            | **Interceptor**        | **AOP**                |
|----------------|-----------------------|------------------------|------------------------|
| **사용 영역**  | Servlet API           | Spring MVC             | 모든 Java 어플리케이션 |
| **흐름 제어**   | Web 요청/응답 전후    | **Controller** 요청 처리 전후 | **메소드(비지니스로직)** 호출 전후 |
| **유연성**      | 제한적(Servlet)       | 중간(Controller)       | 높음                   |
| **복잡한 로직** | 구현 어려움            | 구현 어려움             | 가능                   |
| **주요 용도**   | 인증, 로그 기록       | URL 패턴에 대한 필터링 | 트랜잭션 관리, 로깅 등   |

### Spring Security란?
**Spring Security Framework**
- Spring Framework에서 제공하는 **보안 프레임워크**로 **Application의 인가 및 인증**에 대한 관리에 사용
- **Session 방식**으로, 요청 받은 SessionID(클라이언트에서 만들어진)을 저장해서 이후의 요청 받은 **SessionID(=JSONID)를 비교를 해서 인증처리를 해주는 방식**
- **인증 Authentication**
    - 사용자를 **식별**
- **인가 Authorization**
    - 사용자에게 **권한을 허가**
    - 기능이나 리소스에 접근
- **Security Filter Chain**
    - 인가 인증 Filter 등을 **단계별**로 처리
- **UserDetailsService**
    - 사용자 정보 관리 인터페이스

## JWT 기본 이해

### JWT란 무엇인가요?
**JWT (Json Web Token)**
- JSON 포맷을 이용하여 사용자에 대한 속성을 저장하는 Claim 기반의 Web Token
- 로그인 정보를 **Server**에 저장하지 않고, **Client**에 로그인 정보를 **JWT로 암호화하여 저장** → **JWT를 통해 인증/인가**
- **Secret Key**를 통해 암호화/위조 검증이 가능

#### 장점
- 동시 접속자가 많을 때 서버 측 부하 낮춤
- Client, Server가 다른 도메인을 사용할 때 가능

#### 단점
- 높은 구현의 복잡도
- 네트워크 비용 증가
- 클라이언트에서 응답받아 쿠키에 저장한 JWT를 서버가 만료시킬 수 없음
- Secret key 유출 시 모든 JWT를 조작할 수 있음

### 토큰 발행과 유효성 확인

#### Access / Refresh Token 발행과 검증에 관한 테스트 시나리오

- **<사용자 로그인 성공>**
    - **URL**: `api/sign`
    - **입력**: 사용자 아이디(username), 비밀번호(password)
    - **기대 결과**
        - HTTP Status Code 200 OK
        - AccessToken JSON 형태 token 필드명으로 응답
        - RefreshToken 헤더에 refreshtoken으로 발급
        - 만료기간이 설정된 Access Token 발급

- **<사용자 로그인 실패>**
    - **URL**: `api/sign`
    - **입력**: 잘못된 사용자 아이디(username) 또는 비밀번호(password)
    - **기대 결과**
        - HTTP Status Code 401 Unauthorized
        - JSON 형태 오류 메시지 반환

- **<Access Token 재발급>**
    - **입력**: refresh token
    - **기대 결과**
        - HTTP Status Code 200 OK
        - RefreshToken 헤더에 refresh token으로 발급
        - “Refresh Token 재발급 완료” 메시지 반환

- **<Access Token 재발급 실패>**
    - **입력**: 잘못된(만료된) refresh token
    - **기대 결과**
        - HTTP Status Code 400 Bad Request
        - JSON 형태 “다시 로그인 해주세요” 안내 오류 메시지 반환

## 유닛 테스트 작성

- [x] JUnit을 이용한 JWT Unit 테스트 코드 작성해보기

[JUnit 테스트 예시](https://preasim.github.io/39)  
[Spring Security JWT Controller Unit Test](https://velog.io/@da_na/Spring-Security-JWT-Spring-Security-Controller-Unit-Test하기)

## 백엔드 배포하기

### 테스트 완성

- [x] 백엔드 유닛 테스트 완성하기

### 로직 작성

- [x] 백엔드 로직을 Spring Boot로 작성하기
- [x] 회원가입 - `/signup`
    - **Request Message**
    ```json
    {
      "username": "JIN HO",
      "password": "12341234",
      "nickname": "Mentos"
    }
    ```
    - **Response Message**
    ```json
    {
      "username": "JIN HO",
      "nickname": "Mentos",
      "authorities": [
        {
          "authorityName": "ROLE_USER"
        }
      ]		
    }
    ```

- [x] 로그인 - `/sign`
    - **Request Message**
    ```json
    {
      "username": "JIN HO",
      "password": "12341234"
    }
    ```
    - **Response Message**
    ```json
    {
      "token": "eKDIkdfjoakIdkfjpekdkcjdkoIOdjOKJDFOlLDKFJKL"
    }
    ```

### 배포해보기

- AWS EC2에 배포하기

  - **회원가입하기**
      - [POST] `http://43.200.178.150:8080/api/signup`
  
  - **로그인**
      - [POST] `http://43.200.178.150:8080/api/sign`
  
  - **AccessToken 재발급**
      - [GET] `http://43.200.178.150:8080/api/token`

### API 접근과 검증

- [ ] Swagger UI로 접속 가능하게 하기
