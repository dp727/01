-- =====================================================
-- 社区二手物品信息发布领取平台 - 数据库初始化脚本
-- 数据库: community_marketplace
-- =====================================================

CREATE DATABASE IF NOT EXISTS community_marketplace
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE community_marketplace;

-- =====================================================
-- 用户表
-- =====================================================
CREATE TABLE IF NOT EXISTS users (
    id              BIGINT          PRIMARY KEY AUTO_INCREMENT,
    nickname        VARCHAR(50)     NOT NULL,
    avatar          VARCHAR(500),
    location        VARCHAR(100),
    building_number VARCHAR(20),
    latitude        DECIMAL(10, 7),
    longitude       DECIMAL(10, 7),
    created_at      DATETIME        DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- 分类表
-- =====================================================
CREATE TABLE IF NOT EXISTS categories (
    id          BIGINT          PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(50)     NOT NULL UNIQUE,
    sort_order  INT             DEFAULT 0,
    created_at  DATETIME        DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- 二手物品表
-- =====================================================
CREATE TABLE IF NOT EXISTS items (
    id           BIGINT            PRIMARY KEY AUTO_INCREMENT,
    title        VARCHAR(100)      NOT NULL,
    description  VARCHAR(2000),
    price        DECIMAL(10, 2),
    cover_image  VARCHAR(500),
    images       VARCHAR(2000),
    community    VARCHAR(100),
    latitude     DECIMAL(10, 7),
    longitude    DECIMAL(10, 7),
    status       VARCHAR(20)       NOT NULL DEFAULT 'ON_SALE',
    user_id      BIGINT            NOT NULL,
    category_id  BIGINT            NOT NULL,
    created_at   DATETIME          DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_category_id (category_id),
    INDEX idx_status_created (status, created_at DESC),
    INDEX idx_location (latitude, longitude),
    FOREIGN KEY (user_id)      REFERENCES users(id)      ON DELETE CASCADE,
    FOREIGN KEY (category_id)  REFERENCES categories(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- 收藏表
-- =====================================================
CREATE TABLE IF NOT EXISTS favorites (
    id          BIGINT      PRIMARY KEY AUTO_INCREMENT,
    user_id     BIGINT      NOT NULL,
    item_id     BIGINT      NOT NULL,
    created_at  DATETIME    DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_item (user_id, item_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- 留言/评论表
-- =====================================================
CREATE TABLE IF NOT EXISTS comments (
    id          BIGINT          PRIMARY KEY AUTO_INCREMENT,
    content     VARCHAR(1000)   NOT NULL,
    user_id     BIGINT          NOT NULL,
    item_id     BIGINT          NOT NULL,
    created_at  DATETIME        DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_item_id (item_id),
    INDEX idx_user_id (user_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- 初始化分类数据
-- =====================================================
INSERT INTO categories (name, sort_order) VALUES
    ('家用电器', 1),
    ('家具家居', 2),
    ('数码产品', 3),
    ('图书文具', 4),
    ('服饰鞋帽', 5),
    ('运动户外', 6),
    ('母婴用品', 7),
    ('其他物品', 8);

-- =====================================================
-- 初始化测试用户数据
-- =====================================================
INSERT INTO users (nickname, avatar, location, building_number, latitude, longitude) VALUES
    ('小明同学', 'https://api.dicebear.com/7.x/avataaars/svg?seed=xiaoming', '阳光花园小区', '3栋2单元', 39.9087, 116.3975),
    ('老王',      'https://api.dicebear.com/7.x/avataaars/svg?seed=laowang',   '阳光花园小区', '5栋1单元', 39.9092, 116.3982),
    ('小李子',    'https://api.dicebear.com/7.x/avataaars/svg?seed=xiaoli',    '阳光花园小区', '1栋3单元', 39.9080, 116.3968),
    ('张阿姨',    'https://api.dicebear.com/7.x/avataaars/svg?seed=zhangayi',  '阳光花园小区', '8栋2单元', 39.9095, 116.3960);

-- =====================================================
-- 初始化测试物品数据
-- =====================================================
INSERT INTO items (title, description, price, cover_image, community, latitude, longitude, status, user_id, category_id, created_at) VALUES
    ('九成新小米空气净化器', '搬家了出闲置，小米空气净化器Pro H，使用不到一年，滤芯还很新，原价2000多，现价800出，自提。', 800.00,
     'https://picsum.photos/seed/item1/400/300', '阳光花园小区', 39.9087, 116.3975, 'ON_SALE', 1, 1, DATE_SUB(NOW(), INTERVAL 2 HOUR)),
    ('实木餐桌椅一套', '1.4米实木餐桌配4把椅子，八成新，无磕碰，搬家便宜处理，原价3000买的，现在600出。', 600.00,
     'https://picsum.photos/seed/item2/400/300', '阳光花园小区', 39.9092, 116.3982, 'ON_SALE', 2, 2, DATE_SUB(NOW(), INTERVAL 5 HOUR)),
    ('iPad Air 3 64G', 'iPad Air 3 深空灰 64G WiFi版，配原装键盘和Apple Pencil，屏幕完美无划痕，电池健康92%。', 2200.00,
     'https://picsum.photos/seed/item3/400/300', '阳光花园小区', 39.9080, 116.3968, 'ON_SALE', 3, 3, DATE_SUB(NOW(), INTERVAL 8 HOUR)),
    ('二手考研教材全套', '考研政治英语数学全套教材+历年真题+模拟卷，2024版的，笔记做得很详细，学弟学妹们来捡漏。', 80.00,
     'https://picsum.photos/seed/item4/400/300', '阳光花园小区', 39.9095, 116.3960, 'ON_SALE', 4, 4, DATE_SUB(NOW(), INTERVAL 12 HOUR)),
    ('品牌羽绒服 几乎全新', '波司登男款羽绒服L码，只穿过一次，太暖和了穿不上，原价1299，现价300转让。', 300.00,
     'https://picsum.photos/seed/item5/400/300', '阳光花园小区', 39.9087, 116.3975, 'ON_SALE', 1, 5, DATE_SUB(NOW(), INTERVAL 1 DAY)),
    ('迪卡侬折叠自行车', '迪卡侬TILT 120折叠自行车，20寸，通勤买菜好帮手，骑了半年成色不错，现450出。', 450.00,
     'https://picsum.photos/seed/item6/400/300', '阳光花园小区', 39.9092, 116.3982, 'ON_SALE', 2, 6, DATE_SUB(NOW(), INTERVAL 27 HOUR)),
    ('婴儿推车 8成新', '好孩子品牌婴儿推车，可坐可躺，双向推行，宝宝大了用不上了，便宜处理。', 350.00,
     'https://picsum.photos/seed/item7/400/300', '阳光花园小区', 39.9095, 116.3960, 'ON_SALE', 4, 7, DATE_SUB(NOW(), INTERVAL 2 DAY)),
    ('飞利浦电动剃须刀', '飞利浦S5000系列电动剃须刀，全新未拆封，生日礼物多了一个，原价599现价300。', 300.00,
     'https://picsum.photos/seed/item8/400/300', '阳光花园小区', 39.9080, 116.3968, 'ON_SALE', 3, 8, DATE_SUB(NOW(), INTERVAL 54 HOUR));

-- =====================================================
-- 初始化测试收藏数据
-- =====================================================
INSERT INTO favorites (user_id, item_id) VALUES
    (1, 2), (1, 3),
    (2, 1), (2, 5),
    (3, 4), (3, 6),
    (4, 1), (4, 2);

-- =====================================================
-- 初始化测试留言数据
-- =====================================================
INSERT INTO comments (content, user_id, item_id) VALUES
    ('请问净化器还在吗？可以送到楼下吗？', 2, 1),
    ('还在的，您什么时候方便？', 1, 1),
    ('价格还能再优惠点吗？', 4, 2),
    ('iPad电池充电次数多少？', 1, 3),
    ('考研资料还在吗？我想全要~', 3, 4),
    ('羽绒服是什么颜色的呀？有更多图片吗？', 2, 5),
    ('自行车能折叠到多大？', 1, 6);
