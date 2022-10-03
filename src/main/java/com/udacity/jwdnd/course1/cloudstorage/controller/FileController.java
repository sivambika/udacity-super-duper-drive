package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Controller
@RequestMapping("/file")

public class FileController {
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication authentication, RedirectAttributes redirectAttributes) {
        //get logged-in user details
        User user = userService.getUser(authentication.getName());
        String fileErrorMessage = null;

        // validate if a file was chosen to upload
        if (!fileUpload.isEmpty()) {
            if (!fileService.isFilenameAvailable(fileUpload.getOriginalFilename())) {
                //validate if a file already exists
                fileErrorMessage = "A file with the same file name already exists.";
                redirectAttributes.addFlashAttribute("fileErrorMessage", fileErrorMessage);
            } else {
                try {
                    // upload the file
                    fileService.createFile(fileUpload, user.getUserId());
                    redirectAttributes.addFlashAttribute("fileSuccessMessage",
                            "You successfully uploaded " +
                                    fileUpload.getOriginalFilename() + " !");
                } catch (IOException exception) {
                    fileErrorMessage = "There was an error creating the file. Please try again.";
                    redirectAttributes.addFlashAttribute("fileErrorMessage", fileErrorMessage);
                }
            }
        } else {
            fileErrorMessage = "Please choose a file to upload!!";
            redirectAttributes.addFlashAttribute("fileErrorMessage", fileErrorMessage);
        }
        return "redirect:/home";
    }

    @GetMapping("/view")
    @ResponseBody
    public ResponseEntity
            <Resource> viewFile(@RequestParam("id") Integer fileId) {
        // grab the file with id
        File file = fileService.getFile(fileId);

        // grab the file data
        InputStreamResource
                resource = new InputStreamResource(new ByteArrayInputStream(file.getFileData()));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment:filename=" + file.getFileName())
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(resource);
    }

    @GetMapping("/delete")
    public String deleteFile(@RequestParam("id") Integer fileId, RedirectAttributes redirectAttributes) {
        // deletes the chosen file
        fileService.deleteFile(fileId);
        redirectAttributes.addFlashAttribute("fileDeleteSuccessMessage", true);
        return "redirect:/home";
    }
}
