package com.sparta.taskflow.security.service;

import com.sparta.taskflow.common.exception.BusinessException;
import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.domain.board.service.BoardInvitationService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@Getter
public class CustomSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private final BoardInvitationService boardInvitationService;
    public CustomSecurityExpressionRoot(Authentication authentication, BoardInvitationService boardInvitationService) {
        super(authentication);
        this.boardInvitationService = boardInvitationService;
    }

    public Boolean isCardAllowedByBoardId(Long boardId, Long userId) {
        boolean isAllowed = boardInvitationService.cardAllowedByBoard(boardId, userId);
        if(!isAllowed){
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ACTION_CARD);
        }
        return isAllowed;
    }
    public Boolean isCardAllowedByCardId(Long cardId, Long userId) {
        if(!boardInvitationService.cardAllowedByCard(cardId, userId)){
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ACTION_CARD);
        }
        return true;
    }


    @Override
    public void setFilterObject(Object filterObject) {
        // Not needed for this implementation
    }

    @Override
    public Object getFilterObject() {
        // Not needed for this implementation
        return null;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        // Not needed for this implementation
    }

    @Override
    public Object getReturnObject() {
        // Not needed for this implementation
        return null;
    }

    @Override
    public Object getThis() {
        return this;
    }
}
