public class Seat {
    private Boolean isAvailable = Boolean.TRUE;
    private final int cancelWindow;
    private Boolean canCancel = Boolean.TRUE;

    private int ticketNumber = -1;
    private String buyerPhoneNumber;
    private final SeatCancellationTimer cancellationTimer;

    public Seat(int cancelWindow) {
        this.cancelWindow = cancelWindow;
        this.cancellationTimer = new SeatCancellationTimer(this);
    }

    public void startCancellationTimer() {
        cancellationTimer.start();
    }

    public long getCancelWindow() {
        return this.cancelWindow;
    }

    public boolean getCanCancel() {
        return this.canCancel;
    }

    public void setCanCancel(boolean canCancel) {
        this.canCancel = canCancel;
    }

    public int getTicketNumber() {
        return this.ticketNumber;
    }

    public void setTicketNumber(int ticketCounter) {
        this.ticketNumber = ticketCounter;
    }

    public String getBuyerPhoneNumber() {
        return this.buyerPhoneNumber;
    }

    public void setBuyerPhoneNumber(String phoneNumber) {
        this.buyerPhoneNumber = phoneNumber;
    }

    public boolean getIsAvailable() {
        return this.isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
