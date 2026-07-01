<div align="center">

# 🏥 智慧医院管理系统 (HPOS)

**Vue 3 前端项目 · 医院挂号管理系统**

[![Vue](https://img.shields.io/badge/Vue-3.5-4FC08D?logo=vue.js)](https://vuejs.org/)
[![Vite](https://img.shields.io/badge/Vite-8.0-646CFF?logo=vite)](https://vitejs.dev/)
[![Axios](https://img.shields.io/badge/Axios-1.16-5A29E4?logo=axios)](https://axios-http.com/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-7952B3?logo=bootstrap)](https://getbootstrap.com/)

</div>

---

## 📋 目录

- [项目介绍](#-项目介绍)
- [技术栈](#-技术栈)
- [项目结构](#-项目结构)
- [快速开始](#-快速开始)
- [部署到生产环境](#-部署到生产环境)
- [测试数据](#-测试数据)
- [功能页面](#-功能页面)

---

## 💡 项目介绍

智慧医院管理系统是一个基于 **Vue 3 + Spring Boot 3** 的在线医疗挂号平台，模拟真实医院挂号流程：

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

| 组件 | 技术 | 版本 |
|------|------|------|
| 🖼️ **前端框架** | Vue 3 (Options API) | 3.5.32 |
| ⚡ **构建工具** | Vite | 8.0.8 |
| 🌐 **HTTP 客户端** | Axios | 1.16.1 |
| 🧭 **路由** | Vue Router | 4.6.4 |
| 🎨 **UI 框架** | Bootstrap 5 (CDN) | 5.3 |
| 🔤 **图标** | Font Awesome 6 (CDN) | 6.0 |

### 后端（独立仓库）

本项目需要配合 Spring Boot 后端使用，后端技术栈：

| 组件 | 技术 |
|------|------|
| 🧩 框架 | Spring Boot 3.3.5 |
| ☕ 语言 | Java 17 |
| 🗄️ ORM | MyBatis (注解式) |
| 🛢️ 数据库 | MySQL 8.0 |
| 🔐 认证 | Session（非 JWT） |

---

## 📁 项目结构

```
hpos/
├── README.md
├── .gitignore
│
└── untitled/                        # 🖥️ Vue 前端项目
    ├── index.html                   # HTML 入口
    ├── package.json                 # 前端依赖
    ├── vite.config.js               # Vite 配置（API 代理到 8080）
    ├── deploy.bat                   # 一键构建 + 部署到后端
    │
    └── src/
        ├── main.js                  # 入口文件
        ├── App.vue                  # 根组件（含 Header）
        │
        ├── api/
        │   ├── index.js             # API 封装（axios 拦截器）
        │   └── auth.js              # 响应式认证状态管理
        │
        ├── router/
        │   └── index.js             # Vue Router 路由配置
        │
        ├── views/
        │   ├── Home.vue             # 首页
        │   ├── Login.vue            # 登录页
        │   ├── RegisterUser.vue     # 用户注册
        │   ├── Register.vue         # 挂号预约（4步流程）
        │   ├── Orders.vue           # 我的预约
        │   ├── DepartmentList.vue   # 科室列表
        │   └── Admin.vue            # 管理员后台
        │
        ├── components/
        │   ├── Header.vue           # 导航栏
        │   └── Footer.vue           # 页脚
        │
        └── assets/
            └── main.css             # 全局样式（合并 5 个 CSS）
```

---

## 🚀 快速开始

### 环境要求

| 环境 | 版本 |
|:----|:----:|
| Node.js | 20+ |
| JDK | 17+ |
| MySQL | 8.0+ |
| Maven | 3.8+ |

### 1️⃣ 初始化数据库

```bash
# 导入数据库（需要先在 hospitalend 仓库找到 SQL 脚本）
mysql -u root -p hospital_db < hospital_db.sql
```

### 2️⃣ 启动后端

```bash
# 进入后端目录（hospitalend 仓库）
cd hospitalend
# 修改 application.properties 中的数据库用户名密码
.\mvnw.cmd spring-boot:run
```

后端启动后监听 `http://localhost:8080`

### 3️⃣ 启动前端（开发模式）

```bash
cd untitled
npm install            # 首次需要安装依赖
npm run dev            # 启动 Vite 开发服务器
```

浏览器访问 `http://localhost:5173`

### 4️⃣ 生产部署（单进程）

```bash
# 一键构建前端并复制到后端 static 目录
cd untitled
deploy.bat

# 然后重启后端，访问 http://localhost:8080 即可
```

---

## 🚀 部署到生产环境

### 新电脑部署

```bash
# 1. 安装 JDK 17 + MySQL 8 + Node.js 20

# 2. 导入数据库
mysql -u root -p -e "CREATE DATABASE hospital_db;"
mysql -u root -p hospital_db < hospital_db.sql

# 3. 修改后端数据库配置
# 编辑 hospitalend/src/main/resources/application.properties

# 4. 构建前端
cd untitled
npm install
npm run build

# 5. 复制到后端
xcopy /E /Y dist\* "..\hospitalend\src\main\resources\static\"

# 6. 启动（只需一个 Java 进程）
cd ..\hospitalend
.\mvnw.cmd spring-boot:run

# 7. 打开 http://localhost:8080
```

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
| 首页 | `/` | 系统首页，展示医院信息 |
| 登录 | `/login` | 用户登录 |
| 注册 | `/register-user` | 用户注册 |
| 科室列表 | `/departments` | 所有科室信息 |
| 挂号预约 | `/register` | 4 步挂号流程 |
| 我的预约 | `/orders` | 查看/取消预约记录 |
| 管理后台 | `/admin` | 管理科室/医生/预约/用户（需管理员权限） |

---

<div align="center">

**Made with ❤️ for Graduation Design**

如果这个项目对你有帮助，欢迎 ⭐ Star 支持！

</div>
