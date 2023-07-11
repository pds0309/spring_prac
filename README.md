# spring_prac

## [webflux_prac_movies](https://github.com/pds0309/spring_pr)

<img src="https://github.com/pds0309/spring_prac/assets/76927397/b90d0ccc-046c-445d-be83-8ffe27d353e9" width=400 />

> spring webflux basic practice

### info service moudule

> movie-info crud rest api with annotateted rest controller

**practice**

- RestController crud api with integration test
- mongo db connection & test with data-mongodb-reactive

<br>

### review service module

> movie review crud with functional web endpoints

**practice**

- Functional Router,Handler crud api with unit, integration test
- handle global exception with custom class overrided ErrorWebExceptionHandler

<br>

### client service module

> retrieve Movie(info + review) api by WebClient

**practice**

- WebClient api to interact with two services in non-blocking
- test WebClient endpoint with stubbing two service endpoint
- handle global exception with controllerAdvice
- retry request with specific excpetion

<br>

## [Server Sent Event](https://github.com/pds0309/spring_prac/tree/master/server-sent-event)

![sse](https://github.com/pds0309/spring_prac/assets/76927397/6499566e-2802-489d-ba3e-9f7de9677d8e)

**unicast**
- 하나의 Subscriber만 허용

**multicast**
- 여러 Subscriber를 허용

**replay**
- 이전에 발행된 이벤트들을 기억해 추가로 연결되는 Subscriber 에게 전달
