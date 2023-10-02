import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ShowManagerTest {
    private ShowManager showManager = ShowManager.getInstance();
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();


    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void setup_success() {
        int showNum = 1;
        int numOfRows = 3;
        int numSeatsPerRow = 3;
        int cancelWindow = 30;

        showManager.setup(showNum, numOfRows, numSeatsPerRow, cancelWindow);

        Show show = showManager.getShows().get(showNum);
        Seat[][] seats = show.getSeats();
        assertEquals(numOfRows, seats.length);
        assertEquals(numSeatsPerRow, seats[0].length);
    }

    @Test
    public void view_showBookedSeats() {
        int showNum = 1;
        int numOfRows = 2;
        int numSeatsPerRow = 3;
        int cancelWindow = 120;
        int ticketNum_1 = 1;
        int ticketNum_2 = 2;
        String buyerPhoneNum_1 = "123";
        String buyerPhoneNum_2 = "234";
        showManager.setup(showNum, numOfRows, numSeatsPerRow, cancelWindow);
        Show show = showManager.getShows().get(showNum);

        Seat[][] seats = show.getSeats();
        seats[0][0].setTicketNumber(ticketNum_1);
        seats[0][0].setBuyerPhoneNumber(buyerPhoneNum_1);
        seats[1][1].setTicketNumber(ticketNum_2);
        seats[1][1].setBuyerPhoneNumber(buyerPhoneNum_2);

        // Capture the output of the view method
        outputStream.reset();
        showManager.view(showNum);
        String output = outputStream.toString().trim();

        // Define the expected output based on the booked seats
        String expectedOutput = String.format("Tickets booked for show number 1:\n" +
                "TicketNumber: %d, BuyerPhoneNumber: %s, Seat: A1\n" +
                "TicketNumber: %d, BuyerPhoneNumber: %s, Seat: B2", ticketNum_1, buyerPhoneNum_1, ticketNum_2, buyerPhoneNum_2);

        // Compare the actual and expected output
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testAvailability() {
        // Set up a test show with available and booked seats
        int showNumber = 1;
        int numOfRows = 2;
        int numSeatsPerRow = 3;
        int cancelWindow = 120;

        showManager.setup(showNumber, numOfRows, numSeatsPerRow, cancelWindow);
        Show show = showManager.getShows().get(showNumber);

        Seat[][] seats = show.getSeats();
        seats[0][1].setIsAvailable(false);
        seats[1][0].setIsAvailable(false);

        // Capture the output of the availability method
        outputStream.reset();
        showManager.availability(showNumber);
        String output = outputStream.toString().trim();

        // Define the expected output based on the available seats
        String expectedOutput = String.format("Available seats for Show %d:\n[A1, A3, B2, B3]", showNumber);

        // Normalize line separators in both the expected and actual output
        expectedOutput = expectedOutput.replaceAll("\r\n", "\n"); // Normalize to Unix-style line endings
        output = output.replaceAll("\r\n", "\n");

        // Compare the actual and expected output
        assertEquals(expectedOutput, output);
    }

    @Test
    public void book_seatsBecomesNotAvailable() {
        int showNumber = 1;
        int numOfRows = 2;
        int numSeatsPerRow = 3;
        int cancelWindow = 120;
        showManager.setup(showNumber, numOfRows, numSeatsPerRow, cancelWindow);
        Show show = showManager.getShows().get(showNumber);

        // Mock user input for booking
        String seatList = "A1,A2,A3";

        // Call the book method
        showManager.book(showNumber, "123", seatList);

        // Verify that the seat availability has been updated
        Seat[][] seats = show.getSeats();
        for (String seatStr : seatList.split(",")) {
            String[] parts = seatStr.trim().split("(?<=\\D)(?=\\d)");
            char rowChar = parts[0].charAt(0);
            int row = rowChar - 'A';
            int seatNum = Integer.parseInt(parts[1]) - 1;
            Seat targetSeat = seats[row][seatNum];
            assertFalse(targetSeat.getIsAvailable(), "Seat should be booked");
        }
    }

    @Test
    public void cancel_bookedSeatBecomesAvailable() {
        int showNumber = 1;
        int numOfRows = 2;
        int numSeatsPerRow = 3;
        int cancelWindow = 120;
        showManager.setup(showNumber, numOfRows, numSeatsPerRow, cancelWindow);

        // Check that seat has been booked
        Show show = showManager.getShows().get(showNumber);
        Seat[][] seats = show.getSeats();
        int row = 0;
        int seatNum = 0;
        Seat targetSeat = seats[row][seatNum];
        String phoneNumber = "123";
        String seatList = "A1";
        showManager.book(showNumber, phoneNumber, seatList);
        assertFalse(targetSeat.getIsAvailable());

        // Call the cancel method
        int ticketNumber = 1;
        showManager.cancel(ticketNumber, phoneNumber);

        // Verify that the seat availability has been updated
        assertTrue(targetSeat.getIsAvailable());
        assertEquals(-1, targetSeat.getTicketNumber());
        assertNull(targetSeat.getBuyerPhoneNumber());
    }

}