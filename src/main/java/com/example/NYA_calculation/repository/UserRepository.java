package com.example.NYA_calculation.repository;

import com.example.NYA_calculation.controller.form.UserForm;
import com.example.NYA_calculation.repository.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserRepository {

    Optional<User> findByAccount(@Param("account") String account);

    Optional<User> findById(@Param("id") Integer id);

    int updateUser(UserForm userForm);
    UserForm findFormById(Integer id);
    // 承認者一覧を取得
    List<User> findApprovers(@Param("departmentId")Integer departmentId);
    List<UserForm> pageUser(@Param("limit") int limit, @Param("offset") int offset);
    int countUsers();
    void updateIsStopped(@Param("id") Integer id, @Param("isStopped") boolean isStopped);
    int insertUser(User user);
    Integer countByAccount(@Param("account") String account, @Param("excludeUserId") Integer excludeUserId);
    int adminEditUser(UserForm userForm);
}
