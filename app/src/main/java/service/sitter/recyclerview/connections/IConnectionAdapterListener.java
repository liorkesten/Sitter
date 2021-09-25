package service.sitter.recyclerview.connections;

import service.sitter.models.Connection;

public interface IConnectionAdapterListener {
    void onRequestClick(Connection connection);
}
