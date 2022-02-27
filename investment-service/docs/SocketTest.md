# 소켓 통신 테스트

### git 파일 받기
1. https://github.com/PROJECT-BUSAN/backend-msa.git -> clone 하기
2. feature/investment 브랜치로 swtich 하기
3. investment-service 폴더로 이동하기

<br>

STOMP 프로토콜을 사용합니다.

> 먼저 mysql 과 redis 이미지를 받아야 합니다.<br>
> 아래 코드를 순서대로 입력해서 이미지를 다운로드 해주세요.


```
docker pull mysql:5.7
```

```
docker pull redis:6.2.6
```


> `investment-setvice 폴더로 이동 후` 아래 스크립트 파일을 실행시킵니다.


```
./MakeContainer.sh
```

#### gradle을 설정하는데 시간이 좀 걸립니다. 
#### 스크립트 파일 내부의 명령어를 하나씩 실행해도 무방합니다. 하나씩 실행할 경우 실행 로그를 볼 수 있습니다.(윈도우만 해당)


<hr>


> socket의 connection url은 "/ws-stomp" 입니다.


* /pub/game/message : type, channelId, senderId 를 보내준다.
    > type 종류 : ENTER, EXIT, READY, CANCEL
    ```
    {
        "type": ENTER,
        "channelId": {채널 입장 시 알 수 있는 채널의 id값}
        "senderId": {유저의 id값}
    }
    ```
 <br>
* /sub/game/channel/{channel_id}" : 게임에 입장 시, 방(채널)의 메시지를 구독하는 url

<hr>

> api 명세서(설명은 아직 부족)
```
/api/v1/investment/swagger
```