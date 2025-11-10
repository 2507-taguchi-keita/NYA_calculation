package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.controller.form.DetailTempForm;
import com.example.NYA_calculation.security.LoginUserDetails;
import com.example.NYA_calculation.service.DetailTempService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/detail")
public class DetailTempController {

    @Autowired
    DetailTempService detailTempService;

    @PostMapping("/temp")
    @ResponseBody
    public String saveDetailTemp(
            @ModelAttribute DetailTempForm detailTempForm,
            @RequestParam(name = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal LoginUserDetails loginUserDetails,
            HttpSession session
    ) throws IOException {

        String tempKey = (String) session.getAttribute("slipTempKey");
        if (tempKey == null) {
            tempKey = UUID.randomUUID().toString();
            session.setAttribute("slipTempKey", tempKey);
        }

        detailTempForm.setSlipTempKey(tempKey);
        detailTempForm.setUserId(loginUserDetails.getUser().getId());

        int amount = detailTempForm.getAmount() != null ? detailTempForm.getAmount() : 0;
        String roundTrip = detailTempForm.getRoundTrip();

        int subtotal = roundTrip.equals("往復") ? amount * 2 : amount;
        detailTempForm.setSubtotal(subtotal);

        detailTempService.addDetailTemp(detailTempForm);

        return "ok";
    }


}
