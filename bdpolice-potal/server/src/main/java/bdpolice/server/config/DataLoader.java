package bdpolice.server.config;

import bdpolice.server.entity.ChatRoom;
import bdpolice.server.entity.CriminalRecord;
import bdpolice.server.entity.User;
import bdpolice.server.repo.ChatRoomRepo;
import bdpolice.server.repo.CriminalRecordRepo;
import bdpolice.server.repo.UserRepo;
import bdpolice.shared.dto.Models;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Configuration
public class DataLoader {
  @Bean
  CommandLineRunner seed(UserRepo users, ChatRoomRepo rooms, CriminalRecordRepo crim) {
    return args -> {
      if (users.count() == 0) {
        User admin = new User();
        admin.setName("System Administrator");
        admin.setRole(Models.Role.ADMIN);
        admin.setUsername("admin");
        admin.setPasswordHash("dev");
        users.save(admin);

        User o1 = new User(); o1.setName("Officer Rahman"); o1.setRole(Models.Role.OFFICER); o1.setPoliceId("BD001"); o1.setStatus("online"); users.save(o1);
        User o2 = new User(); o2.setName("Officer Khan"); o2.setRole(Models.Role.OFFICER); o2.setPoliceId("BD002"); o2.setStatus("online"); users.save(o2);
        User o3 = new User(); o3.setName("Officer Ali"); o3.setRole(Models.Role.OFFICER); o3.setPoliceId("BD003"); o3.setStatus("away"); users.save(o3);

        ChatRoom r = new ChatRoom();
        r.setName("Operation Sunrise");
        r.getMembers().addAll(List.of(o1, o2, o3));
        rooms.save(r);
      }

      if (crim.count() == 0) {
        CriminalRecord c1 = new CriminalRecord();
        c1.setName("Ahmed Hassan"); c1.setAge(32); c1.setGender("Male"); c1.setHometown("Dhaka"); c1.setNationalId("1234567890");
        c1.setBirthDate(LocalDate.of(1992, 3, 15).atStartOfDay().toInstant(ZoneOffset.UTC));
        c1.setStatus("Under Investigation"); c1.setDescription("Repeat offender with history of property crimes.");
        crim.save(c1);
      }
    };
  }
}
