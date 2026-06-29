package tableservice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tableservice.adapter.out.persistence.TableAvailability;
import tableservice.api.ReservationResponse;
import tableservice.api.TableAvailabilityRequest;
import tableservice.api.TableDefinitionDTO;
import tableservice.domain.ReservationStatus;

@Component
public class TableDefinitionService {

  private static final Logger logger = LoggerFactory.getLogger(TableDefinitionService.class);


  private final TableDefinitionRepositoryPort repositoryPort;

  private final AvailabilityRepositoryPort availRepositoryPort;


  public TableDefinitionService(TableDefinitionRepositoryPort repositoryPort,
      AvailabilityRepositoryPort availRepositoryPort) {
    this.repositoryPort = repositoryPort;
    this.availRepositoryPort = availRepositoryPort;
  }

  public List<TableDefinitionDTO> findAll() {

    logger.info("TableDefinitionService - GET");

    List<TableDefinitionDTO> tables = repositoryPort.findAll();

    logger.info("TableDefinitionService -tables - " + tables);

    if (tables.isEmpty()) {
      throw new RuntimeException("No tables found");
    }

    return tables;
  }

  public List<TableDefinitionDTO> findBySize(int size) {

    List<TableDefinitionDTO> tables = repositoryPort.findBySize(size);

    if (tables == null || tables.isEmpty()) {
      throw new RuntimeException("No tables found with size: " + size);
    }
    return tables;
  }


  public List<ReservationResponse> findTableByStatus() {

    return availRepositoryPort.findByStatus();
  }

  public List<TableAvailability> findBestFitByCapacity(int partySize) {

    return availRepositoryPort.findBestFitCapacity(partySize);
  }



  public ReservationResponse checkAvailability(TableAvailabilityRequest request) {



    logger.info("Receiving reservation from rest-order: " + request);

    String customerId = request.getCustomerId();
    LocalDate date = request.getDate();
    LocalTime time = request.getTime();
    int partySize = request.getPartySize();

    if (date == null || time == null || customerId == null) {
      throw new IllegalArgumentException("Date, time, and customerId must not be null");
    }

    LocalDateTime startDateTime = LocalDateTime.of(date, time);
    LocalDateTime endDateTime = startDateTime.plusHours(2); // use in overlap check

    List<TableAvailability> availableTables = availRepositoryPort.findBestFitCapacity(partySize);

    if (availableTables.isEmpty()) {
      throw new IllegalArgumentException("No available table found for a party of " + partySize);
    }

    TableAvailability matchedTable = availableTables.get(0);
    matchedTable.setReservationTime(time);
    matchedTable.setStatus(ReservationStatus.PENDING);
    matchedTable.setCustomerId(customerId);
    matchedTable.setReservationDate(date);

    TableAvailability savedObject = availRepositoryPort.save(matchedTable);

    ReservationResponse response = new ReservationResponse();
    response.setId(savedObject.getId());
    response.setStatus(savedObject.getStatus().name());
    response.setSize(savedObject.getCapacity());
    response.setExpiresAt(LocalDateTime
        .of(savedObject.getReservationDate(), savedObject.getReservationTime()).plusHours(2));
    return response;
  }
}
