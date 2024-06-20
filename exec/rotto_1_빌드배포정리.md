# 1. 빌드 배포 정리

## 목차

1. [기술 스택](#기술-스택)
2. [빌드 및 배포](#빌드-및-배포)


## 기술 스택

1. 이슈 관리 : Jira
2. 형상 관리 : Gitlab
3. 빌드/배포 관리 : Jenkins `2.426.3`
4. 커뮤니케이션 : MatterMost, Notion, Discord
5. 개발 환경
    1) 운영체제 Window 10
    2) IDE 
         - VSCode `1.85.1`
         - IntelliJ `2023.3.2`
    3) 데이터베이스
         - MySQL `8.0.35`
         - Redis `7.0.2`
    4) 서버 : AWS EC2
         - Ubuntu `20.04 LTS`
         - Docker `25.0.1`
         - docker-compose `2.24.2`
         - Nginx `1.18.0(ubuntu)`
         - Https/SSL `Let's Encrypt`
    
6. 세부사항
    1) Frontend
        - lang: HTML5, CSS3, JAVASCRIPT, Node.js `21.5.0` 
        - Framework: 
            * react-native: `0.73.5`
            * reduxjs/toolkit: `2.2.1`
        - 주요 Libraries
            * axios: `1.6.7`
            * firebase `10.8.1`
        - build 환경
            * expo: `~ 50.0.13`
        - 개발 도구
            * Vite: `5.0.8`
            * ESLint: `8.56.0`
            * Prettier `3.2.4`

    2) Backend
        - Language: Java 17
        - Framework:
            *  Spring Boot: 3.2.1
            *  Spring Security: 3.2.1
            *  Spring Data JPA
        - 주요 Libraries:
            * Lombok
            * JJwt: `0.11.5`
        -  개발 도구:
            *  Spring Boot Devtools
            *  Gradle `8.5`
        -  API 문서화:
            *  Swagger

    3) BlockChain
        - Language: Solidity 8.0.19
        - Framework:
            * Truffle: `5.11.5`
        - 주요 Libraries:
            * openzeppelin/contracts: `4.9.6`
            * truffle/hdwallet-provider: `2.1.15`
            * dotenv: `16.4.5`


## 빌드 및 배포
### 1. AWS EC2 기본 설정 및 nginx 설치
1) (선택) 우분투 미러서버 변경
    - 처음 우분투를 받았을 때 기본설정 되어 있는 미러서버는 느리거나 update시 일부 다운로드가 되지 않는 오류가 발생하는 경우가 있음
    - 국내에서 접근 가능한 가장 빠른 카카오 미러서버로 기본설정 변경

    ```bash
    $ sudo vim /etc/apt/sources.list

    # esc버튼 클릭 후
    :%s/{기존에 입력되어 있던 미러서버 주소}/mirror.kakao.com
    :wq

    deb http://mirror.kakao.com/ubuntu/ focal main restricted

    deb http://mirror.kakao.com/ubuntu/ focal-updates main restricted

    deb http://mirror.kakao.com/ubuntu/ focal-updates universe

    deb http://mirror.kakao.com/ubuntu/ focal multiverse

    deb http://mirror.kakao.com/ubuntu/ focal-updates multiverse

    deb http://mirror.kakao.com/ubuntu/ focal-backports main restricted universe multiverse
    ```

2) nginx 설치 및 SSL 인증서 발급, 적용
    ```bash
    # nginx 설치
    sudo apt-get update
    sudo apt-get install nginx

    # 설치 및 버전 확인
    nginx -v
    ```

    - nginx설치후 letsencrypt를 이용해 SSL 인증서 발급
    ```bash
    sudo apt-get install letsencrypt # letsencrypt 설치

    sudo systemctl stop nginx # 발급을 위한 nginx 정지

    sudo letsencrypt certonly --standalone -d {도메인 주소} # letsencrypt로 서버 domain에 SSL 인증서 발급
    ```

    - nginx 설정 파일을 프로젝트에 맞게 수정
    ```
    sudo vim /etc/nginx/sites-available/default
    ```
    
    ```
    server {
        listen 80 default_server;
        listen [::]:80 default_server;

        root /var/www/html;

        index index.html index.htm index.nginx-debian.html;

        server_name _;

        location / {
            try_files $uri $uri/ =404;
        }
    }

    server {

        root /var/www/html;

        index index.html index.htm index.nginx-debian.html;
        server_name {도메인 주소}; # managed by Certbot


        location / {
            try_files $uri $uri/ =404;
        }

        listen [::]:443 ssl ipv6only=on; # managed by Certbot
        listen 443 ssl; # managed by Certbot
        ssl_certificate /etc/letsencrypt/live/{도메인 주소}/fullchain.pem; # managed by Certbot
        ssl_certificate_key /etc/letsencrypt/live/{도메인 주소}/privkey.pem; # managed by Certbot
        include /etc/letsencrypt/options-ssl-nginx.conf; # managed by Certbot
        ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem; # managed by Certbot

    }
    server {
        if ($host = {도메인 주소}) {
            return 301 https://$host$request_uri;
        } # managed by Certbot


        listen 80 ;
        listen [::]:80 ;
        server_name {도메인 주소};
        return 404; # managed by Certbot
    }
    ```

    * nginx 테스트 후 재가동
    ```bash
    $ sudo nginx -t
    $ sudo systemctl restart nginx
    ```
### 2. FrontEnd 빌드 및 배포
1) 프로젝트 clone
    ```
    git clone {프로젝트 git 주소}
    ```
2) 프로젝트 폴더로 이동
    ```bash
    $ cd react-native-cli/rotto
    ```

3) react-native CLI 설치
    ```bash
    $ npm install -g react-native
    ```

3) node modules 설치
    ```
    npm i
    ```
4) .env 파일 작성
    ```
    S3URL={your_S3_url}
    CONTRACT_ADDRESS={your_contract_address}
    DOMAIN_URL={your_domain_url}
    PROJECT_ID={your_walletconnect_project_ID}
    RPC_URL={your_blockchain_network_rpc_url}
    CHAIN_ID={your_blockchain_network_chain_ID}
    ```
5) 프로젝트 실행
    ```
    npx react-native run-android 
    ```
6) 프로젝트 안드로이드 빌드 파일
    ```
    npx react-native bundle --platform android --dev false --entry-file index.js --bundle-output android/app/src/main/assets/index.android.bundle --assets-dest android/app/src/main/res/
    ```
7) 안드로이드 스튜디오에서 프로젝트 폴더 내의 android 폴더 프로젝트로 열기
8) 빌드 탭에서 apk 빌드 선택하여 빌드
9) rotto/android/app/build/outputs/apk/debug 경로에서 apk 파일 선택 및 실행

### 3. BackEnd 빌드 및 배포
* BackEnd Dockerfile
    ```dockerfile
    FROM openjdk:17-jdk
    WORKDIR /app
    COPY build/libs/rotto-0.0.1-SNAPSHOT.jar app.jar
    EXPOSE 8080

    CMD ["java", "-jar", "app.jar", "--spring.profiles.active=production", ">>", "/home/ubuntu/applicationBE.log", "2>&1"]
    ```

* ❗ application.yml 파일은 git에 업로드되지 않음.
    ```yml
    server:
        port: 8000
        servlet:
            context-path: /api


    spring:
        datasource:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: ENC(insert-your-db-url)
            username: ENC(db_your_username)
            password: ENC(db_your_password)



        security:
            user:
                name: username
                password: password

        servlet:
            multipart:
                max-file-size: 1000MB
                max-request-size: 1000MB
        jpa:
            open-in-view: false


    jwt:
        token:
            secret-key: ENC(insert_your_token_secret_key)
        access-token:
            expire-length: 1800000
        refresh-token:
            expire-length: 1209600000
        redis:
            host: redis
            port: 6379
            password: ENC(insert_your_redis_password)


    # Multipart File Upload Setting
    file:
        multipart:
            maxUploadSize: 1000000
            maxUploadSizePerFile: 1000000


    # Swagger setting
    springdoc:
        packages-to-scan: com.rezero.rotto.api.controller
        swagger-ui:
            path: /api-docs
            groups-order: DESC
            tags:sorter: alpha
            operations-sorter: alpha
            disabled-swagger-default-url: true
            display-request-duration: true
        api-docs:
            path: /api-docs/json
            groups:
                enabled: true
        show-actuator: true
        cache:
            disabled: true
        default-consumes-media-type: application/json;charset=UTF-8
        default-produces-media-type: application/json;charset=UTF-8


    # log level Setting
    logging:
        level:
            root: info
            org:
                springframework:
                    root: debug
                    web: debug
            com:
                rotto: debug
            zaxxer:
                hikari:
                    pool:
                        HikariPool: debug


    # S3 Bucket
    cloud:
        aws:
            s3:
                # 수정 필요
                bucket: ENC(insert_your_bucket_name)
            region:
                static: ENC(insert_your_s3_region)
            credentials:
                access-key: ENC(insert_your_access_key)
                secret-key: ENC(insert_your_secret_key)
            stack:
                auto: false
    ```

### 4. BlockChain 빌드 및 배포
1. env 파일 추가
```
MNEMONIC="{your_metamask_wallet_SRP(Secret Recovery Phrase)}"
rpcUrl="{your_blockchain_network_rpc_url}"
networkId="{your_blockchain_network_chain_ID}"
```

2. Truffle 설치
```
npm install -g truffle@5.11.5
```

3. BlockChain 폴더 이동 및 빌드
```
cd smartContract
truffle compile
```

4. 배포
```
truffle migrate
```
