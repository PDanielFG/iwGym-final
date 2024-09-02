package es.uca.iw.fullstackwebapp.user;

import es.uca.iw.fullstackwebapp.user.domain.User;
import es.uca.iw.fullstackwebapp.user.services.EmailService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class EmailFakeService implements EmailService {

    @Override
    public boolean sendRegistrationEmail(User user) {
        String subject = "Welcome";
        String body = "You should activate your account. "
                + "Go to http://localhost:8080/useractivation "
                + "and introduce your email and the following code: "
                + user.getRegisterCode();

        try {
            System.out.println("From: app (testing)");
            System.out.println("To: " + user.getEmail());
            System.out.println("Subject: " + subject);
            System.out.println("Body: " + body);

            int secondsToSleep = 5;
            Thread.sleep(secondsToSleep * 1000);
            System.out.println("Email send simulation done!");
            return true;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public boolean sendReservationStatusEmail(User user, String status) {
        String subject = "Reservation Status Update";
        String body = "Your reservation status has been updated to: " + status;

        try {
            System.out.println("From: app (testing)");
            System.out.println("To: " + user.getEmail());
            System.out.println("Subject: " + subject);
            System.out.println("Body: " + body);

            int secondsToSleep = 5;
            Thread.sleep(secondsToSleep * 1000);
            System.out.println("Email send simulation done!");
            return true;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public boolean sendClassReminderEmail(User user, String classDetails, String classDateTime) {
        String subject = "Class Reminder";
        String body = "This is a reminder for your upcoming class:\n"
                + "Details: " + classDetails + "\n"
                + "Date and Time: " + classDateTime;

        try {
            System.out.println("From: app (testing)");
            System.out.println("To: " + user.getEmail());
            System.out.println("Subject: " + subject);
            System.out.println("Body: " + body);

            int secondsToSleep = 5;
            Thread.sleep(secondsToSleep * 1000);
            System.out.println("Email send simulation done!");
            return true;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
