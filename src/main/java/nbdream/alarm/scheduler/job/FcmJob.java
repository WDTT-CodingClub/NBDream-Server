package nbdream.alarm.scheduler.job;

import lombok.extern.slf4j.Slf4j;
import nbdream.alarm.domain.AlarmType;
import nbdream.alarm.dto.AlarmScheduleDto;
import nbdream.alarm.dto.FcmSendDto;
import nbdream.alarm.exception.FcmIntenalServerErrorException;
import nbdream.alarm.repository.AlarmHistoryRepository;
import nbdream.alarm.service.AlarmService;
import nbdream.alarm.service.FcmService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.context.ApplicationContext;

import java.util.List;

@Slf4j
public class FcmJob implements Job {

    private FcmService fcmService;
    private AlarmService alarmService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        ApplicationContext appCtx = (ApplicationContext) jobExecutionContext.getJobDetail().getJobDataMap().get("appContext");

        if (fcmService == null) {
            fcmService = appCtx.getBean(FcmService.class);
        }
        if (alarmService == null) {
            alarmService = appCtx.getBean(AlarmService.class);
        }

        List<AlarmScheduleDto> alarmSchedules = alarmService.GetAlarmSchedules();
        log.info("보내야 할 일정 알람 수 :" + String.valueOf(alarmSchedules.size()));
        for (AlarmScheduleDto alarmSchedule : alarmSchedules) {
            if( !alarmSchedule.getAlarm().isScheduleAlarm()) { continue; }

            FcmSendDto fcmSendDto = FcmSendDto.builder()
                    .token(alarmSchedule.getAlarm().getFcmToken())
                    .title(alarmSchedule.createScheduleAlarmTitle())
                    .body(alarmSchedule.createScheduleAlarmBody())
                    .alarmType(AlarmType.SCHEDULE)
                    .targetId(alarmSchedule.getSchedule().getId())
                    .build();
            try {
                fcmService.sendMessageTo(fcmSendDto);
                alarmService.saveAlarmHistory(fcmSendDto, alarmSchedule.getAlarm());
            } catch (Exception e) {
                throw new FcmIntenalServerErrorException();
            }
        }
    }
}