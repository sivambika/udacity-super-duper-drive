package com.udacity.jwdnd.course1.cloudstorage.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class MaxUploadSizeExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exc,
                                                       RedirectAttributes redirectAttributes) {
        //validate file size
        redirectAttributes.addFlashAttribute("fileErrorMessage", "File is too large to upload!!");
        return "redirect:/home";
    }
}
