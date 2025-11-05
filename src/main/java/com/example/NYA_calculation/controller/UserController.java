package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.controller.form.UserForm;
import io.micrometer.common.util.StringUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static com.example.NYA_calculation.validation.ErrorMessage.E0013;

@Controller
public class UserController {

    //個人設定画面
//    @GetMapping("/setting/{id}")
//    public ModelAndView settingUser(@AuthenticationPrincipal LoginUserDetails loginUser,
//                                    @PathVariable String id,
//                                    RedirectAttributes attributes) {
//
//        List<String> errorMessages = new ArrayList<>();
//        if (StringUtils.isBlank(id) || !id.matches("^[0-9]+$")) {
//            errorMessages.add(E0013);
//            attributes.addFlashAttribute("errorMessages", errorMessages);
//            return new ModelAndView("redirect:/");
//        }
//
//        UserForm user = userService.findById(Integer.valueOf(id));
//        if (user == null) {
//            errorMessages.add(E0013);
//            attributes.addFlashAttribute("errorMessages", errorMessages);
//            return new ModelAndView("redirect:/");
//        }
//
//        ModelAndView mav = new ModelAndView();
//        mav.setViewName("/password-change");
//        mav.addObject("formModel", user);
//        mav.addObject("loginUser", loginUser);
//        return mav;
//    }
}
