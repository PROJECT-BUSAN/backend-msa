# Integral Testing
<br>

- Need Docker
  
> pull branch 'dev'
```
git clone https://github.com/PROJECT-BUSAN/backend-msa.git
```

```
git switch dev
git pull origin dev
```

## 1. M1 Mac

> pull docker images
```
docker pull --platform linux/x86_64 mysql
docker pull rabbitmq:3-management
```

<br>

> build images
```
./image-build.sh
```

<br>

> make containers
```
docker-compose up
```

<br>

<a href="http://localhost:8001/api/v1/crawling">localhost:8001/api/v1/crawling</a>
<- 으로 들어가서 POST버튼을 눌러서 테스트용 주가정보를 크롤링 하기

<br>

```
크롤링 이후 테스트 진행해주시면 됩니다.
```

<br>


> stop&drop containers
```
docker-compose down
```

<br>

