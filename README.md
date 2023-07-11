# spring_prac

## [webflux_prac_movies](https://github.com/pds0309/spring_pr)

<img src="https://github.com/pds0309/spring_prac/assets/76927397/99d402df-3e18-43ce-9162-4eaca41dade6" width=400/>

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
