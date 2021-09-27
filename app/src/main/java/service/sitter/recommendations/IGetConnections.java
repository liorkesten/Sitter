package service.sitter.recommendations;

import java.util.List;

import service.sitter.models.Connection;

public interface IGetConnections {
    void apply(List<Connection> connections);
}
