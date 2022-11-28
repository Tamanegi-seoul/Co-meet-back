package Tamanegiseoul.comeet.api;

import Tamanegiseoul.comeet.domain.ImageData;
import Tamanegiseoul.comeet.dto.ApiResponse;
import Tamanegiseoul.comeet.dto.ResponseMessage;
import Tamanegiseoul.comeet.dto.user.response.ImageUploadResponse;
import Tamanegiseoul.comeet.service.ImageDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/image")
public class ImageDataController {

    @Autowired
    private ImageDataService imageDataService;

    @PostMapping
    public ResponseEntity<ApiResponse> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        ImageUploadResponse response = imageDataService.uploadImage(file);

        return ApiResponse.of(HttpStatus.OK, ResponseMessage.UPLOADED_FILE, response);
    }

    @GetMapping("/info/{name}")
    public ResponseEntity<ApiResponse> getImageInfoByName(@PathVariable("name") String name){
        ImageData image = imageDataService.getInfoByImageByName(name);

        return ApiResponse.of(HttpStatus.OK, ResponseMessage.FOUND_FILE, image);
    }

    @GetMapping("/{name}")
    public ResponseEntity<?>  getImageByName(@PathVariable("name") String name){
        byte[] image = imageDataService.getImage(name);

        return ApiResponse.of(HttpStatus.OK, ResponseMessage.FOUND_FILE, image);
    }

}
