<div align="center">

# 🏥 智慧医院挂号系统 (HPOS)

**基于 Spring Boot 3 + Vue 3 的在线挂号平台 · 毕业设计项目**

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://www.oracle.com/java/)
[![Vue](https://img.shields.io/badge/Vue-3.5-4FC08D?logo=vue.js)](https://vuejs.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql)](https://www.mysql.com/)
[![MyBatis-Plus](https://img.shields.io/badge/MyBatis--Plus-3.5.9-blue)](https://baomidou.com/)
[![Vite](https://img.shields.io/badge/Vite-8.0-646CFF?logo=vite)](https://vitejs.dev/)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

</div>

---

## 📋 目录

- [项目介绍](#-项目介绍)
- [技术栈](#-技术栈)
- [项目结构](#-项目结构)
- [数据库设计](#-数据库设计)
- [API 接口](#-api-接口)
- [快速开始](#-快速开始)
- [测试数据](#-测试数据)
- [常见问题](#-常见问题)
- [开发计划](#-开发计划)

---

## 💡 项目介绍

智慧医院挂号系统是一个**前后端分离**的在线医疗挂号平台，模拟真实医院挂号流程，实现以下业务闭环：

> **选择科室 → 选择医生 → 查看号源排班 → 填写个人信息 → 提交挂号 → 查看/取消挂号记录**

### 核心功能

| 功能 | 说明 |
|------|------|
| 🏢 **科室浏览** | 查看所有科室列表及详情 |
| 👨‍⚕️ **医生查询** | 按科室查看医生信息、职称、专长 |
| 📅 **号源排班** | 查看医生未来一周的上午/下午号源及费用 |
| 📝 **在线挂号** | 填写患者信息，选择号源提交挂号 |
| 📋 **挂号记录** | 查看历史挂号记录及当前状态 |
| ❌ **取消挂号** | 支持取消未就诊的挂号单，自动释放号源 |
| 🔐 **用户登录** | 系统用户登录认证 |

---

## 🛠 技术栈

### 后端

| 组件 | 技术 | 版本 |
|------|------|------|
| 🧩 **开发框架** | Spring Boot | 3.2.5 |
| ☕ **运行环境** | Java | 21 |
| 🗄️ **ORM 框架** | MyBatis-Plus | 3.5.9 |
| 🛢️ **数据库** | MySQL | 8.0+ |
| ✅ **参数校验** | Spring Validation | Jakarta |
| 🛠 **工具库** | Hutool | 5.8.32 |
| 📦 **构建工具** | Maven | 3.8+ |
| 🎯 **简化代码** | Lombok | - |

### 前端

| 组件 | 技术 | 版本 |
|------|------|------|
| 🖼️ **前端框架** | Vue 3 (Composition API) | 3.5.32 |
| ⚡ **构建工具** | Vite | 8.0.8 |
| 🌐 **HTTP 客户端** | Axios | 1.16.1 |
| 🎨 **UI 框架** | Bootstrap 5 (CDN) | - |

---

## 📁 项目结构

```
hpos/
├── pom.xml                              # Maven 项目配置
│
├── src/main/java/com/hpos/
│   ├── HposApplication.java             # 🚀 启动入口
│   ├── config/                          # ⚙️ 配置类（CORS、MyBatis-Plus分页）
│   ├── common/                          # 🔧 公共组件（全局异常处理、自动填充）
│   ├── entity/                          # 📦 实体类（6张表映射）
│   ├── mapper/                          # 🔌 MyBatis-Plus Mapper 接口
│   ├── service/                         # 🧠 业务逻辑层
│   ├── controller/                      # 🌐 RESTful API 控制器
│   ├── dto/                             # 📨 数据传输对象（请求/响应）
│   └── utils/                           # 🔐 工具类（MD5加密、订单号生成）
│
├── src/main/resources/
│   ├── application.yml                  # 📝 项目配置文件
│   └── HPOS.SQL                         # 🗄️ 数据库建表脚本 + 测试数据
│
└── untitled/                            # 🖥️ Vue 前端项目
    ├── vite.config.js                   # Vite 配置（API代理到8080）
    ├── package.json                     # 前端依赖
    └── src/
        ├── App.vue                      # 根组件
        ├── main.js                      # 入口文件
        ├── api/index.js                 # 所有后端API封装
        └── components/
            ├── Header.vue               # 🧭 导航栏 + 欢迎横幅
            ├── RegistrationForm.vue     # 📋 挂号表单 + 记录列表
            └── Footer.vue               # 📄 页脚信息
```

---

## 🗂 数据库设计

系统包含 **6 张核心表**，ER 关系如下：

```
department (1) ──→ (N) doctor (1) ──→ (N) registration_source
                                                      ↑
patient (1) ──→ (N) registration_order ──────────────┘
sys_user (1) ──→ (1) patient
```

### 表结构总览

| 表名 | 说明 | 关键字段 |
|------|------|----------|
| `department` | 科室表 | `id`, `dept_name`, `introduction`, `sort_order`, `status` |
| `doctor` | 医生表 | `id`, `real_name`, `title`, `specialty`, `dept_id`, `status` |
| `registration_source` | 号源表 | `id`, `doctor_id`, `work_date`, `period`, `total_count`, `available_count`, `fee` |
| `patient` | 患者表 | `id`, `real_name`, `gender`, `phone`, `id_card` |
| `registration_order` | 挂号订单表 | `id`, `order_no`, `patient_id`, `doctor_id`, `source_id`, `work_date`, `period`, `fee`, `status` |
| `sys_user` | 用户表 | `id`, `username`, `password`, `phone` |

### 订单状态枚举

| 值 | 状态 | 说明 |
|:--:|:----:|------|
| 0 | ⏳ 待支付 | 已提交但未付款 |
| 1 | ✅ 已支付 | 支付成功，预约生效 |
| 2 | ❌ 已取消 | 用户取消预约 |
| 3 | ✔️ 已就诊 | 已完成就诊 |

### 时段枚举

| 值 | 时段 | 时间 |
|:--:|:----:|:----:|
| 1 | 🌅 上午 | 08:00–12:00 |
| 2 | 🌇 下午 | 13:00–17:00 |

> 详细表结构及字段说明请参见 [`数据库说明.txt`](数据库说明.txt)

---

## 🌐 API 接口

所有接口返回统一格式：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": { ... }
}
```

### 认证模块

| 方法 | 路径 | 说明 | 参数 |
|:----:|------|:----:|:----:|
| POST | `/api/auth/login` | 用户登录 | Body: `{ username, password }` |
| GET | `/api/auth/user/{username}` | 获取用户信息 | Path: `username` |

### 科室 & 医生

| 方法 | 路径 | 说明 | 参数 |
|:----:|------|:----:|:----:|
| GET | `/api/departments` | 获取科室列表 | - |
| GET | `/api/departments/{id}` | 获取科室详情 | Path: `id` |
| GET | `/api/doctors?deptId={id}` | 按科室获取医生列表 | Query: `deptId` |
| GET | `/api/doctors/{id}` | 获取医生详情 | Path: `id` |

### 号源 & 挂号

| 方法 | 路径 | 说明 | 参数 |
|:----:|------|:----:|:----:|
| GET | `/api/sources/schedule` | 获取医生排班 | `doctorId`, `startDate`, `endDate` |
| POST | `/api/orders` | 提交挂号 | Body: 患者+挂号信息 |
| GET | `/api/orders?patientId={id}` | 查询挂号记录 | Query: `patientId` |
| PUT | `/api/orders/{id}/cancel` | 取消挂号 | Path: `id`, Query: `patientId` |
| GET | `/api/patients/phone/{phone}` | 按手机号查找患者 | Path: `phone` |

### POST /api/orders 请求示例

```json
{
  "patientName": "张三",
  "idCard": "110101199001011234",
  "phone": "13800138001",
  "gender": 1,
  "deptId": 1,
  "doctorId": 1,
  "sourceId": 1,
  "workDate": "2026-06-02",
  "period": 1
}
```

### 状态码说明

| 状态码 | 说明 |
|:------:|:----:|
| 200 | ✅ 成功 |
| 400 | ❌ 参数校验失败 |
| 401 | ❌ 登录失败（用户名或密码错误） |
| 404 | ❌ 资源不存在 |
| 500 | ❌ 服务器内部错误（如号源已满） |

---

## 🚀 快速开始

### 环境要求

| 环境 | 版本 |
|:----|:----:|
| JDK | 21+ |
| MySQL | 8.0+ |
| Node.js | 20+ |
| Maven | 3.8+ |

### 1️⃣ 初始化数据库

```bash
# 方式一：命令行导入
mysql -u root -p < src/main/resources/HPOS.SQL

# 方式二：使用 MySQL 客户端（Navicat / DataGrip）执行 HPOS.SQL
```

### 2️⃣ 修改数据库连接

编辑 `src/main/resources/application.yml`，将用户名和密码改为你自己的：

```yaml
spring:
  datasource:
    username: root        # 改为你的 MySQL 用户名
    password: 123456      # 改为你的 MySQL 密码
```

### 3️⃣ 启动后端

```bash
# 在项目根目录 hpos/ 下执行
mvn spring-boot:run
```

看到 `Started HposApplication` 即为启动成功。  
后端 API 地址：`http://localhost:8080/api/...`

### 4️⃣ 启动前端

```bash
# 新开一个终端
cd untitled/
npm install            # 首次启动需要安装依赖
npm run dev            # 启动开发服务器
```

浏览器访问 `http://localhost:5173` 即可进入系统。

---

## 🧪 测试数据

### 登录账号

| 用户名 | 密码 | 手机号 |
|:------:|:----:|:------:|
| `admin` | `123456` | 13800138000 |
| `zhangsan` | `123456` | 13800138001 |
| `lisi` | `123456` | 13800138002 |

### 科室列表

| 科室 | 说明 |
|:----:|:----:|
| 🫀 内科 | 治疗内科常见疾病 |
| 🩺 外科 | 开展各类外科手术 |
| 👶 儿科 | 儿童疾病诊治 |

### 医生团队

| 姓名 | 科室 | 职称 | 专长 |
|:----:|:----:|:----:|:----:|
| 张明 | 内科 | 主任医师 | 心血管疾病、高血压 |
| 李华 | 内科 | 副主任医师 | 消化内科、胃肠疾病 |
| 王强 | 外科 | 主任医师 | 骨科手术、创伤外科 |
| 刘芳 | 儿科 | 副主任医师 | 小儿呼吸、小儿哮喘 |
| 陈伟 | 儿科 | 主治医师 | 小儿保健、生长发育 |

---

## ❓ 常见问题

<details>
<summary><b>Q: 导入数据库后启动失败？</b></summary>
检查 <code>application.yml</code> 中的数据库用户名和密码是否正确，以及 MySQL 服务是否已启动。
</details>

<details>
<summary><b>Q: 前端页面空白或接口报错？</b></summary>
打开浏览器开发者工具（F12 → Console），确认后端是否运行在 8080 端口，以及 API 代理是否正常。
</details>

<details>
<summary><b>Q: Maven 依赖下载缓慢？</b></summary>
在 <code>pom.xml</code> 中添加阿里云镜像：
<pre>
&lt;repositories&gt;
  &lt;repository&gt;
    &lt;id&gt;aliyun&lt;/id&gt;
    &lt;url&gt;https://maven.aliyun.com/repository/public&lt;/url&gt;
  &lt;/repository&gt;
&lt;/repositories&gt;
</pre>
</details>

---

## 🗓 开发计划

- [x] 科室、医生、号源 CRUD
- [x] 挂号提交与取消（事务保证号源一致性）
- [x] 挂号记录查询
- [x] 用户登录认证
- [ ] 🔐 JJWT + Shiro 登录授权
- [ ] 📨 RabbitMQ 异步通知
- [ ] ⏰ Quartz 定时清理过期订单
- [ ] 💳 支付接口对接
- [ ] 📊 管理员后台

---

<div align="center">

**Made with ❤️ for Graduation Design**

如果这个项目对你有帮助，欢迎 ⭐ Star 支持！

</div>
