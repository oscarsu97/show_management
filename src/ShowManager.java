import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowManager {
    private final Map<Integer, Show> shows = new HashMap<>();
    private static ShowManager instance;
    private final Map<Integer, String> ticketNumberMap = new HashMap<>();
    private int ticketCounter = 1;

    private ShowManager() {
    }

    public Map<Integer, Show> getShows() {
        return this.shows;
    }

    public static ShowManager getInstance() {
        if (instance == null) {
            instance = new ShowManager();
        }
        return instance;
    }

    public void setup(int showNum, int numOfRows, int numSeatsPerRow, int cancelWindow) {
        Seat[][] seats = new Seat[numOfRows][numSeatsPerRow];

        for (int row = 0; row < numOfRows; row++) {
            for (int seat = 0; seat < numSeatsPerRow; seat++) {
                seats[row][seat] = new Seat(cancelWindow);
            }
        }

        shows.put(showNum, new Show(seats));
    }

    public void view(int showNumber) {
        Show show = shows.get(showNumber);

        if (show != null) {
            StringBuilder output = new StringBuilder();
            output.append("Tickets booked for show number ").append(showNumber).append(":\n");

            Seat[][] seats = show.getSeats();
            for (int row = 0; row < seats.length; row++) {
                for (int seat = 0; seat < seats[row].length; seat++) {
                    int ticketNumber = seats[row][seat].getTicketNumber();
                    String buyerPhoneNumber = seats[row][seat].getBuyerPhoneNumber();

                    if (ticketNumber != -1 && buyerPhoneNumber != null) {
                        output.append("TicketNumber: ").append(ticketNumber)
                                .append(", BuyerPhoneNumber: ").append(buyerPhoneNumber)
                                .append(", Seat: ").append(getRowLabel(row, seat)).append("\n");
                    }
                }
            }
            System.out.println(output.toString().trim());
        } else {
            System.out.println("Show not found.");
        }
    }

    public void availability(int showNumber) {
        Show show = shows.get(showNumber);
        List<String> availableSeats = new ArrayList<>();
        if (show != null) {
            Seat[][] seats = show.getSeats();
            System.out.println("Available seats for Show " + showNumber + ":");
            for (int row = 0; row < seats.length; row++) {
                for (int seat = 0; seat < seats[row].length; seat++) {
                    if (seats[row][seat].getIsAvailable()) {
                        String rowLabel = getRowLabel(row, seat);
                        availableSeats.add(rowLabel);
                    }
                }
            }
            System.out.println(availableSeats);
        } else {
            System.out.println("Show not found.");
        }
    }


    public void book(int showNumber, String phoneNumber, String seatList) {
        Show show = shows.get(showNumber);

        if (show != null) {
            Seat[][] seats = show.getSeats();
            String[] seatsToBook = seatList.split(",");

            // Check if seats are available and not already booked
            for (String seatStr : seatsToBook) {
                String[] parts = seatStr.trim().split("(?<=\\D)(?=\\d)");
                if (parts.length != 2) {
                    System.out.println("Invalid seat format: " + seatStr);
                    return;
                }

                char rowChar = parts[0].charAt(0);
                int row = rowChar - 'A';
                int seatNum = Integer.parseInt(parts[1]) - 1;

                if (row >= 0 && row < seats.length && seatNum >= 0 && seatNum < seats[row].length) {
                    Seat targetSeat = seats[row][seatNum];

                    if (!targetSeat.getIsAvailable()) {
                        System.out.println("Seat " + seatStr + " is already booked.");
                        return;
                    }
                } else {
                    System.out.println("Seat " + seatStr + " is invalid.");
                    return;
                }
            }

            // All seats are available, proceed with booking
            for (String seatStr : seatsToBook) {
                String[] parts = seatStr.trim().split("(?<=\\D)(?=\\d)");
                char rowChar = parts[0].charAt(0);
                int row = rowChar - 'A';
                int seatNum = Integer.parseInt(parts[1]) - 1;
                Seat targetSeat = seats[row][seatNum];
                targetSeat.setIsAvailable(false);
                targetSeat.setTicketNumber(ticketCounter);
                targetSeat.setBuyerPhoneNumber(phoneNumber);
                targetSeat.startCancellationTimer();
            }
            // Generate a unique ticket number
            int uniqueTicketNumber = ticketCounter++;
            ticketNumberMap.put(uniqueTicketNumber, phoneNumber);
            System.out.println("Booking successful. Ticket number: " + uniqueTicketNumber);

        } else {
            System.out.println("Show not found.");
        }
    }

    public void cancel(int ticketNumber, String phoneNumber) {
        // Check if the provided ticket number exists in the ticketNumberMap
        if (ticketNumberMap.containsKey(ticketNumber)) {
            // Retrieve the associated phone number for the ticket
            String associatedPhoneNumber = ticketNumberMap.get(ticketNumber);

            // Check if the provided phone number matches the phone number associated with the ticket
            if (associatedPhoneNumber.equals(phoneNumber)) {
                // Remove the ticket from the ticketNumberMap
                ticketNumberMap.remove(ticketNumber);

                // Locate the seat(s) associated with the ticket
                for (Show show : shows.values()) {
                    Seat[][] seats = show.getSeats();
                    for (int row = 0; row < seats.length; row++) {
                        for (int seat = 0; seat < seats[row].length; seat++) {
                            Seat targetSeat = seats[row][seat];
                            if (targetSeat.getTicketNumber() == ticketNumber && targetSeat.getCanCancel()) {
                                // Cancel the ticket by updating seat availability and ticket info
                                targetSeat.setIsAvailable(true);
                                targetSeat.setTicketNumber(-1);
                                targetSeat.setBuyerPhoneNumber(null);
                                String rowLabel = getRowLabel(row, seat);
                                System.out.println("Seat " + rowLabel + " has been canceled.");
                            }
                            if (targetSeat.getTicketNumber() == ticketNumber && !targetSeat.getCanCancel()) {
                                System.out.println("Ticket " + ticketNumber + " cannot be canceled as it has passed the cancellation time window.");
                            }
                        }
                    }
                }
            } else {
                System.out.println("Phone number does not match the ticket's associated phone number.");
            }
        } else {
            System.out.println("Ticket number not found.");
        }
    }

    private static String getRowLabel(int row, int seat) {
        char rowLabelChar = (char) ('A' + row);
        return String.valueOf(rowLabelChar) + (seat + 1);
    }
}


