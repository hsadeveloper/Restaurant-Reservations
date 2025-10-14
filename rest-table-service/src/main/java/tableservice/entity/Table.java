package tableservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity

public class Table {

    @Id
    private Long id;

    private int capacity;

    public Table() {
        // Default constructor
    }

    public Table(Long id, int capacity) {
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

    public boolean canAccommodate(Integer partySize) {
        return capacity >= partySize;
    }
}
