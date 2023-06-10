package dz.me.filleattente.services.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dz.me.filleattente.entities.ServerDetails;
import dz.me.filleattente.repositories.ServerDetailsRepository;
import dz.me.filleattente.services.ServerDetailsService;

@Service
public class ServerDetailsServiceImpl implements ServerDetailsService {

    @Autowired
    private ServerDetailsRepository serverDetailsRepository;

    @Override
    public List<ServerDetails> findAll() {
        return serverDetailsRepository.findAll();
    }

    @Override
    public ServerDetails add(ServerDetails serverDetails) {
        return serverDetailsRepository.save(serverDetails);
    }

}
