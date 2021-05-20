package pl.wolniarskim.crm_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.wolniarskim.crm_app.model.Lead;
import pl.wolniarskim.crm_app.model.dto.read.LeadReadModel;
import pl.wolniarskim.crm_app.model.dto.write.LeadWriteModel;
import pl.wolniarskim.crm_app.repository.LeadRepository;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class LeadControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    LeadRepository repository;

    @Autowired
    Flyway flyway;

    @Test
    void shouldReturnErrorMessageAndNotFoundWhenThereIsNoLeadWithGivenId() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/leads/findById/3"))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andReturn();
        String message = result.getResolvedException().getMessage();
        Assertions.assertEquals("Lead with given id doesn't exist",message);
    }

    @Test
    void shouldReturnLeadWithGivenId() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/leads/findById/2"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        LeadReadModel lead = objectMapper.readValue(result.getResponse().getContentAsString(), LeadReadModel.class);
        Assertions.assertEquals(lead.getFirstName(),"Robert");
        Assertions.assertEquals(lead.getLastName(),"Bananowy");
        Assertions.assertEquals(lead.getEmail(),"test2@wp.pl");
    }

    @Test
    void shouldReturnWholeListOfLeads() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/leads/findAll"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        List<Lead> leads = objectMapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);

        Assertions.assertTrue(leads.size() == 2);
    }

    @Test
    void shouldAddLeadToRepository() throws Exception {

        LeadWriteModel writeModel = new LeadWriteModel();
        writeModel.setFirstName("Mateusz");
        writeModel.setLastName("Gruszka");
        writeModel.setEmail("mat@wp.pl");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/leads/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(writeModel)))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        LeadReadModel lead = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), LeadReadModel.class);


        MvcResult mvcResult2 = mockMvc.perform(MockMvcRequestBuilders.get("/api/leads/findById/"+lead.getId()))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        LeadReadModel lead2 = objectMapper.readValue(mvcResult2.getResponse().getContentAsString(), LeadReadModel.class);

        Assertions.assertEquals(lead.getFirstName(), lead2.getFirstName());
        Assertions.assertEquals(lead.getLastName(), lead2.getLastName());
        Assertions.assertEquals(lead.getEmail(), lead2.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenLeadDoesntExistWhileDeleting() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/leads/deleteById/3"))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andReturn();
        String message = result.getResolvedException().getMessage();

        Assertions.assertEquals("Lead with given id doesn't exist", message);
    }

    @Test
    void shouldDeleteLeadFromRepository() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/leads/deleteById/2"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        Assertions.assertEquals(1, repository.findAll().size());
    }

    @Test
    void shouldNotUpdateIdOfTheLeadWhileUpdating() throws Exception {
        Lead lead = new Lead("Kamil","Cebula","cebula@wp.pl");
        lead.setId(5L);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/leads/update?id=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lead)))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/leads/findById/1"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
    }

    @Test
    void shouldThrowExceptionWhenLeadIsUpdatingAndBadIdIsGiven() throws Exception {
        Lead lead = new Lead("Kamil","Cebula","cebula@wp.pl");
        lead.setId(5L);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/leads/update?id=5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lead)))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andReturn();
        String message = result.getResolvedException().getMessage();

        Assertions.assertEquals(message, "Lead with given id doesn't exist");
    }

    @BeforeEach
    void addTestData(){
        flyway.clean();
        flyway.migrate();
        Lead lead = new Lead("Michal","Testowy","test@wp.pl");
        lead.setId(1L);
        Lead lead2 = new Lead("Robert","Bananowy","test2@wp.pl");
        lead2.setId(2L);
        repository.save(lead);
        repository.save(lead2);
    }
}