-- 创建数据库
CREATE DATABASE IF NOT EXISTS oa_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE oa_system;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    avatar VARCHAR(255) COMMENT '头像',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    dept_id BIGINT COMMENT '部门ID',
    post_id BIGINT COMMENT '岗位ID',
    status CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
    gender CHAR(1) COMMENT '性别（1男 2女）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted CHAR(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
    INDEX idx_username (username),
    INDEX idx_dept_id (dept_id),
    INDEX idx_post_id (post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
    description VARCHAR(255) COMMENT '描述',
    status CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted CHAR(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 菜单表
CREATE TABLE IF NOT EXISTS sys_menu (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '菜单ID',
    menu_name VARCHAR(50) NOT NULL COMMENT '菜单名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父菜单ID',
    order_num INT DEFAULT 0 COMMENT '显示顺序',
    path VARCHAR(255) COMMENT '路由地址',
    component VARCHAR(255) COMMENT '组件路径',
    menu_type CHAR(1) COMMENT '菜单类型（M目录 C菜单 F按钮）',
    visible CHAR(1) DEFAULT '0' COMMENT '显示状态（0显示 1隐藏）',
    status CHAR(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
    perms VARCHAR(100) COMMENT '权限标识',
    icon VARCHAR(100) DEFAULT '#' COMMENT '菜单图标',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted CHAR(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
    INDEX idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_role (user_id, role_id),
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 角色菜单关联表
CREATE TABLE IF NOT EXISTS sys_role_menu (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_role_menu (role_id, menu_id),
    INDEX idx_role_id (role_id),
    INDEX idx_menu_id (menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- 部门表
CREATE TABLE IF NOT EXISTS sys_dept (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '部门ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父部门ID',
    dept_name VARCHAR(50) NOT NULL COMMENT '部门名称',
    leader VARCHAR(50) COMMENT '负责人',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    order_num INT DEFAULT 0 COMMENT '显示顺序',
    status CHAR(1) DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted CHAR(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
    INDEX idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 岗位表
CREATE TABLE IF NOT EXISTS sys_post (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '岗位ID',
    post_code VARCHAR(50) NOT NULL COMMENT '岗位编码',
    post_name VARCHAR(50) NOT NULL COMMENT '岗位名称',
    post_sort INT DEFAULT 0 COMMENT '显示顺序',
    status CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted CHAR(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位表';

-- 公告表
CREATE TABLE IF NOT EXISTS sys_announcement (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '公告ID',
    title VARCHAR(200) NOT NULL COMMENT '公告标题',
    content TEXT COMMENT '公告内容',
    type CHAR(1) DEFAULT '1' COMMENT '公告类型（1通知 2公告）',
    status CHAR(1) DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
    create_by BIGINT COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted CHAR(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- 个人便签表
CREATE TABLE IF NOT EXISTS sys_note (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '便签ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    title VARCHAR(100) COMMENT '标题',
    content TEXT COMMENT '内容',
    color VARCHAR(20) DEFAULT '#ffffff' COMMENT '背景颜色',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted CHAR(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='个人便签表';

-- 通讯录表
CREATE TABLE IF NOT EXISTS sys_contact (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '通讯录ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    real_name VARCHAR(50) COMMENT '姓名',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    dept_id BIGINT COMMENT '部门ID',
    dept_name VARCHAR(50) COMMENT '部门名称',
    post_id BIGINT COMMENT '岗位ID',
    post_name VARCHAR(50) COMMENT '岗位名称',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted CHAR(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
    INDEX idx_user_id (user_id),
    INDEX idx_dept_id (dept_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通讯录表';

-- 考勤表
CREATE TABLE IF NOT EXISTS attendance (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '考勤ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    attend_date DATE NOT NULL COMMENT '考勤日期',
    check_in_time DATETIME COMMENT '签到时间',
    check_out_time DATETIME COMMENT '签退时间',
    check_in_status CHAR(1) DEFAULT '0' COMMENT '签到状态（0正常 1迟到）',
    check_out_status CHAR(1) DEFAULT '0' COMMENT '签退状态（0正常 1早退）',
    status CHAR(1) DEFAULT '0' COMMENT '考勤状态（0正常 1异常）',
    remark VARCHAR(255) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted CHAR(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
    UNIQUE KEY uk_user_date (user_id, attend_date),
    INDEX idx_user_id (user_id),
    INDEX idx_attend_date (attend_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考勤表';

-- 考勤统计表
CREATE TABLE IF NOT EXISTS attendance_statistic (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '统计ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    year INT NOT NULL COMMENT '年份',
    month INT NOT NULL COMMENT '月份',
    work_days INT DEFAULT 0 COMMENT '工作日数',
    attend_days INT DEFAULT 0 COMMENT '出勤天数',
    late_days INT DEFAULT 0 COMMENT '迟到天数',
    early_days INT DEFAULT 0 COMMENT '早退天数',
    absence_days INT DEFAULT 0 COMMENT '缺勤天数',
    leave_days INT DEFAULT 0 COMMENT '请假天数',
    status CHAR(1) DEFAULT '0' COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted CHAR(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
    UNIQUE KEY uk_user_year_month (user_id, year, month),
    INDEX idx_user_id (user_id),
    INDEX idx_year_month (year, month)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考勤统计表';

-- 请假申请表
CREATE TABLE IF NOT EXISTS leave_apply (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '请假ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    leave_type CHAR(1) NOT NULL COMMENT '请假类型（1事假 2病假 3年假 4婚假 5产假）',
    start_date DATE NOT NULL COMMENT '开始日期',
    end_date DATE NOT NULL COMMENT '结束日期',
    leave_days DECIMAL(4,1) NOT NULL COMMENT '请假天数',
    reason VARCHAR(500) NOT NULL COMMENT '请假原因',
    status CHAR(1) DEFAULT '0' COMMENT '审批状态（0待审批 1已通过 2已拒绝）',
    approve_by BIGINT COMMENT '审批人ID',
    approve_comment VARCHAR(255) COMMENT '审批意见',
    approve_time DATETIME COMMENT '审批时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted CHAR(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
    INDEX idx_user_id (user_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='请假申请表';

-- 会议室表
CREATE TABLE IF NOT EXISTS meeting_room (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '会议室ID',
    room_name VARCHAR(50) NOT NULL COMMENT '会议室名称',
    room_no VARCHAR(50) COMMENT '会议室编号',
    capacity INT COMMENT '容纳人数',
    location VARCHAR(100) COMMENT '位置',
    equipment VARCHAR(500) COMMENT '设备',
    status CHAR(1) DEFAULT '0' COMMENT '状态（0可用 1占用）',
    remark VARCHAR(255) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted CHAR(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议室表';

-- 会议表
CREATE TABLE IF NOT EXISTS meeting (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '会议ID',
    meeting_name VARCHAR(100) NOT NULL COMMENT '会议名称',
    room_id BIGINT COMMENT '会议室ID',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    participants TEXT COMMENT '参会人员',
    organizer VARCHAR(50) COMMENT '组织者',
    meeting_type CHAR(1) COMMENT '会议类型',
    content TEXT COMMENT '会议内容',
    status CHAR(1) DEFAULT '0' COMMENT '状态（0未开始 1进行中 2已结束）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted CHAR(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
    INDEX idx_room_id (room_id),
    INDEX idx_start_time (start_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议表';

-- 操作日志表
CREATE TABLE IF NOT EXISTS sys_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    module VARCHAR(50) COMMENT '模块',
    business_type VARCHAR(50) COMMENT '业务类型',
    method VARCHAR(200) COMMENT '方法',
    request_method VARCHAR(10) COMMENT '请求方式',
    oper_name VARCHAR(50) COMMENT '操作人员',
    oper_url VARCHAR(255) COMMENT '请求URL',
    oper_ip VARCHAR(50) COMMENT '操作IP',
    oper_location VARCHAR(255) COMMENT '操作地点',
    oper_param TEXT COMMENT '请求参数',
    json_result TEXT COMMENT '返回参数',
    status INT DEFAULT 0 COMMENT '状态（0正常 1异常）',
    error_msg TEXT COMMENT '错误消息',
    oper_time BIGINT COMMENT '操作时间',
    cost_time BIGINT COMMENT '耗时',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_oper_name (oper_name),
    INDEX idx_oper_time (oper_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 插入初始数据
-- 插入角色
INSERT INTO sys_role (role_name, role_code, description, status) VALUES
('超级管理员', 'admin', '超级管理员，拥有所有权限', '0'),
('普通员工', 'employee', '普通员工角色', '0');

-- 插入菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon) VALUES
('首页', 0, 1, '/dashboard', 'dashboard/index', 'C', '0', '0', NULL, 'HomeFilled'),
('考勤管理', 0, 2, '/attendance', NULL, 'M', '0', '0', NULL, 'Calendar'),
('我的考勤', 2, 1, '/my-attendance', 'attendance/my', 'C', '0', '0', 'attendance:my:list', 'Document'),
('考勤统计', 2, 2, '/attendance', 'attendance/index', 'C', '0', '0', 'attendance:list', 'DataAnalysis'),
('请假管理', 0, 3, '/leave', NULL, 'M', '0', '0', NULL, 'Document'),
('我的请假', 5, 1, '/my-leave', 'leave/my', 'C', '0', '0', 'leave:my:list', 'Edit'),
('请假审批', 5, 2, '/leave', 'leave/index', 'C', '0', '0', 'leave:list', 'Checked'),
('个人便签', 0, 4, '/note', 'note/index', 'C', '0', '0', 'note:list', 'EditPen'),
('通讯录', 0, 5, '/contact', 'contact/index', 'C', '0', '0', 'contact:list', 'Phone'),
('会议管理', 0, 6, '/meeting', NULL, 'M', '0', '0', NULL, 'OfficeBuilding'),
('会议室管理', 10, 1, '/meeting-room', 'meeting/room', 'C', '0', '0', 'meeting:room:list', 'OfficeBuilding'),
('会议安排', 10, 2, '/meeting', 'meeting/index', 'C', '0', '0', 'meeting:list', 'Calendar'),
('系统管理', 0, 7, '/system', NULL, 'M', '0', '0', NULL, 'Setting'),
('用户管理', 13, 1, '/user', 'system/user/index', 'C', '0', '0', 'system:user:list', 'User'),
('角色管理', 13, 2, '/role', 'system/role/index', 'C', '0', '0', 'system:role:list', 'UserFilled'),
('菜单管理', 13, 3, '/menu', 'system/menu/index', 'C', '0', '0', 'system:menu:list', 'Menu'),
('部门管理', 13, 4, '/dept', 'system/dept/index', 'C', '0', '0', 'system:dept:list', 'OfficeBuilding'),
('岗位管理', 13, 5, '/post', 'system/post/index', 'C', '0', '0', 'system:post:list', 'Postcard'),
('公告管理', 0, 8, '/announcement', 'announcement/index', 'C', '0', '0', 'announcement:list', 'Bell');

-- 插入部门
INSERT INTO sys_dept (parent_id, dept_name, leader, phone, email, order_num, status) VALUES
(0, '总公司', '张总', '13800138000', 'ceo@company.com', 1, '0'),
(1, '技术部', '李经理', '13800138001', 'tech@company.com', 1, '0'),
(1, '市场部', '王经理', '13800138002', 'market@company.com', 2, '0'),
(1, '人事部', '赵经理', '13800138003', 'hr@company.com', 3, '0');

-- 插入岗位
INSERT INTO sys_post (post_code, post_name, post_sort, status) VALUES
('CEO', '总经理', 1, '0'),
('DEV', '开发工程师', 2, '0'),
('DESIGNER', '设计师', 3, '0'),
('HR', '人事专员', 4, '0');

-- 插入用户（密码: 123456，使用BCrypt加密）
INSERT INTO sys_user (username, password, real_name, phone, email, dept_id, post_id, status, gender) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E', '管理员', '13800000001', 'admin@company.com', 1, 1, '0', '1'),
('employee', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E', '张三', '13800000002', 'zhangsan@company.com', 2, 2, '0', '1');

-- 插入用户角色关联
INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1),
(2, 2);

-- 插入角色菜单关联（管理员拥有所有菜单权限）
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7),
(1, 8), (1, 9), (1, 10), (1, 11), (1, 12), (1, 13), (1, 14),
(1, 15), (1, 16), (1, 17), (1, 18), (1, 19), (1, 20),
(2, 1), (2, 2), (2, 3), (2, 5), (2, 6), (2, 8), (2, 9);

-- 插入会议室
INSERT INTO meeting_room (room_name, room_no, capacity, location, equipment, status) VALUES
('第一会议室', 'A001', 20, 'A栋3楼', '投影仪、白板、音响', '0'),
('第二会议室', 'A002', 10, 'A栋3楼', '投影仪、白板', '0'),
('培训室', 'B001', 50, 'B栋2楼', '投影仪、音响、麦克风', '0');

-- 插入公告
INSERT INTO sys_announcement (title, content, type, status, create_by) VALUES
('系统上线通知', '欢迎使用OA人事管理系统，本系统提供考勤管理、请假审批、会议管理等功能。', '2', '0', 1),
('关于春节放假的通知', '根据国家法定节假日安排，春节放假时间为2月10日至2月17日，请大家提前做好工作安排。', '1', '0', 1);