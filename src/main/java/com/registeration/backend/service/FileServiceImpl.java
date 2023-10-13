package com.registeration.backend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.registeration.backend.entity.Documents;
import com.registeration.backend.entity.Teacher;
import com.registeration.backend.repository.DocumentsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
@Service
public class FileServiceImpl implements FileService{
    @Value("${bucketName}")
    private String bucketName;
    @Autowired
    private final AmazonS3 s3;
    private final DocumentsRepo documentsRepo;
    @Autowired
    public FileServiceImpl(AmazonS3 s3, DocumentsRepo documentsRepo) {
        this.s3 = s3;
        this.documentsRepo = documentsRepo;
    }


    @Override
    public List<String> saveFiles(List<MultipartFile> files, Teacher teacher) {
        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String fileType = file.getContentType();
            String formName = file.getName();

            try {
                // Convert the MultipartFile to InputStream
                InputStream inputStream = file.getInputStream();

                // Upload the InputStream to S3 directly
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(fileType);

                s3.putObject(bucketName, fileName, inputStream, metadata);

                String fileUrl = s3.getUrl(bucketName, fileName).toString();

                // Create a Documents object and save it to your database
                Documents documents = Documents.builder()
                    .documentName(fileName)
                    .documentType(fileType)
                    .formName(formName)
                    .teacher(teacher)
                    .url(fileUrl)
                    .build();
                documentsRepo.save(documents);

                fileUrls.add(fileUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return fileUrls;
    }

    @Override
    public byte[] downloadFile(String filename) {
        S3Object obj=s3.getObject(bucketName,filename);
        S3ObjectInputStream objContent=obj.getObjectContent();
        try {
            return IOUtils.toByteArray(objContent);
        }catch (IOException e){
throw new RuntimeException(e);
        }

    }

    @Override
    public String deleteFile(String filename) {
        return null;
    }

    @Override
    public List<String> listAllFiles() {
        return null;
    }

    @Override
    public void deleteDocumentById(Long id) {
        documentsRepo.deleteById(id);
    }

    private File convertMultipartToFile(MultipartFile file) throws IOException {
File converted=new File(file.getOriginalFilename());
        FileOutputStream fos=new FileOutputStream(converted);
        fos.write(file.getBytes());
        fos.close();
        return converted;
    }
}
