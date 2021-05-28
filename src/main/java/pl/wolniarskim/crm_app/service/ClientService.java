package pl.wolniarskim.crm_app.service;

import org.springframework.stereotype.Service;
import pl.wolniarskim.crm_app.exceptions.EntityNotFoundException;
import pl.wolniarskim.crm_app.model.Client;
import pl.wolniarskim.crm_app.model.Lead;
import pl.wolniarskim.crm_app.model.dto.read.ClientReadModel;
import pl.wolniarskim.crm_app.model.dto.read.LeadReadModel;
import pl.wolniarskim.crm_app.model.dto.write.ClientWriteModel;
import pl.wolniarskim.crm_app.repository.ClientRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService implements IService<ClientReadModel, ClientWriteModel>{

    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientReadModel getById(long id) {
        Optional<Client> tmp = clientRepository.findById(id);
        if(tmp.isPresent())
            return new ClientReadModel().toModel(tmp.get());
        throw new EntityNotFoundException("Client with given id doesn't exist");
    }

    @Override
    public List<ClientReadModel> getAll() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .map(lead -> new ClientReadModel().toModel(lead))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(long id) {
        if(clientRepository.existsById(id))
            clientRepository.deleteById(id);
        else
            throw new EntityNotFoundException("Client with given id doesn't exist");
    }

    @Override
    public ClientReadModel create(ClientWriteModel clientWriteModel) {
        Client tmp = clientRepository.save(clientWriteModel.toEntity());
        return new ClientReadModel().toModel(tmp);
    }

    @Override
    public ClientReadModel update(ClientWriteModel rm, long id) {
        if (!clientRepository.existsById(id))
            throw new EntityNotFoundException("Client with given id doesn't exist");
        Client tmp = rm.toEntity();
        tmp.setId(id);
        Client savedClient = clientRepository.save(tmp);
        return new ClientReadModel().toModel(savedClient);
    }
}
