package nbdream.image.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import nbdream.image.config.GcpStorageProperties;
import nbdream.image.dto.ImageDto;
import nbdream.image.exception.GcsConnectionException;
import nbdream.image.exception.ImageDeleteFailException;
import nbdream.image.exception.InvalidDomainException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static nbdream.image.config.GcpStorageProperties.BASIC_PATH;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final GcpStorageProperties gcpStorageProperties;

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

            Storage.BlobSourceOption preCondition =
                    Storage.BlobSourceOption.generationMatch(blob.getGeneration());

            boolean isDeleted = storage.delete(gcpStorageProperties.getBucketName(), blobPath, preCondition);
            if(!isDeleted) {
                throw new ImageDeleteFailException();
            }
        } catch (IOException e) {
            throw new GcsConnectionException();
        }
    }
}
