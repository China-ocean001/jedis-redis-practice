# 📦 JEE 2026 Redis 课堂练习

> Java Jedis + Redis + Docker Compose 实操项目

<div align="center">

[![Java](https://img.shields.io/badge/Java-17-orange?style=flat&logo=openjdk)](https://openjdk.org/)
[![Redis](https://img.shields.io/badge/Redis-7.x-DC382D?style=flat&logo=redis)](https://redis.io/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=flat&logo=docker)](https://www.docker.com/)
[![JUnit](https://img.shields.io/badge/JUnit-5-25A162?style=flat&logo=junit5)](https://junit.org/)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-02303A?style=flat&logo=gradle)](https://gradle.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat)](LICENSE)

</div>

---

## 📑 目录

- [📖 项目简介](#-项目简介)
- [📚 学习目标](#-学习目标)
- [🛠 技术栈](#-技术栈)
- [📁 项目结构](#-项目结构)
- [🚀 快速开始](#-快速开始)
- [💻 代码示例](#-代码示例)
- [🧪 测试](#-测试)

---

## 📖 项目简介

JEE 课程 Redis 课堂练习项目，通过**学生信息管理系统场景**学习 Jedis 客户端操作 Redis 数据库的核心技能。项目包含完整的学生 CRUD 操作实现和 JUnit 5 单元测试。

### 学习场景

模拟一个学生管理系统 (Student Management Information System)，使用 Redis 作为唯一数据存储，实现学生的增删改查操作。

---

## 📚 学习目标

| 目标 | 描述 | 掌握程度 |
|------|------|----------|
| 🔌 **Jedis 连接** | 学会创建和管理 Redis 连接 | ⭐⭐⭐ |
| 📝 **String 操作** | set/get/del/exists 命令 | ⭐⭐⭐ |
| 📋 **Hash 操作** | 用 Hash 存储对象 | ⭐⭐ |
| 📊 **List 操作** | 列表数据结构 | ⭐⭐ |
| 🐳 **Docker 部署** | Docker Compose 管理 Redis | ⭐⭐ |
| 🧪 **单元测试** | JUnit 5 测试 Redis 操作 | ⭐⭐⭐ |

---

## 🛠 技术栈

| 组件 | 版本 | 说明 |
|------|------|------|
| **语言** | Java 17 | 开发语言 |
| **Redis 客户端** | Jedis 5.1.0 | 最流行的 Java Redis 客户端 |
| **Redis 服务** | 7.x Alpine | Docker 轻量镜像 |
| **测试框架** | JUnit Jupiter 5.10 | 新一代测试框架 |
| **构建工具** | Gradle 8.x | Groovy DSL |
| **简化** | Lombok 1.18 | 减少样板代码 |

---

## 📁 项目结构

```
jedis-redis-practice/
│
├── build.gradle                         # Gradle 构建配置
├── settings.gradle                      # 项目设置
├── docker-compose.yml                   # 🐳 Redis 容器定义
├── README.md                            # 📖 项目说明
│
├── src/main/java/cn/jee/redis/
│   ├── StuMis.java                      # 💻 主程序 (学生管理)
│   │   ├── addStudent(Stu)             # 添加学生
│   │   ├── getStudent(String id)       # 查询学生
│   │   ├── updateStudent(Stu)          # 更新学生
│   │   ├── deleteStudent(String id)    # 删除学生
│   │   └── listStudents()             # 列出所有学生
│   │
│   └── domain/
│       └── Stu.java                     # 📦 学生实体
│           ├── id (String)             # 学号
│           ├── name (String)           # 姓名
│           ├── age (int)               # 年龄
│           ├── grade (String)          # 年级
│           └── major (String)          # 专业
│
└── src/test/java/cn/jee/redis/
    ├── StuMisTest.java                  # 🧪 单元测试
    │   ├── testAddStudent()
    │   ├── testGetStudent()
    │   ├── testUpdateStudent()
    │   ├── testDeleteStudent()
    │   ├── testListStudents()
    │   └── testConnection()
    │
    └── util/
        └── CompSign.java                # 🔐 测试签名验证
```

---

## 🚀 快速开始

### 📋 环境要求

| 软件 | 版本 |
|------|------|
| JDK | 17+ |
| Docker | 20+ |
| Gradle | 8.x (或使用Wrapper) |

### 🐳 启动 Redis

```bash
# 启动 Redis 容器 (后台运行)
docker-compose up -d

# 验证 Redis 是否启动
docker ps | grep redis
redis-cli ping    # 应返回 PONG
```

### 🔧 运行项目

```bash
# 方式一：直接运行主类
./gradlew build
java -cp build/libs/*:libs/* cn.jee.redis.StuMis

# 方式二：运行测试 (推荐)
./gradlew test
```

### 🛑 停止 Redis

```bash
docker-compose down
```

---

## 💻 代码示例

### Jedis 连接

```java
// 创建 Jedis 连接
Jedis jedis = new Jedis("localhost", 6379);

// 测试连接
String pong = jedis.ping();
System.out.println("Redis 连接状态: " + pong); // "PONG"

// 使用完毕后关闭连接
jedis.close();
```

### 学生 CRUD

```java
// 创建学生管理实例
StuMis stuMis = new StuMis();

// 添加学生
Stu stu = new Stu("2024001", "张三", 20, "大三", "计算机科学");
stuMis.addStudent(stu);

// 查询学生
Stu found = stuMis.getStudent("2024001");
System.out.println(found);

// 更新学生
stu.setGrade("大四");
stuMis.updateStudent(stu);

// 列出所有学生
List<Stu> students = stuMis.listStudents();
students.forEach(System.out::println);

// 删除学生
stuMis.deleteStudent("2024001");
```

### Redis 数据结构

```java
// 使用 Hash 存储学生对象
Map<String, String> stuMap = new HashMap<>();
stuMap.put("name", "张三");
stuMap.put("age", "20");
stuMap.put("grade", "大三");
stuMap.put("major", "计算机科学");
jedis.hset("student:2024001", stuMap);

// 获取学生信息
Map<String, String> result = jedis.hgetAll("student:2024001");

// 使用 List 维护学号列表
jedis.rpush("student:ids", "2024001");
List<String> ids = jedis.lrange("student:ids", 0, -1);
```

---

## 🧪 测试

```bash
# 运行全部测试
./gradlew test

# 查看测试报告
open build/reports/tests/test/index.html   # macOS
start build/reports/tests/test/index.html  # Windows
```

### 测试覆盖

| 测试类 | 测试用例 | 验证点 |
|--------|----------|--------|
| `StuMisTest.java` | testAddStudent | 添加学生后能查询到 |
| | testGetStudent | 查询不存在的学生返回null |
| | testUpdateStudent | 更新后字段变更 |
| | testDeleteStudent | 删除后查询返回null |
| | testListStudents | 列表包含所有学生 |
| | testConnection | Redis 连接可用 |
| `CompSign.java` | - | 测试类完整性校验 |

---

## ☕ 支持作者

如果这个项目对你有帮助，欢迎请我喝杯咖啡~

<div align="center">

| <img src="https://raw.githubusercontent.com/China-ocean001/campus-repair-platform/master/sponsor/alipay.jpg" width="260" alt="支付宝"> | <img src="https://raw.githubusercontent.com/China-ocean001/campus-repair-platform/master/sponsor/wechat.jpg" width="260" alt="微信支付"> |
|:---:|:---:|
| **支付宝** | **微信支付** |

</div>

---

## 📄 License

MIT License — 仅供学习交流使用

---

<div align="center">

**⭐ 如果这个项目对你有帮助，请给一个 Star！**

</div>
