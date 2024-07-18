package nbdream.alarm.scheduler;

import jakarta.annotation.PostConstruct;
import nbdream.alarm.scheduler.job.FcmJob;
import nbdream.alarm.scheduler.job.FcmJobListener;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.TimeZone;

@Configuration
public class SchedulerConfiguration implements WebMvcConfigurer {

    private Scheduler scheduler;
    private final ApplicationContext applicationContext;

    private static String APPLICATION_NAME = "appContext";

    public SchedulerConfiguration(Scheduler sch, ApplicationContext applicationContext) {
        this.scheduler = sch;
        this.applicationContext = applicationContext;
    }

    /**
     * FCM 전송을 위한 스케줄러 구성
     */
    @PostConstruct
    private void configScheduler() throws SchedulerException {

        JobDataMap ctx = new JobDataMap();
        ctx.put(APPLICATION_NAME, applicationContext);

        JobDetail job = JobBuilder
                .newJob(FcmJob.class)
                .withIdentity("fcmSendJob", "fcmGroup")
                .withDescription("FCM 처리를 위한 조회 Job")
                .setJobData(ctx)
                .build();

        //매 정시 마다 : 0 0 * * * ?
        //5분 마다    : "0 */5 * * * ?
        //1분 마다     : 0 * * * * ?

        CronTrigger cronTrigger = TriggerBuilder
                .newTrigger()
                .withIdentity("fcmSendTrigger", "fcmGroup")
                .withDescription("FCM 처리를 위한 조회 Trigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 * * * ?")
                        .inTimeZone(TimeZone.getTimeZone("Asia/Seoul")))
                .build();

        scheduler = new StdSchedulerFactory().getScheduler();
        FcmJobListener fcmJobListener = new FcmJobListener();
        scheduler.getListenerManager().addJobListener(fcmJobListener);
        scheduler.start();
        scheduler.scheduleJob(job, cronTrigger);
    }
}
