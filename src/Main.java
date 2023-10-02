import enums.CommandEnum;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ShowManager showManager = ShowManager.getInstance();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter a command (or 'exit' to quit): ");
            args = scanner.nextLine().split(" ");

            if (args[0].equals("exit")) {
                System.out.println("Exiting...");
                break;
            }

            processCommand(args, showManager);
        }

        scanner.close();
    }

    private static void processCommand(String[] args, ShowManager showManager) {
        String command = args[0].toUpperCase();
        try {
            switch (CommandEnum.valueOf(command)) {
                case SETUP:
                    handleSetup(args, showManager);
                    break;
                case VIEW:
                    handleView(args, showManager);
                    break;
                case AVAILABILITY:
                    handleAvailability(args, showManager);
                    break;
                case BOOK:
                    handleBooking(args, showManager);
                    break;
                case CANCEL:
                    handleCancellation(args, showManager);
                    break;
                default:
                    System.out.println("Invalid command");
                    break;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid command");
        }
    }

    private static void handleSetup(String[] args, ShowManager showManager) {
        if (args.length != 5) {
            System.out.println("Invalid number of arguments for Setup command.");
            return;
        }

        int showNum = Integer.parseInt(args[1]);
        int numOfRows = Integer.parseInt(args[2]);
        int numSeatsPerRow = Integer.parseInt(args[3]);
        int cancelWindow = Integer.parseInt(args[4]);

        showManager.setup(showNum, numOfRows, numSeatsPerRow, cancelWindow);
    }

    private static void handleView(String[] args, ShowManager showManager) {
        if (args.length != 2) {
            System.out.println("Invalid number of arguments for View command.");
            return;
        }

        int showNumToView = Integer.parseInt(args[1]);
        showManager.view(showNumToView);
    }

    private static void handleAvailability(String[] args, ShowManager showManager) {
        if (args.length != 2) {
            System.out.println("Invalid number of arguments for Availability command.");
            return;
        }
        int showNum = Integer.parseInt(args[1]);
        showManager.availability(showNum);
    }

    private static void handleBooking(String[] args, ShowManager showManager) {
        if (args.length != 4) {
            System.out.println("Invalid number of arguments for Book command.");
            return;
        }
        int showNum = Integer.parseInt(args[1]);
        String phoneNum = args[2];
        String seatList = args[3];
        showManager.book(showNum, phoneNum, seatList);
    }

    private static void handleCancellation(String[] args, ShowManager showManager) {
        if (args.length != 3) {
            System.out.println("Invalid number of arguments for Cancellation command.");
            return;
        }
        int ticketNum = Integer.parseInt(args[1]);
        String phoneNum = args[2];
        showManager.cancel(ticketNum, phoneNum);
    }
}