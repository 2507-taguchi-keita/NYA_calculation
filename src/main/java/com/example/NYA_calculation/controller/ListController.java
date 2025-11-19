package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.dto.SlipWithUserDto;
import com.example.NYA_calculation.repository.entity.User;
import com.example.NYA_calculation.security.LoginUserDetails;
import com.example.NYA_calculation.service.SlipService;
import com.example.NYA_calculation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/list")
public class ListController {

    @Autowired
    SlipService slipService;
    @Autowired
    UserService userService;

    @GetMapping("/temporary")
    public String showTemporaryList(Model model,
                                    @AuthenticationPrincipal LoginUserDetails loginUserDetails,
                                    @RequestParam(required = false) String success,
                                    @RequestParam(defaultValue = "0") int page) {

        List<SlipWithUserDto> slips = slipService.getTemporarySlips(loginUserDetails.getUser().getId());
        List<SlipWithUserDto> sorted = new ArrayList<>(slips);
        sorted.sort(Comparator.comparing(SlipWithUserDto::getId).reversed());
        slips = sorted;

        int pageSize = 10;
        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, slips.size());

        List<SlipWithUserDto> slipList = new ArrayList<>();
        if (fromIndex < slips.size()) {
            slipList = slips.subList(fromIndex, toIndex);
        }

        int totalPages = (int) Math.ceil((double) slips.size() / pageSize);

        for (SlipWithUserDto s : slips) {
            String statusLabel = switch (s.getStatus()) {
                case 0 -> "未保存";
                case 1 -> "一時保存";
                case 2 -> "申請中";
                case 3 -> "差し戻し";
                case 4 -> "承認済み";
                default -> "不明";
            };
            s.setStatusLabel(statusLabel);

            String stepLabel = switch (s.getStep()) {
                case 0 -> "未申請";
                case 1 -> "経理部";
                case 2 -> "経理部長";
                case 3 -> "所属部長";
                case 4 -> "承認済み";
                default -> "不明";
            };
            s.setStepLabel(stepLabel);

            String departmentName = switch (s.getUserId()) {
                case 1 -> "経理部";
                case 2 -> "人事部";
                case 3 -> "営業部";
                default -> "未所属";
            };
            s.setDepartmentName(departmentName);
        }

        model.addAttribute("slipList", slipList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);


        return "list/temporary";
    }

    @GetMapping("/application")
    public String showApplicationList(Model model,
                                      @AuthenticationPrincipal LoginUserDetails loginUserDetails,
                                      @RequestParam(defaultValue = "0") int page) {

        List<SlipWithUserDto> slips = slipService.getApplicationSlips(loginUserDetails.getUser().getId());
        List<SlipWithUserDto> sorted = new ArrayList<>(slips);
        sorted.sort(Comparator.comparing(SlipWithUserDto::getId).reversed());
        slips = sorted;

        int pageSize = 10;
        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, slips.size());

        List<SlipWithUserDto> slipList = new ArrayList<>();
        if (fromIndex < slips.size()) {
            slipList = slips.subList(fromIndex, toIndex);
        }

        int totalPages = (int) Math.ceil((double) slips.size() / pageSize);

        for (SlipWithUserDto s : slips) {
            String statusLabel = switch (s.getStatus()) {
                case 0 -> "未保存";
                case 1 -> "一時保存";
                case 2 -> "申請中";
                case 3 -> "差し戻し";
                case 4 -> "承認済み";
                default -> "不明";
            };
            s.setStatusLabel(statusLabel);

            String stepLabel = switch (s.getStep()) {
                case 0 -> "未申請";
                case 1 -> "経理部";
                case 2 -> "経理部長";
                case 3 -> "所属部長";
                case 4 -> "承認済み";
                default -> "不明";
            };
            s.setStepLabel(stepLabel);

            String departmentName = switch (s.getUserId()) {
                case 1 -> "経理部";
                case 2 -> "人事部";
                case 3 -> "営業部";
                default -> "未所属";
            };
            s.setDepartmentName(departmentName);
        }

        model.addAttribute("slipList", slipList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "list/application";
    }

    //差し戻し一覧表示画面
    @GetMapping("/remand")
    public String showRemandList(Model model,
                                 @AuthenticationPrincipal LoginUserDetails loginUserDetails,
                                 @RequestParam(defaultValue = "0") int page) {

        List<SlipWithUserDto> slips = slipService.getRemandSlips(loginUserDetails.getUser().getId());
        List<SlipWithUserDto> sorted = new ArrayList<>(slips);
        sorted.sort(Comparator.comparing(SlipWithUserDto::getId).reversed());
        slips = sorted;

        int pageSize = 10;
        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, slips.size());

        List<SlipWithUserDto> slipList = new ArrayList<>();
        if (fromIndex < slips.size()) {
            slipList = slips.subList(fromIndex, toIndex);
        }

        int totalPages = (int) Math.ceil((double) slips.size() / pageSize);

        for (SlipWithUserDto s : slips) {
            String statusLabel = switch (s.getStatus()) {
                case 0 -> "未保存";
                case 1 -> "一時保存";
                case 2 -> "申請中";
                case 3 -> "差し戻し";
                case 4 -> "承認済み";
                default -> "不明";
            };
            s.setStatusLabel(statusLabel);

            String stepLabel = switch (s.getStep()) {
                case 0 -> "未申請";
                case 1 -> "経理部";
                case 2 -> "経理部長";
                case 3 -> "所属部長";
                case 4 -> "承認済み";
                default -> "不明";
            };
            s.setStepLabel(stepLabel);

            String departmentName = switch (s.getUserId()) {
                case 1 -> "経理部";
                case 2 -> "人事部";
                case 3 -> "営業部";
                default -> "未所属";
            };
            s.setDepartmentName(departmentName);
        }

        model.addAttribute("slipList", slipList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "list/remand";
    }

    @GetMapping("/approval")
    public String showApprovalList(Model model,
                                   @AuthenticationPrincipal LoginUserDetails loginUserDetails,
                                   @RequestParam(required = false) String success,
                                   @RequestParam(defaultValue = "0") int page) {

        User loginUser = loginUserDetails.getUser();
        List<SlipWithUserDto> slips = slipService.getApprovalSlips(loginUser);
        List<SlipWithUserDto> sorted = new ArrayList<>(slips);
        sorted.sort(Comparator.comparing(SlipWithUserDto::getId).reversed());
        slips = sorted;

        int pageSize = 10;
        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, slips.size());

        List<SlipWithUserDto> slipList = new ArrayList<>();
        if (fromIndex < slips.size()) {
            slipList = slips.subList(fromIndex, toIndex);
        }

        int totalPages = (int) Math.ceil((double) slips.size() / pageSize);

        for (SlipWithUserDto s : slips) {
            String statusLabel = switch (s.getStatus()) {
                case 0 -> "未保存";
                case 1 -> "一時保存";
                case 2 -> "申請中";
                case 3 -> "差し戻し";
                case 4 -> "承認済み";
                default -> "不明";
            };
            s.setStatusLabel(statusLabel);

            String stepLabel = switch (s.getStep()) {
                case 0 -> "未申請";
                case 1 -> "経理部";
                case 2 -> "経理部長";
                case 3 -> "所属部長";
                case 4 -> "承認済み";
                default -> "不明";
            };
            s.setStepLabel(stepLabel);

            String departmentName = switch (s.getUserId()) {
                case 1 -> "経理部";
                case 2 -> "人事部";
                case 3 -> "営業部";
                default -> "未所属";
            };
            s.setDepartmentName(departmentName);
        }

        model.addAttribute("slipList", slipList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "list/approval";
    }

}
