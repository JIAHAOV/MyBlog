package com.study.reproduce.service.impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.StorageClass;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.study.reproduce.confiig.properties.TencentCosProperties;
import com.study.reproduce.service.UploadFileService;
import lombok.Setter;
import org.apache.http.entity.ContentType;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Setter
@Component
@EnableConfigurationProperties(TencentCosProperties.class)
public class TencentCosService implements UploadFileService {
    private String secretId;
    private String secretKey;
    private String bucketName;
    private String region;

    private TencentCosProperties tencentCosProperties;

    public TencentCosService(TencentCosProperties tencentCosProperties) {
        this.tencentCosProperties = tencentCosProperties;
        this.secretId = tencentCosProperties.getSecretId();
        this.secretKey = tencentCosProperties.getSecretKey();
        this.bucketName = tencentCosProperties.getBucketName();
        this.region = tencentCosProperties.getRegion();
    }

    // private TransferManager transferManager;

    //从输入流进行读取并上传到COS
    @Override
    public String SimpleUploadFileFromStream(String key, InputStream inputStream) throws IOException {
        // bucket名需包含appid
        ObjectMetadata metadata = new ObjectMetadata();
        // 从输入流上传必须制定content length, 否则http客户端可能会缓存所有数据，存在内存OOM的情况
        metadata.setContentLength(inputStream.available());
        // 默认下载时根据cos路径key的后缀返回响应的contenttype, 上传时设置contenttype会覆盖默认值
        metadata.setContentType(ContentType.IMAGE_JPEG.toString());
        //bucketName—新对象要上传到的现有桶的名称。
        //key—用于存储新对象的键。
        //input-数据流上传至Qcloud COS。
        //metadata—对象元数据。这至少指定了正在上传的数据流的内容长度。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, metadata);
        // 设置存储类型, 默认是标准(Standard), 低频(standard_ia)
        putObjectRequest.setStorageClass(StorageClass.Standard);
        PutObjectResult result = uploadFile(putObjectRequest);
        if (result != null) {
            return "https://" + bucketName + ".cos." + region + ".myqcloud.com/" + key;
        }
        return null;
    }

    @Override
    public String SimpleUploadFileFromLocal(String key, String filePath) {
        // 根据文件路径创建文件对象
        File localFile = new File(filePath);
        // 创建请求
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
        // 设置存储类型
        putObjectRequest.setStorageClass(StorageClass.Standard);
        PutObjectResult result = uploadFile(putObjectRequest);
        if (result != null) {
            return "https://" + bucketName + ".cos." + region + ".myqcloud.com/" + key;
        }
        return null;
    }

    public PutObjectResult uploadFile(PutObjectRequest putObjectRequest) {
        // 创建凭证
        COSCredentials credentials = new BasicCOSCredentials(secretId, secretKey);
        // 创建客户端配置
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        // 创建客户端
        COSClient client = new COSClient(credentials, clientConfig);
        TransferManager manager = new TransferManager(client);
        try {
            return client.putObject(putObjectRequest);
        } catch (CosServiceException e) {
            e.printStackTrace();
        } finally {
            client.shutdown();
        }
        return null;
    }
}
