package com.forum.config;

import com.forum.entity.User;
import com.forum.entity.Category;
import com.forum.entity.Post;
import com.forum.entity.Comment;
import com.forum.repository.UserRepository;
import com.forum.repository.CategoryRepository;
import com.forum.repository.PostRepository;
import com.forum.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
@Transactional
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 如果数据库中已有数据，则不再初始化
        if (userRepository.count() > 0) {
            return;
        }

        // 创建测试用户
        createTestUsers();
        
        // 创建论坛分类
        createCategories();
        
        // 创建测试帖子
        createTestPosts();
        
        // 创建测试评论
        createTestComments();
        
        System.out.println("测试数据初始化完成！");
    }

    private void createTestUsers() {
        // 管理员用户
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@forum.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(User.Role.ADMIN);
        admin.setBio("论坛管理员");
        userRepository.save(admin);

        // 版主用户
        User moderator = new User();
        moderator.setUsername("moderator");
        moderator.setEmail("moderator@forum.com");
        moderator.setPassword(passwordEncoder.encode("mod123"));
        moderator.setRole(User.Role.MODERATOR);
        moderator.setBio("论坛版主");
        userRepository.save(moderator);

        // 普通用户
        List<String[]> users = Arrays.asList(
            new String[]{"张小明", "zhangsan@example.com", "张小明是一位技术爱好者"},
            new String[]{"李小华", "lisi@example.com", "李小华热爱编程和技术分享"},
            new String[]{"王小军", "wangwu@example.com", "王小军是一位前端开发工程师"},
            new String[]{"赵小刚", "zhaoliu@example.com", "赵小刚专注于后端开发"},
            new String[]{"钱小峰", "qianqi@example.com", "钱小峰是一位全栈开发者"},
            new String[]{"孙小龙", "sunba@example.com", "孙小龙对人工智能很感兴趣"},
            new String[]{"周小雨", "zhoujiu@example.com", "周小雨是一位产品经理"},
            new String[]{"吴小文", "wushi@example.com", "吴小文喜欢分享技术心得"}
        );

        for (String[] userData : users) {
            User user = new User();
            user.setUsername(userData[0]);
            user.setEmail(userData[1]);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRole(User.Role.USER);
            user.setBio(userData[2]);
            userRepository.save(user);
        }
    }

    private void createCategories() {
        List<String[]> categories = Arrays.asList(
            new String[]{"技术讨论", "分享和讨论各种技术话题", "#007bff"},
            new String[]{"前端开发", "HTML、CSS、JavaScript、Vue、React等前端技术", "#28a745"},
            new String[]{"后端开发", "Java、Python、Node.js、数据库等后端技术", "#dc3545"},
            new String[]{"移动开发", "Android、iOS、Flutter、React Native等移动开发", "#ffc107"},
            new String[]{"人工智能", "机器学习、深度学习、AI算法等", "#6f42c1"},
            new String[]{"产品设计", "UI/UX设计、产品规划、用户体验等", "#fd7e14"},
            new String[]{"职业发展", "求职面试、职业规划、技能提升等", "#20c997"},
            new String[]{"开源项目", "开源项目分享、协作开发等", "#6c757d"},
            new String[]{"闲聊灌水", "日常聊天、生活分享等", "#17a2b8"}
        );

        for (String[] categoryData : categories) {
            Category category = new Category();
            category.setName(categoryData[0]);
            category.setDescription(categoryData[1]);
            category.setColor(categoryData[2]);
            categoryRepository.save(category);
        }
    }

    private void createTestPosts() {
        List<User> users = userRepository.findAll();
        List<Category> categories = categoryRepository.findAll();

        // 技术讨论帖子
        createPost("Vue 3.0 新特性详解", 
            "Vue 3.0 带来了许多激动人心的新特性，包括 Composition API、更好的性能、更小的包体积等。\n\n" +
            "### 主要新特性\n" +
            "1. **Composition API**: 提供了更灵活的组件逻辑组织方式\n" +
            "2. **性能优化**: 更快的渲染和更小的包体积\n" +
            "3. **TypeScript支持**: 原生 TypeScript 支持\n" +
            "4. **新的响应式系统**: 基于 Proxy 的响应式系统\n\n" +
            "大家对 Vue 3.0 有什么看法吗？", 
            findUserByUsername(users, "张小明"), 
            findCategoryByName(categories, "前端开发"));

        createPost("Spring Boot 微服务架构实践", 
            "最近在公司项目中使用Spring Boot构建微服务架构，想和大家分享一下经验。\n\n" +
            "### 技术栈\n" +
            "- Spring Boot 3.2\n" +
            "- Spring Cloud Gateway\n" +
            "- MySQL + Redis\n" +
            "- Docker容器化\n\n" +
            "### 遇到的问题\n" +
            "1. 服务间通信的性能问题\n" +
            "2. 分布式事务处理\n" +
            "3. 配置管理的复杂性\n\n" +
            "有经验的朋友可以分享一下最佳实践吗？", 
            findUserByUsername(users, "赵小刚"), 
            findCategoryByName(categories, "后端开发"));

        createPost("机器学习入门指南", 
            "准备学习机器学习，整理了一些学习资源和路线图。\n\n" +
            "### 学习路线\n" +
            "1. **数学基础**: 线性代数、概率论、统计学\n" +
            "2. **编程基础**: Python、NumPy、Pandas\n" +
            "3. **机器学习基础**: 监督学习、无监督学习\n" +
            "4. **深度学习**: 神经网络、CNN、RNN\n" +
            "5. **实践项目**: Kaggle竞赛、开源项目\n\n" +
            "### 推荐资源\n" +
            "- 《统计学习方法》 - 李航\n" +
            "- 《机器学习》 - 周志华\n" +
            "- Coursera的机器学习课程\n\n" +
            "新手求指导，有什么建议吗？", 
            findUserByUsername(users, "孙小龙"), 
            findCategoryByName(categories, "人工智能"));

        createPost("React Hooks 最佳实践", 
            "React Hooks 改变了我们编写 React 组件的方式，分享一些使用心得。\n\n" +
            "### 常用Hooks\n" +
            "1. **useState**: 状态管理\n" +
            "2. **useEffect**: 副作用处理\n" +
            "3. **useContext**: 上下文共享\n" +
            "4. **useReducer**: 复杂状态管理\n" +
            "5. **useMemo**: 性能优化\n\n" +
            "### 注意事项\n" +
            "- 遵循Hooks规则\n" +
            "- 合理使用依赖数组\n" +
            "- 避免过度优化\n\n" +
            "大家在使用Hooks时有什么经验分享吗？", 
            findUserByUsername(users, "王小军"), 
            findCategoryByName(categories, "前端开发"));

        createPost("Flutter vs React Native 对比", 
            "最近在选择跨平台开发框架，对比了Flutter和React Native。\n\n" +
            "### Flutter优势\n" +
            "- 性能接近原生\n" +
            "- 丰富的UI组件\n" +
            "- 单一代码库\n" +
            "- Google支持\n\n" +
            "### React Native优势\n" +
            "- JavaScript生态\n" +
            "- 社区成熟\n" +
            "- 热更新支持\n" +
            "- Facebook支持\n\n" +
            "有使用经验的朋友能给点建议吗？", 
            findUserByUsername(users, "李小华"), 
            findCategoryByName(categories, "移动开发"));

        createPost("产品经理的一天", 
            "作为一名产品经理，想和大家分享一下日常工作。\n\n" +
            "### 日常工作内容\n" +
            "- 需求分析和梳理\n" +
            "- 产品规划和设计\n" +
            "- 跨部门沟通协调\n" +
            "- 数据分析和优化\n" +
            "- 用户调研和反馈\n\n" +
            "### 挑战与思考\n" +
            "产品经理需要在技术、商业和用户之间找到平衡，这是一个很有挑战性的工作。\n\n" +
            "同行们有什么经验可以分享吗？", 
            findUserByUsername(users, "周小雨"), 
            findCategoryByName(categories, "产品设计"));

        createPost("程序员职业发展规划", 
            "工作3年了，想和大家聊聊程序员的职业发展。\n\n" +
            "### 发展路径\n" +
            "1. **技术路线**: 高级工程师 → 架构师 → 技术专家\n" +
            "2. **管理路线**: 团队Lead → 技术经理 → 技术总监\n" +
            "3. **产品路线**: 产品经理 → 产品专家 → 产品总监\n" +
            "4. **创业路线**: 技术合伙人 → 创业者\n\n" +
            "### 技能建议\n" +
            "- 扎实的技术基础\n" +
            "- 良好的沟通能力\n" +
            "- 持续学习的能力\n" +
            "- 业务理解能力\n\n" +
            "大家是怎么规划自己的职业发展的？", 
            findUserByUsername(users, "钱小峰"), 
            findCategoryByName(categories, "职业发展"));

        createPost("开源项目推荐", 
            "推荐一些优秀的开源项目，供大家学习参考。\n\n" +
            "### 前端项目\n" +
            "- Vue.js: 渐进式JavaScript框架\n" +
            "- React: 用于构建用户界面的JavaScript库\n" +
            "- Element Plus: Vue 3.0的桌面端组件库\n\n" +
            "### 后端项目\n" +
            "- Spring Boot: Java微服务框架\n" +
            "- Express.js: Node.js Web应用框架\n" +
            "- Django: Python Web框架\n\n" +
            "### 工具项目\n" +
            "- VS Code: 开源代码编辑器\n" +
            "- Git: 版本控制系统\n" +
            "- Docker: 容器化平台\n\n" +
            "大家还知道哪些优秀的开源项目？", 
            findUserByUsername(users, "吴小文"), 
            findCategoryByName(categories, "开源项目"));

        // 创建一个置顶帖子
        Post pinnedPost = createPost("论坛使用指南", 
            "欢迎来到技术论坛！这里是一个技术交流和分享的平台。\n\n" +
            "### 论坛规则\n" +
            "1. 保持友善和尊重\n" +
            "2. 发布有价值的内容\n" +
            "3. 遵守法律法规\n" +
            "4. 不发布广告和垃圾信息\n\n" +
            "### 功能介绍\n" +
            "- 发布技术文章和讨论\n" +
            "- 回复和评论\n" +
            "- 分类浏览\n" +
            "- 搜索功能\n\n" +
            "### 联系我们\n" +
            "如有问题或建议，请联系管理员。\n\n" +
            "祝大家使用愉快！", 
            findUserByUsername(users, "admin"), 
            findCategoryByName(categories, "技术讨论"));
        
        pinnedPost.setPinned(true);
        postRepository.save(pinnedPost);
    }

    private void createTestComments() {
        List<Post> posts = postRepository.findAll();
        List<User> users = userRepository.findAll();

        // 为每个帖子创建一些评论
        for (Post post : posts) {
            // 获取帖子作者
            User postAuthor = post.getAuthor();
            
            // 创建主要评论
            Comment comment1 = new Comment();
            comment1.setContent("很好的分享！学到了很多东西。");
            comment1.setPost(post);
            User comment1Author = getRandomUser(users);
            comment1.setAuthor(comment1Author);
            commentRepository.save(comment1);

            Comment comment2 = new Comment();
            comment2.setContent("感谢分享，对我很有帮助。有个问题想请教一下...");
            comment2.setPost(post);
            User comment2Author = getRandomUser(users);
            comment2.setAuthor(comment2Author);
            commentRepository.save(comment2);

            Comment comment3 = new Comment();
            comment3.setContent("我也遇到过类似的问题，楼主的解决方案很不错。");
            comment3.setPost(post);
            User comment3Author = getRandomUser(users);
            comment3.setAuthor(comment3Author);
            commentRepository.save(comment3);

            // 创建回复评论
            Comment reply1 = new Comment();
            reply1.setContent("@" + comment2Author.getUsername() + " 可以详细说一下你遇到的问题吗？");
            reply1.setPost(post);
            reply1.setAuthor(postAuthor);
            reply1.setParent(comment2);
            commentRepository.save(reply1);

            Comment reply2 = new Comment();
            reply2.setContent("@" + postAuthor.getUsername() + " 谢谢回复！我的问题是...");
            reply2.setPost(post);
            reply2.setAuthor(comment2Author);
            reply2.setParent(comment2);
            commentRepository.save(reply2);
        }
    }

    private Post createPost(String title, String content, User author, Category category) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(author);
        post.setCategory(category);
        post.setViewCount((int) (Math.random() * 500) + 10);
        post.setLikeCount((int) (Math.random() * 50) + 1);
        return postRepository.save(post);
    }

    private User findUserByUsername(List<User> users, String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(users.get(0));
    }

    private Category findCategoryByName(List<Category> categories, String name) {
        return categories.stream()
                .filter(category -> category.getName().equals(name))
                .findFirst()
                .orElse(categories.get(0));
    }

    private User getRandomUser(List<User> users) {
        int randomIndex = (int) (Math.random() * users.size());
        return users.get(randomIndex);
    }
} 