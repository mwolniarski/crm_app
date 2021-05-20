package pl.wolniarskim.crm_app.service;

import org.springframework.stereotype.Service;
import pl.wolniarskim.crm_app.exceptions.EntityNotFoundException;
import pl.wolniarskim.crm_app.model.Lead;
import pl.wolniarskim.crm_app.model.dto.read.LeadReadModel;
import pl.wolniarskim.crm_app.model.dto.write.LeadWriteModel;
import pl.wolniarskim.crm_app.repository.LeadRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeadService implements IService<LeadReadModel, LeadWriteModel>{

    private LeadRepository leadRepository;

    public LeadService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    @Override
    public LeadReadModel getById(long id) {
        Optional<Lead> tmp = leadRepository.findById(id);
        if(tmp.isPresent())
            return new LeadReadModel().toModel(tmp.get());
        throw new EntityNotFoundException("Lead with given id doesn't exist");
    }

    @Override
    public List<LeadReadModel> getAll() {
        List<Lead> leads = leadRepository.findAll();
        return leads.stream()
                .map(lead -> new LeadReadModel().toModel(lead))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(long id) {
        if(leadRepository.existsById(id))
            leadRepository.deleteById(id);
        else
            throw new EntityNotFoundException("Lead with given id doesn't exist");
    }

    @Override
    public LeadReadModel create(LeadWriteModel writeModel) {
        Lead tmp = leadRepository.save(writeModel.toEntity());
        return new LeadReadModel().toModel(tmp);
    }

    @Override
    public LeadReadModel update(LeadWriteModel writeModel, long id) {
        if (!leadRepository.existsById(id))
            throw new EntityNotFoundException("Lead with given id doesn't exist");
        Lead tmp = writeModel.toEntity();
        tmp.setId(id);
        Lead savedLead = leadRepository.save(tmp);
        return new LeadReadModel().toModel(savedLead);
    }
}
