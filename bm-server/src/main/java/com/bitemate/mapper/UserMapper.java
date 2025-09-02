package com.bitemate.mapper;

import com.bitemate.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {
    /**
     * Get user by using their openid
     * @param openId
     * @return
     */
    @Select("select * from user where openid = #{openId}")
    User getUserByOpenId(String openId);

    /**
     * Insert New User
     * @param user
     */
    void insertUser(User user);

    /**
     * Get user by user Id
     * @param userId
     * @return
     */
    @Select("select * from user where id = #{userId}")
    User getById(Long userId);

    /**
     * Count new or total user
     * @param map
     * @return
     */
    Integer countUserByMap(Map map);

    /**
     * Find user by email
     * @param email
     * @return
     */
    @Select("select * from user where email = #{email}")
    User checkUser(String email);
}
