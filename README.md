# Restaurant

Второе дз по КПО. Галиуллин Руслан

## Описание API

В файле [openapi.json]() находится json описание, чтобы посмотреть через https://editor-next.swagger.io. В нем присутствуют описания всех ошибок и описаний endpoint'ов

After starting the application it is accessible under `localhost:8080`.

## Build

The application can be built using the following command:

```
mvnw clean package
```

The application can then be started with the following command - here with the profile `production`:

```
java -Dspring.profiles.active=production -jar ./target/restaurant-0.0.1-SNAPSHOT.jar
```

## Further readings

* [Maven docs](https://maven.apache.org/guides/index.html)  
* [Spring Boot reference](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)  
* [Spring Data JPA reference](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)  
* [Thymeleaf docs](https://www.thymeleaf.org/documentation.html)  
* [Bootstrap docs](https://getbootstrap.com/docs/5.2/getting-started/introduction/)  
* [Htmx in a nutshell](https://htmx.org/docs/)  
