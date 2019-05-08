package com.qlm.dao;

import com.qlml.entity.LoginUser;
import com.qlml.entity.LoginUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LoginUserDAODAODAO {
      int countByExample(LoginUserExample example);

      int deleteByExample(LoginUserExample example);

      int deleteByPrimaryKey(Integer userId);

      int insert(LoginUser record);

      int insertSelective(LoginUser record);

      List<LoginUser> selectByExample(LoginUserExample example);

      LoginUser selectByPrimaryKey(Integer userId);

      int updateByExampleSelective(@Param("record") LoginUser record, @Param("example") LoginUserExample example);

      int updateByExample(@Param("record") LoginUser record, @Param("example") LoginUserExample example);

      int updateByPrimaryKeySelective(LoginUser record);

      int updateByPrimaryKey(LoginUser record);
}