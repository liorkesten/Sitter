package service.sitter.db;

import java.util.List;

import service.sitter.models.Connection;

@FunctionalInterface
public interface IApplyOnConnections {
    void apply(List<Connection> connections);
}
