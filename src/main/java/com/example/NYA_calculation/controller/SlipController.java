package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.constant.SlipConstants;
import com.example.NYA_calculation.controller.form.DetailForm;
import com.example.NYA_calculation.controller.form.SlipForm;
import com.example.NYA_calculation.repository.entity.Detail;
import com.example.NYA_calculation.repository.entity.DetailTemp;
import com.example.NYA_calculation.repository.entity.Slip;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
                              HttpSession session,
                              @AuthenticationPrincipal LoginUserDetails loginUserDetails) {

        String tempKey = (String) session.getAttribute("slipTempKey");
        if (tempKey != null) {
            model.addAttribute("detailForms", detailTempService.getTempDetails(tempKey));
        }

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


    @GetMapping("/temp/{id}")
    public String showTempSlip(Model model,
                               @PathVariable Integer id,
                               HttpSession session,
                               @AuthenticationPrincipal LoginUserDetails loginUserDetails) {

        User loginUser = userService.findById(loginUserDetails.getUser().getId());
        User approver = userService.findById(loginUser.getApproverId());
        SlipForm slipForm = slipService.getSlip(id);
        List<DetailForm> detailForms = detailService.getDetails(slipForm.getId());

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("approver", approver);
        model.addAttribute("detailForm", new DetailForm());
        model.addAttribute("slipForm", slipForm);
        model.addAttribute("detailForms", detailForms);
        model.addAttribute("reasonList", SlipConstants.REASONS);
        model.addAttribute("transportList", SlipConstants.TRANSPORTS);

        return "slip/temp";
    }

    @GetMapping("/approval/{id}")
    public String showApprovalSlip(Model model,
                                   @PathVariable Integer id,
                                   HttpSession session,
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

    @PostMapping("/save")
    public String saveSlip(HttpSession session,
                           @AuthenticationPrincipal LoginUserDetails loginUserDetails) throws IOException {

        String tempKey = (String) session.getAttribute("slipTempKey");
        if (tempKey == null) return "redirect:/slip/new";

        User loginUser = userService.findById(loginUserDetails.getUser().getId());

        Slip slip = new Slip();
        slip.setStatus(0);
        slip.setStep(0);
        slip.setUserId(loginUser.getId());
        slip.setApproverId(loginUser.getApproverId());

        Integer slipId = slipService.createSlip(slip);

        // 2. detail_temp の一覧取得
        List<DetailTemp> tempDetails = detailTempService.getTempDetails(tempKey);

        // 3. detail にコピーして slipId をセット
        for (DetailTemp temp : tempDetails) {
            Detail detail = new Detail();
            BeanUtils.copyProperties(temp, detail);
            detail.setSlipId(slipId);
            detailService.insert(detail);
        }

        // 4. 一時データ削除
        detailTempService.removeTemp(tempKey);

        // 5. SessionのTempKeyも削除
        session.removeAttribute("slipTempKey");

        return "redirect:/";
    }

}
