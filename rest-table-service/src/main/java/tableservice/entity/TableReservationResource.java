package tableservice.entity;

import java.time.LocalDateTime;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

public class TableReservationResource extends RepresentationModel<TableReservationResource> {
    private Long id;
    private String customerId;
    private Long tableId;
    private LocalDateTime slotStart;
    private LocalDateTime slotEnd;
    private String status;
	public TableReservationResource() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TableReservationResource(Iterable<Link> initialLinks) {
		super(initialLinks);
		// TODO Auto-generated constructor stub
	}
	public TableReservationResource(Link initialLink) {
		super(initialLink);
		// TODO Auto-generated constructor stub
	}
	public TableReservationResource(Long id, String customerId, Long tableId, LocalDateTime slotStart,
			LocalDateTime slotEnd, String status) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.tableId = tableId;
		this.slotStart = slotStart;
		this.slotEnd = slotEnd;
		this.status = status;
	}
	public Long getId() {
		return id;
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
	public LocalDateTime getSlotEnd() {
		return slotEnd;
	}
	public String getStatus() {
		return status;
	}
	public void setId(Long id) {
		this.id = id;
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
	public void setSlotEnd(LocalDateTime slotEnd) {
		this.slotEnd = slotEnd;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
    
}