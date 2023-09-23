package com.pds.websocket_chat_prac.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Message {
    private String from;
    private String text;
}
