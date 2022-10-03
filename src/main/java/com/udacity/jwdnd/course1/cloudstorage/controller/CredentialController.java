package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/credential")

public class CredentialController {

    @Autowired
    private UserService userService;
    @Autowired
    private CredentialService credentialService;

    @PostMapping("/save")
    public String handleCredentialDetails(@ModelAttribute Credential credential, Authentication authentication, RedirectAttributes redirectAttributes) {
        //get logged-in user details
        User user = userService.getUser(authentication.getName());
        credential.setUserId(user.getUserId());

        // if the credential id is null, create an entry else update the details
        if (credential.getCredentialId() == null) {
            credentialService.saveCredentials(credential);
            redirectAttributes.addFlashAttribute("credentialStatusMessage", "The credential details has been created successfully!");
        } else {
            credentialService.saveCredentials(credential);
            redirectAttributes.addFlashAttribute("credentialStatusMessage", "The credential details has been updated successfully!");
        }
        return "redirect:/home#nav-credentials";
    }

    @GetMapping("/delete")
    public String deleteCredential(@RequestParam("id") Integer credentialId, RedirectAttributes redirectAttributes) {
        credentialService.deleteCredential(credentialId);
        redirectAttributes.addFlashAttribute("credentialDelMessage", "The credential has been deleted successfully!");
        return "redirect:/home#nav-credentials";
    }
}
