package Controllers;

import Gateways.Gateway;
import Presenters.Presenter;
import UseCases.UserManager;

import java.util.Scanner;

public class LoginController {

    public UserManager manager;
    public Scanner sc = new Scanner(System.in);
    private final Presenter presenter = new Presenter();

    String breaker = "-1";

    /**
     * Constructs a LoginController object.
     *
     * @param manager The UserManager object.
     */
    public LoginController(UserManager manager) {
        this.manager = manager;
    }

    /**
     * Login method for a user, prompts user to enter username and password.
     *
     * @return Sends the user to the correct homepage.
     */
    Object[] login() {
        int output = 5;
        String username;
        do {
            presenter.print("Please enter your username. If you wish to go back to the landing page, enter -1.");
            username = sc.nextLine();
            if (username.matches(breaker)) {
                break;
            }
            presenter.print("Please enter your password.");
            String password = sc.nextLine();
            output = manager.matchUser(username, password);
            if (output != 0 && output != 1 && output != 2 && output != 3) {
                presenter.print("Incorrect username and/or password. Please try again.");
            }
        } while (output != 0 && output != 1 && output != 2 && output != 3);
        if (!username.matches(breaker)) {
            printLoggingInPrompt(username, false);
            if (output == 0) {
                return new Object[]{true, new AttendeeController(username)};
            } else if (output == 1) {
                return new Object[]{true, new OrganizerController(username)};
            } else if (output == 2) {
                return new Object[]{true, new SpeakerController(username)};
            } else {
                return new Object[]{true, new VIPController(username)};
            }
        }
        return new Object[]{false};
    }

    /**
     * Prints the signup/login text after successful login/signup.
     *
     * @param username username of the user who signed in/up
     * @param signup whether it was a signup or a login; true if signup
     */
    private void printLoggingInPrompt(String username, boolean signup) {
        String welcome;
        if (signup) {
            welcome = "Account creation successful. Logging you in now, " + username + ".";
        } else {
            welcome = "Welcome back " + username;
        }
        String line = "##########################################################################";
        String initialization = "Initializing system.";
        presenter.print(welcome);
        presenter.print(line);
        presenter.print(initialization);
    }

    /**
     * Sign up method for a user.
     *
     * @return Sends the user to the correct homepage.
     */
    Object[] signup() {
        String username;
        boolean output = false;
        String userType = "";
        String typeRegex = "[0-3]";
        String alNumRegex = "[A-z0-9]+";

        do {
            presenter.print("Please enter your desired username. Note that your username has to be alphanumeric, cannot be System, and cannot contain spaces.");
            presenter.print("If you wish to go back to the landing page, enter -1.");
            username = sc.nextLine();
            if (username.matches(breaker)) {
                break;
            }
            presenter.print("Please enter your desired password.");
            String password = sc.nextLine();
            presenter.print("Please enter your desired display name.");
            String displayName = sc.nextLine();
            presenter.print("Please enter your user type; 0 = attendee, 1 = organizer, 2 = speaker, 3 = VIP.");
            userType = sc.nextLine();
            if (userType.matches(typeRegex) && username.matches(alNumRegex) && !username.equalsIgnoreCase("system")) {
                output = manager.createUser(username, password, Integer.parseInt(userType), displayName);
            }
            if (!output) {
                presenter.print("Your chosen username is either taken or invalid, and/or you inputted an invalid user type. Please retry.");
            }
        } while (!output);
        if (!username.matches(breaker)) {
            printLoggingInPrompt(username, true);
            Gateway newGateway = new Gateway();
            newGateway.exportManager(manager);
            if (Integer.parseInt(userType) == 0) {
                return new Object[]{true, new AttendeeController(username)};
            } else if (Integer.parseInt(userType) == 1) {
                return new Object[]{true, new OrganizerController(username)};
            } else if (Integer.parseInt(userType) == 2) {
                return new Object[]{true, new SpeakerController(username)};
            } else {
                return new Object[]{true, new VIPController(username)};
            }
        }
        return new Object[]{false};
    }

}
