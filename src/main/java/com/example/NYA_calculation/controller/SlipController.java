package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.constant.SlipConstants;
import com.example.NYA_calculation.controller.form.DetailForm;
import com.example.NYA_calculation.controller.form.SlipForm;
import com.example.NYA_calculation.repository.entity.Detail;
import com.example.NYA_calculation.repository.entity.DetailTemp;
import com.example.NYA_calculation.repository.entity.User;
import com.example.NYA_calculation.security.LoginUserDetails;
import com.example.NYA_calculation.service.DetailService;
import com.example.NYA_calculation.service.DetailTempService;
import com.example.NYA_calculation.service.SlipService;
import com.example.NYA_calculation.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/slip")
public class SlipController {

    @Autowired
    UserService userService;
    @Autowired
    SlipService slipService;
    @Autowired
    DetailService detailService;
    @Autowired
    DetailTempService detailTempService;

    @GetMapping("/new")
    public String showNewSlip(Model model,
                              @AuthenticationPrincipal LoginUserDetails loginUserDetails) {

        User loginUser = userService.findById(loginUserDetails.getUser().getId());
        User approver = userService.findById(loginUser.getApproverId());

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("approver", approver);
        model.addAttribute("slipForm", new SlipForm());
        model.addAttribute("detailForm", new DetailForm());
        model.addAttribute("reasonList", SlipConstants.REASONS);
        model.addAttribute("transportList", SlipConstants.TRANSPORTS);

        return "slip/new";
    }

    @GetMapping("/temporary/{id}")
    public String showTemporarySlip(Model model,
                                    @PathVariable Integer id,
                                    @AuthenticationPrincipal LoginUserDetails loginUserDetails) {

        User loginUser = userService.findById(loginUserDetails.getUser().getId());
        User approver = userService.findById(loginUser.getApproverId());
        SlipForm slipForm = slipService.getSlip(id);
        List<DetailForm> detailForms = detailService.getDetails(slipForm.getId());

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("approver", approver);
        model.addAttribute("slipForm", slipForm);
        model.addAttribute("detailForms", detailForms);
        model.addAttribute("detailForm", new DetailForm());
        model.addAttribute("reasonList", SlipConstants.REASONS);
        model.addAttribute("transportList", SlipConstants.TRANSPORTS);

        return "slip/temporary";
    }

    @GetMapping("/approval/{id}")
    public String showApprovalSlip(Model model,
                                   @PathVariable Integer id,
                                   @AuthenticationPrincipal LoginUserDetails loginUserDetails) {

        User User = userService.findById(loginUserDetails.getUser().getId());
        User approver = userService.findById(User.getApproverId());
        SlipForm slipForm = slipService.getSlip(id);
        List<DetailForm> detailForms = detailService.getDetails(slipForm.getId());

        model.addAttribute("loginUser", User);
        model.addAttribute("approver", approver);
        model.addAttribute("slipForm", slipForm);
        model.addAttribute("detailForms", detailForms);
        model.addAttribute("reasonList", SlipConstants.REASONS);
        model.addAttribute("transportList", SlipConstants.TRANSPORTS);

        return "slip/approval";
    }

    @PostMapping("/temporary")
    public String saveSlip(@ModelAttribute("slipForm") SlipForm slipForm,
                           @AuthenticationPrincipal LoginUserDetails loginUserDetails,
                           HttpSession session) throws IOException {

        SlipForm result = new SlipForm();
        if (slipForm.getId() == null) {
            User loginUser = userService.findById(loginUserDetails.getUser().getId());
            slipForm.setUserId(loginUser.getId());
            slipForm.setApproverId(loginUser.getApproverId());
            slipForm.setStatus(0);
            slipForm.setStep(0);
            result = slipService.createSlip(slipForm);
        } else {
            slipForm.setStatus(0);
            slipForm.setStep(0);
            result = slipService.updateSlips(slipForm);
        }

        String tempKey = (String) session.getAttribute("slipTempKey");
        List<DetailTemp> tempDetails = detailTempService.getTempDetails(tempKey);

        for (DetailTemp temp : tempDetails) {
            Detail detail = new Detail();
            BeanUtils.copyProperties(temp, detail);
            detail.setSlipId(result.getId());
            detailService.insert(detail);
        }

        detailTempService.removeTemp(tempKey);
        session.removeAttribute("slipTempKey");

        return "redirect:/";
    }

    @PostMapping("/application")
    public String applySlip(@ModelAttribute("slipForm") SlipForm slipForm,
                            @AuthenticationPrincipal LoginUserDetails loginUserDetails) {

        if (slipForm.getId() == null) {
            User loginUser = userService.findById(loginUserDetails.getUser().getId());
            slipForm.setUserId(loginUser.getId());
            slipForm.setApproverId(loginUser.getApproverId());
            slipForm.setStatus(1);
            slipForm.setStep(1);
            slipService.createSlip(slipForm);
        } else {
            slipForm.setStatus(1);
            slipForm.setStep(1);
            slipService.updateSlips(slipForm);
        }

        return "redirect:/";
    }

}
