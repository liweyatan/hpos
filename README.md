<div align="center">

# 🏥 智慧医院管理系统 (HPOS)

**Spring Boot 3 + Vue 3 在线挂号平台 · 毕业设计项目**

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://www.oracle.com/java/)
[![Vue](https://img.shields.io/badge/Vue-3.5-4FC08D?logo=vue.js)](https://vuejs.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql)](https://www.mysql.com/)
[![MyBatis](https://img.shields.io/badge/MyBatis-3.0-blue)](https://mybatis.org/mybatis-3/)
[![Vite](https://img.shields.io/badge/Vite-8.0-646CFF?logo=vite)](https://vitejs.dev/)

</div>

---

## 📋 目录

- [项目介绍](#-项目介绍)
- [技术栈](#-技术栈)
- [项目结构](#-项目结构)
- [数据库设计](#-数据库设计)
- [快速开始](#-快速开始)
- [部署到生产环境](#-部署到生产环境)
- [测试数据](#-测试数据)
- [功能页面](#-功能页面)

---

## 💡 项目介绍

智慧医院管理系统是一个**前后端分离**的在线医疗挂号平台，模拟真实医院挂号流程：

> **选择科室 → 选择医生 → 选择时间 → 填写个人信息 → 提交挂号 → 查看/取消挂号记录**

### 核心功能

| 功能 | 说明 |
|------|------|
| 🏢 **科室浏览** | 查看所有科室列表及详情 |
| 👨‍⚕️ **医生查询** | 按科室查看医生信息、职称、专长 |
| 📝 **在线挂号** | 4 步挂号流程：选科室 → 选医生 → 选时间 → 确认信息 |
| 📋 **挂号记录** | 查看历史挂号记录及当前状态 |
| ❌ **取消挂号** | 支持取消待处理/已确认的预约 |
| 🔐 **用户登录/注册** | 用户认证，支持管理员/普通用户角色 |
| 👨‍💼 **管理员后台** | 管理科室、医生、预约、用户 |

---

## 🛠 技术栈

### 后端

| 组件 | 技术 | 版本 |
|------|------|------|
| 🧩 **框架** | Spring Boot | 3.3.5 |
| ☕ **语言** | Java | 17 |
| 🗄️ **ORM** | MyBatis (注解式) | 3.0.3 |
| 🛢️ **数据库** | MySQL | 8.0+ |
| 🔐 **认证** | Session | - |
| 📦 **构建** | Maven | 3.8+ |

### 前端

| 组件 | 技术 | 版本 |
|------|------|------|
| 🖼️ **框架** | Vue 3 (Options API) | 3.5.32 |
| ⚡ **构建** | Vite | 8.0.8 |
| 🌐 **HTTP** | Axios | 1.16.1 |
| 🧭 **路由** | Vue Router | 4.6.4 |
| 🎨 **UI** | Bootstrap 5 (本地) | 5.3 |

---

## 📁 项目结构

```
hpos/
├── README.md
├── .gitignore
│
├── backend/                             # ☕ Spring Boot 后端
│   ├── pom.xml                          # Maven 配置
│   ├── mvnw / mvnw.cmd                  # Maven Wrapper
│   │
│   └── src/main/
│       ├── java/com/hospital/
│       │   ├── HospitalendApplication.java   # 🚀 启动入口
│       │   ├── config/                       # ⚙️ 配置（CORS、静态资源）
│       │   ├── entity/                       # 📦 实体类
│       │   ├── repository/                   # 🔌 MyBatis Mapper
│       │   ├── service/                      # 🧠 业务逻辑
│       │   ├── controller/                   # 🌐 REST API
│       │   │   ├── api/                      #    公开 API（认证、科室、医生、订单）
│       │   │   ├── HomeController.java       #    首页路由 → index.html
│       │   │   └── AdminApiController.java   #    管理员 API
│       │   └── dto/                          # 📨 数据传输对象
│       │
│       └── resources/
│           ├── application.properties        # 📝 配置文件
│           └── static/                       # 🖥️ Vue 构建产物（部署后）
│
└── frontend/                            # 🖥️ Vue 3 前端
    ├── index.html
    ├── package.json
    ├── vite.config.js                   # Vite 配置（API 代理到 8080）
    ├── deploy.bat                       # 一键构建 + 部署
    │
    └── src/
        ├── main.js
        ├── App.vue
        ├── api/
        │   ├── index.js                 # API 封装（axios）
        │   └── auth.js                  # 认证状态管理
        ├── router/index.js              # 路由配置
        ├── views/                       # 页面组件
        │   ├── Home.vue                 # 首页
        │   ├── Login.vue                # 登录
        │   ├── RegisterUser.vue         # 注册
        │   ├── Register.vue             # 挂号预约
        │   ├── Orders.vue               # 我的预约
        │   ├── DepartmentList.vue       # 科室列表
        │   └── Admin.vue                # 管理员后台
        ├── components/
        │   ├── Header.vue               # 导航栏
        │   └── Footer.vue               # 页脚
        └── assets/main.css              # 全局样式
```

---

## 🗂 数据库设计

系统包含 **5 张核心表**：

```
department (1) ──→ (N) doctor
                          │
patient (1) ──→ (N) registration_order ←──┘
                          │
user (1) ──→ (1) patient
```

| 表名 | 说明 | 关键字段 |
|------|------|----------|
| `department` | 科室表 | `id`, `name`, `description`, `director`, `phone`, `location` |
| `doctor` | 医生表 | `id`, `name`, `department_id`, `title`, `specialty`, `max_patients` |
| `patient` | 患者表 | `id`, `name`, `id_card`, `phone`, `gender` |
| `registration_order` | 挂号订单 | `id`, `patient_id`, `doctor_id`, `register_time`, `status`, `symptoms` |
| `user` | 用户表 | `id`, `username`, `password`, `role`, `phone`, `real_name` |

### 订单状态

| 状态 | 说明 |
|:----:|------|
| `PENDING` | 待处理 |
| `CONFIRMED` | 已确认 |
| `COMPLETED` | 已完成 |
| `CANCELLED` | 已取消 |

---

## 🚀 快速开始

### 环境要求

| 环境 | 版本 |
|:----|:----:|
| JDK | 17+ |
| MySQL | 8.0+ |
| Node.js | 20+ |
| Maven | 3.8+ |

### 1️⃣ 初始化数据库

```bash
mysql -u root -p -e "CREATE DATABASE hospital_db;"
mysql -u root -p hospital_db < backend/src/main/resources/hospital_db.sql
```

### 2️⃣ 修改数据库配置

编辑 `backend/src/main/resources/application.properties`：

```properties
spring.datasource.username=root        # 你的 MySQL 用户名
spring.datasource.password=123456      # 你的 MySQL 密码
```

### 3️⃣ 启动后端

```bash
cd backend
.\mvnw.cmd spring-boot:run
```

后端启动在 `http://localhost:8080`

### 4️⃣ 启动前端

```bash
cd frontend
npm install
npm run dev
```

浏览器访问 `http://localhost:5173`

---

## 🚀 部署到生产环境

### 新电脑一键部署

```bash
# 1. 安装 JDK 17 + MySQL 8 + Node.js 20

# 2. 导入数据库
mysql -u root -p -e "CREATE DATABASE hospital_db;"
mysql -u root -p hospital_db < backend/src/main/resources/hospital_db.sql

# 3. 修改数据库密码
# 编辑 backend/src/main/resources/application.properties

# 4. 构建前端并部署到后端
cd frontend
npm install
npm run build
xcopy /E /Y dist\* "..\backend\src\main\resources\static\"

# 5. 启动（单个 Java 进程）
cd ..\backend
.\mvnw.cmd spring-boot:run

# 6. 打开 http://localhost:8080
```

或使用一键脚本：`frontend/deploy.bat`

---

## 🧪 测试数据

### 登录账号

| 用户名 | 密码 | 角色 | 手机号 |
|:------:|:----:|:----:|:------:|
| `kobe` | `123456` | 管理员 | 13800138888 |
| `kobe1` | `123456` | 普通用户 | 13944445556 |

### 科室列表

| 科室 | 主任 | 电话 | 位置 |
|:----:|:----:|:----:|:----:|
| 内科 | 张主任 | 010-88881111 | 门诊楼3层 |
| 外科 | 李主任 | 010-88882222 | 门诊楼4层 |
| 全科 | 王主任 | 010-88883333 | 门诊楼1层 |
| 儿科 | 赵主任 | 010-88884444 | 儿科楼2层 |
| 妇产科 | 陈主任 | 010-88885555 | 妇产科楼3层 |
| 眼科 | 刘主任 | 010-88886666 | 门诊楼5层 |

### 医生团队

| 姓名 | 科室 | 职称 | 专长 |
|:----:|:----:|:----:|:----:|
| 张医生 | 内科 | 主任医师 | 心血管疾病 |
| 李医生 | 外科 | 副主任医师 | 普外科 |
| 王医生 | 全科 | 主治医师 | 全科诊疗 |
| 赵医生 | 儿科 | 副主任医师 | 儿科常见疾病 |
| 陈医生 | 妇产科 | 主任医师 | 妇产科疾病 |
| 刘医生 | 眼科 | 主治医师 | 眼科疾病 |

---

## 📄 功能页面

| 页面 | 路由 | 说明 |
|------|------|------|
| 首页 | `/` | 系统首页 |
| 登录 | `/login` | 用户登录 |
| 注册 | `/register-user` | 用户注册 |
| 科室列表 | `/departments` | 所有科室信息 |
| 挂号预约 | `/register` | 4 步挂号流程 |
| 我的预约 | `/orders` | 查看/取消预约记录 |
| 管理后台 | `/admin` | 管理科室/医生/预约/用户（需管理员权限） |

---

## 🌐 API 接口

### 认证

| 方法 | 路径 | 说明 |
|:----:|------|:----:|
| POST | `/api/auth/login` | 登录 |
| POST | `/api/auth/register` | 注册 |

### 科室 & 医生

| 方法 | 路径 | 说明 |
|:----:|------|:----:|
| GET | `/api/departments` | 科室列表 |
| GET | `/api/doctors` | 医生列表 |
| GET | `/api/doctors/department/{id}` | 按科室查医生 |

### 挂号订单

| 方法 | 路径 | 说明 |
|:----:|------|:----:|
| POST | `/api/registration-orders/with-patient` | 创建挂号 |
| GET | `/api/registration-orders/patient/{id}` | 查询患者预约 |
| PUT | `/api/registration-orders/{id}/status` | 取消预约 |

### 管理员

| 方法 | 路径 | 说明 |
|:----:|------|:----:|
| GET/POST/PUT/DELETE | `/api/admin/departments` | 科室 CRUD |
| GET/POST/PUT/DELETE | `/api/admin/doctors` | 医生 CRUD |
| GET | `/api/admin/appointments` | 预约列表 |
| PUT | `/api/admin/appointments/{id}/status` | 更新预约状态 |
| GET/POST/PUT/DELETE | `/api/admin/users` | 用户 CRUD |

---

## 🐛 常见问题

### 页面加载慢 / 白屏

- 浏览器按 `Ctrl + Shift + F5` 强制刷新清缓存
- 或用无痕模式打开 `http://localhost:8080`
- 确认 F12 → Console 没有报错

### 前端样式加载不出来

- Bootstrap / Font Awesome / Bootstrap Icons 已本地化到 `frontend/public/`，不需要联网
- 确认 `backend/src/main/resources/static/` 下有 `bootstrap.min.css`、`fontawesome.min.css` 等文件

### CORS 跨域错误

- `CorsConfig.java` 已全局配置 CORS，支持 `withCredentials`
- 如有 Nginx 反向代理，需在 Nginx 配置 CORS 头

### 数据库连接失败

- 确认 MySQL 服务已启动
- 确认 `application.properties` 中用户名密码正确
- 确认 `hospital_db` 数据库已创建并导入 SQL

---

<div align="center">

**Made with ❤️ for Graduation Design**

如果这个项目对你有帮助，欢迎 ⭐ Star 支持！

</div>
