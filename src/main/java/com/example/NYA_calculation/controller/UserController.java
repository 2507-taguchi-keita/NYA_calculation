package com.example.NYA_calculation.controller;

import org.springframework.stereotype.Controller;

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
