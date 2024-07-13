package com.yupi.service;

import com.aliyun.oss.model.OSSObjectSummary;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件服务
 */
public interface FileService {


    /**
     * 文件上传
     *
     * @param file 文件
     * @return {@link String}
     */
    String fileUpload(MultipartFile file);

    /**
     * 获得临时访问
     *
     * @param key 关键
     * @return {@link String}
     */
    String getTmpAccess(String key);

    public String getImgUrl(String fileUrl) ;

    public List<OSSObjectSummary> list();
}