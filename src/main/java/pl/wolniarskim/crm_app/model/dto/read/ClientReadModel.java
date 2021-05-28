package pl.wolniarskim.crm_app.model.dto.read;

import pl.wolniarskim.crm_app.model.Client;
import pl.wolniarskim.crm_app.model.Lead;

public class ClientReadModel implements IReadModel<Client>{

    private long id;
    private String firstName;
    private String lastName;
    private String email;

    public ClientReadModel() {
    }

    @Override
    public ClientReadModel toModel(Client client) {
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.id = client.getId();
        return this;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
