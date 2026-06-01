package tableservice;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tableservice.adapter.out.persistence.TableAvailability;
import tableservice.api.ReservationResponse;
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



}
