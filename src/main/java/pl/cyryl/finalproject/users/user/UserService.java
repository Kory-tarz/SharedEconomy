package pl.cyryl.finalproject.users.user;

import pl.cyryl.finalproject.users.user.exception.EmailAlreadyRegisteredException;
import pl.cyryl.finalproject.users.authentication.verification.VerificationToken;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUserName(String name);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    User processNewUserRegistration(User user) throws EmailAlreadyRegisteredException;
    void saveRegisteredUser(User user);
    void createVerificationToken(User user, String token);
    Optional<VerificationToken> getVerificationToken(String token);
    User processOAuthLogin(User user);
}
