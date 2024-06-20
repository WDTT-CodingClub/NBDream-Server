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

    public GcpStorageProperties(@Value("${spring.cloud.gcp.storage.credentials.location}")String credentials,
                                @Value("${spring.cloud.gcp.storage.bucket}")String bucketName){
        this.credentials = credentials;
        this.bucketName = bucketName;
    }

    public InputStream getCredentialKey() throws IOException {
        return ResourceUtils.getURL(credentials).openStream();
    }
}