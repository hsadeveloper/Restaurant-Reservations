package tableservice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import tableservice.adapter.web.TableController;
import tableservice.api.ReservationResponse;
import tableservice.api.TableAvailabilityRequest;
import tableservice.api.TableDefinitionDTO;

@WebMvcTest(TableController.class)
class TableControllerTest {

  @Autowired
  private MockMvc mockMvc;

  // ✅ create manually instead of autowiring
  private final ObjectMapper objectMapper = new ObjectMapper();

  @MockitoBean
  private TableDefinitionService tableService;

  @MockitoBean
  private AvailabilityRepositoryPort availRepositoryPort;

  @Test
  void getAllTables_returnsOk() throws Exception {
    when(tableService.findAll())
        .thenReturn(List.of(new TableDefinitionDTO("T1", 4), new TableDefinitionDTO("T2", 6)));

    mockMvc.perform(get("/api/tables/all")).andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2)).andExpect(jsonPath("$[0].tableId").value("T1"));
  }

  @Test
  void getBySize_returnsMatchingTables() throws Exception {
    when(tableService.findBySize(4)).thenReturn(List.of(new TableDefinitionDTO("T1", 4)));

    mockMvc.perform(get("/api/tables/size").param("size", "4")).andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1)).andExpect(jsonPath("$[0].capacity").value(4));
  }

  @Test
  void getAllAvailable_returnsOk() throws Exception {
    when(tableService.findTableByStatus()).thenReturn(List.of(new ReservationResponse()));

    mockMvc.perform(get("/api/tables/available")).andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1));
  }

  @Test
  void checkAvailability_returnsReservationResponse() throws Exception {
    TableAvailabilityRequest request = new TableAvailabilityRequest();
    request.setCustomerId("customer-123");
    request.setPartySize(4);

    ReservationResponse response = new ReservationResponse();

    when(tableService.checkAvailability(any(TableAvailabilityRequest.class))).thenReturn(response);

    mockMvc.perform(post("/api/tables/availability").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request))).andExpect(status().isOk());
  }
}
