package nbdream.image.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@Getter
public class GcpStorageProperties {

    public static final String BASIC_PATH = "https://storage.googleapis.com/nbdream_bucket_1/";

    private final String credentials;
    private final String bucketName;
    private final InputStream credentialKey;

    public GcpStorageProperties(@Value("${spring.cloud.gcp.storage.credentials.location}")String credentials,
                                @Value("${spring.cloud.gcp.storage.bucket}")String bucketName) throws IOException {
        this.credentials = credentials;
        this.bucketName = bucketName;
        this.credentialKey = ResourceUtils.getURL(credentials).openStream();
    }
}