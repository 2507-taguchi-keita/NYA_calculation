package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.controller.form.UserForm;
import com.example.NYA_calculation.repository.entity.Department;
import com.example.NYA_calculation.repository.entity.User;
import com.example.NYA_calculation.security.LoginUserDetails;
import com.example.NYA_calculation.service.DepartmentService;
import com.example.NYA_calculation.service.UserService;
import com.example.NYA_calculation.validation.CreateGroup;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.groups.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    DepartmentService departmentService;

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
    public String updateUser(@ModelAttribute("formModel") @Validated UserForm userForm,
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
        model.addAttribute("userForm", new UserForm());
        model.addAttribute("departments", departmentService.getDepartments());
        return "admin/users/new";
    }

    //ユーザー登録機能（管理者用）
    @PostMapping("/admin/users/add")
    public String adminAddUser(@ModelAttribute("userForm") @Validated({Default.class, CreateGroup.class}) UserForm userForm,
                               BindingResult result,
                               Model model) {

        if (result.hasErrors()) {
            model.addAttribute("validationErrors", result);
            model.addAttribute("userForm", userForm);
            model.addAttribute("departments", departmentService.getDepartments());
            return "admin/users";
        }

        boolean success = userService.addUser(userForm);

        if (!success) {
            model.addAttribute("accountError", E0020);
            model.addAttribute("userForm", userForm);
            model.addAttribute("departments", departmentService.getDepartments());
            return "admin/users";
        }
        return "redirect:/admin/users";
    }
}
