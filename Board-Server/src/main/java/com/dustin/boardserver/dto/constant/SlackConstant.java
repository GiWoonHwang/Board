package com.dustin.boardserver.dto.constant;

// SlackConstant 클래스: 슬랙(Slack) 채널 이름과 관련된 상수들을 정의하는 클래스입니다.
// 이 클래스는 상수를 통해 코드에서 일관되게 슬랙 채널 이름을 사용할 수 있도록 합니다.
public class SlackConstant {

    // MONITOR_CHANNEL: 모니터링 채널의 이름을 상수로 정의합니다.
    // 이 상수는 슬랙에서 모니터링과 관련된 메시지를 전송할 때 사용됩니다.
    public static final String MONITOR_CHANNEL = "#모니터링";

    // WARNING_CHANNEL: 경고 채널의 이름을 상수로 정의합니다.
    // 이 상수는 슬랙에서 경고 메시지를 전송할 때 사용됩니다.
    public static final String WARNING_CHANNEL = "#경고";
}
