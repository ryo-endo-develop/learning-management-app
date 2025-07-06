package com.learningapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 学習管理アプリケーションのメインクラス
 * 
 * CQRS（Command Query Responsibility Segregation）パターンを採用し、
 * 書き込み操作（Command）と読み込み操作（Query）を分離して設計。
 */
@SpringBootApplication
@EnableCaching
@EnableTransactionManagement
public class LearningManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearningManagementApplication.class, args);
    }
}
