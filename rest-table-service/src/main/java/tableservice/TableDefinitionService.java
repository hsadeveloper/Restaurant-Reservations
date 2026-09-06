package tableservice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tableservice.adapter.out.persistence.TableAvailability;
import tableservice.api.ReservationResponse;
import tableservice.api.TableAvailabilityRequest;
import tableservice.api.TableDefinitionDTO;

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

  public void confirmTable(Long Id) {
    logger.info("TableDefinitionService -confirmTable - " + Id);
    TableAvailability table = availRepositoryPort.confirm(Id);
    table.setStatus(ReservationStatus.CONFIRMED);
    availRepositoryPort.save(table);
    return;
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

  public ReservationResponse checkAvailability(TableAvailabilityRequest request) {
    logger.info(
        "TableDefinitionService: Table Service Receiving Reservation Request From Rest-Order Service: "
            + request.toString());
    String customerId = request.getCustomerId();
    LocalDate date = request.getDate();
    LocalTime time = request.getTime();
    int partySize = request.getPartySize();
    if (date == null || time == null || customerId == null) {
      throw new IllegalArgumentException("Date, time, and customerId must not be null");
    }
    List<TableAvailability> availableTables = availRepositoryPort.findAvailable();
    if (availableTables == null || availableTables.isEmpty()) {
      throw new IllegalArgumentException("No available table found for a party of " + partySize);
    }
    logger.info("TableDefinitionService: findBestFitCapacity ------{}" + availableTables);
    if (availableTables.isEmpty()) {
      throw new IllegalArgumentException(
          "TableDefinitionService: No available table found for a party of " + partySize);
    }

    // Min-heap ordered by capacity ascending -- smallest capacity floats to the top
    PriorityQueue<TableAvailability> minHeap =
        new PriorityQueue<>(Comparator.comparingInt(TableAvailability::getCapacity));

    for (TableAvailability table : availableTables) {
      if (table.getCapacity() >= partySize) {
        minHeap.offer(table);
      }
    }

    TableAvailability matchedTable = minHeap.poll();

    if (matchedTable == null) {
      throw new IllegalArgumentException("No table large enough for a party of " + partySize);
    }

    Long selectedTableId = matchedTable.getId();

    matchedTable.setReservationTime(time);
    matchedTable.setStatus(ReservationStatus.PENDING);
    matchedTable.setCustomerId(customerId);
    matchedTable.setReservationDate(date);

    TableAvailability savedObject = availRepositoryPort.save(matchedTable);

    ReservationResponse response = new ReservationResponse();

    // This must be the order-service reservation ID,
    // not necessarily the table-service table ID.
    response.setId(request.getReservationId());

    // This is the selected table ID.
    response.setTableId(savedObject.getId());

    response.setStatus(savedObject.getStatus().name());
    response.setSize(savedObject.getCapacity());
    response.setExpiresAt(LocalDateTime
        .of(savedObject.getReservationDate(), savedObject.getReservationTime()).plusHours(2));

    return response;
  }
}
