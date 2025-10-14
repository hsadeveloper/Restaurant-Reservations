package tableservice.entity;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

public class AvailabilityResource extends RepresentationModel<AvailabilityResource> {
    private Long tableId;
    private Integer capacity;
    private LocalDateTime slotStart;
    private LocalDateTime slotEnd;
    private Boolean available;
    private String reason;
	public AvailabilityResource(Long tableId, Integer capacity, LocalDateTime slotStart, LocalDateTime slotEnd,
			Boolean available, String reason) {
		super();
		this.tableId = tableId;
		this.capacity = capacity;
		this.slotStart = slotStart;
		this.slotEnd = slotEnd;
		this.available = available;
		this.reason = reason;
	}
	public AvailabilityResource() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getTableId() {
		return tableId;
	}
	public Integer getCapacity() {
		return capacity;
	}
	public LocalDateTime getSlotStart() {
		return slotStart;
	}
	public LocalDateTime getSlotEnd() {
		return slotEnd;
	}
	public Boolean getAvailable() {
		return available;
	}
	public String getReason() {
		return reason;
	}
	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	public void setSlotStart(LocalDateTime slotStart) {
		this.slotStart = slotStart;
	}
	public void setSlotEnd(LocalDateTime slotEnd) {
		this.slotEnd = slotEnd;
	}
	public void setAvailable(Boolean available) {
		this.available = available;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
    
    
}