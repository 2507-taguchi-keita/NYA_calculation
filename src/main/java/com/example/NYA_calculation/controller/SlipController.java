package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.constant.SlipConstants;
import com.example.NYA_calculation.controller.form.DetailForm;
import com.example.NYA_calculation.controller.form.SlipForm;
import com.example.NYA_calculation.dto.ApprovalHistoryWithUserDto;
import com.example.NYA_calculation.repository.entity.User;
import com.example.NYA_calculation.security.LoginUserDetails;
import com.example.NYA_calculation.service.ApprovalHistoryService;
import com.example.NYA_calculation.service.DetailService;
import com.example.NYA_calculation.service.SlipService;
import com.example.NYA_calculation.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
                              HttpSession session,
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
        List<ApprovalHistoryWithUserDto> approvalHistoryList = approvalHistoryService.getHistory(id);
        SlipForm slipForm = slipService.getSlip(id);
        slipForm.setDetailForms(detailService.getDetails(slipForm.getId()));

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("approver", approver);
        model.addAttribute("approvalHistoryList", approvalHistoryList);
        model.addAttribute("slipForm", slipForm);
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
        List<ApprovalHistoryWithUserDto> approvalHistoryList = approvalHistoryService.getHistory(id);
        SlipForm slipForm = slipService.getSlip(id);
        slipForm.setDetailForms(detailService.getDetails(slipForm.getId()));

        model.addAttribute("loginUser", User);
        model.addAttribute("approver", approver);
        model.addAttribute("approvalHistoryList", approvalHistoryList);
        model.addAttribute("slipForm", slipForm);

        return "slip/approval";
    }

    @PostMapping("/temporary")
    public String saveSlip(@ModelAttribute("slipForm") SlipForm slipForm,
                           @AuthenticationPrincipal LoginUserDetails loginUserDetails) {

        slipForm.setUserId(loginUserDetails.getUser().getId());
        slipForm.setApproverId(loginUserDetails.getUser().getApproverId());
        slipForm.setStatus(0);
        slipForm.setStep(0);

        slipService.saveSlip(slipForm);

        return "redirect:/list/temporary";
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
        slipForm.setStatus(1);
        slipForm.setStep(1);
        slipService.saveSlip(slipForm);

        return "redirect:/";
    }

    @PostMapping("/approval")
    public String approveSlip(@ModelAttribute("slipForm") SlipForm slipForm,
                              @AuthenticationPrincipal LoginUserDetails loginUserDetails) {

        User approver = userService.findById(loginUserDetails.getUser().getApproverId());
        SlipForm targetSlip = slipService.getSlip(slipForm.getId());
        int currentStep = targetSlip.getStep();

        switch (currentStep) {
            case 1:
                if (approver.getDepartmentId() == 1 && approver.getAuthority() == 0) {
                    targetSlip.setStep(2);
                }
                break;

            case 2:
                if (approver.getDepartmentId() == 1 && approver.getAuthority() == 1) {
                    targetSlip.setStep(3);
                }
                break;

            case 3:
                if (approver.getAuthority() == 1) {
                    targetSlip.setStep(4);
                    targetSlip.setStatus(3);
                }
                break;
        }

        slipService.saveSlip(targetSlip);
        approvalHistoryService.saveApprovalHistory(targetSlip.getId(), loginUserDetails.getUser().getId(), currentStep);

        return "redirect:/";
    }

    @PostMapping("/remand")
    public String remandSlip(@ModelAttribute("slipForm") SlipForm slipForm,
                             @AuthenticationPrincipal LoginUserDetails loginUserDetails) {

        SlipForm targetSlip = slipService.getSlip(slipForm.getId());
        targetSlip.setStatus(2);
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
        model.addAttribute("type", resolveType(type));
        model.addAttribute("detailForm", new DetailForm());
        model.addAttribute("reasonList", SlipConstants.REASONS);
        model.addAttribute("transportList", SlipConstants.TRANSPORTS);

        return "fragments/slipFragment :: detailFragment";
    }

    private String resolveType(String type) {
        if (type == null || type.isBlank()) {
            return "temporary"; // デフォルト
        }

        return switch (type) {
            case "new", "temporary", "approval", "remand", "confirm" -> type;
            default -> "temporary";
        };
    }

}
