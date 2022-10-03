package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final UserService userService;
    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;

    public HomeController(UserService userService, FileService fileService, NoteService noteService, CredentialService credentialService) {
        this.userService = userService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }

    @GetMapping()
    public String getHomePage(Authentication authentication, Model model) {
        //get logged-in user details
        User user = userService.getUser(authentication.getName());

        // get all files for the logged-in user
        model.addAttribute("allFiles", fileService.getFiles(user.getUserId()));

        // get all notes for the logged-in user
        model.addAttribute("allNotes", noteService.getNotes(user.getUserId()));

        // get all credentials for the logged-in user
        model.addAttribute("allCredentials", credentialService.getAllCredentials(user.getUserId()));

        return "home";
    }


}
