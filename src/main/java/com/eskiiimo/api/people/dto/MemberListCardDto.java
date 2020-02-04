package com.eskiiimo.api.people.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MemberListCardDto {
    private long id;
    private String imageUrl;
    private String userName;
    private int level;
    private List<String> tags = new ArrayList<>();
}
