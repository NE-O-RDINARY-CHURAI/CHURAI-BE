package com.site.churaibe.domain.post.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReactionType {
    CHURAI("츄라이"),
    INTERESTED("흥미");

    private final String description;
}
