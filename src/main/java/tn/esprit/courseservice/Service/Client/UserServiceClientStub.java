package tn.esprit.courseservice.Service.Client;

import org.springframework.stereotype.Service;
import tn.esprit.courseservice.DTO.UserDTO;

@Service
public class UserServiceClientStub implements UserServiceClient{

    @Override
    public UserDTO getUserById(Integer userId) {

        UserDTO dummyUser = new UserDTO();
        dummyUser.setId(userId);
        dummyUser.setFirstName("Dummy");
        dummyUser.setLastName("User");
        dummyUser.setEmail("meyyoussef@gmail.com");
        dummyUser.setRole("ROLE_TUTOR");
        return dummyUser;
    }


}
