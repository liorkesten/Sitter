package service.sitter.recommendations;

import java.util.List;

import service.sitter.models.Babysitter;

public interface IGetBabysitters {
    void apply(List<Babysitter> babysitters);
}
