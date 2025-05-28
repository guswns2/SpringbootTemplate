package com.jhj.template.stomp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Socket {
    private String sender, msg, roomId;

}
