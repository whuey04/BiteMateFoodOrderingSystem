-- =========================================
-- Create database
-- =========================================
CREATE DATABASE IF NOT EXISTS bitemate DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE bitemate;


create table address_book
(
    id            bigint auto_increment
        primary key,
    user_id       bigint               not null,
    consignee     varchar(50)          null,
    sex           varchar(2)           null,
    phone         varchar(11)          not null,
    province_code varchar(12)          null,
    province_name varchar(32)          null,
    city_code     varchar(12)          null,
    city_name     varchar(32)          null,
    district_code varchar(12)          null,
    district_name varchar(32)          null,
    detail        varchar(200)         null,
    label         varchar(100)         null,
    is_default    tinyint(1) default 0 not null comment '1 yes 0 no'
);

create table category
(
    id          bigint auto_increment comment 'category id'
        primary key,
    name        varchar(32)   not null comment 'category name',
    type        int           null comment 'category type  1 Dish 2 SetMeal',
    sort        int default 0 not null comment 'category sort',
    status      int           null comment 'category status',
    create_time datetime      null comment 'category create time',
    update_time datetime      null comment 'category update time',
    create_user bigint        null comment 'category create user',
    update_user bigint        null comment 'category update user',
    constraint idx_category_name
        unique (name)
);

create table dish
(
    id          bigint auto_increment comment 'dish id'
        primary key,
    name        varchar(32)    not null comment 'dish name',
    category_id bigint         not null comment 'category id',
    price       decimal(10, 2) null comment 'dish price',
    image       varchar(255)   null comment 'dish image',
    description varchar(255)   null comment 'dish description',
    status      int default 1  null comment '0: stop 1: sale',
    create_time datetime       null comment 'dish create time',
    update_time datetime       null comment 'dish update time',
    create_user bigint         null comment 'dish create user',
    update_user bigint         null comment 'dish update user',
    constraint idk_dish_name
        unique (name)
);

create table dish_flavor
(
    id      bigint auto_increment comment 'dish flavor id'
        primary key,
    dish_id bigint       not null comment 'dish id',
    name    varchar(32)  null comment 'dish flavor name',
    value   varchar(255) null comment 'flavor list'
);

create table employee
(
    id          bigint auto_increment comment 'employee id'
        primary key,
    name        varchar(32)   null comment 'employee name',
    username    varchar(32)   not null comment 'username',
    password    varchar(64)   not null comment 'password',
    phone       varchar(11)   not null comment 'phone number',
    sex         varchar(2)    null comment 'sex',
    id_number   varchar(18)   not null comment 'id number',
    status      int default 1 not null comment 'acc status; 1:normal, 2:locked',
    create_time datetime      not null comment 'create time',
    update_time datetime      not null comment 'update time',
    create_user bigint        not null comment ' create_user id',
    update_user bigint        not null comment 'update_user id',
    constraint idx_username
        unique (username)
);

-- =========================================
-- Initialize employee data
-- =========================================
INSERT INTO employee (id, name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) 
VALUES 
(1,'Admin','admin','e10adc3949ba59abbe56e057f20f883e','13812312312','1','110101199001010047',1,'2025-02-15 15:51:20','2025-02-17 09:16:20',10,1),
(2,'Mark','mark','e10adc3949ba59abbe56e057f20f883e','13812315647','1','110101199001019653',1,'2025-08-25 14:47:14','2025-08-25 14:47:14',1,1);


create table order_detail
(
    id          bigint auto_increment
        primary key,
    name        varchar(32)    null,
    image       varchar(255)   null,
    order_id    bigint         not null,
    dish_id     bigint         null,
    setmeal_id  bigint         null,
    dish_flavor varchar(50)    null,
    number      int default 1  not null,
    amount      decimal(10, 2) not null
);

create table orders
(
    id                      bigint auto_increment
        primary key,
    number                  varchar(50)          null,
    status                  int        default 1 not null comment 'Order Status 1. Pending Payment 2. Pending Acceptance 3. Accepted 4. In Transit 5. Completed 6. Cancelled 7. Refunded',
    user_id                 bigint               not null,
    address_book_id         bigint               not null,
    order_time              datetime             not null,
    checkout_time           datetime             null,
    pay_method              int        default 1 not null,
    pay_status              tinyint    default 0 not null comment 'Payment Status 0: Unpaid 1: Paid 2: Refunded',
    amount                  decimal(10, 2)       not null,
    remark                  varchar(100)         null,
    phone                   varchar(11)          null,
    address                 varchar(255)         null,
    user_name               varchar(32)          null,
    consignee               varchar(32)          null,
    cancel_reason           varchar(255)         null,
    rejection_reason        varchar(255)         null,
    cancel_time             datetime             null,
    estimated_delivery_time datetime             null,
    delivery_status         tinyint(1) default 1 not null,
    delivery_time           datetime             null,
    pack_amount             int                  null,
    tableware_number        int                  null,
    tableware_status        tinyint(1) default 1 not null
);

create table setmeal
(
    id          bigint auto_increment comment 'setmeal id'
        primary key,
    category_id bigint         not null comment 'category id',
    name        varchar(32)    not null comment 'setmeal name',
    price       decimal(10, 2) not null comment 'setmeal price',
    status      int default 1  null comment '0 stop 1 sale',
    description varchar(255)   null comment 'setmeal description',
    image       varchar(255)   null,
    create_time datetime       null,
    update_time datetime       null,
    create_user bigint         null,
    update_user bigint         null,
    constraint idk_setmeal_name
        unique (name)
);

create table setmeal_dish
(
    id         bigint auto_increment
        primary key,
    setmeal_id bigint         null,
    dish_id    bigint         null,
    name       varchar(32)    null,
    price      decimal(10, 2) null,
    copies     int            null comment 'no of dish in setmeal'
);

create table shopping_cart
(
    id          bigint auto_increment
        primary key,
    name        varchar(32)    null,
    image       varchar(255)   null,
    user_id     bigint         not null,
    dish_id     bigint         null,
    setmeal_id  bigint         null,
    dish_flavor varchar(50)    null,
    number      int default 1  not null,
    amount      decimal(10, 2) not null,
    create_time datetime       null
);

create table user
(
    id          bigint auto_increment
        primary key,
    email       varchar(50)  null,
    openid      varchar(45)  null,
    name        varchar(50)  null,
    phone       varchar(100) null,
    sex         varchar(2)   null,
    id_number   varchar(18)  null,
    avatar      varchar(500) null,
    create_time datetime     null
);


