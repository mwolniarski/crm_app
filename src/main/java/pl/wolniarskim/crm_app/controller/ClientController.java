package pl.wolniarskim.crm_app.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import pl.wolniarskim.crm_app.model.dto.read.ClientReadModel;
import pl.wolniarskim.crm_app.model.dto.read.LeadReadModel;
import pl.wolniarskim.crm_app.model.dto.write.ClientWriteModel;
import pl.wolniarskim.crm_app.model.dto.write.LeadWriteModel;
import pl.wolniarskim.crm_app.service.ClientService;
import pl.wolniarskim.crm_app.service.LeadService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/clients")
public class ClientController {

    ClientService service;

    public ClientController(ClientService service){
        this.service = service;
    }

    @GetMapping("/findById/{id}")
    public ClientReadModel findById(@PathVariable("id") long id){
        return service.getById(id);
    }

    @PostMapping("/create")
    public ClientReadModel create(@RequestBody ClientWriteModel writeModel){
        return service.create(writeModel);
    }

    @GetMapping("/findAll")
    public List<ClientReadModel> findAll(){
        return service.getAll();
    }

    @DeleteMapping("/deleteById/{id}")
    public void deleteById(@PathVariable long id){
        service.deleteById(id);
    }

    @PutMapping("/update")
    public ClientReadModel update(@Param("id") long id, @RequestBody ClientWriteModel writeModel){
        return service.update(writeModel,id);
    }
}