package nbdream.alarm.service;

import lombok.RequiredArgsConstructor;
import nbdream.alarm.domain.Alarm;
import nbdream.alarm.dto.AlarmStatusDto;
import nbdream.alarm.dto.FcmSendDto;
import nbdream.alarm.dto.FcmTokenDto;
import nbdream.alarm.exception.AlarmNotFoundException;
import nbdream.alarm.exception.FcmIntenalServerErrorException;
import nbdream.alarm.exception.FcmTokenNotFoundException;
import nbdream.alarm.repository.AlarmRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final FcmService fcmService;

    public AlarmStatusDto getAlarmStatus(Long memberId) {
        Alarm alarm = alarmRepository.findByMemberId(memberId);
        isAlarmValid(alarm);
        return new AlarmStatusDto(alarm.isCommentAlarm(), alarm.isScheduleAlarm());
    }

    public AlarmStatusDto updateAlarmStatus(Long memberId, AlarmStatusDto alarmStatusDto) {
        Alarm alarm = alarmRepository.findByMemberId(memberId);
        isAlarmValid(alarm);
        alarm.updateAlarmStatus(alarmStatusDto.isCommentAlarm(), alarmStatusDto.isScheduleAlarm());
        alarmRepository.save(alarm);
        return new AlarmStatusDto(alarm.isCommentAlarm(), alarm.isScheduleAlarm());
    }

    public void saveFcmToken(Long memberId, FcmTokenDto request) {
        Alarm alarm = alarmRepository.findByMemberId(memberId);
        isAlarmValid(alarm);
        alarm.updateFcmToken(request.token());
        alarmRepository.save(alarm);
    }

    public void deleteFcmToken(Long memberId) {
        Alarm alarm = alarmRepository.findByMemberId(memberId);
        isAlarmValid(alarm);
        alarm.updateFcmToken(null);
        alarmRepository.save(alarm);
    }

    public void sendCommentAlarm(Long memberId) {
        Alarm alarm = alarmRepository.findByMemberId(memberId);
        isAlarmValid(alarm);
        hasToken(alarm);
        if(!alarm.isCommentAlarm()){
            return;
        }
        try {
            fcmService.sendMessageTo(new FcmSendDto(alarm.getFcmToken(), "농부의 꿈", "게시글에 새 댓글이 달렸습니다."));
        }catch (Exception e){
            throw new FcmIntenalServerErrorException();
        }
        System.out.println("댓글 알림 전송 성공!");
    }

    private void isAlarmValid(Alarm alarm){
        if(alarm == null){
            throw new AlarmNotFoundException();
        }
    }

    private void hasToken(Alarm alarm){
        if(alarm.getFcmToken() == null){
            throw new FcmTokenNotFoundException();
        }
    }
}
