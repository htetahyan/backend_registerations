package com.registeration.backend.service;

import com.registeration.backend.entity.Teacher;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<String> saveFiles(List<MultipartFile> file, Teacher teacherId);
byte[] downloadFile(String filename);
String deleteFile(String filename);
List<String> listAllFiles();
}
