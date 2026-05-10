package tableservice.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class TableAvailabilityRequest {
    private LocalDate date;      // "2025-10-21"
    private LocalTime time;      // "19:00"
    private Integer partySize;
    private String customerId;
    
	public TableAvailabilityRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TableAvailabilityRequest(LocalDate date, LocalTime time, Integer partySize, String customerId) {
		super();
		this.date = date;
		this.time = time;
		this.partySize = partySize;
		this.customerId = customerId;
	}



	public LocalDate getDate() {
		return date;
	}

	public LocalTime getTime() {
		return time;
	}

	public Integer getPartySize() {
		return partySize;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
	

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public void setPartySize(Integer partySize) {
		this.partySize = partySize;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	

	public String getCustomerId() {
		return customerId;
	}

	@Override
	public String toString() {
		return "TableAvailabilityRequest [date=" + date + ", time=" + time + ", partySize=" + partySize
				+ ", customerId=" + customerId + "]";
	}
	
  
}
