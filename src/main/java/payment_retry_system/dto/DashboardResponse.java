package payment_retry_system.dto;

public class DashboardResponse {

    private long totalPayments;
    private long pendingPayments;
    private long successfulPayments;
    private long failedPayments;
    private long recoveredPayments;
    private long permanentFailures;
    private long totalRetries;
    private double recoveryRate;


    public DashboardResponse() {
    }


    public DashboardResponse(
            long totalPayments,
            long pendingPayments,
            long successfulPayments,
            long failedPayments,
            long recoveredPayments,
            long permanentFailures,
            long totalRetries,
            double recoveryRate
    ) {
        this.totalPayments = totalPayments;
        this.pendingPayments = pendingPayments;
        this.successfulPayments = successfulPayments;
        this.failedPayments = failedPayments;
        this.recoveredPayments = recoveredPayments;
        this.permanentFailures = permanentFailures;
        this.totalRetries = totalRetries;
        this.recoveryRate = recoveryRate;
    }


    public long getTotalPayments() {
        return totalPayments;
    }

    public void setTotalPayments(long totalPayments) {
        this.totalPayments = totalPayments;
    }


    public long getPendingPayments() {
        return pendingPayments;
    }

    public void setPendingPayments(long pendingPayments) {
        this.pendingPayments = pendingPayments;
    }


    public long getSuccessfulPayments() {
        return successfulPayments;
    }

    public void setSuccessfulPayments(long successfulPayments) {
        this.successfulPayments = successfulPayments;
    }


    public long getFailedPayments() {
        return failedPayments;
    }

    public void setFailedPayments(long failedPayments) {
        this.failedPayments = failedPayments;
    }


    public long getRecoveredPayments() {
        return recoveredPayments;
    }

    public void setRecoveredPayments(long recoveredPayments) {
        this.recoveredPayments = recoveredPayments;
    }


    public long getPermanentFailures() {
        return permanentFailures;
    }

    public void setPermanentFailures(long permanentFailures) {
        this.permanentFailures = permanentFailures;
    }


    public long getTotalRetries() {
        return totalRetries;
    }

    public void setTotalRetries(long totalRetries) {
        this.totalRetries = totalRetries;
    }


    public double getRecoveryRate() {
        return recoveryRate;
    }

    public void setRecoveryRate(double recoveryRate) {
        this.recoveryRate = recoveryRate;
    }
}