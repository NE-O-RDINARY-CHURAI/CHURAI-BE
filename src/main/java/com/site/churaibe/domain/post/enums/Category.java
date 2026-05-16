package com.site.churaibe.domain.post.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    MAIN_DISH("식사류"),
    DESSERT("디저트류");

    private final String description;
}
