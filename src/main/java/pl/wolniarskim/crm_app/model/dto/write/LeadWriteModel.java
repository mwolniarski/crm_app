package pl.wolniarskim.crm_app.model.dto.write;

import pl.wolniarskim.crm_app.model.Lead;

public class LeadWriteModel implements IWriteModel<Lead>{

    private String firstName;
    private String lastName;
    private String email;

    @Override
    public Lead toEntity() {
        Lead tmp = new Lead(firstName, lastName, email);
        return tmp;
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

