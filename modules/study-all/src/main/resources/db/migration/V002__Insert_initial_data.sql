-- V002__Insert_initial_data.sql
-- 初期データの投入

-- 学習分野の初期データ
INSERT INTO study_categories (id, name, description, display_order) VALUES
('11111111-1111-1111-1111-111111111111', '午前I', '基本情報技術者レベルの基礎知識', 1),
('22222222-2222-2222-2222-222222222222', '午前II', 'データベース専門分野', 2),
('33333333-3333-3333-3333-333333333333', '午後I', '記述式問題（データベース設計・SQL）', 3),
('44444444-4444-4444-4444-444444444444', '午後II', '論述式問題（システム提案）', 4),
('55555555-5555-5555-5555-555555555555', 'SQL実践', 'SQL文法とクエリ最適化', 5),
('66666666-6666-6666-6666-666666666666', 'データベース設計', '概念設計・論理設計・物理設計', 6),
('77777777-7777-7777-7777-777777777777', 'パフォーマンス', 'インデックス・実行計画・チューニング', 7),
('88888888-8888-8888-8888-888888888888', '運用管理', 'バックアップ・リカバリ・セキュリティ', 8);

-- サンプルユーザー（開発・テスト用）
INSERT INTO users (id, name, email, password_hash) VALUES
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'テストユーザー', 'test@example.com', '$2a$10$dummy.hash.for.testing.purposes.only');

-- サンプル学習計画
INSERT INTO study_plans (id, user_id, title, description, start_date, end_date, target_hours_per_day) VALUES
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 
 'データベーススペシャリスト合格への道', 
 '90日間でデータベーススペシャリスト試験に合格するための学習計画',
 CURRENT_DATE, 
 CURRENT_DATE + INTERVAL '90 days',
 2);

-- サンプル学習目標
INSERT INTO study_goals (study_plan_id, category_id, target_score, target_hours) VALUES
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '11111111-1111-1111-1111-111111111111', 70, 30),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '22222222-2222-2222-2222-222222222222', 80, 40),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '33333333-3333-3333-3333-333333333333', 60, 50),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '44444444-4444-4444-4444-444444444444', 60, 30),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '55555555-5555-5555-5555-555555555555', 85, 25),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '66666666-6666-6666-6666-666666666666', 75, 35),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '77777777-7777-7777-7777-777777777777', 70, 20),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '88888888-8888-8888-8888-888888888888', 65, 15);
