package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    //get credentials list for the particular user
    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    List<Credential> getCredentialsForUser(Integer userId);

    //query to update the credential details based on id
    @Update("UPDATE CREDENTIALS SET " +
            "url = #{url}, " +
            "username = #{userName}, " +
            "key = #{key}, " +
            "password=#{password}" +
            " WHERE credentialid = #{credentialId}")
    Integer updateCredential(Credential credential);

    // query to insert a new credential
    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid)" +
            "VALUES(#{url} ,#{userName}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    Integer insert(Credential credential);

    // delete a Credential based on id
    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    void delete(Integer credentialId);


}
