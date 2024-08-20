-- 学生表

CREATE TABLE student_info(
    stu_id INT PRIMARY KEY NOT NULL COMMENT '学号',
    stu_name VARCHAR(100) NOT NULL COMMENT '姓名',
    stu_gender ENUM('男', '女') COMMENT '性别',
    stu_major VARCHAR(255) NOT NULL COMMENT '专业',
    stu_class VARCHAR(100) NOT NULL COMMENT '班级',
    stu_birth VARCHAR(10) NOT NULL COMMENT '出生日期',
    stu_phone VARCHAR(11) UNIQUE COMMENT '电话',
    stu_status ENUM('在校','离校') NOT NULL COMMENT '在校状态',
    stu_grades JSON COMMENT '成绩'
);

INSERT INTO student_info VALUES(
    '2024010101',
    '张三',
    '男',
    '信息安全',
    '2024信息安全(1)班',
    '2004-06-26',
    '13561101974',
    '在校',
    '{"高等数学":27,"大学英语":35,"C语言程序设计":97}'
);

INSERT INTO student_info VALUES(
    '2024010102',
    '李四',
    '女',
    '信息安全',
    '2024信息安全(1)班',
    '2004-12-10',
    '12004567810',
    '在校',
    '{"高等数学":85,"大学英语":91,"C语言程序设计":60}'
);

-- 用户表

CREATE TABLE users(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    username VARCHAR(16) NOT NULL UNIQUE,
    password VARCHAR(32) NOT NULL
);

INSERT INTO users VALUES(
    1,
    'admin',
    -- 该密码是md5加密后的'admin'
    '21232f297a57a5a743894a0e4a801fc3'
);