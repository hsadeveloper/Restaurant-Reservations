package tableservice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import tableservice.adapter.out.ReservationController;
import tableservice.domain.ReservationStatus;
import tableservice.domain.RestaurantTable;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {
	 @Autowired
	    private MockMvc mockMvc;          // ✅ now injected

	    @MockBean
	    private RestaurantTableRepositoryAdapter repositoryAdapter;

    /**
     * Test case for the {@code GET /api/tables} endpoint.
     * It mocks the repository to return a list of available tables and verifies the HTTP response.
     */
	 @Test
	    void testGetTables() throws Exception {
	        when(repositoryAdapter.findAllAvailable())
	                .thenReturn(List.of(
	                        new RestaurantTable(4, "cust1"),
	                        new RestaurantTable(6, "cust2")
	                ));

	        mockMvc.perform(get("/api/tables")
	                        .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$[0].partySize").value(4))
	                .andExpect(jsonPath("$[1].partySize").value(6));
	    }


//    @Test
//    void testCheckAvailability() throws Exception {
//        RestaurantTable savedTable = new RestaurantTable(4, "cust123");
//        savedTable.setId(1L);
//        savedTable.setStatus(ReservationStatus.PENDING);
//
//        when(repositoryAdapter.save(any(RestaurantTable.class)))
//                .thenReturn(savedTable);
//
//        String requestJson = """
//            {
//              "date": "2025-10-27",
//              "time": "19:00",
//              "partySize": 4,
//              "customerId": "cust123"
//            }
//        """;
//
//        mockMvc.perform(post("/api/tables/availability")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.status").value("PENDING")) // matches your entity
//                .andExpect(jsonPath("$._links.confirm.href")
//                        .value("/api/reservations/1/confirm"));
//    }
}
