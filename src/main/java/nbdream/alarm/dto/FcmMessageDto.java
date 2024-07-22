package nbdream.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FcmMessageDto {
    private boolean validateOnly;
    private FcmMessageDto.Message message;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        private FcmMessageDto.Notification notification;
        private FcmMessageDto.Data data;
        private String token;
        private FcmMessageDto.Android android;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Data {
        private String targetId;
        private String alarmType;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Android {
        private Android.Notification notification;

        @Builder
        @AllArgsConstructor
        @Getter
        public static class Notification {
            private String icon;
        }
    }
}

