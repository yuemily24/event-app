package Controllers;

import Presenters.Presenter;
import UseCases.UserManager;
import Gateways.Gateway;

import java.util.Scanner;

public class SystemController {

    private Gateway newGateway = new Gateway();
    private UserManager users = (UserManager) newGateway.importManager("Users");
    private LoginController login = new LoginController(users);
    private UserController controller;
    private Scanner scanner = new Scanner(System.in);
    private Presenter presenter = new Presenter();


    /**
     * Start up method of the program, prompts user to either log in or sign up.
     */
    public void startup() {
        String input;
        String intRegex = "(-1)|[1-9]+";
        boolean proceed = false;
        do {
            presenter.print("Welcome. To log in, please enter 1; to sign up, please enter 2.");
            input = scanner.nextLine();
            while (!input.matches(intRegex) || Integer.parseInt(input) != 1 && Integer.parseInt(input) != 2) {
                presenter.print("Please enter a valid option.");
                input = scanner.nextLine();
            }
            if (Integer.parseInt(input) == 1) {
                boolean everythingIsFine = this.login();
                if (everythingIsFine) {
                    proceed = true;
                }
            } else {
                boolean everythingIsFine = this.signup();
                if (everythingIsFine) {
                    proceed = true;
                }
            }
        } while (!proceed);

        boolean keepGoing;
        do {
            keepGoing = controller.homepage();
        } while (keepGoing);
        newGateway.exportManager(controller.userManager);

        presenter.print("Log-out successful, closing the program.");
    }

    /**
     * Login method for the program.
     */
    private boolean login() {
        Object[] array = login.login();
        if ((boolean) array[0]) {
            controller = (UserController) array[1];
            controller.setUserManager(users);
            return true;
        }
        return false;
    }

    /**
     * Signup method for the program.
     */
    private boolean signup() {
        Object[] array = login.signup();
        if ((boolean) array[0]) {
            controller = (UserController) array[1];
            controller.setUserManager(users);
            return true;
        }
        return false;
    }
}
