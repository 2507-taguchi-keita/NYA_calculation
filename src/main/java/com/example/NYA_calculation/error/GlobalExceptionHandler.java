package com.example.NYA_calculation.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.example.NYA_calculation.validation.ErrorMessages.E0013;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @PathVariable の変換失敗などを補足
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        redirectAttributes.addFlashAttribute("errorMessage", E0013);

        HttpSession session = request.getSession(false);
        if (session != null) {
            String lastPage = (String) session.getAttribute("lastPage");
            if (lastPage != null) {
                return "redirect:" + lastPage;
            }
        }

        return "redirect:/";
    }

    /**
     * 存在しないIDが指定された場合
     */
    @ExceptionHandler(RecordNotFoundException.class)
    public String handleRecordNotFound(
            RecordNotFoundException ex,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());

        HttpSession session = request.getSession(false);
        if (session != null) {
            String lastPage = (String) session.getAttribute("lastPage");
            if (lastPage != null) {
                return "redirect:" + lastPage;
            }
        }

        return "redirect:/";
    }

    /**
     * ログインユーザー以外のIDが指定された場合
     */
    @ExceptionHandler(UnauthorizedAccessException.class)
    public String handleUnauthorizedAccess(
            UnauthorizedAccessException ex,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());

        HttpSession session = request.getSession(false);
        if (session != null) {
            String lastPage = (String) session.getAttribute("lastPage");
            if (lastPage != null) {
                return "redirect:" + lastPage;
            }
        }

        return "redirect:/";
    }

}
