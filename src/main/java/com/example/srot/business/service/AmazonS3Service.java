package com.example.srot.business.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.example.srot.business.domain.DefaultResponse;
import com.example.srot.business.service.exceptions.S3Exception;
import com.example.srot.data.model.Investor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.srot.business.domain.DefaultResponse.Status.SUCCESS;

@Service
@Slf4j
public class AmazonS3Service {

    private final AmazonS3 s3client = AmazonS3ClientBuilder.standard()
            .withRegion(Regions.AP_SOUTH_1).build();

    @Value("${srot.amazon.s3.url}")
    private String endpointUrl;
    @Value("${srot.amazon.s3.bucketName}")
    private String bucketName;
    private final AuthenticationService authenticationService;

    @Autowired
    public AmazonS3Service(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }


    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.Private));
    }
    private byte[] downloadFileFromS3Bucket(String fileName){

        try {
            S3ObjectInputStream content = s3client.getObject(bucketName, fileName).getObjectContent();
            return content.readAllBytes();
        } catch (IOException e) {
            log.error("Download unsuccessfull: {}", e.getMessage());
            throw new S3Exception(e.getMessage());
        }

    }
    public void uploadFile(String fileName, MultipartFile multipartFile) {

        try {
            File file = convertMultiPartToFile(multipartFile);
            log.info("Uploading file...");
            uploadFileTos3bucket(fileName, file);
            log.info("Upload successful");
            file.delete();
        } catch (Exception e) {
            log.error("Upload unsuccessful: {}",e.getMessage());
            throw new S3Exception(e.getMessage());
        }
    }

    public void uploadDocuments(MultipartFile file, String docName){

        Investor entity = authenticationService.findAuthenticatedInvestor();
        String fileName = entity.getName().replace(" ","_") + entity.getId() +
                docName;
        log.info("Uploading new {}",docName);
        uploadFile(fileName,file);
    }

    public void deleteFileFromS3Bucket(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        s3client.deleteObject(new DeleteObjectRequest(bucketName + "/", fileName));
    }

    public List<Byte> downloadDocuments(String doc) {
        Investor entity = authenticationService.findAuthenticatedInvestor();
        String fileName = entity.getName().replace(" ","_") + entity.getId() +
                doc;
        List<Byte> response = new ArrayList<>();
        byte[] result = downloadFileFromS3Bucket(fileName);
        for (byte b : result) {
            response.add(b);
        }
        return response;
    }
}
