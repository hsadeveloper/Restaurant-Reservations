package tableservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


public class TableEntity {
	

    private final Long id;
    private final String customerId;
    private final int capacity;
    private final String status;

    public TableEntity(Long id, String customerId, int capacity, String status) {
        this.id = id;
        this.customerId = customerId;
        this.capacity = capacity;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getStatus() {
        return status;
    }

   
}
