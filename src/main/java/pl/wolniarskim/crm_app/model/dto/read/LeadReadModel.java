package pl.wolniarskim.crm_app.model.dto.read;

import pl.wolniarskim.crm_app.model.Lead;

public class LeadReadModel implements IReadModel<Lead>{

    private long id;
    private String firstName;
    private String lastName;
    private String email;

    public LeadReadModel() {
    }

    @Override
    public LeadReadModel toModel(Lead lead) {
        this.firstName = lead.getFirstName();
        this.lastName = lead.getLastName();
        this.email = lead.getEmail();
        this.id = lead.getId();
        return this;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}