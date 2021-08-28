package service.sitter.requests;

import service.sitter.models.Request;

public interface IRequestAdapterListener {
    void onRequestClick(Request request);
}
