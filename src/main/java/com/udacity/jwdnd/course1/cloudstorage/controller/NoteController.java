package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private UserService userService;
    @Autowired
    private NoteService noteService;

    @PostMapping("/save")
    public String handleNotes(@ModelAttribute Note note, Authentication authentication, RedirectAttributes redirectAttributes) {
        //get logged-in user details
        User user = userService.getUser(authentication.getName());
        note.setUserId(user.getUserId());

        // if the noteId is null create a note else update the note
        if (note.getNoteId() == null) {
            noteService.createNote(note);
            redirectAttributes.addFlashAttribute("noteStatusMessage", "A note has been created successfully!");
        } else {
            noteService.updateNote(note);
            redirectAttributes.addFlashAttribute("noteStatusMessage", "A note has been updated successfully!");
        }

        return "redirect:/home#nav-notes";
    }

    @GetMapping("/delete")
    public String deleteNote(@RequestParam("id") Integer noteId, RedirectAttributes redirectAttributes) {
        noteService.deleteNote(noteId);
        redirectAttributes.addFlashAttribute("noteDeleteMessage", "The note has been deleted successfully!");
        return "redirect:/home#nav-notes";
    }


}
