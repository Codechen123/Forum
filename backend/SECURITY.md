# 论坛权限验证系统

## 概述

本论坛系统实现了基于 JWT 的权限验证机制，支持多角色权限控制和细粒度的资源访问控制。

## 用户角色

系统支持三种用户角色：

1. **USER（普通用户）**

   - 发布帖子和评论
   - 编辑和删除自己的帖子和评论
   - 浏览所有公开内容

2. **MODERATOR（版主）**

   - 拥有普通用户的所有权限
   - 可以编辑和删除任何用户的帖子和评论
   - 可以置顶和锁定帖子
   - 可以管理分类
   - 可以禁用普通用户

3. **ADMIN（管理员）**
   - 拥有版主的所有权限
   - 可以管理所有用户
   - 可以分配用户角色
   - 可以删除用户
   - 可以禁用版主

## 认证流程

### 1. 用户登录

```
POST /api/auth/login
{
  "username": "用户名",
  "password": "密码"
}
```

### 2. 获取 JWT Token

登录成功后，系统返回 JWT Token：

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "user": {
    "id": 1,
    "username": "用户名",
    "role": "USER"
  }
}
```

### 3. 使用 Token 访问 API

在 HTTP 请求头中添加：

```
Authorization: Bearer <token>
```

## 权限验证方式

### 1. 基于角色的访问控制（RBAC）

使用`@RequireRole`注解进行角色验证：

```java
@RequireRole({User.Role.ADMIN, User.Role.MODERATOR})
public ResponseEntity<?> adminFunction() {
    // 只有管理员和版主可以访问
}
```

### 2. 基于资源所有权的访问控制

使用`@RequireOwnership`注解进行所有权验证：

```java
@RequireOwnership(resourceType = "post", resourceIdParam = "id")
public ResponseEntity<?> editPost(@PathVariable Long id) {
    // 只有帖子作者、版主和管理员可以编辑
}
```

### 3. Spring Security 方法级安全

使用`@PreAuthorize`注解进行复杂权限验证：

```java
@PreAuthorize("hasRole('ADMIN') or (hasRole('MODERATOR') and #categoryId != 1)")
public ResponseEntity<?> manageCategory(@PathVariable Long categoryId) {
    // 复杂的权限逻辑
}
```

## API 访问权限

### 公开访问（无需认证）

- `GET /api/posts/**` - 浏览帖子
- `GET /api/categories/**` - 浏览分类
- `GET /api/comments/**` - 浏览评论
- `GET /api/users/**` - 浏览用户信息
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册

### 需要认证的 API

- `GET /api/auth/me` - 获取当前用户信息
- `POST /api/posts/**` - 发布帖子
- `PUT /api/posts/**` - 编辑帖子（需要所有权或管理权限）
- `DELETE /api/posts/**` - 删除帖子（需要所有权或管理权限）
- `POST /api/comments/**` - 发布评论
- `PUT /api/comments/**` - 编辑评论（需要所有权或管理权限）
- `DELETE /api/comments/**` - 删除评论（需要所有权或管理权限）

### 管理员专用 API

- `GET /api/admin/stats` - 获取系统统计（版主+）
- `GET /api/admin/users` - 获取用户列表（版主+）
- `PUT /api/admin/users/{id}/role` - 更改用户角色（管理员）
- `DELETE /api/admin/users/{id}` - 删除用户（管理员）
- `PUT /api/admin/users/{id}/ban` - 禁用用户（版主+）
- `PUT /api/admin/posts/{id}/pin` - 置顶帖子（版主+）
- `PUT /api/admin/posts/{id}/lock` - 锁定帖子（版主+）

## 权限验证流程

1. **JWT Token 验证**

   - `JwtAuthenticationFilter`拦截请求
   - 验证 Token 有效性和过期时间
   - 提取用户信息并设置 Authentication

2. **角色权限验证**

   - `AuthAspect`处理`@RequireRole`注解
   - 检查用户角色是否满足要求

3. **资源所有权验证**

   - `AuthAspect`处理`@RequireOwnership`注解
   - 检查用户是否拥有资源或有管理权限

4. **业务逻辑验证**
   - `PermissionService`提供细粒度权限检查
   - 在业务逻辑中进行额外的权限验证

## 异常处理

系统提供统一的异常处理机制：

- `AccessDeniedException` - 权限不足（403）
- `AuthenticationException` - 认证失败（401）
- `BadCredentialsException` - 凭证错误（401）
- `MethodArgumentNotValidException` - 参数验证失败（400）

## 安全最佳实践

1. **JWT Token 管理**

   - Token 过期时间设置为 24 小时
   - 客户端需要处理 Token 过期情况
   - 敏感操作应该重新验证

2. **密码安全**

   - 使用 BCrypt 进行密码加密
   - 强制要求密码复杂度
   - 实施密码重置机制

3. **API 访问控制**

   - 对敏感 API 进行速率限制
   - 记录访问日志
   - 实施 IP 白名单机制

4. **权限最小化原则**
   - 用户默认为普通用户角色
   - 管理员权限谨慎分配
   - 定期审查用户权限

## 使用示例

### 1. 用户登录并发布帖子

```javascript
// 登录
const loginResponse = await fetch("/api/auth/login", {
  method: "POST",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify({
    username: "testuser",
    password: "password123",
  }),
});

const { token } = await loginResponse.json();

// 发布帖子
const postResponse = await fetch("/api/posts", {
  method: "POST",
  headers: {
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`,
  },
  body: JSON.stringify({
    title: "我的第一篇帖子",
    content: "这是帖子内容",
    categoryId: 1,
  }),
});
```

### 2. 管理员管理用户

```javascript
// 获取用户列表（需要版主权限）
const usersResponse = await fetch("/api/admin/users", {
  headers: {
    Authorization: `Bearer ${adminToken}`,
  },
});

// 更改用户角色（需要管理员权限）
const roleResponse = await fetch("/api/admin/users/123/role", {
  method: "PUT",
  headers: {
    Authorization: `Bearer ${adminToken}`,
  },
  body: "MODERATOR",
});
```

## 故障排除

### 常见问题

1. **401 Unauthorized**

   - 检查 Token 是否正确
   - 检查 Token 是否过期
   - 检查 Authorization 头格式

2. **403 Forbidden**

   - 检查用户角色权限
   - 检查资源所有权
   - 检查业务逻辑权限

3. **Token 过期**
   - 客户端需要重新登录
   - 实施 Token 刷新机制

### 调试技巧

1. 开启 Spring Security 调试日志：

```yaml
logging:
  level:
    org.springframework.security: DEBUG
```

2. 查看 JWT Token 内容：

```bash
# 使用在线JWT解码器或命令行工具
echo "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." | base64 -d
```

## 总结

本权限验证系统提供了全面的安全控制机制，通过多层次的权限验证确保系统安全。开发人员在添加新功能时，应该：

1. 明确功能的权限要求
2. 选择合适的权限验证方式
3. 进行充分的权限测试
4. 遵循安全最佳实践
