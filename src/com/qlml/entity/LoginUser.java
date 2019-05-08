package com.qlml.entity;

import java.util.Date;

public class LoginUser {
      /**
       *  用户的自增id号
       */
      private Integer userId;

      /**
       *  登录名和userid必须和中心用户表保持一致
       */
      private String loginName;

      /**
       *  0为未激活，1为正常，-1为禁止
       */
      private Integer status;

      /**
       *  公司ID
       */
      private Integer orgId;

      /**
       *  全名
       */
      private String fullName;

      /**
       *  
       */
      private String email;

      /**
       *  创建时间
       */
      private Date createTime;

      /**
       *  系统管理员为1，无权限限制，普通用户默认为0！
       */
      private Boolean systemAdmin;

      /**
       *  公司连接，父-子-中支（ID连接在一起）
       */
      private String orgLinks;

      /**
       *  是否可以授权
       */
      private Integer withOption;

      /**
       *  创建者
       */
      private String createUser;

      public Integer getUserId() {
            return userId;
      }

      public void setUserId(Integer userId) {
            this.userId = userId;
      }

      public String getLoginName() {
            return loginName;
      }

      public void setLoginName(String loginName) {
            this.loginName = loginName == null ? null : loginName.trim();
      }

      public Integer getStatus() {
            return status;
      }

      public void setStatus(Integer status) {
            this.status = status;
      }

      public Integer getOrgId() {
            return orgId;
      }

      public void setOrgId(Integer orgId) {
            this.orgId = orgId;
      }

      public String getFullName() {
            return fullName;
      }

      public void setFullName(String fullName) {
            this.fullName = fullName == null ? null : fullName.trim();
      }

      public String getEmail() {
            return email;
      }

      public void setEmail(String email) {
            this.email = email == null ? null : email.trim();
      }

      public Date getCreateTime() {
            return createTime;
      }

      public void setCreateTime(Date createTime) {
            this.createTime = createTime;
      }

      public Boolean getSystemAdmin() {
            return systemAdmin;
      }

      public void setSystemAdmin(Boolean systemAdmin) {
            this.systemAdmin = systemAdmin;
      }

      public String getOrgLinks() {
            return orgLinks;
      }

      public void setOrgLinks(String orgLinks) {
            this.orgLinks = orgLinks == null ? null : orgLinks.trim();
      }

      public Integer getWithOption() {
            return withOption;
      }

      public void setWithOption(Integer withOption) {
            this.withOption = withOption;
      }

      public String getCreateUser() {
            return createUser;
      }

      public void setCreateUser(String createUser) {
            this.createUser = createUser == null ? null : createUser.trim();
      }
}