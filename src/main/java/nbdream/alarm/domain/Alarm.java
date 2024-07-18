package nbdream.alarm.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nbdream.accountBook.domain.AccountBookHistory;
import nbdream.accountBook.domain.TransactionType;
import nbdream.common.entity.BaseEntity;
import nbdream.member.domain.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Alarm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String fcmToken;

    private boolean commentAlarm;

    private boolean scheduleAlarm;

    public Alarm(final Member member) {
        this.member = member;
    }

    public void updateAlarmStatus(boolean commentAlarm, boolean scheduleAlarm){
        this.commentAlarm = commentAlarm;
        this.scheduleAlarm = scheduleAlarm;
    }

    public void updateFcmToken(String token){
        this.fcmToken = token;
    }
}
