package com.heyunxia.domain;

import lombok.Data;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2015-11-25 下午12:05
 */
@Data
public class User extends BaseModel {

    private String loginName;

    private String userName;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
