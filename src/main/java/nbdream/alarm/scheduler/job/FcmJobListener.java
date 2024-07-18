package nbdream.alarm.scheduler.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

public class FcmJobListener implements JobListener {
    @Override
    public String getName() {
        return "hello";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        System.out.println("[-] Job이 실행되기전 수행됩니다");
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
        System.out.println("[-] Job이 실행 취소된 시점 수행됩니다.");

    }

    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        System.out.println("[+] Job이 실행 완료된 시점 수행됩니다.");
    }
}
