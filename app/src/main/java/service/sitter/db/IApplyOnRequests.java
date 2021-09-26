package service.sitter.db;

import java.util.List;

import service.sitter.models.Request;

@FunctionalInterface
public interface IApplyOnRequests {
    void apply(List<Request> requests);
}
