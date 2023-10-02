import java.util.Timer;
import java.util.TimerTask;

public class SeatCancellationTimer {
    private final Timer timer;
    private final Seat seat;

    public SeatCancellationTimer(Seat seat) {
        this.timer = new Timer(true);
        this.seat = seat;
    }

    public void start() {
        // Schedule the task to run after the cancellation window (in ms)
        timer.schedule(new SeatCancellationTask(), seat.getCancelWindow() * 1000);
    }

    private class SeatCancellationTask extends TimerTask {
        @Override
        public void run() {
            seat.setCanCancel(false);
            System.out.println("\nCancellation window for seat " + seat.getTicketNumber() + " has expired.");
        }
    }
}
