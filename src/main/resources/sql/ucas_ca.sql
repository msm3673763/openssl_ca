/*
Navicat MySQL Data Transfer

Source Server         : rdp_demo
Source Server Version : 50626
Source Host           : 172.17.21.59:3306
Source Database       : ucas_ca

Target Server Type    : MYSQL
Target Server Version : 50626
File Encoding         : 65001

Date: 2017-04-26 13:59:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `ca_cer_file`
-- ----------------------------
DROP TABLE IF EXISTS `ca_cer_file`;
CREATE TABLE `ca_cer_file` (
  `FILE_DBID` char(18) NOT NULL COMMENT '文件存储ID',
  `CER_UUID` varchar(32) NOT NULL COMMENT '证书UUID',
  `FILE_TYPE` char(18) DEFAULT NULL COMMENT '文件类型',
  `P12_SECRET` char(18) DEFAULT NULL COMMENT 'P12密码',
  `FILE_DATE` char(18) DEFAULT NULL COMMENT '文件创建日期',
  PRIMARY KEY (`FILE_DBID`),
  KEY `R_1` (`CER_UUID`),
  CONSTRAINT `ca_cer_file_ibfk_1` FOREIGN KEY (`CER_UUID`) REFERENCES `ca_cer_info` (`CER_UUID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数字证书文件表';

-- ----------------------------
-- Records of ca_cer_file
-- ----------------------------

-- ----------------------------
-- Table structure for `ca_cer_info`
-- ----------------------------
DROP TABLE IF EXISTS `ca_cer_info`;
CREATE TABLE `ca_cer_info` (
  `CER_UUID` varchar(32) NOT NULL COMMENT '证书UUID',
  `CER_TYPE` char(1) DEFAULT NULL COMMENT '证书类型（1应用服务器，2终端）',
  `DOMAIN_NAME` varchar(50) DEFAULT NULL COMMENT '域名',
  `COUNTRY` varchar(50) DEFAULT NULL COMMENT '国家',
  `PROVINCE` varchar(50) DEFAULT NULL COMMENT '省份',
  `ORG_NAME` varchar(100) DEFAULT NULL COMMENT '组织名',
  `ORG_UNIT_NAME` varchar(100) DEFAULT NULL COMMENT '组织单位名',
  `EAMIL` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER` varchar(50) DEFAULT NULL COMMENT '创建人',
  `DEAD_TIME` datetime DEFAULT NULL COMMENT '失效时间',
  `CER_STATUS` char(1) DEFAULT NULL COMMENT '证书状态（1有效，2失效）',
  `CRL_URL` char(18) DEFAULT NULL COMMENT 'CRL撤消列表URL',
  PRIMARY KEY (`CER_UUID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数字证书信息表';

-- ----------------------------
-- Records of ca_cer_info
-- ----------------------------

-- ----------------------------
-- Table structure for `manage_config`
-- ----------------------------
DROP TABLE IF EXISTS `manage_config`;
CREATE TABLE `manage_config` (
  `id` varchar(36) NOT NULL,
  `param_key` varchar(50) NOT NULL,
  `param_value` varchar(2000) DEFAULT NULL,
  `param_desc` varchar(200) DEFAULT NULL,
  `status` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `param_key` (`param_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of manage_config
-- ----------------------------
INSERT INTO `manage_config` VALUES ('2c9182ec-5ba2dc21-015b-a2dc4a80-0001', '测试数据', '1', '1', null);

-- ----------------------------
-- Table structure for `manage_ip_schedule_task`
-- ----------------------------
DROP TABLE IF EXISTS `manage_ip_schedule_task`;
CREATE TABLE `manage_ip_schedule_task` (
  `id` char(36) NOT NULL COMMENT 'id',
  `task_code` varchar(100) NOT NULL COMMENT '任务名称编码',
  `task_name` varchar(100) NOT NULL COMMENT '任务名',
  `task_conf` varchar(100) NOT NULL COMMENT '任务执行表达式',
  `task_class` varchar(100) NOT NULL COMMENT '任务执行类',
  `task_server_ip` varchar(100) NOT NULL COMMENT '任务执行的服务器',
  `status` char(1) NOT NULL DEFAULT '1' COMMENT '任务状态1:启用;0：禁用',
  `remark` varchar(250) NOT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统运行调度表';

-- ----------------------------
-- Records of manage_ip_schedule_task
-- ----------------------------
INSERT INTO `manage_ip_schedule_task` VALUES ('DEFAULT', 'DEFAULT', 'DEFAULT', '0/50 * * * * ?', 'com.ucsmy.ucas.config.quartz.scan.DefaultScanJob', '172.17.21.59', '0', 'DEFAULT');

-- ----------------------------
-- Table structure for `manage_log_info`
-- ----------------------------
DROP TABLE IF EXISTS `manage_log_info`;
CREATE TABLE `manage_log_info` (
  `id` varchar(36) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `ip_address` varchar(16) DEFAULT NULL,
  `log_level` varchar(16) DEFAULT NULL,
  `message` varchar(256) DEFAULT NULL,
  `user_name` varchar(36) DEFAULT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of manage_log_info
-- ----------------------------

-- ----------------------------
-- Table structure for `manage_module`
-- ----------------------------
DROP TABLE IF EXISTS `manage_module`;
CREATE TABLE `manage_module` (
  `module_id` varchar(36) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `sn` varchar(32) DEFAULT NULL COMMENT '判断标志',
  `url` varchar(245) DEFAULT NULL,
  `parent_id` varchar(36) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `leaf` char(1) DEFAULT '0' COMMENT '是否是叶子节点。0：不是叶子节点；1：是叶子节点',
  PRIMARY KEY (`module_id`),
  KEY `FK_MODULE_PARENT_ID` (`parent_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of manage_module
-- ----------------------------
INSERT INTO `manage_module` VALUES ('08c3b96c-c0f4-4214-86fe-08bfb77147e9', '组织管理描述', '4', '', 'pages/organization/index.js', '2', '组织管理', '0');
INSERT INTO `manage_module` VALUES ('1', '统一认证管理系统', '1', '0', '', '0', '统一认证管理系统', '0');
INSERT INTO `manage_module` VALUES ('2', '系统管理', '1', '0', '', '1', '系统管理', '0');
INSERT INTO `manage_module` VALUES ('3', '用户管理', '1', '0', 'pages/user/index.js', '2', '用户管理', '1');
INSERT INTO `manage_module` VALUES ('4', '菜单管理', '3', '0', 'pages/module/index.js', '2', '菜单管理', '1');
INSERT INTO `manage_module` VALUES ('5', '角色管理', '2', '0', 'pages/role/index.js', '2', '角色管理', '1');
INSERT INTO `manage_module` VALUES ('6', '权限管理', '4', '0', 'pages/permission/index.js', '2', '权限管理', '1');
INSERT INTO `manage_module` VALUES ('7', '参数管理', '4', '0', 'pages/config/index.js', '2', '参数管理', '1');
INSERT INTO `manage_module` VALUES ('def06628-a916-47bd-97a2-31b4768fc0a3', '定时管理', '6', '', 'pages/schedule/index.js', '2', '定时管理', '0');

-- ----------------------------
-- Table structure for `manage_organization`
-- ----------------------------
DROP TABLE IF EXISTS `manage_organization`;
CREATE TABLE `manage_organization` (
  `org_id` varchar(36) NOT NULL DEFAULT '' COMMENT '机构唯一标识',
  `name` varchar(64) NOT NULL COMMENT '名称',
  `description` varchar(256) DEFAULT NULL COMMENT '描述',
  `priority` int(11) DEFAULT NULL COMMENT '优先级',
  `parent_id` varchar(36) DEFAULT NULL COMMENT '父机构id',
  `root_id` varchar(36) DEFAULT NULL COMMENT '根机构id',
  PRIMARY KEY (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of manage_organization
-- ----------------------------
INSERT INTO `manage_organization` VALUES ('2c9182d3-5ac657fb-015a-c657fbf7-0000', '总行营业部', '总行营业部', '0', 'abdb6fe7ee9e11e6bfa2005056adf4cb', null);
INSERT INTO `manage_organization` VALUES ('2c9182d3-5ac657fb-015a-c658fff0-0001', '东莞分行', '东莞分行', '1', 'abdb6fe7ee9e11e6bfa2005056adf4cb', null);
INSERT INTO `manage_organization` VALUES ('2c9182d3-5ac657fb-015a-c659421e-0002', '中心区支行', '中心区支行', '1', '2c9182d3-5ac657fb-015a-c658fff0-0001', null);
INSERT INTO `manage_organization` VALUES ('2c9182d3-5ac657fb-015a-c65a7544-0003', '东湖支行', '东湖支行', '2', '2c9182d3-5ac657fb-015a-c658fff0-0001', null);
INSERT INTO `manage_organization` VALUES ('2c9182d3-5ac657fb-015a-c65aacc5-0004', '万江支行', '万江支行', '2', '2c9182d3-5ac657fb-015a-c658fff0-0001', null);
INSERT INTO `manage_organization` VALUES ('2c9182d3-5ac657fb-015a-c65affbf-0005', '东城支行', '东城支行', '2', '2c9182d3-5ac657fb-015a-c658fff0-0001', null);
INSERT INTO `manage_organization` VALUES ('2c9182ec-5ac64b7e-015a-c64b7eb6-0000', '广州分行', '广州分行', '1', 'abdb6fe7ee9e11e6bfa2005056adf4cb', null);
INSERT INTO `manage_organization` VALUES ('2c9182ec-5ac64b7e-015a-c64c0357-0001', '广州番禺支行', '广州番禺支行', '2', '2c9182ec-5ac64b7e-015a-c64b7eb6-0000', null);
INSERT INTO `manage_organization` VALUES ('2c9182ec-5ac64b7e-015a-c64c389a-0002', '广州增城支行', '广州增城支行', '2', '2c9182ec-5ac64b7e-015a-c64b7eb6-0000', null);
INSERT INTO `manage_organization` VALUES ('2c9182ec-5ac64b7e-015a-c64cd54f-0004', '广州天河支行', '广州天河支行', '2', '2c9182ec-5ac64b7e-015a-c64b7eb6-0000', null);
INSERT INTO `manage_organization` VALUES ('2c9182ec-5ac64b7e-015a-c64d69cd-0008', '广州白云支行', '广州白云支行', '2', '2c9182ec-5ac64b7e-015a-c64b7eb6-0000', null);
INSERT INTO `manage_organization` VALUES ('2c9182ec-5ac64b7e-015a-c64d93c0-0009', '广东南沙分行', '广东自贸试验区 南沙分行', '2', '2c9182ec-5ac64b7e-015a-c64b7eb6-0000', null);
INSERT INTO `manage_organization` VALUES ('2c9182ec-5ac64b7e-015a-c64dbcc1-000a', '深圳分行', '深圳分行', '1', 'abdb6fe7ee9e11e6bfa2005056adf4cb', null);
INSERT INTO `manage_organization` VALUES ('2c9182ec-5ac64b7e-015a-c64de718-000b', '深圳龙岗支行', '深圳龙岗支行', '2', '2c9182ec-5ac64b7e-015a-c64dbcc1-000a', null);
INSERT INTO `manage_organization` VALUES ('2c9182ec-5ac64b7e-015a-c64e0d09-000c', '深圳宝安支行', '深圳宝安支行', '2', '2c9182ec-5ac64b7e-015a-c64dbcc1-000a', null);
INSERT INTO `manage_organization` VALUES ('2c9182fd-5aca7f0d-015a-ca816ff7-0002', '广州海珠分行', '广州海珠分行', '2', '2c9182ec-5ac64b7e-015a-c64b7eb6-0000', null);
INSERT INTO `manage_organization` VALUES ('abdb6fe7ee9e11e6bfa2005056adf4cb', '东莞银行', '东莞银行', '0', '', null);

-- ----------------------------
-- Table structure for `manage_permission`
-- ----------------------------
DROP TABLE IF EXISTS `manage_permission`;
CREATE TABLE `manage_permission` (
  `permission_id` varchar(36) NOT NULL,
  `description` varchar(256) DEFAULT NULL COMMENT '描述',
  `name` varchar(64) DEFAULT NULL COMMENT '名称',
  `sn` varchar(36) DEFAULT NULL COMMENT '判断标识',
  `module_id` varchar(36) DEFAULT NULL COMMENT '模块id',
  `url_action` varchar(256) DEFAULT NULL COMMENT '资源URL',
  PRIMARY KEY (`permission_id`),
  KEY `FK_PERMISSION_MODULEID` (`module_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of manage_permission
-- ----------------------------
INSERT INTO `manage_permission` VALUES ('18a4d221-c245-4fa9-b770-08d4633a7026', '查询角色', '查询', 'role_query', '5', '/role.role.pgflow/queryRoleList*');
INSERT INTO `manage_permission` VALUES ('1f759941-e52d-426a-aace-b6fa99ef68c8', '角色绑定权限', '绑定权限', 'role_bind_permission', '5', '/rolePermission.addRolePermission.pgflow/addRolePermission*');
INSERT INTO `manage_permission` VALUES ('1fdd8fcf-ed8d-46fd-9509-7a4187e7ea38', '删除用户', '删除', 'user_delete', '3', '/user.user.pgflow/deleteUser*');
INSERT INTO `manage_permission` VALUES ('2380627f-7ae7-48bf-a3e8-3d66e3549509', '删除参数', '删除', 'config_delete', '7', '/config.config.pgflow/delete_config*');
INSERT INTO `manage_permission` VALUES ('26935107-cf4d-42cd-a05e-acecee79fc24', '修改菜单', '修改', 'module_update', '4', '/module.module.pgflow/updateModule*');
INSERT INTO `manage_permission` VALUES ('29f373da-cb7f-42e4-830c-1a1240b32fd7', '组织解绑用户', '解绑用户', 'org_unbind_user', '08c3b96c-c0f4-4214-86fe-08bfb77147e9', '/organization.organization.pgflow/unbindOrganization*');
INSERT INTO `manage_permission` VALUES ('3595c7a4-1ec1-4361-925c-9ad8e3380221', '密码修改', '密码修改', 'user_update_password', '3', '/user.user.pgflow/updateUserPassword*');
INSERT INTO `manage_permission` VALUES ('36e05dd5-79f8-49e1-bb13-14e81203d547', '删除', '删除', 'org_delete', '08c3b96c-c0f4-4214-86fe-08bfb77147e9', '/organization.organization.pgflow/deleteOrganization*');
INSERT INTO `manage_permission` VALUES ('3c7c931b-114f-4ca3-9220-f9118e1060ed', '修改', '修改', 'org_update', '08c3b96c-c0f4-4214-86fe-08bfb77147e9', '/organization.organization.pgflow/editOrganization*');
INSERT INTO `manage_permission` VALUES ('3d57b77d-b148-4fea-9fa0-71619fe33f5a', '新增用户', '新增', 'user_add', '3', '/user.user.pgflow/addUser*');
INSERT INTO `manage_permission` VALUES ('4b72e940-3647-4cc4-8b56-45761432984e', '解绑用户对应角色', '解绑角色', 'role_unbind_user', '5', '/role.user_role.pgflow/unbindUser*');
INSERT INTO `manage_permission` VALUES ('54105a72-8433-478e-b878-70fff83b46ed', '删除权限', '删除', 'role_delete', '5', '/role.role.pgflow/deleteRole*');
INSERT INTO `manage_permission` VALUES ('56d1d066-1ef6-4ad4-8ea7-d1591f18a51f', '修改角色', '修改', 'role_update', '5', '/role.role.pgflow/updateRole*');
INSERT INTO `manage_permission` VALUES ('58c54c9a-c4ae-454d-b05c-576de1975a42', '用户查询', '查询', 'user_query', '3', '/user.user.pgflow/queryUserList*');
INSERT INTO `manage_permission` VALUES ('5c5a71a1-7f27-452c-9e34-9d424be48850', '新增子节点', '新增', 'org_add', '08c3b96c-c0f4-4214-86fe-08bfb77147e9', '/organization.organization.pgflow/addOrganization*');
INSERT INTO `manage_permission` VALUES ('6658ffb8-8556-4d5d-bd9f-890d6eca508b', '组织绑定用户', '绑定用户', 'org_bind_user', '08c3b96c-c0f4-4214-86fe-08bfb77147e9', '/organization.organization.pgflow/bindOrganization*');
INSERT INTO `manage_permission` VALUES ('6cdc5d4e-bba6-4187-a5f3-908276aa41f2', '绑定用户对应角色', '绑定角色', 'role_bind_user', '5', '/role.user_role.pgflow/bind_user*');
INSERT INTO `manage_permission` VALUES ('6f5edffe-83a5-4bcf-907a-6e10c2c1245f', '组织查询', '查询', 'org_query', '08c3b96c-c0f4-4214-86fe-08bfb77147e9', '/organization.organization.pgflow/queryOrganization*');
INSERT INTO `manage_permission` VALUES ('70ceae1c-573c-42c1-8e09-4b6979ff47da', '删除菜单', '删除', 'module_delete', '4', '/module.module.pgflow/delModule*');
INSERT INTO `manage_permission` VALUES ('8fc06f62-633d-47a2-8345-3ec0ee7d94b0', '权限查询', '查询', 'permission_query', '6', '/permission.permission.pgflow/getPermissionList*');
INSERT INTO `manage_permission` VALUES ('9707e828-3422-41c5-937a-a4afd4f9b849', '权限修改', '修改', 'permission_update', '6', '/permission.permission.pgflow/updatePermission*');
INSERT INTO `manage_permission` VALUES ('99daa3fe-5b64-4dce-9dee-1e9693434210', '新增子节点', '新增', 'module_add', '4', '/module.module.pgflow/addModule*');
INSERT INTO `manage_permission` VALUES ('b0dac8d0-4367-41b4-b965-e7a7f0ae0c8c', '参数查询', '查询', 'config_query', '7', '/config.config.pgflow/query_config*');
INSERT INTO `manage_permission` VALUES ('bdf5deec-3d3d-47d3-aca2-474f8c6f1a38', '新增参数', '新增', 'config_add', '7', '/config.config.pgflow/add_config*');
INSERT INTO `manage_permission` VALUES ('c25361e4-e891-41f0-9585-9fbf4dc0199d', '新增角色', '新增', 'role_add', '5', '/role.role.pgflow/addRole*');
INSERT INTO `manage_permission` VALUES ('c9f62ced-e67c-4438-af54-8f99a490fec0', '修改用户', '修改', 'user_update', '3', '/user.user.pgflow/updateUser*');
INSERT INTO `manage_permission` VALUES ('d2852505-4e70-40c6-9c8d-ddfe72520952', '修改参数', '修改', 'config_update', '7', '/config.config.pgflow/edit_config*');
INSERT INTO `manage_permission` VALUES ('d7386557-360a-444d-b71b-3b071afead51', '菜单查询', '查询', 'module_query', '4', '/module.module.pgflow/moduleList*');
INSERT INTO `manage_permission` VALUES ('e36a76d6-2426-4e5a-a876-06dfc031311c', '新增权限', '新增', 'permission_add', '6', '/permission.permission.pgflow/addPermission*');

-- ----------------------------
-- Table structure for `manage_role`
-- ----------------------------
DROP TABLE IF EXISTS `manage_role`;
CREATE TABLE `manage_role` (
  `role_id` varchar(36) NOT NULL COMMENT '角色唯一标识',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `name` varchar(36) DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of manage_role
-- ----------------------------
INSERT INTO `manage_role` VALUES ('2c9182d4-5a1c3f70-015a-1c3f7089-0000', '超级管理', '超级管理员');
INSERT INTO `manage_role` VALUES ('2c9182d4-5a1c3f70-015a-1c3f7089-0011', '录入岗', '录入岗');
INSERT INTO `manage_role` VALUES ('2c9182d4-5a1c3f70-015a-1c3f7089-1012', '初审岗', '初审岗');
INSERT INTO `manage_role` VALUES ('2c9182d4-5a1c3f70-015a-1c3f7089-1112', '审批岗', '审批岗');
INSERT INTO `manage_role` VALUES ('2c9182e7-5adac6cd-015a-dacf96aa-0001', '跟进岗', '跟进岗');
INSERT INTO `manage_role` VALUES ('2c9182fd-5aa690f1-015a-a6c25184-0001', '系统功能测试+UI测试', '测试人员');

-- ----------------------------
-- Table structure for `manage_role_module`
-- ----------------------------
DROP TABLE IF EXISTS `manage_role_module`;
CREATE TABLE `manage_role_module` (
  `id` varchar(36) NOT NULL,
  `module_id` varchar(36) NOT NULL COMMENT '菜单Id',
  `role_id` varchar(36) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of manage_role_module
-- ----------------------------
INSERT INTO `manage_role_module` VALUES ('2c9182c3-5b8437c6-015b-845164fb-002e', '1', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_module` VALUES ('2c9182c3-5b8437c6-015b-845164fb-002f', '2', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_module` VALUES ('2c9182c3-5b8437c6-015b-845164fb-0030', '3', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_module` VALUES ('2c9182c3-5b8437c6-015b-845164fb-0031', '08c3b96c-c0f4-4214-86fe-08bfb77147e9', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_module` VALUES ('2c9182c3-5b8437c6-015b-845164fb-0032', '4', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_module` VALUES ('2c9182c3-5b8437c6-015b-845164fb-0033', '5', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_module` VALUES ('2c9182c3-5b8437c6-015b-845164fb-0034', '6', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_module` VALUES ('2c9182c3-5b8437c6-015b-845164fb-0035', '7', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_module` VALUES ('2c9182d5-5aeaa4bd-015a-eaa4bd05-0000', '1', '2c9182d4-5a1c3f70-015a-1c3f7089-0011');
INSERT INTO `manage_role_module` VALUES ('2c9182d5-5af52b2a-015a-f560716f-0001', '1', '2c9182d4-5a1c3f70-015a-1c3f7089-1012');
INSERT INTO `manage_role_module` VALUES ('2c9182d5-5af8cb1e-015a-f8cb1eb7-0003', '1', '2c9182d4-5a1c3f70-015a-1c3f7089-1112');
INSERT INTO `manage_role_module` VALUES ('2c9182e7-5afa2a6d-015a-fa2a6dd8-0001', '1', '2c9182e7-5adac6cd-015a-dacf96aa-0001');
INSERT INTO `manage_role_module` VALUES ('2c9182ec-5ad664dc-015a-d679294f-0026', '7', '2c9182ec-5ad664dc-015a-d664dc61-0000');
INSERT INTO `manage_role_module` VALUES ('2c9182ec-5ad664dc-015a-d679294f-0027', '2', '2c9182ec-5ad664dc-015a-d664dc61-0000');
INSERT INTO `manage_role_module` VALUES ('2c9182ec-5ad664dc-015a-d679294f-0028', '1', '2c9182ec-5ad664dc-015a-d664dc61-0000');
INSERT INTO `manage_role_module` VALUES ('2c9182ec-5ad9d7f9-015a-d9d8bec1-0020', '3', '2c9182ec-5ad664dc-015a-d67cd228-002a');
INSERT INTO `manage_role_module` VALUES ('2c9182ec-5ad9d7f9-015a-d9d8bec1-0021', '2', '2c9182ec-5ad664dc-015a-d67cd228-002a');
INSERT INTO `manage_role_module` VALUES ('2c9182ec-5ad9d7f9-015a-d9d8bec1-0023', '1', '2c9182ec-5ad664dc-015a-d67cd228-002a');
INSERT INTO `manage_role_module` VALUES ('2c9182ec-5ada1ddd-015a-da1f3834-003b', '1', '2c9182d4-5a1c3f70-015a-1c3f7089-1818');
INSERT INTO `manage_role_module` VALUES ('2c9182ec-5ada1ddd-015a-da1f4c5e-0043', '3', '2c9182fd-5aa690f1-015a-a6c25184-0001');
INSERT INTO `manage_role_module` VALUES ('2c9182ec-5ada1ddd-015a-da1f4c5e-0044', '2', '2c9182fd-5aa690f1-015a-a6c25184-0001');
INSERT INTO `manage_role_module` VALUES ('2c9182ec-5ada1ddd-015a-da1f4c5e-0045', '1', '2c9182fd-5aa690f1-015a-a6c25184-0001');
INSERT INTO `manage_role_module` VALUES ('2c9182ec-5ada1ddd-015a-da1f4c5e-0046', '7', '2c9182fd-5aa690f1-015a-a6c25184-0001');
INSERT INTO `manage_role_module` VALUES ('2c9182ec-5ada1ddd-015a-da1f4c5e-0047', '6', '2c9182fd-5aa690f1-015a-a6c25184-0001');
INSERT INTO `manage_role_module` VALUES ('2c9182ec-5ada1ddd-015a-da1f4c5e-0048', '5', '2c9182fd-5aa690f1-015a-a6c25184-0001');
INSERT INTO `manage_role_module` VALUES ('2c9182ec-5ada1ddd-015a-da1f4c5e-0049', '4', '2c9182fd-5aa690f1-015a-a6c25184-0001');
INSERT INTO `manage_role_module` VALUES ('2c9182ec-5ada1ddd-015a-da1f4c5e-004c', '08c3b96c-c0f4-4214-86fe-08bfb77147e9', '2c9182fd-5aa690f1-015a-a6c25184-0001');
INSERT INTO `manage_role_module` VALUES ('2c9182ec-5b1e323d-015b-1e323d92-0003', '3', '2c9182ec-5ad9f5d5-015a-d9f77eca-002a');
INSERT INTO `manage_role_module` VALUES ('2c9182ec-5b1e323d-015b-1e323d92-0005', '2', '2c9182ec-5ad9f5d5-015a-d9f77eca-002a');
INSERT INTO `manage_role_module` VALUES ('2c9182ec-5b1e323d-015b-1e323d92-0007', '1', '2c9182ec-5ad9f5d5-015a-d9f77eca-002a');
INSERT INTO `manage_role_module` VALUES ('2c9182ec-5b1e323d-015b-1e323d92-0008', '7', '2c9182ec-5ad9f5d5-015a-d9f77eca-002a');
INSERT INTO `manage_role_module` VALUES ('2c9182ec-5b1e323d-015b-1e323d92-0009', '6', '2c9182ec-5ad9f5d5-015a-d9f77eca-002a');
INSERT INTO `manage_role_module` VALUES ('2c9182ec-5b1e323d-015b-1e323d92-000a', '5', '2c9182ec-5ad9f5d5-015a-d9f77eca-002a');
INSERT INTO `manage_role_module` VALUES ('2c9182ec-5b1e323d-015b-1e323d92-000b', '4', '2c9182ec-5ad9f5d5-015a-d9f77eca-002a');
INSERT INTO `manage_role_module` VALUES ('2c9182ec-5b1e323d-015b-1e323d92-000e', '08c3b96c-c0f4-4214-86fe-08bfb77147e9', '2c9182ec-5ad9f5d5-015a-d9f77eca-002a');
INSERT INTO `manage_role_module` VALUES ('ff808081-5b17c0ef-015b-17c0ef9e-0000', '3', '2c9182ed-5afa089f-015a-fa089fe6-0000');
INSERT INTO `manage_role_module` VALUES ('ff808081-5b17c0ef-015b-17c0ef9e-0001', '2', '2c9182ed-5afa089f-015a-fa089fe6-0000');
INSERT INTO `manage_role_module` VALUES ('ff808081-5b17c0ef-015b-17c0ef9e-0002', '1', '2c9182ed-5afa089f-015a-fa089fe6-0000');

-- ----------------------------
-- Table structure for `manage_role_param`
-- ----------------------------
DROP TABLE IF EXISTS `manage_role_param`;
CREATE TABLE `manage_role_param` (
  `role_id` varchar(36) NOT NULL DEFAULT '角色id',
  `mark` varchar(15) NOT NULL COMMENT '角色标志位',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色参数表';

-- ----------------------------
-- Records of manage_role_param
-- ----------------------------
INSERT INTO `manage_role_param` VALUES ('2c9182d4-5a1c3f70-015a-1c3f7089-0000', 'admin');
INSERT INTO `manage_role_param` VALUES ('2c9182d4-5a1c3f70-015a-1c3f7089-0011', 'typing');
INSERT INTO `manage_role_param` VALUES ('2c9182d4-5a1c3f70-015a-1c3f7089-1012', 'firstAudit');
INSERT INTO `manage_role_param` VALUES ('2c9182d4-5a1c3f70-015a-1c3f7089-1112', 'secondAudit');
INSERT INTO `manage_role_param` VALUES ('2c9182e7-5adac6cd-015a-dacf96aa-0001', 'follow');

-- ----------------------------
-- Table structure for `manage_role_permission`
-- ----------------------------
DROP TABLE IF EXISTS `manage_role_permission`;
CREATE TABLE `manage_role_permission` (
  `id` varchar(36) NOT NULL,
  `permission_id` varchar(36) NOT NULL COMMENT '权限Id',
  `role_id` varchar(36) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`),
  KEY `FK_roleperm_permission` (`permission_id`) USING BTREE,
  KEY `FK_roleperm_role` (`role_id`) USING BTREE,
  CONSTRAINT `manage_role_permission_ibfk_1` FOREIGN KEY (`permission_id`) REFERENCES `manage_permission` (`permission_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `manage_role_permission_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `manage_role` (`role_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of manage_role_permission
-- ----------------------------
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-0036', '36e05dd5-79f8-49e1-bb13-14e81203d547', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-0037', '99daa3fe-5b64-4dce-9dee-1e9693434210', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-0038', '56d1d066-1ef6-4ad4-8ea7-d1591f18a51f', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-0039', '18a4d221-c245-4fa9-b770-08d4633a7026', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-003a', '2380627f-7ae7-48bf-a3e8-3d66e3549509', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-003b', '3c7c931b-114f-4ca3-9220-f9118e1060ed', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-003c', '70ceae1c-573c-42c1-8e09-4b6979ff47da', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-003d', 'b0dac8d0-4367-41b4-b965-e7a7f0ae0c8c', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-003e', 'c9f62ced-e67c-4438-af54-8f99a490fec0', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-003f', '3595c7a4-1ec1-4361-925c-9ad8e3380221', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-0040', 'd2852505-4e70-40c6-9c8d-ddfe72520952', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-0041', '3d57b77d-b148-4fea-9fa0-71619fe33f5a', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-0042', '54105a72-8433-478e-b878-70fff83b46ed', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-0043', '58c54c9a-c4ae-454d-b05c-576de1975a42', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-0044', '26935107-cf4d-42cd-a05e-acecee79fc24', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-0045', 'e36a76d6-2426-4e5a-a876-06dfc031311c', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-0046', '29f373da-cb7f-42e4-830c-1a1240b32fd7', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-0047', '6cdc5d4e-bba6-4187-a5f3-908276aa41f2', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-0048', '9707e828-3422-41c5-937a-a4afd4f9b849', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-0049', '5c5a71a1-7f27-452c-9e34-9d424be48850', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-004a', 'c25361e4-e891-41f0-9585-9fbf4dc0199d', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-004b', '4b72e940-3647-4cc4-8b56-45761432984e', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-004c', '1fdd8fcf-ed8d-46fd-9509-7a4187e7ea38', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-004d', '8fc06f62-633d-47a2-8345-3ec0ee7d94b0', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-004e', '1f759941-e52d-426a-aace-b6fa99ef68c8', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-004f', 'bdf5deec-3d3d-47d3-aca2-474f8c6f1a38', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-0050', 'd7386557-360a-444d-b71b-3b071afead51', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');
INSERT INTO `manage_role_permission` VALUES ('2c9182c3-5b8437c6-015b-845164fb-0051', '6f5edffe-83a5-4bcf-907a-6e10c2c1245f', '2c9182d4-5a1c3f70-015a-1c3f7089-0000');

-- ----------------------------
-- Table structure for `manage_serial_number`
-- ----------------------------
DROP TABLE IF EXISTS `manage_serial_number`;
CREATE TABLE `manage_serial_number` (
  `serial_number_id` varchar(36) NOT NULL,
  `current_serial_number` bigint(20) DEFAULT NULL,
  `generated_serial_number` bigint(20) NOT NULL,
  `serial_number_date` date NOT NULL,
  PRIMARY KEY (`serial_number_id`),
  UNIQUE KEY `serial_number_unique` (`generated_serial_number`,`serial_number_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of manage_serial_number
-- ----------------------------
INSERT INTO `manage_serial_number` VALUES ('2c9182f8-5acb5230-015a-cb5230ed-0000', '2', '0', '2017-03-14');

-- ----------------------------
-- Table structure for `manage_user_account`
-- ----------------------------
DROP TABLE IF EXISTS `manage_user_account`;
CREATE TABLE `manage_user_account` (
  `user_id` varchar(36) NOT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `salt` varchar(32) NOT NULL COMMENT '加密盐值',
  `status` tinyint(4) DEFAULT '1' COMMENT '1:激活 ;  0:初始;  2：停用',
  `account` varchar(32) NOT NULL COMMENT '账号',
  `email` varchar(32) DEFAULT NULL COMMENT '邮箱',
  `mobilephone` varchar(20) DEFAULT NULL COMMENT '手机',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `account` (`account`) USING BTREE,
  UNIQUE KEY `email` (`email`) USING BTREE,
  UNIQUE KEY `mobilephone` (`mobilephone`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of manage_user_account
-- ----------------------------
INSERT INTO `manage_user_account` VALUES ('2f01b7ed-b746-42e3-80ce-02d0381a7c53', '2017-03-10 10:52:09', '9d1babba61153ba817013e2c70d5c11a', 'miWbu', '0', 'xsxs', '12@123.cn', '13312341234');
INSERT INTO `manage_user_account` VALUES ('412ff21c-a85d-4673-ba13-77a2796ade1d', '2017-03-13 10:25:02', '4e07650088d3f5edb5117f51766cde9f', 'Ci6E8', '0', 'gjg', 'sdsfdsfdfsdf@126.com', '18640401010');
INSERT INTO `manage_user_account` VALUES ('7ebbb432-fbf8-44d2-8fad-e9faddae970c', '2017-03-09 11:28:06', 'e0cdf7e461423fb4c68709a7e50a05a0', 'geHDS', '0', 'admin', '13763340378@qq.com', '13763340378');
INSERT INTO `manage_user_account` VALUES ('b1587e20-2be9-47c5-8608-7bf8ca4c2aee', '2017-03-10 17:15:23', '9dbccf57a88579ba0f2f39b8d472d1cb', 'XBuRj', '0', 'xs12', '12@12.cn', '13333333333');
INSERT INTO `manage_user_account` VALUES ('fbf1ad31-9d7c-41bd-a508-b22ade38da4c', '2017-03-11 12:24:29', '7406894fedfef85760f6f9c40c9e7786', 'K14nr', '0', 'xujunwei', 'junwei@qq.com', '13422000000');

-- ----------------------------
-- Table structure for `manage_user_organization`
-- ----------------------------
DROP TABLE IF EXISTS `manage_user_organization`;
CREATE TABLE `manage_user_organization` (
  `id` varchar(36) NOT NULL,
  `user_id` varchar(36) NOT NULL COMMENT '用户id',
  `org_id` varchar(36) NOT NULL COMMENT '机构id',
  `root_org_id` varchar(36) DEFAULT NULL COMMENT '所属机构根节点id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of manage_user_organization
-- ----------------------------
INSERT INTO `manage_user_organization` VALUES ('2c9182e5-5ac5a777-015a-c5ad718c-0002', '57523de9-7261-4bb6-929f-a9e07e3cb50c', 'de7efdacef6d11e6bfa2005056adf4cb', null);
INSERT INTO `manage_user_organization` VALUES ('2c9182ec-5af401c0-015a-f401c076-0001', '7ebbb432-fbf8-44d2-8fad-e9faddae970c', 'abdb6fe7ee9e11e6bfa2005056adf4cb', null);
INSERT INTO `manage_user_organization` VALUES ('2c9182fd-5aca7f0d-015a-ca8ef9c5-000c', 'd089efd5-5738-4d4f-9331-2802356ba204', '2c9182fd-5aca7f0d-015a-ca816ff7-0002', null);
INSERT INTO `manage_user_organization` VALUES ('2c9182fd-5aca7f0d-015a-ca8ef9c5-000d', 'b6ddfc2c-72e6-47da-90f1-633e98e136d1', '2c9182fd-5aca7f0d-015a-ca816ff7-0002', null);
INSERT INTO `manage_user_organization` VALUES ('ff808081-5b17c0ef-015b-1e0e8a27-000d', 'de2ace6a-4373-48ad-b36b-5300f7a96bd3', 'abdb6fe7ee9e11e6bfa2005056adf4cb', null);

-- ----------------------------
-- Table structure for `manage_user_profile`
-- ----------------------------
DROP TABLE IF EXISTS `manage_user_profile`;
CREATE TABLE `manage_user_profile` (
  `user_id` varchar(36) NOT NULL,
  `name` varchar(32) DEFAULT NULL COMMENT '用户姓名',
  `introduce` varchar(255) DEFAULT NULL COMMENT '个人介绍',
  `telephone` varchar(32) DEFAULT NULL COMMENT '工作电话',
  `mobilephone` varchar(20) DEFAULT NULL COMMENT '移动电话',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `email` varchar(40) DEFAULT NULL COMMENT '邮箱',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `gender` varchar(4) DEFAULT '' COMMENT '性别',
  `starff_id` varchar(36) DEFAULT NULL COMMENT '员工号',
  `position` varchar(36) DEFAULT NULL COMMENT '职位',
  `type` varchar(255) DEFAULT 'import' COMMENT '用户类型，import,ldap，register',
  `isDelected` char(1) DEFAULT NULL COMMENT '0-正常 1-删除',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of manage_user_profile
-- ----------------------------
INSERT INTO `manage_user_profile` VALUES ('2f01b7ed-b746-42e3-80ce-02d0381a7c53', 'xsxs', null, '12345678', '13312341234', '2017-03-10 10:52:09', null, '12@123.cn', null, '1', null, null, 'register', null);
INSERT INTO `manage_user_profile` VALUES ('412ff21c-a85d-4673-ba13-77a2796ade1d', '跟进岗', null, '5762012', '18640401010', '2017-03-13 10:25:02', '2017-03-13 10:25:02', 'sdsfdsfdfsdf@126.com', '1990-01-01', '1', null, null, 'register', null);
INSERT INTO `manage_user_profile` VALUES ('7ebbb432-fbf8-44d2-8fad-e9faddae970c', '管理员', null, '5102391', '13763340378', '2017-03-09 11:28:06', null, '13763340378@qq.com', null, '1', null, null, 'register', null);
INSERT INTO `manage_user_profile` VALUES ('b1587e20-2be9-47c5-8608-7bf8ca4c2aee', 'xs31', null, '1254789632', '13333333333', '2017-03-10 17:15:23', null, '12@12.cn', null, '1', null, null, 'register', null);
INSERT INTO `manage_user_profile` VALUES ('fbf1ad31-9d7c-41bd-a508-b22ade38da4c', '录入', null, '', '13422000000', '2017-03-11 12:24:29', '2017-03-11 13:15:17', 'junwei@qq.com', '1990-01-01', '1', null, null, 'register', null);

-- ----------------------------
-- Table structure for `manage_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `manage_user_role`;
CREATE TABLE `manage_user_role` (
  `id` varchar(36) NOT NULL,
  `role_id` varchar(36) NOT NULL COMMENT '角色唯一标识',
  `user_id` varchar(36) DEFAULT NULL,
  `priority` int(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_userrole_role` (`role_id`) USING BTREE,
  KEY `FK_userrole_user` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of manage_user_role
-- ----------------------------
INSERT INTO `manage_user_role` VALUES ('06e5f45e-591f-44ac-bffe-087a3a21bded', '2c9182d4-5a1c3f70-015a-1c3f7089-0000', '0f2f507c-4907-4710-87e9-e453f39b7d1e', null);
INSERT INTO `manage_user_role` VALUES ('16ab60ca-4f39-48cc-94aa-37426a963e33', '2c9182d4-5a1c3f70-015a-1c3f7089-0011', 'ba5c17ec-954e-41d5-8b7b-0790091e88f0', null);
INSERT INTO `manage_user_role` VALUES ('1911222c-82ac-41b9-868c-622edfe1c35c', '2c9182e7-5adac6cd-015a-dacf96aa-0001', '7c94c036-ee24-4cc4-8268-a1a6361de649', null);
INSERT INTO `manage_user_role` VALUES ('1980c276-7d19-4482-b887-781687ec1062', '2c9182d4-5a1c3f70-015a-1c3f7089-0000', '78813c93-fbc7-4223-a9d7-03067acd6dfd', null);
INSERT INTO `manage_user_role` VALUES ('1da7017a-6bf8-42e9-92ff-edc52ba36ed4', '2c9182d4-5a1c3f70-015a-1c3f7089-0000', '5f7e40ab-e102-4d37-b66c-03ef4738e64c', null);
INSERT INTO `manage_user_role` VALUES ('1e72e22f-8621-4340-ba57-b6b6f4b3c6c6', '2c9182d4-5a1c3f70-015a-1c3f7089-1112', 'b1587e20-2be9-47c5-8608-7bf8ca4c2aee', null);
INSERT INTO `manage_user_role` VALUES ('2c0c3039-a6b4-447f-b8fc-fccf48e79b8e', '2c9182d4-5a1c3f70-015a-1c3f7089-0011', 'ca6e68d4-1c83-4cf2-9228-0a7ea66182bb', null);
INSERT INTO `manage_user_role` VALUES ('2c9182c2-5af3d71c-015a-f492ff95-0045', '2c9182d4-5a1c3f70-015a-1c3f7089-0011', 'e10781dd-1fb4-4892-9f3d-341f035abf9d', null);
INSERT INTO `manage_user_role` VALUES ('2c9182c3-5b807545-015b-80754558-0000', '2c9182d4-5a1c3f70-015a-1c3f7089-0000', 'de2ace6a-4373-48ad-b36b-5300f7a96bd3', null);
INSERT INTO `manage_user_role` VALUES ('2c9182c3-5b8076bf-015b-8076bf57-0000', '2c9182d4-5a1c3f70-015a-1c3f7089-0000', '30991c75-08ae-41df-9059-0dddd3cf2687', null);
INSERT INTO `manage_user_role` VALUES ('2c9182e7-5adac6cd-015a-dacffd4e-0009', '2c9182e7-5adac6cd-015a-dacf96aa-0001', '412ff21c-a85d-4673-ba13-77a2796ade1d', null);
INSERT INTO `manage_user_role` VALUES ('2c9182ec-5aad1720-015a-ad17202a-0000', '2c9182d4-5a1c3f70-015a-1c3f7089-0011', '91a678cf-e10f-4d6a-a7ec-f34231e8132e', null);
INSERT INTO `manage_user_role` VALUES ('2c9182ec-5b1e323d-015b-1e32a7bf-001c', '2c9182d4-5a1c3f70-015a-1c3f7089-0011', 'b93576b7-b919-410b-aee7-122ca4ed53e7', null);
INSERT INTO `manage_user_role` VALUES ('2c9182ec-5ba2b362-015b-a2b3627c-0000', '2c9182d4-5a1c3f70-015a-1c3f7089-0011', 'd0d1cbe1-5966-4751-b6b1-d6c71566c709', null);
INSERT INTO `manage_user_role` VALUES ('2d214d2b-da65-479a-adf4-98926539a742', '2c9182d4-5a1c3f70-015a-1c3f7089-0000', '15fb08a1-6cc2-400c-b2bd-e979a1f177bc', null);
INSERT INTO `manage_user_role` VALUES ('3a42b6ae-256d-449e-8c80-e890b188715e', '2c9182d4-5a1c3f70-015a-1c3f7089-0000', 'cf695c8f-844d-47e3-9219-31cd7af07e99', null);
INSERT INTO `manage_user_role` VALUES ('40fb1e05-c58c-413a-b917-ae53611a847d', '2c9182d4-5a1c3f70-015a-1c3f7089-0011', 'd141e096-0dbe-4a2d-8821-425b0d0e99a0', null);
INSERT INTO `manage_user_role` VALUES ('42a89b4a-d563-40a4-a179-255c771970bb', '2c9182d4-5a1c3f70-015a-1c3f7089-0000', '3016ad12-da43-48bd-aa0b-6f83ab2068e8', null);
INSERT INTO `manage_user_role` VALUES ('50997fde-9335-47ef-a6d2-823bb96a1abc', '2c9182d4-5a1c3f70-015a-1c3f7089-0011', '57523de9-7261-4bb6-929f-a9e07e3cb50c', null);
INSERT INTO `manage_user_role` VALUES ('5454f465-5583-4c5f-893c-fd372564d409', '2c9182d4-5a1c3f70-015a-1c3f7089-0000', '7ebbb432-fbf8-44d2-8fad-e9faddae970c', null);
INSERT INTO `manage_user_role` VALUES ('5ea77129-1ae4-4916-a7fb-475494df4cec', '2c9182d4-5a1c3f70-015a-1c3f7089-0000', '74d5ecdf-6181-4109-9b0f-fcab56c17fd7', null);
INSERT INTO `manage_user_role` VALUES ('6194a523-9911-4007-b8e6-239807b7802d', '2c9182d4-5a1c3f70-015a-1c3f7089-0000', '89cb9fa7-5d48-4f84-a39a-30e64dbf7b83', null);
INSERT INTO `manage_user_role` VALUES ('62028590-3a60-470d-806e-bee9999869b5', '2c9182d4-5a1c3f70-015a-1c3f7089-0000', '0170b0fd-ff98-43d5-b087-1d75be53980d', null);
INSERT INTO `manage_user_role` VALUES ('6db8a937-efc0-411a-a175-9f14482ef70b', '2c9182d4-5a1c3f70-015a-1c3f7089-0000', '27569f0e-09b3-41de-b2f6-07072e909b3a', null);
INSERT INTO `manage_user_role` VALUES ('7a30ffcf-ec5f-4da1-9f78-ee563b32147c', '2c9182d4-5a1c3f70-015a-1c3f7089-1112', 'd1561c88-c599-43ed-8bf1-eacd2070dc16', null);
INSERT INTO `manage_user_role` VALUES ('8ab6ac9a-65f3-4a66-9314-6d68bf2bc564', '2c9182d4-5a1c3f70-015a-1c3f7089-0000', '636804c1-c25e-4253-a940-c540f0333d0b', null);
INSERT INTO `manage_user_role` VALUES ('8bec26e7-f686-4421-a399-8eac9a09d211', '2c9182d4-5a1c3f70-015a-1c3f7089-0000', 'af39c3ba-bd5d-4e56-80f1-e2e3ced1922b', null);
INSERT INTO `manage_user_role` VALUES ('9899485a-c653-4a55-8256-04715e8e4d4b', '2c9182d4-5a1c3f70-015a-1c3f7089-0011', 'b6ddfc2c-72e6-47da-90f1-633e98e136d1', null);
INSERT INTO `manage_user_role` VALUES ('9d9ef736-7bbf-4ef1-b099-c879d34357d4', '2c9182d4-5a1c3f70-015a-1c3f7089-1012', 'b2d27bce-24a9-4df1-9d9c-d8e5c7311b54', null);
INSERT INTO `manage_user_role` VALUES ('a77af218-47a1-4da9-bebd-933002dfa57e', '2c9182fd-5aa690f1-015a-a6c25184-0001', '2ae486ee-3fd3-416a-a748-99e890a7fdda', null);
INSERT INTO `manage_user_role` VALUES ('aad8c883-a965-46a9-ab5d-acf979c23941', '2c9182d4-5a1c3f70-015a-1c3f7089-0000', '97f26c38-d099-4a3b-a9b2-21f8245a315b', null);
INSERT INTO `manage_user_role` VALUES ('b52056ad-4f05-4219-ad44-1e3ecb2cceb9', '2c9182d4-5a1c3f70-015a-1c3f7089-0000', '6922082e-b156-4c09-b7da-30250989b3cd', null);
INSERT INTO `manage_user_role` VALUES ('b663703f-9d05-474b-b3c4-487bd574100e', '2c9182d4-5a1c3f70-015a-1c3f7089-0011', '03c835fc-9de8-4552-8b85-96e8141a418e', null);
INSERT INTO `manage_user_role` VALUES ('c2b9f4de-0fd3-4d0b-b233-e299581503df', '2c9182d4-5a1c3f70-015a-1c3f7089-0000', '3893142b-c7b5-4287-aa42-f3f079ae30ff', null);
INSERT INTO `manage_user_role` VALUES ('c79a1313-b10b-4636-9fc2-65aed77a546f', '2c9182d4-5a1c3f70-015a-1c3f7089-0000', 'd089efd5-5738-4d4f-9331-2802356ba204', null);
INSERT INTO `manage_user_role` VALUES ('cba5802c-5422-4962-9ec8-478a378e87e6', '2c9182d4-5a1c3f70-015a-1c3f7089-0000', 'ab079061-7f29-41de-8a4a-3f9bdecf881b', null);
INSERT INTO `manage_user_role` VALUES ('ce5d5e8e-efd9-4ffd-82b9-a633b784749c', '2c9182d4-5a1c3f70-015a-1c3f7089-0000', '67bcebd4-9710-4de4-b20c-70954a22ee85', null);
INSERT INTO `manage_user_role` VALUES ('d41474da-8393-4005-b326-07576a80e251', '2c9182d4-5a1c3f70-015a-1c3f7089-0000', 'e18edd42-93b3-4f29-a29a-555fa1178337', null);
INSERT INTO `manage_user_role` VALUES ('db7c1b21-39d6-4fb6-976f-3a0257bbda36', '2c9182e7-5adac6cd-015a-dacf96aa-0001', '2498fb7b-c572-4668-804b-7db7d16a3c40', null);
INSERT INTO `manage_user_role` VALUES ('f0713b63-112d-43f8-b573-f0fceab4b8c1', '2c9182d4-5a1c3f70-015a-1c3f7089-0011', 'fbf1ad31-9d7c-41bd-a508-b22ade38da4c', null);
INSERT INTO `manage_user_role` VALUES ('f13c5b75-829b-4e65-94cb-38cfeb09c6eb', '2c9182d4-5a1c3f70-015a-1c3f7089-1012', '2f01b7ed-b746-42e3-80ce-02d0381a7c53', null);
INSERT INTO `manage_user_role` VALUES ('f3c1242e-c85c-424b-a4c2-0ab22a94709a', '2c9182d4-5a1c3f70-015a-1c3f7089-0000', '723349c5-031c-4af6-af19-2f1f12edaf9d', null);
INSERT INTO `manage_user_role` VALUES ('f99f8845-b82c-4528-89ed-ed4c3846fb5d', '2c9182d4-5a1c3f70-015a-1c3f7089-1012', '26f729c6-928f-4337-9d20-b6759d055adf', null);
INSERT INTO `manage_user_role` VALUES ('fcddab8e-746a-4698-bb41-87f8e20fb70e', '2c9182d4-5a1c3f70-015a-1c3f7089-0000', '779b3dff-7c81-46d2-8c77-13469851fd73', null);
INSERT INTO `manage_user_role` VALUES ('ff808081-5b17c0ef-015b-197547a0-0009', '2c9182d4-5a1c3f70-015a-1c3f7089-0011', '210604fc-c791-4420-b36b-f241f9062088', null);
