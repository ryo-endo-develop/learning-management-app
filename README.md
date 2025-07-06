# Learning Management App

データベーススペシャリスト学習管理アプリケーション

## Quick Start

```bash
# 環境起動
docker-compose up -d

# DB マイグレーション
./gradlew :study-all:flywayMigrate

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

✅ study-base: 基盤クラス・共通エンティティ・テーブル設計  
🚧 study-plan: 学習計画ドメイン（次回実装）  
⏳ study-session: 学習セッションドメイン  
⏳ study-analytics: 学習分析ドメイン  
⏳ study-all: API層

### Database Schema

```
users (id, name, email, created_at, updated_at)
study_categories (id, name, description, display_order, ...)
study_plans (id, user_id, title, description, start_date, end_date, status, ...)
study_goals (id, study_plan_id, category_id, target_score, target_hours, ...)
study_sessions (id, user_id, study_plan_id, category_id, title, session_type, duration_minutes, score, ...)
study_progress_summary (CQRS Query側最適化)
weekly_study_stats (CQRS Query側最適化)
```

## Development

```bash
# テスト実行
./gradlew test

# 特定モジュールのテスト
./gradlew :study-plan:test

# DB マイグレーション
./gradlew :study-all:flywayMigrate

# DB マイグレーション情報
./gradlew :study-all:flywayInfo
```
