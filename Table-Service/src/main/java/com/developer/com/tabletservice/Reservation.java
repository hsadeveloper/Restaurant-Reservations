package com.developer.com.tabletservice;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {
    private Long id;
    private String customerName;
    private LocalDate date;
    private LocalTime time;
    private int partySize;
    private Long tableId;
    
	public Reservation(Long id, String customerName, LocalDate date, LocalTime time, int partySize, Long tableId) {
		super();
		this.id = id;
		this.customerName = customerName;
		this.date = date;
		this.time = time;
		this.partySize = partySize;
		this.tableId = tableId;
	}

	public Reservation(String customerName, LocalDate date, LocalTime time, int partySize, Long tableId) {
		super();
		this.customerName = customerName;
		this.date = date;
		this.time = time;
		this.partySize = partySize;
		this.tableId = tableId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public LocalDate getDate() {
		return date;
	}

	public LocalTime getTime() {
		return time;
	}

	public int getPartySize() {
		return partySize;
	}

	public Long getTableId() {
		return tableId;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public void setPartySize(int partySize) {
		this.partySize = partySize;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	@Override
	public String toString() {
		return "Reservation [id=" + id + ", customerName=" + customerName + ", date=" + date + ", time=" + time
				+ ", partySize=" + partySize + ", tableId=" + tableId + "]";
	}
	
    
    
}
