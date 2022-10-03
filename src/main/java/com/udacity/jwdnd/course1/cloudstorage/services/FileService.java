package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    // this method returns true if a fileName already exists
    public boolean isFilenameAvailable(String fileName) {
        return fileMapper.getFileByFilename(fileName) == null;
    }

    // this method uploads/creates a file
    public int createFile(MultipartFile fileUpload, Integer userId) throws IOException {
        File file = new File();
        file.setFileSize(String.valueOf(fileUpload.getSize()));
        file.setFileName(fileUpload.getOriginalFilename());
        file.setContentType(fileUpload.getContentType());
        file.setUserId(userId);
        file.setFileData(fileUpload.getBytes());
        return fileMapper.insert(file);
    }

    //this method gets a file to view/download
    public File getFile(Integer fileId) {
        return fileMapper.getFile(fileId);
    }

    //this method deletes a File
    public void deleteFile(Integer fileId) {
        fileMapper.delete(fileId);
    }

    //this method returns list of files for a particular user
    public List<File> getFiles(Integer userId) {
        return fileMapper.getFilesForUser(userId);
    }

}
