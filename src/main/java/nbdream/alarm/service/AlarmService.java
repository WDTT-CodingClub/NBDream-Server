package nbdream.alarm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbdream.alarm.domain.Alarm;
import nbdream.alarm.domain.AlarmHistory;
import nbdream.alarm.domain.AlarmType;
import nbdream.alarm.dto.*;
import nbdream.alarm.exception.AlarmHistoryNotFoundException;
import nbdream.alarm.exception.AlarmNotFoundException;
import nbdream.alarm.exception.FcmIntenalServerErrorException;
import nbdream.alarm.exception.FcmTokenNotFoundException;
import nbdream.alarm.repository.AlarmHistoryRepository;
import nbdream.alarm.repository.AlarmRepository;
import nbdream.bulletin.domain.Bulletin;
import nbdream.farm.domain.Schedule;
import nbdream.farm.repository.SearchScheduleRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final FcmService fcmService;
    private final SearchScheduleRepository searchScheduleRepository;
    private final AlarmHistoryRepository alarmHistoryRepository;

    public AlarmStatusDto getAlarmStatus(Long memberId) {
        Alarm alarm = alarmRepository.findByMemberId(memberId);
        isAlarmValid(alarm);
        return new AlarmStatusDto(alarm.isCommentAlarm(), alarm.isScheduleAlarm());
    }

    public AlarmStatusDto updateAlarmStatus(Long memberId, AlarmStatusDto request) {
        Alarm alarm = alarmRepository.findByMemberId(memberId);
        isAlarmValid(alarm);
        alarm.updateAlarmStatus(request.isCommentAlarm(), request.isScheduleAlarm());
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

    public void sendCommentAlarm(Bulletin bulletin, String nickname) {
        Alarm alarm = alarmRepository.findByMemberId(bulletin.getAuthor().getId());
        isAlarmValid(alarm);
        if(!alarm.isCommentAlarm()){
            return;
        }
        hasToken(alarm);
        try {
            String body = "\"" + nickname + "\"" + "님이 게시글에 새 댓글을 달았습니다.";
            FcmSendDto fcmSendDto = new FcmSendDto(alarm.getFcmToken(), "농부의 꿈", body, bulletin.getId(), AlarmType.COMMENT);
            fcmService.sendMessageTo(fcmSendDto);
            //알람 저장
            saveAlarmHistory(fcmSendDto, alarm);
        }catch (Exception e){
            throw new FcmIntenalServerErrorException();
        }
    }

    public void saveAlarmHistory(FcmSendDto fcmSendDto, Alarm alarm){
        AlarmHistory alarmHistory = new AlarmHistory(alarm, fcmSendDto.getAlarmType(), fcmSendDto.getTitle(), fcmSendDto.getBody(), false);
        alarmHistoryRepository.save(alarmHistory);
    }

    //알람을 보내야 할 일정들과 토큰을 반환
    public List<AlarmScheduleDto> GetAlarmSchedules(){
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul")).truncatedTo(ChronoUnit.MINUTES);
        List<AlarmScheduleDto> alarmSchedules = searchScheduleRepository.findAlarmsForSchedules(now);
        return alarmSchedules;
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
    
    public AlarmHistoryListResDto getAlarmHistory(Long memberId) {
        Alarm alarm = alarmRepository.findByMemberId(memberId);
        List<AlarmHistory> alarmHistories = alarmHistoryRepository.findByAlarmId(alarm.getId());
        return AlarmHistoryListResDto.from(alarmHistories);
    }

    public void checkAlarmHistory(AlarmHistoryCheckReqDto request) {
        for(Long id : request.idList()){
            AlarmHistory alarmHistory = alarmHistoryRepository.findById(id).orElseThrow(AlarmHistoryNotFoundException::new);
            alarmHistory.alarmCheck();
            alarmHistoryRepository.save(alarmHistory);
        }
    }

    public void deleteAlarmHistory(AlarmHistoryDeleteReqDto request) {
        for(Long id : request.idList()) {
            AlarmHistory alarmHistory = alarmHistoryRepository.findById(id).orElseThrow(AlarmHistoryNotFoundException::new);
            alarmHistoryRepository.delete(alarmHistory);
        }
    }
}
