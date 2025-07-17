-- 论坛测试数据 SQL 脚本 (H2 Database Compatible)

-- 插入用户数据
INSERT INTO users (username, email, password, bio, role, created_at, updated_at) VALUES
('admin', 'admin@forum.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P8jSkKBbZTRTyG', '论坛管理员', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('moderator', 'moderator@forum.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P8jSkKBbZTRTyG', '论坛版主', 'MODERATOR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('张小明', 'zhangsan@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P8jSkKBbZTRTyG', '张小明是一位技术爱好者', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('李小华', 'lisi@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P8jSkKBbZTRTyG', '李小华热爱编程和技术分享', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('王小军', 'wangwu@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P8jSkKBbZTRTyG', '王小军是一位前端开发工程师', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('赵小刚', 'zhaoliu@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P8jSkKBbZTRTyG', '赵小刚专注于后端开发', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('钱小峰', 'qianqi@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P8jSkKBbZTRTyG', '钱小峰是一位全栈开发者', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('孙小龙', 'sunba@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P8jSkKBbZTRTyG', '孙小龙对人工智能很感兴趣', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('周小雨', 'zhoujiu@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P8jSkKBbZTRTyG', '周小雨是一位产品经理', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('吴小文', 'wushi@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P8jSkKBbZTRTyG', '吴小文喜欢分享技术心得', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 插入分类数据
INSERT INTO categories (name, description, color, created_at, updated_at) VALUES
('技术讨论', '分享和讨论各种技术话题', '#007bff', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('前端开发', 'HTML、CSS、JavaScript、Vue、React等前端技术', '#28a745', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('后端开发', 'Java、Python、Node.js、数据库等后端技术', '#dc3545', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('移动开发', 'Android、iOS、Flutter、React Native等移动开发', '#ffc107', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('人工智能', '机器学习、深度学习、AI算法等', '#6f42c1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('产品设计', 'UI/UX设计、产品规划、用户体验等', '#fd7e14', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('职业发展', '求职面试、职业规划、技能提升等', '#20c997', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('开源项目', '开源项目分享、协作开发等', '#6c757d', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('闲聊灌水', '日常聊天、生活分享等', '#17a2b8', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

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
- 发布帖子和回复
- 点赞和收藏
- 用户个人中心
- 分类浏览

感谢大家的支持！', 1, 1, 150, 25, true, 'PUBLISHED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Vue 3 Composition API 详解', 
'Vue 3 引入了 Composition API，这是一个全新的 API 风格，让我们可以更好地组织组件逻辑。

## 什么是 Composition API？
Composition API 是一组低级 API，允许我们使用导入的函数而不是声明选项来编写组件。

## 基本用法
```javascript
import { ref, reactive, computed, onMounted } from ''vue''

export default {
  setup() {
    const count = ref(0)
    const increment = () => count.value++
    
    return {
      count,
      increment
    }
  }
}
```

## 优势
1. 更好的类型推断
2. 更好的代码组织
3. 更好的逻辑复用

欢迎大家讨论！', 3, 2, 89, 15, false, 'PUBLISHED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Spring Boot 微服务架构实践', 
'在现代软件开发中，微服务架构已经成为主流。Spring Boot 为微服务开发提供了强大的支持。

## 微服务架构的优势
1. 独立部署
2. 技术多样性
3. 故障隔离
4. 扩展性好

## Spring Boot 微服务技术栈
- Spring Cloud Gateway：API 网关
- Spring Cloud Config：配置中心
- Spring Cloud Netflix：服务发现
- Spring Cloud Sleuth：链路追踪

## 实践建议
1. 合理划分服务边界
2. 做好服务监控
3. 处理分布式事务
4. 考虑数据一致性

期待大家的经验分享！', 6, 3, 120, 20, false, 'PUBLISHED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('React Native 开发心得', 
'React Native 让我们可以使用 React 开发原生移动应用。分享一些开发心得。

## 环境搭建
1. 安装 Node.js
2. 安装 React Native CLI
3. 配置 Android/iOS 开发环境

## 性能优化
- 使用 FlatList 替代 ScrollView
- 避免在 render 中创建新对象
- 使用 memo 和 useMemo
- 图片优化

## 常见问题
1. 热重载失效
2. 第三方库兼容性
3. 原生模块集成

有问题欢迎讨论！', 5, 4, 75, 12, false, 'PUBLISHED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('机器学习入门指南', 
'机器学习是人工智能的核心技术之一。本文为初学者提供一个入门指南。

## 基础概念
- 监督学习：有标签的数据训练
- 无监督学习：无标签的数据训练
- 强化学习：通过奖励机制学习

## 常用算法
1. 线性回归
2. 逻辑回归
3. 决策树
4. 随机森林
5. 支持向量机
6. 神经网络

## 学习路径
1. 数学基础：线性代数、概率统计
2. 编程语言：Python、R
3. 框架工具：TensorFlow、PyTorch
4. 实战项目：Kaggle 竞赛

加油！', 8, 5, 200, 35, false, 'PUBLISHED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('UI设计趋势分析', 
'2024年UI设计有哪些新趋势？让我们一起来分析。

## 主要趋势
1. 极简主义继续流行
2. 暗黑模式成为标配
3. 3D元素增多
4. 微交互更加精细
5. 可访问性设计受重视

## 色彩趋势
- 渐变色彩
- 柔和色调
- 高对比度
- 品牌色彩

## 设计工具
- Figma：协作设计
- Sketch：Mac平台首选
- Adobe XD：Adobe生态
- Principle：原型动画

期待设计师们的见解！', 9, 6, 95, 18, false, 'PUBLISHED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('程序员职业发展路径', 
'作为程序员，如何规划自己的职业发展？分享一些思考。

## 技术路线
1. 初级开发者：掌握基础技能
2. 中级开发者：深入某个领域
3. 高级开发者：技术专家
4. 架构师：系统设计能力

## 管理路线
1. Team Leader：带领小团队
2. 项目经理：项目管理
3. 技术经理：技术+管理
4. CTO：技术战略

## 能力提升
- 技术能力：持续学习新技术
- 沟通能力：与团队协作
- 业务理解：理解产品和业务
- 领导能力：影响和激励他人

大家觉得呢？', 4, 7, 180, 28, false, 'PUBLISHED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('开源项目推荐', 
'推荐一些优秀的开源项目，希望对大家有帮助。

## 前端项目
- Vue.js：渐进式框架
- React：用户界面库
- Angular：企业级框架
- Webpack：模块打包器

## 后端项目
- Spring Boot：Java微服务
- Express.js：Node.js框架
- Django：Python Web框架
- Laravel：PHP框架

## 工具项目
- VS Code：代码编辑器
- Git：版本控制
- Docker：容器化
- Kubernetes：容器编排

## 参与开源
1. 阅读文档
2. 提交Issue
3. 贡献代码
4. 维护项目

一起为开源做贡献！', 7, 8, 110, 22, false, 'PUBLISHED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('今天的学习心得', 
'今天学习了一些新知识，记录一下。

## 学习内容
- 复习了数据结构中的树结构
- 学习了Redis的持久化机制
- 看了一些系统设计的文章

## 收获
1. 对二叉树的遍历更加熟练
2. 理解了RDB和AOF的区别
3. 学会了如何设计可扩展的系统

## 明天计划
- 继续学习算法
- 实践Redis集群
- 阅读设计模式

坚持学习，每天进步一点点！', 10, 9, 45, 8, false, 'PUBLISHED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 插入评论数据
INSERT INTO comments (content, author_id, post_id, parent_id, like_count, created_at, updated_at) VALUES
('感谢分享！这个指南很有用。', 3, 1, NULL, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('论坛界面很清爽，点赞！', 4, 1, NULL, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('期待更多功能的加入。', 5, 1, NULL, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('同意楼主的观点。', 6, 1, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Composition API确实很强大！', 4, 2, NULL, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('相比Options API，逻辑更清晰了。', 5, 2, NULL, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('TypeScript支持更好了。', 6, 2, NULL, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('有没有完整的项目示例？', 7, 2, NULL, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('我也在学习Vue 3，一起加油！', 8, 2, 5, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('微服务架构确实是趋势。', 3, 3, NULL, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Spring Cloud的生态很完善。', 4, 3, NULL, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('分布式事务处理很重要。', 5, 3, NULL, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('服务监控怎么做比较好？', 7, 3, NULL, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('推荐使用Prometheus + Grafana。', 6, 3, 12, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('React Native开发确实有些坑。', 3, 4, NULL, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('性能优化很重要。', 6, 4, NULL, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('热重载经常出问题。', 7, 4, NULL, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('第三方库兼容性需要注意。', 8, 4, NULL, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('机器学习入门确实需要数学基础。', 3, 5, NULL, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Python是最好的选择。', 4, 5, NULL, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('推荐Andrew Ng的课程。', 5, 5, NULL, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('实战项目很重要。', 6, 5, NULL, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Kaggle是个好平台。', 7, 5, 19, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('设计趋势分析很到位。', 3, 6, NULL, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('暗黑模式确实很流行。', 4, 6, NULL, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Figma协作功能很强大。', 5, 6, NULL, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('可访问性设计很重要。', 6, 6, NULL, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('职业规划很重要。', 3, 7, NULL, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('技术和管理都要考虑。', 5, 7, NULL, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('沟通能力确实很重要。', 6, 7, NULL, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('持续学习是关键。', 7, 7, NULL, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('开源项目推荐很棒！', 3, 8, NULL, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Vue.js确实很好用。', 4, 8, NULL, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Docker容器化很方便。', 5, 8, NULL, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('我也想参与开源项目。', 6, 8, NULL, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('学习心得很真实。', 4, 9, NULL, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('坚持学习很重要。', 5, 9, NULL, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('每天进步一点点！', 6, 9, NULL, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
