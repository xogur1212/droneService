package com.xy124.drone.service;

import com.xy124.drone.model.DroneSocket;
import com.xy124.drone.repository.DroneSocketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DroneSocketService implements IDroneSocketService{

    private final DroneSocketRepository droneSocketRepository;

    @Override
    public List<DroneSocket> getList() {return droneSocketRepository.findAll();}

    @Transactional
    @Override
    public void saveList(DroneSocket droneSocket){
        //TODO list에 아무것도 없을때 처리가 필요함
        droneSocketRepository.deleteAllBySystemId(droneSocket.getSystemId());
        droneSocketRepository.save(droneSocket);
    }

    @Transactional
    @Override
    public void delete() {
        droneSocketRepository.deleteAllInBatch();
    }


}
