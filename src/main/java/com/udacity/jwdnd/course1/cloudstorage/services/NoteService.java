package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    // this method creates a note
    public int createNote(Note note) {
        return noteMapper.insert(note);
    }

    //this method edit/updates a note.
    public int updateNote(Note note) {
        return noteMapper.updateNote(note);
    }

    //this method deletes a Note
    public void deleteNote(Integer noteId) {
        noteMapper.delete(noteId);
    }

    //this method returns list of notes for a particular user
    public List<Note> getNotes(Integer userId) {
        return noteMapper.getNotesForUser(userId);
    }

}
