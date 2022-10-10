package service;

import model.User;

public class EmailVerificationServiceImpl implements EmailVerificationService {
    @Override
    public void scheduleEmailConfirmation(User user) {
        //put user details into email queue
        System.out.println("test");

    }
}
