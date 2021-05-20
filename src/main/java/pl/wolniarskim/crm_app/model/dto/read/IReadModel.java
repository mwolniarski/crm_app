package pl.wolniarskim.crm_app.model.dto.read;

public interface IReadModel<T> {
    IReadModel toModel(T t);
}
