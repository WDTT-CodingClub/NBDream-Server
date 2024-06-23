package nbdream.farm.service;

import lombok.RequiredArgsConstructor;
import nbdream.farm.util.Coordinates;
import nbdream.farm.exception.KakaoLocalServiceErrorException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KakaoAsyncService {

    private final KakaoAddressService kakaoAddressService;

    @Async("taskExecutor")
    public CompletableFuture<Coordinates> getCoordinatesAsync(String address) {
        try {
            return CompletableFuture.completedFuture(kakaoAddressService.kakaoAddressSearch(address));
        } catch (Exception e) {
            throw new KakaoLocalServiceErrorException();
        }
    }
}
