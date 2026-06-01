# SpringDoc OpenAPI (Swagger) 集成说明

## 集成概述
已成功为宠物领养后端项目集成了 SpringDoc OpenAPI 1.7.0（Swagger 3），用于生成和展示 API 文档。

## 完成的配置

### 1. 依赖添加
在 `pom.xml` 中添加了以下 SpringDoc OpenAPI 依赖：
- springdoc-openapi-ui (1.7.0)

### 2. Swagger 配置类
创建了 [SwaggerConfig.java](file:///d:/pet-adoption-backend/src/main/java/com/pet/adoption/config/SwaggerConfig.java)，包含：
- 配置 OpenAPI 基本信息（标题、描述、版本等）
- 支持通过配置文件启用/禁用 Swagger

### 3. Controller 注解
为所有控制器添加了 Swagger 注解：
- [PetController](file:///d:/pet-adoption-backend/src/main/java/com/pet/adoption/controller/PetController.java) - 宠物管理
- [UserController](file:///d:/pet-adoption-backend/src/main/java/com/pet/adoption/controller/UserController.java) - 用户管理
- [AdoptionController](file:///d:/pet-adoption-backend/src/main/java/com/pet/adoption/controller/AdoptionController.java) - 领养申请管理
- [CommentController](file:///d:/pet-adoption-backend/src/main/java/com/pet/adoption/controller/CommentController.java) - 评论管理
- [AdminController](file:///d:/pet-adoption-backend/src/main/java/com/pet/adoption/controller/AdminController.java) - 管理员管理

使用的注解（OpenAPI 3）：
- `@Tag` - 标记控制器类，添加标签说明
- `@Operation` - 描述每个 API 接口的功能
- `@Parameter` - 描述接口参数的含义和要求

### 4. DTO 和实体类注解
为所有数据传输对象和实体类添加了 Swagger 注解：

**DTO 类：**
- [RegisterRequest](file:///d:/pet-adoption-backend/src/main/java/com/pet/adoption/dto/RegisterRequest.java)
- [LoginRequest](file:///d:/pet-adoption-backend/src/main/java/com/pet/adoption/dto/LoginRequest.java)
- [AdoptionRequest](file:///d:/pet-adoption-backend/src/main/java/com/pet/adoption/dto/AdoptionRequest.java)
- [CommentRequest](file:///d:/pet-adoption-backend/src/main/java/com/pet/adoption/dto/CommentRequest.java)

**实体类：**
- [Pet](file:///d:/pet-adoption-backend/src/main/java/com/pet/adoption/entity/Pet.java)
- [User](file:///d:/pet-adoption-backend/src/main/java/com/pet/adoption/entity/User.java)
- [Adoption](file:///d:/pet-adoption-backend/src/main/java/com/pet/adoption/entity/Adoption.java)
- [Comment](file:///d:/pet-adoption-backend/src/main/java/com/pet/adoption/entity/Comment.java)

**通用类：**
- [Result](file:///d:/pet-adoption-backend/src/main/java/com/pet/adoption/common/Result.java)

使用的注解（OpenAPI 3）：
- `@Schema` - 描述模型类和字段的用途、是否必填、示例值等

### 5. 配置文件
在 [application.yml](file:///d:/pet-adoption-backend/src/main/resources/application.yml) 中添加了 Swagger 配置项：
```yaml
swagger:
  enabled: true
```

## 使用方法

### 访问 Swagger UI
启动应用后，访问以下地址查看 API 文档：
```
http://localhost:8080/swagger-ui.html
```

### 功能特性
1. **API 列表**：按模块分组显示所有 API 接口
2. **接口详情**：查看每个接口的请求方法、路径、参数说明
3. **在线测试**：可以直接在页面上测试 API 接口
4. **模型查看**：查看所有 DTO 和实体类的字段说明
5. **响应示例**：查看接口的响应格式和数据结构

### 生产环境禁用
在生产环境中，建议在 `application.yml` 中禁用 Swagger：
```yaml
swagger:
  enabled: false
```

## 注意事项

### Java 版本要求
本项目使用 Java 21，请确保已安装 JDK 21 并正确设置 JAVA_HOME 环境变量。

**快速启动：**
可以使用提供的 `build-with-jdk21.bat` 脚本自动设置 Java 环境并编译运行项目。

## API 模块说明

Swagger UI 中将 API 按以下模块分组：
1. **宠物管理** - 宠物的查询、详情查看
2. **用户管理** - 用户注册、登录、登出
3. **领养申请管理** - 提交领养申请、查看我的申请
4. **评论管理** - 添加评论、查看宠物评论
5. **管理员管理** - 宠物管理、领养审核、用户管理、评论管理

## 更新记录

- 2026-05-25: 完成 SpringDoc OpenAPI 1.7.0 集成
  - 添加 springdoc-openapi-ui 依赖
  - 创建 OpenAPI 配置类
  - 为所有 Controller、DTO、Entity 添加 OpenAPI 3 注解
  - 更新 README 文档
  - 使用 Java 21 作为运行时环境
