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

<br>

## [WebSocket Simple Chat](https://github.com/pds0309/spring_prac/tree/master/websocket_chat_prac)

![socketsample](https://github.com/pds0309/spring_prac/assets/76927397/cf72552d-4e13-45f1-9c7a-4c89747e6eae)

### Client Dependencies

- sockjs
- stompjs

### Server Dependencies

- boot-starter-websocket (spring-websocket, spring-messaging)

### Enable Websocket 

Add this annotation to an @Configuration class to enable broker-backed messaging over WebSocket using a higher-level messaging sub-protocol.

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends WebSocketMessageBrokerConfigurer {
}
```

Register STOMP endpoints mapping each to a specific URL and (optionally) enabling and configuring SockJS fallback options.

```java
// WebSocketMessageBrokerConfigurer
// Defines methods for configuring message handling with simple messaging protocols (e.g. STOMP) from WebSocket clients.

// example
// registry.addEndpoint("/portfolio").withSockJS();
default void registerStompEndpoints(StompEndpointRegistry registry) {

}
```

Configure message broker options


```java
// example
// config.enableSimpleBroker("/topic"); =>  enable an in-memory message broker to carry the messages back to the client on destinations prefixed with “/topic”.
// config.setApplicationDestinationPrefixes("/app"); => /app” prefix to filter destinations targeting application annotated methods (via @MessageMapping).
default void configureMessageBroker(MessageBrokerRegistry registry) {

}
```
<br>

### Controller

```java
@Controller
public class Controller {
  @MessageMapping("/chat")
  @SendTo("/topic/messages")
  public ? send() {}
}
```

<br>

### Client Side

**Connect & Subscribe**

```javascript
const socket = new SockJS('/chat');
stompClient = Stomp.over(socket);  
stompClient.connect({}, function(frame) {
  setConnected(true);
  stompClient.subscribe('/topic/messages', function(messageOutput) {
    showMessageOutput(JSON.parse(messageOutput.body));
  });
});
```

**Send Message**

```javascript
stompClient.send("/app/chat", {}, YOUR_MESSAGE);
```


