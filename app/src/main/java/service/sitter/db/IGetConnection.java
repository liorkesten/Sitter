package service.sitter.db;

import service.sitter.models.Connection;

public interface IGetConnection {

    void connectionFound(Connection connection);

    void connectionIsNotExist(String userA, String userB);
}

