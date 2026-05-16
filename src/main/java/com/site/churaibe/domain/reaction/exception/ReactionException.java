package com.site.churaibe.domain.reaction.exception;

import com.site.churaibe.global.apiPayload.code.BaseErrorCode;
import com.site.churaibe.global.apiPayload.exception.GeneralException;

public class ReactionException extends GeneralException {
    public ReactionException(BaseErrorCode code) {
        super(code);
    }
}
