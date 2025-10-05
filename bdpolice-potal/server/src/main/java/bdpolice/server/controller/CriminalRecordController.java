package bdpolice.server.controller;

import bdpolice.server.entity.CriminalRecord;
import bdpolice.server.repo.CriminalRecordRepo;
import bdpolice.shared.dto.Models;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/api/criminal-records")
public class CriminalRecordController {
  private final CriminalRecordRepo repo;
  public CriminalRecordController(CriminalRecordRepo repo) { this.repo = repo; }

  @GetMapping
  public List<CriminalRecord> search(@RequestParam(required = false) String name,
                                     @RequestParam(required = false) Integer age,
                                     @RequestParam(required = false) String gender,
                                     @RequestParam(required = false) String hometown,
                                     @RequestParam(required = false) String nationalId,
                                     @RequestParam(required = false) String birthDate) {
    return repo.search(name, gender, hometown, nationalId);
  }
}
