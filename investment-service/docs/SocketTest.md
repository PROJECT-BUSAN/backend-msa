# 소켓 통신 테스트

STOMP 프로토콜

> 먼저 mysql 과 redis 이미지를 받아야 합니다.<br>
> 아래 코드를 순서대로 입력해서 이미지를 다운로드 해주세요.


```
docker pull mysql:5.7
```

```
docker pull redis:6.2.6
```


> investment-setvice 폴더로 이동 후 아래 스크립트 파일을 실행시킵니다.


```
./MakeContainer.sh
```

<hr>

> socket의 connection url은 "/ws-stomp" 입니다.


* /pub/game/message : type, channelId, senderId 를 보내준다. key값 동일
    > type 종류 : ENTER, EXIT, READY, CANCEL
 <br>
* /sub/game/channel/{channel_id}" : 게임에 입장 시, 방(채널)의 메시지를 구독하는 url