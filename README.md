# 线上宠物领养平台 - 后端

## 项目简介
本项目为“线上宠物领养平台”的 JavaWeb 后端，基于 Spring Boot + MySQL 开发，提供 RESTful API 给前端调用，实现用户注册登录、宠物展示、领养申请、评论管理以及管理员后台功能。

## 技术栈
- Java 21
- Spring Boot 2.7.12
- Spring Data JPA
- MySQL 8.0
- Maven
- SpringDoc OpenAPI 1.7.0（API文档）

## 开发环境准备
1. **安装 JDK 21**
   - 下载并安装 JDK 21（推荐 Oracle JDK 21 或 OpenJDK 21）
   - 设置环境变量 `JAVA_HOME` 指向 JDK 21 安装目录
   - 验证安装：`java -version`

2. **安装 Maven 3.6+**
   - 下载并安装 Maven 3.6 或更高版本
   - 设置环境变量 `MAVEN_HOME` 和 `PATH`
   - 验证安装：`mvn -version`

3. **安装 MySQL 8.0**
   - 下载并安装 MySQL 8.0
   - 启动 MySQL 服务
   - 创建数据库：
     ```sql
     CREATE DATABASE pet_adoption CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
     ```
   - 记录数据库用户名和密码（默认用户名为 `root`）

4. **IDE 选择**
   - VS Code：需安装 Extension Pack for Java
   - IntelliJ IDEA：推荐使用 Ultimate 版本

## 项目启动

### 1. 配置数据库连接
修改 `src/main/resources/application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/pet_adoption?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=UTF-8
    username: root          # 修改为你的 MySQL 用户名
    password: W123456       # 修改为你的 MySQL 密码
    driver-class-name: com.mysql.cj.jdbc.Driver
```

**重要提示：**
- 第一次运行时，新建一个名称为pet_adoption的数据库，在该数据库中运行 `src\main\resources\db\init.sql` 文件
- 将 `username` 和 `password` 修改为你实际的 MySQL 数据库凭据
- 确保 MySQL 服务已启动

### 2. 设置 Java 环境
设置 `JAVA_HOME` 环境变量为 JDK 21，或使用提供的脚本：

**Windows 用户：**
```bash
# 使用提供的批处理脚本（自动设置 JAVA_HOME）
build-with-jdk21.bat
```

**手动设置（所有平台）：**
```bash
# Windows (PowerShell)
$env:JAVA_HOME="D:\Program Files\Java\jdk-21.0.10"
$env:PATH="D:\Program Files\Java\jdk-21.0.10\bin;" + $env:PATH

# Linux/Mac
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk
export PATH=$JAVA_HOME/bin:$PATH
```

### 3. 启动应用
在项目根目录执行：

```bash
mvn spring-boot:run
```

或直接运行 `PetAdoptionApplication` 主类。

### 4. 验证启动
服务启动后，访问以下地址：
- **应用首页**: http://localhost:8080
- **Swagger API文档**: http://localhost:8080/swagger-ui.html

### 5. 自动建表说明
项目配置了 JPA `ddl-auto: update`，首次启动时会自动创建所需的数据库表：
- `user` - 用户表
- `pet` - 宠物表
- `adoption` - 领养申请表
- `comment` - 评论表

**注意：** 首次运行后，建议将 `ddl-auto` 改为 `validate` 以防止意外修改表结构。

## 接口文档（完整）

### 统一响应格式
所有接口返回统一的 JSON 格式：
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {}
}
```

**响应码说明：**
- `200`: 操作成功
- `401`: 未登录或登录过期
- `403`: 权限不足
- `500`: 操作失败

---

### 📱 用户模块

#### 前台接口
| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/api/user/register` | 用户注册（支持手机号验证） | ❌ |
| POST | `/api/user/login` | 用户登录（支持账号/手机号） | ❌ |
| GET | `/api/user/logout` | 退出登录 | ✅ |
| GET | `/api/user/current` | 获取当前用户信息 | ✅ |

**注册参数示例：**
```json
{
  "account": "user123",
  "password": "password123",
  "nickname": "张三",
  "phone": "13800138000"
}
```

---

### 🐾 宠物模块

#### 前台接口
| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/api/pet/list?species=&keyword=&page=1&size=8` | 宠物列表（分页+筛选，自动过滤下架宠物） | ❌ |
| GET | `/api/pet/{id}` | 宠物详情 | ❌ |

**列表查询参数：**
- `species`: 物种（猫/狗/其他），可选
- `keyword`: 搜索关键词（名称/品种/描述），可选
- `page`: 页码，从1开始，默认1
- `size`: 每页数量，默认8，最大50

#### 管理员接口
| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/api/admin/pet/add` | 添加宠物 | 🔐 |
| PUT | `/api/admin/pet/update` | 更新宠物信息 | 🔐 |
| DELETE | `/api/admin/pet/{id}` | 删除宠物 | 🔐 |
| GET | `/api/admin/pets?page=1&size=10` | 所有宠物列表（含下架，分页） | 🔐 |
| PUT | `/api/admin/pet/{id}/status?status=待领养` | 修改宠物状态 | 🔐 |

**宠物状态：**
- `待领养`: 可以被申请领养
- `已领养`: 已被领养
- `下架`: 不对外展示

---

### 📝 领养模块

#### 前台接口
| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/api/adoption/apply` | 提交领养申请（防重复提交） | ✅ |
| GET | `/api/adoption/my?page=1&size=5` | 我的申请列表（分页+含宠物信息） | ✅ |
| GET | `/api/adoption/{appId}` | 申请详情 | ✅ |
| POST | `/api/adoption/cancel/{appId}` | 撤回申请（仅待审核可撤回） | ✅ |

**申请参数示例：**
```json
{
  "petId": 1,
  "reason": "我很喜欢这只宠物，有足够的时间和空间照顾它...",
  "address": "北京市朝阳区xxx小区"
}
```

**申请状态：**
- `待审核`: 等待管理员审核
- `通过`: 审核通过
- `驳回`: 审核驳回
- `已撤回`: 用户主动撤回

#### 管理员接口
| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/api/admin/adoptions?page=1&size=10` | 所有领养申请（分页） | 🔐 |
| PUT | `/api/admin/adoption/{id}` | 审核申请（通过/驳回） | 🔐 |

**审核参数示例：**
```json
{
  "status": "通过"
}
```

**审核逻辑：**
- 审核通过时，自动将宠物状态改为"已领养"
- 防止重复审核
- 校验宠物状态仍为"待领养"

---

### 💬 评论模块

#### 前台接口
| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/api/comment/add` | 添加评论（最多500字符） | ✅ |
| GET | `/api/comment/list/{petId}?page=1&size=10` | 评论列表（分页+用户昵称） | ❌ |
| DELETE | `/api/comment/{commentId}` | 删除评论（仅作者或管理员） | ✅ |

**评论参数示例：**
```json
{
  "petId": 1,
  "content": "这只宠物真可爱！"
}
```

**评论列表响应（含用户昵称）：**
```json
{
  "content": [
    {
      "comment": {...},
      "nickname": "爱宠人士"
    }
  ],
  "totalElements": 20,
  "totalPages": 2
}
```

#### 管理员接口
| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| DELETE | `/api/admin/comment/{id}` | 删除任意评论 | 🔐 |
| GET | `/api/admin/comments` | 查看所有评论 | 🔐 |

---

### 🖼️ 文件上传模块

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/api/upload/pet-image` | 单图上传（JPG/PNG/GIF/WEBP，最大5MB） | ✅ |
| POST | `/api/upload/pet-images` | 批量上传（最多10张） | ✅ |

**上传说明：**
- Content-Type: `multipart/form-data`
- 文件按日期分目录存储：`uploads/images/yyyy/MM/dd/`
- 文件名使用 UUID，防止冲突
- 静态资源映射：`/uploads/**` → `./uploads/`

---

### 👨‍💼 管理员后台

#### 用户管理
| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/api/admin/users?page=1&size=10` | 用户列表（分页） | 🔐 |
| PUT | `/api/admin/user/{userId}/status?status=0` | 修改用户状态（0:正常, 1:禁用） | 🔐 |

#### 权限说明
- 所有 `/api/admin/**` 接口需要管理员权限（role=1）
- 通过 `AdminInterceptor` 进行权限验证
- 普通用户访问会返回 403 错误

## 📚 前端开发文档

项目提供完整的前端开发指南，包含详细的 API 说明和 Vue.js 组件示例：

- **[FRONTEND_DEVELOPMENT_GUIDE.md](FRONTEND_DEVELOPMENT_GUIDE.md)** - 完整的前端开发指南（1558行）
  - Axios 配置与拦截器
  - Session 认证机制
  - 所有模块的完整 API 文档
  - Vue.js 组件示例代码
  - Element Plus 集成示例
  - 常见问题解答

**Swagger 在线文档：** http://localhost:8080/swagger-ui.html

---

## 🏗️ 项目结构说明

```
src/main/java/com/pet/adoption/
├── common/                    # 通用组件
│   ├── Result.java           # 统一响应结果封装
│   └── SessionUtils.java     # Session 工具类
├── config/                    # 配置类
│   ├── WebMvcConfig.java     # Web MVC 配置（拦截器、静态资源）
│   └── SwaggerConfig.java    # Swagger 配置
├── controller/                # 控制器层（6个）
│   ├── UserController.java   # 用户接口
│   ├── PetController.java    # 宠物接口（前台）
│   ├── AdoptionController.java # 领养接口（前台）
│   ├── CommentController.java # 评论接口（前台）
│   ├── AdminController.java  # 管理员接口
│   └── FileUploadController.java # 文件上传接口
├── dto/                       # 数据传输对象
│   ├── RegisterRequest.java  # 注册请求
│   ├── LoginRequest.java     # 登录请求
│   ├── AdoptionRequest.java  # 领养申请请求
│   └── CommentRequest.java   # 评论请求
├── entity/                    # 实体类（4个）
│   ├── User.java             # 用户实体
│   ├── Pet.java              # 宠物实体
│   ├── Adoption.java         # 领养申请实体
│   └── Comment.java          # 评论实体
├── interceptor/               # 拦截器
│   ├── LoginInterceptor.java # 登录验证拦截器
│   └── AdminInterceptor.java # 管理员权限拦截器
├── repository/                # 数据访问层（4个）
│   ├── UserRepository.java
│   ├── PetRepository.java
│   ├── AdoptionRepository.java
│   └── CommentRepository.java
├── service/                   # 业务逻辑层
│   ├── UserService.java
│   ├── PetService.java
│   ├── AdoptionService.java
│   ├── CommentService.java
│   └── impl/                 # 服务实现类
└── PetAdoptionApplication.java # 启动类

src/main/resources/
├── db/init.sql               # 数据库初始化脚本
├── application.yml           # 应用配置文件
└── uploads/                  # 文件上传目录（Git忽略）
```

## 🗄️ 数据库设计

### 自动初始化（推荐）
项目使用 JPA 的 `ddl-auto: update` 配置，首次启动时会自动创建所有必需的表。

### 手动初始化
如果需要手动初始化数据库，可以执行 `src/main/resources/db/init.sql` 脚本：

```bash
mysql -u root -p pet_adoption < src/main/resources/db/init.sql
```

### 数据库表结构
系统包含以下核心表：

| 表名 | 字段数 | 外键 | 说明 |
|------|--------|------|------|
| `user` | 7 | - | 用户表（id, account, password, nickname, phone, role, status） |
| `pet` | 10 | - | 宠物表（id, name, species, breed, age, image, description, health, status, add_time） |
| `adoption` | 8 | pet_id, user_id | 领养申请表（id, pet_id, user_id, reason, address, status, apply_time, audit_time） |
| `comment` | 5 | pet_id, user_id | 评论表（id, pet_id, user_id, content, create_time） |

### 初始数据
`init.sql` 中包含以下初始数据：
- **管理员账号**: admin / admin123（role=1）
- **测试用户**: testuser / 123456（role=0）
- **示例宠物**: 3只宠物（猫、狗各一只）

### 字符集
- 数据库字符集：`utf8mb4`
- 排序规则：`utf8mb4_unicode_ci`
- 支持完整的 Unicode 字符（包括 emoji）

## ⚠️ 注意事项

### 数据库相关
- **自动建表**：默认 JPA 会执行 `ddl-auto: update` 自动建表，首次运行后可将该配置改为 `validate`
- **字符集**：使用 `utf8mb4` 字符集以支持完整的 Unicode 字符（包括 emoji）
- **时区设置**：连接 URL 中已配置 `serverTimezone=Asia/Shanghai`，确保时间戳正确
- **SSL 连接**：开发环境使用 `useSSL=false`，生产环境建议启用 SSL

### 安全相关
- **密码加密**：当前使用 MD5 加密存储，生产环境建议使用 BCrypt
- **Swagger 安全**：生产环境务必在 `application.yml` 中设置 `swagger.enabled: false` 禁用 Swagger
- **会话管理**：当前使用 Session 管理用户状态，分布式部署需考虑 Session 共享方案（如 Redis）
- **权限控制**：
  - 登录拦截器（LoginInterceptor）保护需要登录的接口
  - 管理员拦截器（AdminInterceptor）保护管理员接口
  - 评论删除权限：只有作者或管理员可以删除
  - 领养申请防重复：同一用户对同一宠物只能有一个待审核或通过的应用

### 跨域配置
- 前后端分离模式下，需注意跨域问题
- 当前版本未配置 CORS，可通过以下方式解决：
  - 使用 Nginx 反向代理
  - 前端配置代理（Vue CLI / Vite proxy）
  - 在 `WebMvcConfig` 中添加跨域支持

### 文件上传
- 上传目录：`./uploads/images/`（按日期分目录）
- Git 忽略：`uploads/` 目录已在 `.gitignore` 中配置
- 文件大小限制：单张最大 5MB
- 支持格式：JPG、PNG、GIF、WEBP
- 静态资源映射：`/uploads/**` → `./uploads/`

### 其他
- **API 文档**：所有接口的详细请求/响应示例请访问 Swagger UI
- **管理员账号**：`init.sql` 已包含管理员账号（admin / admin123）
- **编译要求**：必须使用 JDK 21，Maven 编译时需设置 JAVA_HOME
- **前端文档**：详见 [FRONTEND_DEVELOPMENT_GUIDE.md](FRONTEND_DEVELOPMENT_GUIDE.md)

---

## 📊 项目统计

- **API 接口总数**: 27+
- **代码行数**: 约 3000+ 行
- **文档页数**: 4 份完整文档
- **编译状态**: ✅ 通过
- **功能完成度**: 94%

---

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

**开发流程：**
1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

---

## 📄 许可证

本项目仅供学习和交流使用。

---

**最后更新**: 2026-06-01  
**维护者**: 后端开发团队
