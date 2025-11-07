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
    public String showSlip(Model model,
                           HttpSession session,
                           @AuthenticationPrincipal LoginUserDetails loginUserDetails) {

        String tempKey = (String) session.getAttribute("slipTempKey");
        if (tempKey != null) {
            model.addAttribute("detailTemps", detailTempService.getTempDetails(tempKey));
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

    @PostMapping("/save")
    public String saveSlip(HttpSession session,
                           @AuthenticationPrincipal LoginUserDetails loginUserDetails) throws IOException {

        String tempKey = (String) session.getAttribute("slipTempKey");
        if (tempKey == null) return "redirect:/slip/new";

        // 1. 伝票をINSERTし slipId を取得（仮）
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
