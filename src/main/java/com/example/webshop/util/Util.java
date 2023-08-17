package com.example.webshop.util;

import com.example.webshop.models.dto.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Util {

    public static Page<Product> getPage(Pageable page, List<Product> list)
    {
        int start = (int) page.getOffset();
        int end = Math.min((start + page.getPageSize()), list.size());
        Page<Product> productPage = new PageImpl<>(list.subList(start, end), page, list.size());
        return productPage;

    }

    public static String uploadAvatarImage(MultipartFile file,String dir)
    {
        try {
            String imageName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path imagePath = Paths.get(dir + imageName);
            Files.copy(file.getInputStream(), imagePath);
            return imageName;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static List<String> uploadProductImages(List<MultipartFile> files,String dir)
    {
        List<String> productImages=new ArrayList<>();
        try {
            for(MultipartFile file:files)
            {
                String imageName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path imagePath = Paths.get(dir + imageName);
                Files.copy(file.getInputStream(), imagePath);
                productImages.add(imageName);
            }
            return productImages;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
