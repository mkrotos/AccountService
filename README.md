# Account Service

## How to run

1. Build project using provided gradle wrapper
```shell
./gradlew build
```
2. Run jar file using Java 11
```shell
java -jar build/libs/AccountService-1.0.jar
```

## How to use
Request example:
```http request
GET http://localhost:8080/account/usd?userId=1
```

Accounts available for users id's: 1, 2 and 3