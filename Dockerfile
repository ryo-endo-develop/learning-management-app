FROM openjdk:17-jdk-slim

WORKDIR /app

# Gradleラッパーとビルドファイルをコピー
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 依存関係をダウンロード（キャッシュ効率化）
RUN ./gradlew dependencies --no-daemon

# ソースコードをコピー
COPY src src

# アプリケーションをビルド
RUN ./gradlew bootJar --no-daemon

# 実行用の軽量なイメージに切り替え
FROM openjdk:17-jre-slim

WORKDIR /app

# ビルドされたJARファイルをコピー
COPY --from=0 /app/build/libs/*.jar app.jar

# アプリケーション実行
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
