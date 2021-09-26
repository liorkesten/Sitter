package service.sitter.db;

import service.sitter.models.Connection;
import service.sitter.models.User;

public interface IGetConnection {

    void connectionFound(Connection connection);

    void connectionIsNotExist(User userA, User userB);
}

