package tableservice.entity;

import java.time.LocalDateTime;

public class CreateTableReservationRequest {
    private String customerId;
    private Long tableId;
    private LocalDateTime slotStart;
    
    
    
	public CreateTableReservationRequest() {
		super();
		// TODO Auto-generated constructor stub
	}



	public CreateTableReservationRequest(String customerId, Long tableId, LocalDateTime slotStart) {
		super();
		this.customerId = customerId;
		this.tableId = tableId;
		this.slotStart = slotStart;
	}



	public String getCustomerId() {
		return customerId;
	}



	public Long getTableId() {
		return tableId;
	}



	public LocalDateTime getSlotStart() {
		return slotStart;
	}



	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}



	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}



	public void setSlotStart(LocalDateTime slotStart) {
		this.slotStart = slotStart;
	}
	
	
    
    
}