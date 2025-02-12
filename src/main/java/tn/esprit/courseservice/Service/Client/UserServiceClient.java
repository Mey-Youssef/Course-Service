package tn.esprit.courseservice.Service.Client;

import tn.esprit.courseservice.DTO.UserDTO;

public interface UserServiceClient {
    UserDTO getUserById(Integer userId);
}

