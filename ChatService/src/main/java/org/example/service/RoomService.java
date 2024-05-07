package org.example.service;

import org.example.exception.CustomException;
import org.example.model.Room;

public interface RoomService {
    void saveRoom(Room room);
    Room findById(String id) throws CustomException;

    void deleteById(String id) throws CustomException;
}
