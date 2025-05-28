package com.jhj.template.notice.model;

import lombok.Data;

@Data
public class Notice {

    private int idx;
    private String title;
    private String userId;
    private String createdAt;
    private String content;

}
