package com.udacity.jwdnd.course1.cloudstorage.mapper;


import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    //get all notes for the particular user
    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Note> getNotesForUser(Integer userId);

    //query to update the title and description based on id
    @Update("UPDATE NOTES SET notetitle = #{noteTitle}, notedescription = #{noteDescription} " +
            " WHERE noteid = #{noteId}")
    Integer updateNote(Note note);

    //query to create a new note
    @Insert("INSERT INTO NOTES (notetitle, notedescription,userid) " +
            "VALUES(#{noteTitle} ,#{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    Integer insert(Note note);

    // delete a Note based on id
    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    void delete(Integer noteId);
}
