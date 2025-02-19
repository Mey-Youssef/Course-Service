package tn.esprit.courseservice.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {
    @Value("${admin.id}")
    private Integer adminId;

    public Integer getAdminId() {
        return adminId;
    }
    //    // Suppression de @Value("${admin.id}")
    //    public Integer getAdminIdFromToken(String token) {
    //        // Ici, tu décodes le JWT et récupères l'ID admin
    //        return JwtUtils.extractAdminId(token);
    //    }

}
