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
import pl.wolniarskim.crm_app.model.Client;
import pl.wolniarskim.crm_app.model.Lead;
import pl.wolniarskim.crm_app.model.dto.read.ClientReadModel;
import pl.wolniarskim.crm_app.model.dto.read.LeadReadModel;
import pl.wolniarskim.crm_app.model.dto.write.ClientWriteModel;
import pl.wolniarskim.crm_app.model.dto.write.LeadWriteModel;
import pl.wolniarskim.crm_app.repository.ClientRepository;
import pl.wolniarskim.crm_app.repository.LeadRepository;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ClientRepository repository;

    @Autowired
    Flyway flyway;

    @Test
    void shouldReturnErrorMessageAndNotFoundWhenThereIsNoClientWithGivenId() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/clients/findById/3"))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andReturn();
        String message = result.getResolvedException().getMessage();
        Assertions.assertEquals("Client with given id doesn't exist",message);
    }

    @Test
    void shouldReturnLeadWithGivenId() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/clients/findById/2"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        ClientReadModel client = objectMapper.readValue(result.getResponse().getContentAsString(), ClientReadModel.class);
        Assertions.assertEquals(client.getFirstName(),"Robert");
        Assertions.assertEquals(client.getLastName(),"Bananowy");
        Assertions.assertEquals(client.getEmail(),"test2@wp.pl");
    }

    @Test
    void shouldReturnWholeListOfLeads() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/clients/findAll"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        List<Client> clients = objectMapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);

        Assertions.assertTrue(clients.size() == 2);
    }

    @Test
    void shouldAddLeadToRepository() throws Exception {

        ClientWriteModel writeModel = new ClientWriteModel();
        writeModel.setFirstName("Mateusz");
        writeModel.setLastName("Gruszka");
        writeModel.setEmail("mat@wp.pl");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/clients/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(writeModel)))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        ClientReadModel client = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ClientReadModel.class);


        MvcResult mvcResult2 = mockMvc.perform(MockMvcRequestBuilders.get("/api/clients/findById/"+client.getId()))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        ClientReadModel client2 = objectMapper.readValue(mvcResult2.getResponse().getContentAsString(), ClientReadModel.class);

        Assertions.assertEquals(client.getFirstName(), client2.getFirstName());
        Assertions.assertEquals(client.getLastName(), client2.getLastName());
        Assertions.assertEquals(client.getEmail(), client2.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenLeadDoesntExistWhileDeleting() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/clients/deleteById/3"))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andReturn();
        String message = result.getResolvedException().getMessage();

        Assertions.assertEquals("Client with given id doesn't exist", message);
    }

    @Test
    void shouldDeleteLeadFromRepository() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/clients/deleteById/2"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        Assertions.assertEquals(1, repository.findAll().size());
    }

    @Test
    void shouldNotUpdateIdOfTheLeadWhileUpdating() throws Exception {
        Client client = new Client("Kamil","Cebula","cebula@wp.pl");
        client.setId(5L);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/clients/update?id=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(client)))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/clients/findById/1"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
    }

    @Test
    void shouldThrowExceptionWhenLeadIsUpdatingAndBadIdIsGiven() throws Exception {
        Client client = new Client("Kamil","Cebula","cebula@wp.pl");
        client.setId(5L);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/clients/update?id=5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(client)))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andReturn();
        String message = result.getResolvedException().getMessage();

        Assertions.assertEquals(message, "Client with given id doesn't exist");
    }

    @BeforeEach
    void addTestData(){
        flyway.clean();
        flyway.migrate();
        Client client = new Client("Michal","Testowy","test@wp.pl");
        client.setId(1L);
        Client client2 = new Client("Robert","Bananowy","test2@wp.pl");
        client2.setId(2L);
        repository.save(client);
        repository.save(client2);
    }
}