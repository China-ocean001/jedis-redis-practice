# 📦 JEE 2026 Redis 课堂练习

> Java Jedis + Redis + Docker Compose 实操项目

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.org/)
[![Redis](https://img.shields.io/badge/Redis-7-DC382D.svg)](https://redis.io/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED.svg)](https://www.docker.com/)
[![JUnit](https://img.shields.io/badge/JUnit-5-25A162.svg)](https://junit.org/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

## 📖 项目简介

JEE 课程 Redis 课堂练习项目，通过学生信息管理场景学习 Jedis 客户端操作 Redis 数据库。

## ✨ 学习内容

- 🔌 **Jedis 连接**：使用 Jedis 客户端连接 Redis
- 📝 **CRUD 操作**：通过 Redis 完成学生数据增删改查
- 🐳 **Docker 部署**：Docker Compose 一键启动 Redis
- 🧪 **单元测试**：JUnit 5 测试驱动开发
- 🔐 **签名验证**：测试类完整性校验

## 🛠 技术栈

- **语言**：Java 17
- **Redis 客户端**：Jedis 5.1.0
- **测试框架**：JUnit Jupiter 5.10
- **容器化**：Docker Compose (Redis 7 Alpine)
- **构建工具**：Gradle
- **简化**：Lombok

## 📁 项目结构

```
jedis-redis-practice/
├── src/main/java/cn/jee/redis/
│   ├── StuMis.java               # 学生管理主类
│   └── domain/Stu.java           # 学生实体
├── src/test/java/cn/jee/redis/
│   ├── StuMisTest.java           # 单元测试
│   └── util/CompSign.java        # 签名工具
├── docker-compose.yml            # Redis 容器配置
├── build.gradle
└── README.md
```

## 🚀 快速开始

### 启动 Redis
```bash
docker-compose up -d
```

### 运行测试
```bash
./gradlew test
```

### 手动运行
```bash
./gradlew build
java -cp build/libs/*:libs/* cn.jee.redis.StuMis
```

## 📄 License

MIT License — 仅供学习交流使用
