# Learning Management App

データベーススペシャリスト学習管理アプリケーション

## Quick Start

```bash
# 環境起動
docker-compose up -d

# アプリケーション起動
./gradlew :study-all:bootRun
```

## Architecture

```
learning-management-app/
├── modules/
│   ├── study-all/           # API層 (Controller)
│   ├── study-base/          # 共通基盤・CQRS
│   ├── study-plan/          # 学習計画ドメイン
│   ├── study-session/       # 学習セッションドメイン
│   └── study-analytics/     # 学習分析ドメイン
```

**CQRS + マルチモジュール構成**
- Command/Query責務分離
- ドメイン毎のモジュール分割
- MyBatis Dynamic SQLによるデータアクセス

## Progress

✅ study-base: 基盤クラス・共通エンティティ  
🚧 study-plan: 学習計画ドメイン（次回実装）  
⏳ study-session: 学習セッションドメイン  
⏳ study-analytics: 学習分析ドメイン  
⏳ study-all: API層

## Development

```bash
# テスト実行
./gradlew test

# 特定モジュールのテスト
./gradlew :study-plan:test

# DB マイグレーション
./gradlew flywayMigrate
```
