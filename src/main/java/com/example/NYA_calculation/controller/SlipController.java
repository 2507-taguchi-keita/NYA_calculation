package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.constant.SlipConstants;
import com.example.NYA_calculation.controller.form.DetailForm;
import com.example.NYA_calculation.controller.form.SlipForm;
import com.example.NYA_calculation.dto.ApprovalHistoryDto;
import com.example.NYA_calculation.dto.UserDto;
import com.example.NYA_calculation.repository.entity.User;
import com.example.NYA_calculation.security.LoginUserDetails;
import com.example.NYA_calculation.service.ApprovalHistoryService;
import com.example.NYA_calculation.service.DetailService;
import com.example.NYA_calculation.service.SlipService;
import com.example.NYA_calculation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/slip")
@SessionAttributes("slipForm")
public class SlipController {

    @Autowired
    UserService userService;
    @Autowired
    SlipService slipService;
    @Autowired
    DetailService detailService;
    @Autowired
    ApprovalHistoryService approvalHistoryService;

    @GetMapping("/new")
    public String showNewSlip(Model model,
                              @AuthenticationPrincipal LoginUserDetails loginUserDetails) {

        Integer userId = loginUserDetails.getUser().getId();
        SlipForm slipForm = new SlipForm();
        slipForm.setStatus(0);
        slipForm.setDetailForms(new ArrayList<>());
        UserDto userDto = userService.getUserDto(userId);

        model.addAttribute("slipForm", slipForm);
        model.addAttribute("userDto", userDto);
        model.addAttribute("approvalHistoryDtoList", new ArrayList<>());
        model.addAttribute("detailForm", new DetailForm());
        model.addAttribute("reasonList", SlipConstants.REASONS);
        model.addAttribute("transportList", SlipConstants.TRANSPORTS);

        return "slip/new";
    }

    @GetMapping("/temporary/{id}")
    public String showTemporarySlip(Model model,
                                    @PathVariable Integer id,
                                    @AuthenticationPrincipal LoginUserDetails loginUserDetails) {

        Integer userId = loginUserDetails.getUser().getId();
        SlipForm slipForm = slipService.getSlipForm(id);
        UserDto userDto = userService.getUserDto(userId);
        List<ApprovalHistoryDto> approvalHistoryDtoList = approvalHistoryService.getApprovalHistoryDtoList(id);

        model.addAttribute("slipForm", slipForm);
        model.addAttribute("userDto", userDto);
        model.addAttribute("approvalHistoryDtoList", approvalHistoryDtoList);
        model.addAttribute("detailForm", new DetailForm());
        model.addAttribute("reasonList", SlipConstants.REASONS);
        model.addAttribute("transportList", SlipConstants.TRANSPORTS);

        return "slip/temporary";
    }

    @GetMapping("/approval/{id}")
    public String showApprovalSlip(Model model,
                                   @PathVariable Integer id,
                                   @AuthenticationPrincipal LoginUserDetails loginUserDetails) {

        Integer userId = loginUserDetails.getUser().getId();
        SlipForm slipForm = slipService.getSlipForm(id);
        UserDto userDto = userService.getUserDto(userId);
        List<ApprovalHistoryDto> approvalHistoryDtoList = approvalHistoryService.getApprovalHistoryDtoList(id);

        model.addAttribute("slipForm", slipForm);
        model.addAttribute("userDto", userDto);
        model.addAttribute("approvalHistoryDtoList", approvalHistoryDtoList);
        model.addAttribute("detailForm", new DetailForm());
        model.addAttribute("reasonList", SlipConstants.REASONS);
        model.addAttribute("transportList", SlipConstants.TRANSPORTS);

        return "slip/approval";
    }

    @PostMapping("/temporary")
    public String saveSlip(@ModelAttribute("slipForm") SlipForm slipForm,
                           @AuthenticationPrincipal LoginUserDetails loginUserDetails) {

        slipForm.setUserId(loginUserDetails.getUser().getId());
        slipForm.setApproverId(loginUserDetails.getUser().getApproverId());
        slipForm.setStatus(1);
        slipForm.setStep(1);

        slipService.saveSlip(slipForm);

        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteSlip(@ModelAttribute("slipForm") SlipForm slipForm,
                             @AuthenticationPrincipal LoginUserDetails loginUserDetails) {

        slipService.deleteSlip(slipForm);

        return "redirect:/list/temporary";
    }

    @PostMapping("/application")
    public String applySlip(@ModelAttribute("slipForm") SlipForm slipForm,
                            @AuthenticationPrincipal LoginUserDetails loginUserDetails) {

        if (slipForm.getId() == null) {
            slipForm.setUserId(loginUserDetails.getUser().getId());
            slipForm.setApproverId(loginUserDetails.getUser().getApproverId());
        }
        slipForm.setStatus(2);
        slipForm.setStep(2);
        slipForm.setApplicationDate(Timestamp.valueOf(LocalDateTime.now()));

        slipService.saveSlip(slipForm);

        return "redirect:/application-list";
    }

    @PostMapping("/approval")
    public String approveSlip(@ModelAttribute("slipForm") SlipForm slipForm,
                              @AuthenticationPrincipal LoginUserDetails loginUserDetails) {

        User approver = userService.findById(loginUserDetails.getUser().getApproverId());
        SlipForm targetSlip = slipService.getSlip(slipForm.getId());
        int currentStep = targetSlip.getStep();

        switch (currentStep) {
            case 1:
                if (approver.getDepartmentId() == 1 && approver.getAuthority() == 1) {
                    targetSlip.setStep(2);
                }
                break;

            case 2:
                if (approver.getDepartmentId() == 1 && approver.getAuthority() == 2) {
                    targetSlip.setStep(3);
                }
                break;

            case 3:
                if (approver.getAuthority() == 3) {
                    targetSlip.setStep(4);
                    targetSlip.setStatus(4);
                }
                break;
        }

        slipService.saveSlip(targetSlip);
        approvalHistoryService.saveApprovalHistory(targetSlip.getId(), loginUserDetails.getUser().getId());

        return "redirect:/list/approval";
    }

    @PostMapping("/remand")
    public String remandSlip(@ModelAttribute("slipForm") SlipForm slipForm,
                             @AuthenticationPrincipal LoginUserDetails loginUserDetails) {

        SlipForm targetSlip = slipService.getSlip(slipForm.getId());
        targetSlip.setStatus(3);
        slipService.saveSlip(targetSlip);

        return "redirect:/";
    }

    @PostMapping("/temp/bulk-add")
    public String bulkAddDetailsFragment(@ModelAttribute("slipForm") SlipForm slipForm,
                                         @RequestParam(required = false) String type,
                                         Model model) {

        slipForm.getDetailForms().forEach(d -> {
            if (d.getTempId() == null || d.getTempId().isEmpty()) {
                d.setTempId(UUID.randomUUID().toString());
            }
            d.setNewFromCsv(false);
            int amount = d.getAmount() != null ? Integer.parseInt(d.getAmount()) : 0;
            d.setSubtotal("往復".equals(d.getRoundTrip()) ? amount*2 : amount);
        });

        int total = slipForm.getDetailForms().stream()
                .mapToInt(d -> d.getSubtotal() != null ? d.getSubtotal() : 0)
                .sum();
        slipForm.setTotalAmount(total);

        boolean hasCsvDetails = slipForm.getDetailForms().stream()
                .anyMatch(DetailForm::isNewFromCsv);

        // modelにセット
        model.addAttribute("slipForm", slipForm);
        model.addAttribute("editable", true);
        model.addAttribute("showCsvOnly", hasCsvDetails);
        model.addAttribute("detailForm", new DetailForm());
        model.addAttribute("reasonList", SlipConstants.REASONS);
        model.addAttribute("transportList", SlipConstants.TRANSPORTS);

        return "fragments/slipFragment :: detailFragment";
    }

}
