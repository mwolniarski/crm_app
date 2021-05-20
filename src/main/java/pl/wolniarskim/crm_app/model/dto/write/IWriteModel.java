package pl.wolniarskim.crm_app.model.dto.write;

public interface IWriteModel<T> {
    T toEntity();
}
