package service.sitter.db;

import java.util.List;

import service.sitter.models.Parent;

@FunctionalInterface
public interface IApplyOnParents {
    void apply(List<Parent> parents);
}

