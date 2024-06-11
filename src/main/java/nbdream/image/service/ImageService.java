package nbdream.image.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@PropertySource("classpath:application.yml")
public class ImageService {
    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String credentials;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    private static final String basicPath = "https://storage.googleapis.com/nbdream_bucket_1/";

    public String saveImage(MultipartFile image, String domain) throws IOException {

        InputStream credentialKey = ResourceUtils.getURL(credentials).openStream();
        String uuid = UUID.randomUUID().toString();
        String ext = image.getContentType();

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(credentialKey))
                .build()
                .getService();

        String blobName = domain + "/" + uuid;
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, blobName)
                .setContentType(ext)
                .build();

        storage.create(blobInfo, image.getBytes());
        return basicPath + blobName;

    }

    public byte[] loadImage(String domain, Long targetId) throws IOException {
        InputStream credentialKey = ResourceUtils.getURL(credentials).openStream();

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(credentialKey))
                .build()
                .getService();

        Blob blob = storage.get(bucketName, domain + "/" + targetId);
        return blob.getContent();
    }

    @Transactional
    public void deleteImage(String domain, String fileName) throws IOException {

        InputStream credentialKey = ResourceUtils.getURL(credentials).openStream();

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(credentialKey))
                .build()
                .getService();

        String blobPath = domain + "/" + fileName;
        Blob blob = storage.get(bucketName, blobPath);

        Storage.BlobSourceOption preCondition =
                Storage.BlobSourceOption.generationMatch(blob.getGeneration());

        boolean deleted = storage.delete(bucketName, blobPath, preCondition);
        if(!deleted) {
            throw new BadRequestException("Failed to delete blob " + blobPath);
        }

    }




}
