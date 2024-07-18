package nbdream.alarm.service;

import nbdream.alarm.dto.FcmSendDto;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface FcmService {
    int sendMessageTo(FcmSendDto fcmSendDto) throws IOException;
}
