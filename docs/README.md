# Integral Testing
<br>

- Need Docker

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

> stop&drop containers
```
docker-compose down
```