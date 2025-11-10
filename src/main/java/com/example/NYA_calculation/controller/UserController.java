package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.constant.DepartmentConstants;
import com.example.NYA_calculation.controller.form.UserForm;
import com.example.NYA_calculation.converter.UserConverter;
import com.example.NYA_calculation.error.RecordNotFoundException;
import com.example.NYA_calculation.repository.entity.User;
import com.example.NYA_calculation.security.LoginUserDetails;
import com.example.NYA_calculation.service.UserService;
import com.example.NYA_calculation.validation.CreateGroup;
import com.example.NYA_calculation.validation.SettingGroup;
import com.example.NYA_calculation.validation.UpdateGroup;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.groups.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static com.example.NYA_calculation.validation.ErrorMessages.E0013;
import static com.example.NYA_calculation.validation.ErrorMessages.E0020;

@Controller
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    UserConverter userConverter;

    //個人設定画面
    @GetMapping("/setting/{id}")
    public ModelAndView settingUser(@AuthenticationPrincipal LoginUserDetails loginUser,
                                    @PathVariable String id,
                                    RedirectAttributes attributes) {

        List<String> errorMessages = new ArrayList<>();
        if (StringUtils.isBlank(id) || !id.matches("^[0-9]+$")) {
            errorMessages.add(E0013);
            attributes.addFlashAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/");
        }

        UserForm user = userService.findFormById(Integer.valueOf(id));
        if (user == null) {
            errorMessages.add(E0013);
            attributes.addFlashAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/");
        }
        List<User> approvers = userService.getApprovers();

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/setting");
        mav.addObject("formModel", user);
        mav.addObject("approvers", approvers);
        mav.addObject("loginUser", loginUser);
        return mav;
    }

    //個人設定処理
    @PostMapping("/setting/update/{id}")
    public String updateUser(@ModelAttribute("formModel") @Validated({Default.class, SettingGroup.class}) UserForm userForm,
                             BindingResult result,
                             Model model) {

        if (result.hasErrors()) {
            model.addAttribute("formModel", userForm);
            model.addAttribute("approvers", userService.getApprovers());
            return "setting";
        }

        boolean success = userService.updateUser(userForm);

        if (!success) {
            model.addAttribute("accountError", E0020);
            model.addAttribute("formModel", userForm);
            List<User> approvers = userService.getApprovers();
            model.addAttribute("approvers", approvers);
            return "setting";
        }

        return "redirect:/";
    }

    //ユーザー登録画面表示（管理者用）
    @GetMapping("/admin/users/new")
    public String adminNewUser(Model model){
        List<User> approvers = userService.getApprovers();
        model.addAttribute("approvers", approvers);
        model.addAttribute("userForm", new UserForm());
        model.addAttribute("departments", DepartmentConstants.DEPARTMENTS);
        return "admin/users/new";
    }

    //ユーザー登録機能（管理者用）
    @PostMapping("/admin/users/add")
    public String adminAddUser(@ModelAttribute("userForm") @Validated({Default.class, CreateGroup.class}) UserForm userForm,
                               BindingResult result,
                               Model model) {

        if (result.hasErrors()) {
//            System.out.println("---- バリデーションエラー発生 ----");
//            result.getFieldErrors().forEach(error -> {
//                System.out.println("フィールド: " + error.getField());
//                System.out.println("メッセージ: " + error.getDefaultMessage());
//            });
//            result.getGlobalErrors().forEach(error -> {
//                System.out.println("グローバルエラー: " + error.getDefaultMessage());
//            });
//            System.out.println("----------------------------");
            model.addAttribute("validationErrors", result);
            model.addAttribute("userForm", userForm);
            model.addAttribute("approvers", userService.getApprovers());
            model.addAttribute("departments", DepartmentConstants.DEPARTMENTS);
            return "admin/users/new";
        }

        boolean success = userService.addUser(userForm);

        if (!success) {
            model.addAttribute("accountError", E0020);
            model.addAttribute("userForm", userForm);
            model.addAttribute("approvers", userService.getApprovers());
            model.addAttribute("departments", DepartmentConstants.DEPARTMENTS);
            return "admin/users/new";
        }
        return "redirect:/admin/users";
    }

    //ユーザー編集画面表示（管理者用）
    @GetMapping("/admin/users/edit/{id}")
    public String adminEditUser(@PathVariable String id,
                                @RequestParam(required = false) String referer,
                                HttpSession session,
                                @AuthenticationPrincipal LoginUserDetails loginUser,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        // 管理者チェック
        if (loginUser.getAuthorities().stream()
                .noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            redirectAttributes.addFlashAttribute("errorMessages", List.of(E0013));
            return "redirect:/admin/users";
        }

        // ID形式チェック
        if (id == null || id.isBlank() || !id.matches("^[0-9]+$")) {
            redirectAttributes.addFlashAttribute("errorMessages", List.of(E0013));
            return "redirect:/admin/users";
        }

        Integer userId = Integer.valueOf(id);

        try {
            User user = userService.findById(userId);
            UserForm userForm = userConverter.toForm(user);

            if (referer != null) {
                session.setAttribute("lastPage", referer);
            }

            model.addAttribute("departments", DepartmentConstants.DEPARTMENTS);
            model.addAttribute("userForm", userForm);
            return "admin/users/edit";
        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessages", List.of(e.getMessage()));
            return "redirect:/admin/users";
        }
    }


    //ユーザー編集処理（管理者用）
    @PostMapping("/admin/users/update/{id}")
    public String adminUpdateUser(@PathVariable Integer id,
                                  @ModelAttribute("userForm") @Validated({UpdateGroup.class}) UserForm userForm,
                                  BindingResult result, Model model){

        if (result.hasErrors()) {
            model.addAttribute("userForm", userForm);
            model.addAttribute("departments", DepartmentConstants.DEPARTMENTS);
            return "admin/users/edit";
        }

        boolean success = userService.adminEditUser(userForm);

        if (!success) {
            model.addAttribute("accountError", E0020);
            model.addAttribute("userForm", userForm);
            model.addAttribute("departments", DepartmentConstants.DEPARTMENTS);
            return "admin/users/edit";
        }

        return "redirect:/admin/users";
    }
}
