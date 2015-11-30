package com.heyunxia.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2015-11-25 下午2:12
 */
@Data
public class BaseModel implements Serializable {
    private String id;

    private long createAt;
    private Long createAT;

    private int projectID;
    private Integer projectId;

    private String createBy;
}
