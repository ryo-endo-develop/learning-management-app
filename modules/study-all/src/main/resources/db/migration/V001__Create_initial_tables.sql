-- V001__Create_initial_tables.sql
-- 学習管理アプリケーションの初期テーブル作成

-- ユーザーテーブル
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 学習分野テーブル（午前I、午前II、午後I、午後II等）
CREATE TABLE study_categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    display_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 学習計画テーブル
CREATE TABLE study_plans (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'COMPLETED', 'PAUSED', 'CANCELLED')),
    target_hours_per_day INTEGER DEFAULT 2,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 学習目標テーブル
CREATE TABLE study_goals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    study_plan_id UUID NOT NULL REFERENCES study_plans(id) ON DELETE CASCADE,
    category_id UUID NOT NULL REFERENCES study_categories(id),
    target_score INTEGER NOT NULL, -- 目標スコア（0-100）
    target_hours INTEGER NOT NULL, -- 目標学習時間
    current_best_score INTEGER DEFAULT 0,
    total_studied_hours INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(study_plan_id, category_id)
);

-- 学習セッションテーブル
CREATE TABLE study_sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    study_plan_id UUID NOT NULL REFERENCES study_plans(id) ON DELETE CASCADE,
    category_id UUID NOT NULL REFERENCES study_categories(id),
    title VARCHAR(200) NOT NULL,
    session_type VARCHAR(20) NOT NULL CHECK (session_type IN ('THEORY', 'PRACTICE', 'EXAM', 'REVIEW')),
    duration_minutes INTEGER NOT NULL,
    score INTEGER, -- 正答率等（0-100、nullable）
    max_score INTEGER DEFAULT 100,
    notes TEXT,
    completed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 学習進捗サマリービュー用テーブル（CQRS - Query側最適化）
CREATE TABLE study_progress_summary (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    study_plan_id UUID NOT NULL REFERENCES study_plans(id) ON DELETE CASCADE,
    category_id UUID NOT NULL REFERENCES study_categories(id),
    total_sessions INTEGER NOT NULL DEFAULT 0,
    total_study_time_minutes INTEGER NOT NULL DEFAULT 0,
    average_score DECIMAL(5,2), -- nullable
    best_score INTEGER, -- nullable
    last_session_date DATE, -- nullable
    goal_achievement_rate DECIMAL(5,2) DEFAULT 0.0, -- 目標達成率（0.0-100.0）
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, study_plan_id, category_id)
);

-- 週次学習統計テーブル（CQRS - Query側最適化）
CREATE TABLE weekly_study_stats (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    year INTEGER NOT NULL,
    week_number INTEGER NOT NULL, -- 1-53
    total_sessions INTEGER NOT NULL DEFAULT 0,
    total_study_time_minutes INTEGER NOT NULL DEFAULT 0,
    average_score DECIMAL(5,2), -- nullable
    categories_studied INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, year, week_number)
);

-- パフォーマンス最適化のためのインデックス
CREATE INDEX idx_study_sessions_user_id ON study_sessions(user_id);
CREATE INDEX idx_study_sessions_plan_id ON study_sessions(study_plan_id);
CREATE INDEX idx_study_sessions_category_id ON study_sessions(category_id);
CREATE INDEX idx_study_sessions_completed_at ON study_sessions(completed_at);
CREATE INDEX idx_study_sessions_user_completed ON study_sessions(user_id, completed_at DESC);

CREATE INDEX idx_study_plans_user_id ON study_plans(user_id);
CREATE INDEX idx_study_plans_status ON study_plans(status);
CREATE INDEX idx_study_plans_user_status ON study_plans(user_id, status);

CREATE INDEX idx_study_goals_plan_id ON study_goals(study_plan_id);
CREATE INDEX idx_study_goals_category_id ON study_goals(category_id);

CREATE INDEX idx_study_progress_summary_user_id ON study_progress_summary(user_id);
CREATE INDEX idx_study_progress_summary_plan_id ON study_progress_summary(study_plan_id);

CREATE INDEX idx_weekly_study_stats_user_year_week ON weekly_study_stats(user_id, year, week_number);

-- トリガー関数: updated_atの自動更新
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- updated_atトリガー設定
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_study_categories_updated_at BEFORE UPDATE ON study_categories
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_study_plans_updated_at BEFORE UPDATE ON study_plans
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_study_goals_updated_at BEFORE UPDATE ON study_goals
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_study_progress_summary_updated_at BEFORE UPDATE ON study_progress_summary
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- テーブルコメント
COMMENT ON TABLE users IS 'ユーザー情報テーブル';
COMMENT ON TABLE study_categories IS '学習分野マスタテーブル（午前I、午前II、午後I、午後II等）';
COMMENT ON TABLE study_plans IS '学習計画テーブル';
COMMENT ON TABLE study_goals IS '学習計画毎の分野別目標テーブル';
COMMENT ON TABLE study_sessions IS '学習セッション実績テーブル';
COMMENT ON TABLE study_progress_summary IS '学習進捗サマリービュー（CQRS Query側最適化）';
COMMENT ON TABLE weekly_study_stats IS '週次学習統計テーブル（CQRS Query側最適化）';
