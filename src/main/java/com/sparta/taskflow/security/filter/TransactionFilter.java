package com.sparta.taskflow.security.filter;

import com.sparta.taskflow.common.exception.TransactionIdHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TransactionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String transactionId = TransactionIdHolder.generateTransactionId();
            TransactionIdHolder.setTrId(transactionId);
            request.setAttribute("transactionId", transactionId);
            filterChain.doFilter(request, response);
        } finally {
            TransactionIdHolder.clear();
        }
    }
}
