package com.heyunxia.domain;

import lombok.Data;

@Data
public class Follow extends User {

    User follower;

    User followed;

}
