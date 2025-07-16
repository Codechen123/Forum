-- 论坛测试数据 SQL 脚本
-- 使用前请确保数据库已创建：CREATE DATABASE IF NOT EXISTS forum_db;

USE forum_db;

-- 清空现有数据（可选）
-- SET FOREIGN_KEY_CHECKS = 0;
-- TRUNCATE TABLE comments;
-- TRUNCATE TABLE posts;
-- TRUNCATE TABLE categories;
-- TRUNCATE TABLE users;
-- SET FOREIGN_KEY_CHECKS = 1;

-- 插入用户数据
INSERT INTO users (username, email, password, bio, role, created_at, updated_at) VALUES
('admin', 'admin@forum.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P8jSkKBbZTRTyG', '论坛管理员', 'ADMIN', NOW(), NOW()),
('moderator', 'moderator@forum.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P8jSkKBbZTRTyG', '论坛版主', 'MODERATOR', NOW(), NOW()),
('张小明', 'zhangsan@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P8jSkKBbZTRTyG', '张小明是一位技术爱好者', 'USER', NOW(), NOW()),
('李小华', 'lisi@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P8jSkKBbZTRTyG', '李小华热爱编程和技术分享', 'USER', NOW(), NOW()),
('王小军', 'wangwu@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P8jSkKBbZTRTyG', '王小军是一位前端开发工程师', 'USER', NOW(), NOW()),
('赵小刚', 'zhaoliu@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P8jSkKBbZTRTyG', '赵小刚专注于后端开发', 'USER', NOW(), NOW()),
('钱小峰', 'qianqi@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P8jSkKBbZTRTyG', '钱小峰是一位全栈开发者', 'USER', NOW(), NOW()),
('孙小龙', 'sunba@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P8jSkKBbZTRTyG', '孙小龙对人工智能很感兴趣', 'USER', NOW(), NOW()),
('周小雨', 'zhoujiu@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P8jSkKBbZTRTyG', '周小雨是一位产品经理', 'USER', NOW(), NOW()),
('吴小文', 'wushi@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P8jSkKBbZTRTyG', '吴小文喜欢分享技术心得', 'USER', NOW(), NOW());

-- 插入分类数据
INSERT INTO categories (name, description, color, created_at, updated_at) VALUES
('技术讨论', '分享和讨论各种技术话题', '#007bff', NOW(), NOW()),
('前端开发', 'HTML、CSS、JavaScript、Vue、React等前端技术', '#28a745', NOW(), NOW()),
('后端开发', 'Java、Python、Node.js、数据库等后端技术', '#dc3545', NOW(), NOW()),
('移动开发', 'Android、iOS、Flutter、React Native等移动开发', '#ffc107', NOW(), NOW()),
('人工智能', '机器学习、深度学习、AI算法等', '#6f42c1', NOW(), NOW()),
('产品设计', 'UI/UX设计、产品规划、用户体验等', '#fd7e14', NOW(), NOW()),
('职业发展', '求职面试、职业规划、技能提升等', '#20c997', NOW(), NOW()),
('开源项目', '开源项目分享、协作开发等', '#6c757d', NOW(), NOW()),
('闲聊灌水', '日常聊天、生活分享等', '#17a2b8', NOW(), NOW());

-- 插入帖子数据
INSERT INTO posts (title, content, author_id, category_id, view_count, like_count, pinned, status, created_at, updated_at) VALUES
('论坛使用指南', 
'欢迎来到技术论坛！这里是一个技术交流和分享的平台。

### 论坛规则
1. 保持友善和尊重
2. 发布有价值的内容
3. 遵守法律法规
4. 不发布广告和垃圾信息

### 功能介绍
- 发布技术文章和讨论
- 回复和评论
- 分类浏览
- 搜索功能

### 联系我们
如有问题或建议，请联系管理员。

祝大家使用愉快！', 
1, 1, 1250, 45, 1, 'PUBLISHED', NOW(), NOW()),

('Vue 3.0 新特性详解', 
'Vue 3.0 带来了许多激动人心的新特性，包括 Composition API、更好的性能、更小的包体积等。

### 主要新特性
1. **Composition API**: 提供了更灵活的组件逻辑组织方式
2. **性能优化**: 更快的渲染和更小的包体积
3. **TypeScript支持**: 原生 TypeScript 支持
4. **新的响应式系统**: 基于 Proxy 的响应式系统

大家对 Vue 3.0 有什么看法吗？', 
3, 2, 320, 28, 0, 'PUBLISHED', NOW(), NOW()),

('Spring Boot 微服务架构实践', 
'最近在公司项目中使用Spring Boot构建微服务架构，想和大家分享一下经验。

### 技术栈
- Spring Boot 3.2
- Spring Cloud Gateway
- MySQL + Redis
- Docker容器化

### 遇到的问题
1. 服务间通信的性能问题
2. 分布式事务处理
3. 配置管理的复杂性

有经验的朋友可以分享一下最佳实践吗？', 
6, 3, 428, 32, 0, 'PUBLISHED', NOW(), NOW()),

('机器学习入门指南', 
'准备学习机器学习，整理了一些学习资源和路线图。

### 学习路线
1. **数学基础**: 线性代数、概率论、统计学
2. **编程基础**: Python、NumPy、Pandas
3. **机器学习基础**: 监督学习、无监督学习
4. **深度学习**: 神经网络、CNN、RNN
5. **实践项目**: Kaggle竞赛、开源项目

### 推荐资源
- 《统计学习方法》 - 李航
- 《机器学习》 - 周志华
- Coursera的机器学习课程

新手求指导，有什么建议吗？', 
8, 5, 267, 19, 0, 'PUBLISHED', NOW(), NOW()),

('React Hooks 最佳实践', 
'React Hooks 改变了我们编写 React 组件的方式，分享一些使用心得。

### 常用Hooks
1. **useState**: 状态管理
2. **useEffect**: 副作用处理
3. **useContext**: 上下文共享
4. **useReducer**: 复杂状态管理
5. **useMemo**: 性能优化

### 注意事项
- 遵循Hooks规则
- 合理使用依赖数组
- 避免过度优化

大家在使用Hooks时有什么经验分享吗？', 
5, 2, 189, 24, 0, 'PUBLISHED', NOW(), NOW()),

('Flutter vs React Native 对比', 
'最近在选择跨平台开发框架，对比了Flutter和React Native。

### Flutter优势
- 性能接近原生
- 丰富的UI组件
- 单一代码库
- Google支持

### React Native优势
- JavaScript生态
- 社区成熟
- 热更新支持
- Facebook支持

有使用经验的朋友能给点建议吗？', 
4, 4, 356, 31, 0, 'PUBLISHED', NOW(), NOW()),

('产品经理的一天', 
'作为一名产品经理，想和大家分享一下日常工作。

### 日常工作内容
- 需求分析和梳理
- 产品规划和设计
- 跨部门沟通协调
- 数据分析和优化
- 用户调研和反馈

### 挑战与思考
产品经理需要在技术、商业和用户之间找到平衡，这是一个很有挑战性的工作。

同行们有什么经验可以分享吗？', 
9, 6, 203, 16, 0, 'PUBLISHED', NOW(), NOW()),

('程序员职业发展规划', 
'工作3年了，想和大家聊聊程序员的职业发展。

### 发展路径
1. **技术路线**: 高级工程师 → 架构师 → 技术专家
2. **管理路线**: 团队Lead → 技术经理 → 技术总监
3. **产品路线**: 产品经理 → 产品专家 → 产品总监
4. **创业路线**: 技术合伙人 → 创业者

### 技能建议
- 扎实的技术基础
- 良好的沟通能力
- 持续学习的能力
- 业务理解能力

大家是怎么规划自己的职业发展的？', 
7, 7, 445, 38, 0, 'PUBLISHED', NOW(), NOW()),

('开源项目推荐', 
'推荐一些优秀的开源项目，供大家学习参考。

### 前端项目
- Vue.js: 渐进式JavaScript框架
- React: 用于构建用户界面的JavaScript库
- Element Plus: Vue 3.0的桌面端组件库

### 后端项目
- Spring Boot: Java微服务框架
- Express.js: Node.js Web应用框架
- Django: Python Web框架

### 工具项目
- VS Code: 开源代码编辑器
- Git: 版本控制系统
- Docker: 容器化平台

大家还知道哪些优秀的开源项目？', 
10, 8, 298, 22, 0, 'PUBLISHED', NOW(), NOW());

-- 插入评论数据
INSERT INTO comments (content, post_id, author_id, parent_id, like_count, created_at, updated_at) VALUES
-- 论坛使用指南的评论
('很好的介绍！新手必看。', 1, 3, NULL, 5, NOW(), NOW()),
('感谢管理员的详细说明。', 1, 4, NULL, 3, NOW(), NOW()),
('规则很清楚，会遵守的。', 1, 5, NULL, 2, NOW()),
('@张三 是的，这个指南很实用。', 1, 1, 1, 1, NOW(), NOW()),
('@admin 谢谢！', 1, 3, 1, 0, NOW(), NOW()),

-- Vue 3.0 新特性详解的评论
('Composition API确实很强大！', 2, 5, NULL, 8, NOW(), NOW()),
('性能提升很明显，升级后页面加载快了很多。', 2, 6, NULL, 6, NOW(), NOW()),
('TypeScript支持让开发体验更好了。', 2, 7, NULL, 4, NOW(), NOW()),
('@王五 你在项目中用过Composition API吗？', 2, 3, 6, 2, NOW(), NOW()),
('@张三 用过，特别适合复杂组件的逻辑组织。', 2, 5, 6, 1, NOW(), NOW()),

-- Spring Boot 微服务架构实践的评论
('分布式事务确实是个难点。', 3, 7, NULL, 12, NOW(), NOW()),
('推荐使用Saga模式处理分布式事务。', 3, 2, NULL, 8, NOW(), NOW()),
('配置管理可以用Spring Cloud Config。', 3, 8, NULL, 6, NOW(), NOW()),
('@钱七 Saga模式具体怎么实现？', 3, 6, 8, 3, NOW(), NOW()),
('@赵六 可以参考我之前写的文章。', 3, 7, 8, 1, NOW(), NOW()),

-- 机器学习入门指南的评论
('学习路线很清晰，收藏了！', 4, 9, NULL, 15, NOW(), NOW()),
('推荐先学好Python基础。', 4, 10, NULL, 10, NOW(), NOW()),
('数学基础确实很重要。', 4, 3, NULL, 8, NOW(), NOW()),
('@周九 有什么好的数学教材推荐吗？', 4, 8, 16, 4, NOW(), NOW()),
('@孙八 可以看看《概率论与数理统计》。', 4, 9, 16, 2, NOW(), NOW()),

-- React Hooks 最佳实践的评论
('useEffect的依赖数组总是容易出错。', 5, 4, NULL, 11, NOW(), NOW()),
('useMemo要谨慎使用，不要过度优化。', 5, 6, NULL, 9, NOW(), NOW()),
('自定义Hook很好用！', 5, 7, NULL, 7, NOW(), NOW()),
('@李四 确实，eslint-plugin-react-hooks很有用。', 5, 5, 20, 5, NOW(), NOW()),
('@王五 自定义Hook有什么好的示例？', 5, 4, 20, 2, NOW(), NOW()),

-- Flutter vs React Native 对比的评论
('Flutter的性能确实更好。', 6, 8, NULL, 13, NOW(), NOW()),
('RN的生态更成熟一些。', 6, 9, NULL, 11, NOW(), NOW()),
('看项目需求，各有优势。', 6, 10, NULL, 8, NOW(), NOW()),
('@孙八 Flutter学习成本怎么样？', 6, 4, 25, 6, NOW(), NOW()),
('@李四 有Dart基础的话比较容易上手。', 6, 8, 25, 3, NOW(), NOW()),

-- 产品经理的一天的评论
('产品经理真的很不容易！', 7, 3, NULL, 9, NOW(), NOW()),
('跨部门沟通确实是个挑战。', 7, 6, NULL, 7, NOW(), NOW()),
('用户调研很重要。', 7, 8, NULL, 5, NOW(), NOW()),
('@张三 怎么平衡各方需求？', 7, 9, 30, 4, NOW(), NOW()),
('@周九 需要很强的沟通协调能力。', 7, 3, 30, 2, NOW(), NOW()),

-- 程序员职业发展规划的评论
('技术路线和管理路线各有优势。', 8, 4, NULL, 14, NOW(), NOW()),
('持续学习很重要！', 8, 5, NULL, 12, NOW(), NOW()),
('业务理解能力容易被忽视。', 8, 2, NULL, 10, NOW(), NOW()),
('@李四 你选择哪个方向？', 8, 7, 35, 7, NOW(), NOW()),
('@钱七 我倾向于技术路线。', 8, 4, 35, 4, NOW(), NOW()),

-- 开源项目推荐的评论
('这些项目都很经典！', 9, 3, NULL, 16, NOW(), NOW()),
('VS Code是最好的编辑器。', 9, 5, NULL, 13, NOW(), NOW()),
('Docker真的改变了开发方式。', 9, 6, NULL, 11, NOW(), NOW()),
('@张三 还有什么好的工具推荐？', 9, 10, 40, 8, NOW(), NOW()),
('@吴十 推荐Postman做API测试。', 9, 3, 40, 5, NOW(), NOW());

-- 查询验证数据
SELECT '=== 数据统计 ===' as info;
SELECT '用户数量' as type, COUNT(*) as count FROM users;
SELECT '分类数量' as type, COUNT(*) as count FROM categories;
SELECT '帖子数量' as type, COUNT(*) as count FROM posts;
SELECT '评论数量' as type, COUNT(*) as count FROM comments;
