package service.sitter.ui.parent.connections;

import service.sitter.models.Babysitter;

public interface IOnGettingBabysitterFromDb {
    void babysitterFound(Babysitter babysitter);
}
