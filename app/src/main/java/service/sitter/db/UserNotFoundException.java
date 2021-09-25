package service.sitter.db;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String userUID) {
        super(String.format("User not found. userUID: <%s>", userUID));
    }


}
