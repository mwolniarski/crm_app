package pl.wolniarskim.crm_app.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import pl.wolniarskim.crm_app.model.dto.read.LeadReadModel;
import pl.wolniarskim.crm_app.model.dto.write.LeadWriteModel;
import pl.wolniarskim.crm_app.service.LeadService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/leads")
public class LeadController {

    LeadService service;

    public LeadController(LeadService service){
        this.service = service;
    }

    @GetMapping("/findById/{id}")
    public LeadReadModel findById(@PathVariable("id") long id){
        return service.getById(id);
    }

    @PostMapping("/create")
    public LeadReadModel create(@RequestBody LeadWriteModel writeModel){
        return service.create(writeModel);
    }

    @GetMapping("/findAll")
    public List<LeadReadModel> findAll(){
        return service.getAll();
    }

    @DeleteMapping("/deleteById/{id}")
    public void deleteById(@PathVariable long id){
        service.deleteById(id);
    }

    @PutMapping("/update")
    public LeadReadModel update(@Param("id") long id, @RequestBody LeadWriteModel writeModel){
        return service.update(writeModel,id);
    }
}