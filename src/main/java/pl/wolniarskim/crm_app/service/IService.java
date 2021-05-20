package pl.wolniarskim.crm_app.service;

import pl.wolniarskim.crm_app.model.dto.read.IReadModel;
import pl.wolniarskim.crm_app.model.dto.write.IWriteModel;

import java.util.List;

public interface IService<RM extends IReadModel, WM extends IWriteModel> {

    RM getById(long id);
    List<RM> getAll();
    void deleteById(long id);
    RM create(WM wm);
    RM update(WM rm, long id);
}