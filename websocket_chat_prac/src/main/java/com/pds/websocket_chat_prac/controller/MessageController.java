package com.pds.websocket_chat_prac.controller;

import com.pds.websocket_chat_prac.model.Message;
import com.pds.websocket_chat_prac.model.MessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Controller
public class MessageController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public MessageDto send(Message message) throws Exception {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        log.info("send!");
        return new MessageDto(time, message);
    }

}
