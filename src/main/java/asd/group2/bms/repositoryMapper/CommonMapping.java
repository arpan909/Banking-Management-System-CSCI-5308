package asd.group2.bms.repositoryMapper;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Component
public class CommonMapping {

  public Instant mapCreatedAtOrUpdatedAt(LocalDate date) {
    return date.atStartOfDay(ZoneOffset.UTC).toInstant();
  }

}
