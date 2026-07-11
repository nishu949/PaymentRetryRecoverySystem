package payment_retry_system.model;

public enum FailureReason {

    NONE,

    NETWORK_ERROR,

    BANK_TIMEOUT,

    CARD_DECLINED,

    INSUFFICIENT_FUNDS,

    OTP_EXPIRED,

    SERVER_ERROR

}