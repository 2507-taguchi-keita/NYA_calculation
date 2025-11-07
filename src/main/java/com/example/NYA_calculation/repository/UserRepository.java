package com.example.NYA_calculation.repository;

import com.example.NYA_calculation.controller.form.UserForm;
import com.example.NYA_calculation.repository.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
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

    int updateUser(UserForm userForm);
    UserForm findFormById(Integer id);
    // 承認者一覧を取得
    List<User> findApprovers();
    List<UserForm> pageUser(@Param("limit") int limit, @Param("offset") int offset);
    int countUsers();
    void updateIsStopped(@Param("id") Integer id, @Param("isStopped") boolean isStopped);
    int insertUser(User user);
    Integer countByAccount(@Param("account") String account, @Param("excludeUserId") Integer excludeUserId);
}
