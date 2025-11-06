package com.example.NYA_calculation.repository;

import com.example.NYA_calculation.repository.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserRepository {

    /**
     * アカウント名でユーザーを検索
     *
     * @param account ログインアカウント
     * @return Optional<User> 該当ユーザー or empty
     */
    Optional<User> findByAccount(@Param("account") String account);

    /**
     * IDでユーザーを検索
     *
     * @param id ログインユーザーID
     * @return Optional<User> 該当ユーザー or empty
     */
    Optional<User> findById(@Param("id") Integer id);

}
