# **PROJECT 모의투자 게임 💸**


## **:bulb: The purpose of a project**
    동아리 페이지 활성화를 위한 모의투자 게임 프로젝트
  
<br>

---

## **Stack**
    Java : 11
    SpringBoot : 2.6.3
    Python : 3.9
    Django : 3.2.7
    JPA : 2.6.1
    Redis : 6.2.6
    Mysql : 5.7
    Docker : 20.10.12
    AWS EC2

<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white"/> <img src="https://img.shields.io/badge/Redis-DC382D?style=flat&logo=Redis&logoColor=white"/> <img src="https://img.shields.io/badge/Java-007396?style=flat&logo=Java&logoColor=white"/> <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=flat&logo=Spring-Boot&logoColor=white"/> <img src="https://img.shields.io/badge/Docker-2496ED?style=flat&logo=Docker&logoColor=white"/> <img src="https://img.shields.io/badge/JPA-FF3621?style=flat&logo=Databricks&logoColor=white"/> <img src="https://img.shields.io/badge/Python-3776AB?style=flat&logo=Python&logoColor=white"/>
<img src="https://img.shields.io/badge/Django-092E20?style=flat&logo=Django&logoColor=white"/><img src="https://img.shields.io/badge/Nginx-009639?style=flat&logo=Nginx&logoColor=white"/>
<br>

---

## **Service Architecture**
<img width="883" alt="image" src="https://user-images.githubusercontent.com/76645095/165769511-8e0c82fc-3173-4e2a-b851-903795e127de.png">

---

<br>

## **주요기능**

> ## **Api Gateway**
<br>

- ### **Architecture**

![image](https://user-images.githubusercontent.com/76645095/165774043-a40de8ce-a97d-4001-8b43-1e73d0d4bee2.png)
```
가장 앞단에 위치한 Nginx 는 Reverse Proxy 역할로, 소켓 및 HTTP 요청을 로드밸런싱 해준다. 

proxy 서버로는 소켓 통신을 사용하는 Investment 서버와 ApiGateway 서버를 두었다.
```

```
하나의 엔드포인트로 API를 관리하기 위해서 Django 로 구현한 API Gateway를 적용했다.
Celery와 RabbitMQ 를 사용해 모든 Request를 비동기 처리한다.
```

1. HTTP Request 를 올바른 서비스로 매핑한다.


2. JWT 기반의 인증 방식을 사용하여 End-Point별 접근 권한을 설정한다.


<br>


> ## **Investment Service**
- #### **ERD**

<img width="500" alt="image" src="https://user-images.githubusercontent.com/68914294/165770557-ec136546-14dd-4274-afea-02e3c4e3b2e4.png">
<br>
<br>

- ### **Architecture**

<img width="922" alt="image" src="https://user-images.githubusercontent.com/76645095/165768575-daf29ea8-fabc-4c09-8d11-2528c2c899b3.png">

```
소켓 통신을 위한 서브 프로토콜로 STOMP를 사용했다. 
STOMP를 이용한 메시지 전달은 redis Publish/Subscribe 기능을 사용하여 구현 하였고, 채널별로 message를 구별하기위해 redis에서 ChannelTopic을 관리하였다.
```
<br>

전체 동작 흐름은 아래의 그림과 같다.

<img width="1080" alt="image" src="https://user-images.githubusercontent.com/76645095/165784888-093c2e72-ecb5-4637-a99b-6c4d7c4fa541.png">

<br>

- ### **게임 시나리오**

1. 방 생성
   
```
생성 요청은 HTTP 프로토콜을 사용해 요청을 하기 때문에 ApiGateway 를 거쳐서 Investment 서비스로 전달된다. 
Socket Message 전달을 위한 새로운 ChannelTopic이 생성된다.
이때 게임에 사용될 2~4개의 기업이 선택된다.
```

2. 방 입장   

```
방 입장 요청을 보낸 후, 조건을 만족한 User는 소켓 연결을 시도한다.
방에 입장하기 위한 조건으로 입장료를 설정하였다. 따라서 각 User의 보유 포인트를 확인하기 위하여 Profile Service의 API를 호출한다.
```

3. 게임 준비

```
유저는 게임 준비, 취소 요청을 보낼 수있고, 해당 channel topic을 구독한 사용자 모두에게 이를 알려준다.
```

4. 게임 시작  

```
모든 유저가 게임 준비 상태일때 방장은 게임을 시작할 수 있다.
게임이 시작되면 1.에서 선택된 기업의 실제 5년치 주가 정보 중 랜덤으로 연속된 60일이 선택된다.

멀티 스레드를 사용해 서버에서 10초마다 1일치 주가 정보를 전송한다.
이때 클라이언트의 주가 동기화 문제 및 정보 위조 문제를 해결하기 위해서 서버에서 주가 정보를 10초에 한 번씩 publish 하는 방법을 선택했다.

유저는 실시간으로 매수, 매도 요청을 보낼 수 있고, 60일 간의 주가를 모두 전송받으면 게임이 종료되며, 수익률을 기반으로 참가자들의 순위를 확인할 수 있다.
방 입장에 사용된 포인트는 최종 순위에 따라 차등 부여된다.
```

<br>

> ### **Crawling Service**

pykrx 라이브러리를 사용해 주가 정보 크롤링


<br>

> ### **Profile Service**

유저 포인트 및 기타 정보


<br>

> ### **Board Service**

게시판


<br>

> ### **Survey Service**

동아리 지원

<br>
