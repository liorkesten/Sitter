package service.sitter.recommendations;

import java.util.List;

import service.sitter.models.Parent;

public interface IGetParents {
    void apply(List<Parent> parents);
}
