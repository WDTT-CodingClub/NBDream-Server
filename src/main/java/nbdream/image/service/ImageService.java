package nbdream.image.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import nbdream.image.infrastructure.GcpStorageProperties;
import nbdream.image.domain.Image;
import nbdream.image.dto.ImageDto;
import nbdream.image.exception.GcsConnectionException;
import nbdream.image.exception.ImageDeleteFailException;
import nbdream.image.exception.InvalidDomainException;
import nbdream.image.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static nbdream.image.infrastructure.GcpStorageProperties.BASIC_PATH;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final GcpStorageProperties gcpStorageProperties;
    private final ImageRepository imageRepository;

    public String uploadImage(String domain, MultipartFile image) {
        try {
            if (domain == null || domain.isEmpty()) {
                throw new InvalidDomainException();
            }
            String uuid = UUID.randomUUID().toString();
            String ext = image.getContentType();

            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(gcpStorageProperties.getCredentialKey()))
                    .build()
                    .getService();

            String blobName = domain + "/" + uuid;
            BlobInfo blobInfo = BlobInfo.newBuilder(gcpStorageProperties.getBucketName(), blobName)
                    .setContentType(ext)
                    .build();

            storage.create(blobInfo, image.getBytes());
            return BASIC_PATH + blobName;
        } catch (IOException e) {
            throw new GcsConnectionException();
        }
    }

    public void deleteImage(ImageDto request) {
        try {
            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(gcpStorageProperties.getCredentialKey()))
                    .build()
                    .getService();

            String blobPath =request.imageUrl().replace(BASIC_PATH, "");
            Blob blob = storage.get(gcpStorageProperties.getBucketName(), blobPath);
            if (blob != null) {
                Storage.BlobSourceOption preCondition =
                        Storage.BlobSourceOption.generationMatch(blob.getGeneration());
                boolean isDeleted = storage.delete(gcpStorageProperties.getBucketName(), blobPath, preCondition);
                if(!isDeleted) {
                    throw new ImageDeleteFailException();
                }
            }

            Optional<Image> image = imageRepository.findByImageUrl(request.imageUrl());
            if (!image.isEmpty()) {
                imageRepository.delete(image.get());
            }

        } catch (IOException e) {
            throw new GcsConnectionException();
        }
    }

    public void saveImageUrls(Long targetId, List<String> imageUrls) {
        imageUrls.stream()
                .forEach(imageUrl -> imageRepository.save(new Image(targetId, imageUrl)));
    }

    public void updateImageUrls(Long targetId, List<String> imageUrls) {
        deleteImageUrls(targetId);
        saveImageUrls(targetId, imageUrls);
    }

    public void deleteImageUrls(Long targetId) {
       imageRepository.deleteAllByTargetId(targetId);
    }

    public void deleteImageUrlsWithImage(List<String> imageUrls) {
        imageRepository.deleteAllByImageUrl(imageUrls);
        for (String imageUrl : imageUrls) {
            deleteImage(new ImageDto(imageUrl));
        }
    }

    public void updateTargetImages(final Long targetId, final List<String> imageUrls) {
        List<Image> images = imageRepository.findAllByTargetId(targetId);
        List<String> savedImageUrls = images.stream().map(image -> image.getImageUrl()).collect(Collectors.toList());
        List<String> deletedImageUrls = new ArrayList<>();

        for (Image image : images) {
            if (!imageUrls.contains(image.getImageUrl())) {
                deletedImageUrls.add(image.getImageUrl());
            }
        }

        deleteImageUrlsWithImage(deletedImageUrls);

        for (String imageUrl : imageUrls) {
            if (!savedImageUrls.contains(imageUrl)) {
                imageRepository.save(new Image(targetId, imageUrl));
            }
        }
    }

    public List<Image> findAllByTargetId(Long targetId) {
        return imageRepository.findAllByTargetId(targetId);
    }
}
