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

## 接口文档（简要）
所有接口返回统一格式：
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": ...
}
```

### 用户模块
- `POST /api/user/register` – 注册（{account, password, nickname, phone}）
- `POST /api/user/login` – 登录（{account, password}）
- `GET /api/user/logout` – 退出

### 宠物模块
- `GET /api/pet/list?species=&keyword=` – 宠物列表
- `GET /api/pet/{id}` – 宠物详情

### 领养申请（需登录）
- `POST /api/adoption/apply` – 提交申请（{petId, reason, address}）
- `GET /api/adoption/my` – 我的申请

### 评论模块（需登录）
- `POST /api/comment/add` – 添加评论（{petId, content}）
- `GET /api/comment/list/{petId}` – 查看某宠物评论

### 管理员后台（需管理员权限）
- `POST /api/admin/pet/add` – 添加宠物
- `PUT /api/admin/pet/update` – 更新宠物
- `DELETE /api/admin/pet/{id}` – 删除宠物
- `GET /api/admin/adoptions` – 所有领养申请
- `PUT /api/admin/adoption/{id}` – 审核申请（参数：{status: "通过"/"驳回"}）
- `GET /api/admin/users` – 用户列表
- `PUT /api/admin/user/{id}/ban` – 禁用用户
- `PUT /api/admin/user/{id}/unban` – 启用用户
- `DELETE /api/admin/comment/{id}` – 删除评论
- `GET /api/admin/comments` – 查看所有评论

## 项目结构说明
- `config` – 拦截器与Web配置
- `common` – 统一返回结果、Session工具类
- `interceptor` – 登录拦截器、管理员权限拦截器
- `dto` – 前端请求参数对象
- `entity` – 数据库实体映射
- `repository` – JPA 数据访问接口
- `service` – 业务逻辑接口及实现
- `controller` – RESTful 接口控制器
- `resources/db` – 数据库初始化脚本

## 数据库初始化

### 自动初始化（推荐）
项目使用 JPA 的 `ddl-auto: update` 配置，首次启动时会自动创建所有必需的表。

### 手动初始化
如果需要手动初始化数据库，可以执行 `src/main/resources/db/init.sql` 脚本：

```bash
mysql -u root -p pet_adoption < src/main/resources/db/init.sql
```

### 数据库表结构
系统包含以下核心表：

| 表名 | 说明 | 主要字段 |
|------|------|----------|
| `user` | 用户表 | id, account, password, nickname, phone, role, status |
| `pet` | 宠物表 | id, name, species, breed, age, image, description, health, status, add_time |
| `adoption` | 领养申请表 | id, pet_id, user_id, reason, address, status, apply_time, audit_time |
| `comment` | 评论表 | id, pet_id, user_id, content, create_time |

### 初始数据建议
建议在数据库中预先创建至少一个管理员账号：

```sql
INSERT INTO user (account, password, nickname, role, status) 
VALUES ('admin', MD5('admin123'), '管理员', 1, 0);
```

## 注意事项

### 数据库相关
- **自动建表**：默认 JPA 会执行 `ddl-auto: update` 自动建表，首次运行后可将该配置改为 `validate`
- **字符集**：建议使用 `utf8mb4` 字符集以支持完整的 Unicode 字符（包括 emoji）
- **时区设置**：连接 URL 中已配置 `serverTimezone=Asia/Shanghai`，确保时间戳正确
- **SSL 连接**：开发环境使用 `useSSL=false`，生产环境建议启用 SSL

### 安全相关
- **密码加密**：当前使用 MD5 加密存储，生产环境建议使用 BCrypt
- **Swagger 安全**：生产环境务必在 `application.yml` 中设置 `swagger.enabled: false` 禁用 Swagger
- **会话管理**：当前使用 Session 管理用户状态，分布式部署需考虑 Session 共享方案

### 跨域配置
- 前后端分离模式下，需注意跨域问题
- 当前版本未配置 CORS，可通过以下方式解决：
  - 使用 Nginx 反向代理
  - 前端配置代理
  - 在 `WebMvcConfig` 中添加跨域支持

### 其他
- 所有接口的详细请求/响应示例请访问 Swagger UI 查看
- 管理员账号需要手动在数据库中设置 `role=1`
- 建议在 `src/main/resources/db/init.sql` 中添加初始化数据脚本
