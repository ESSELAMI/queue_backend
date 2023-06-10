package dz.me.filleattente.services;

import java.util.List;

import dz.me.filleattente.entities.ServerDetails;

public interface ServerDetailsService {
    public List<ServerDetails> findAll();

    public ServerDetails add(ServerDetails serverDetails);

}
