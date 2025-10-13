package com.developer.com.tabletservice;

public class Table {
    private Long id;
    private int capacity;
    
    
	public Table(Long id, int capacity) {
		super();
		this.id = id;
		this.capacity = capacity;
	}


	public Long getId() {
		return id;
	}


	public int getCapacity() {
		return capacity;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}


	@Override
	public String toString() {
		return "Table [id=" + id + ", capacity=" + capacity + "]";
	}
    
	
    
}
