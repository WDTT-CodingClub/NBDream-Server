package nbdream.image.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import nbdream.image.exception.GcsConnectionException;
import nbdream.image.exception.InvalidDomainException;
import nbdream.image.infrastructure.GcpStorageProperties;
import nbdream.image.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static nbdream.image.infrastructure.GcpStorageProperties.BASIC_PATH;

@Service
@RequiredArgsConstructor
public class TestImageService {

    private final GcpStorageProperties gcpStorageProperties;
    private final ImageRepository imageRepository;


    public String uploadTestImage(MultipartFile image) {
        try {

            String uuid = UUID.randomUUID().toString();
            String ext = image.getContentType();

            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(gcpStorageProperties.getCredentialKey()))
                    .build()
                    .getService();

            String blobName = uuid;
            BlobInfo blobInfo = BlobInfo.newBuilder(gcpStorageProperties.getBucketName(), blobName)
                    .setContentType(ext)
                    .build();

            storage.create(blobInfo, image.getBytes());
            return BASIC_PATH + blobName;
        } catch (IOException e) {
            throw new GcsConnectionException();
        }
    }

}
