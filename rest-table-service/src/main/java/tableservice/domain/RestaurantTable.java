package tableservice.domain;

import java.time.LocalDateTime;

public class RestaurantTable {
	
	   	private Long id;
	    private ReservationStatus status;
	    private LocalDateTime expiresAt;
	    private Integer tableNumber;
	    private String customerId;
	    private  int capacity;
	    
		public RestaurantTable() {
			super();
		}

		

		public RestaurantTable(Long id, ReservationStatus status, LocalDateTime expiresAt, Integer tableNumber, String customerId,
				int capacity) {
			super();
			this.id = id;
			this.status = status;
			this.expiresAt = expiresAt;
			this.tableNumber = tableNumber;
			this.customerId = customerId;
			this.capacity = capacity;
		}

      
		public RestaurantTable(ReservationStatus status, LocalDateTime expiresAt, Integer tableNumber, String customerId,
				int capacity) {
			super();
			this.status = status;
			this.expiresAt = expiresAt;
			this.tableNumber = tableNumber;
			this.customerId = customerId;
			this.capacity = capacity;
		}


		public RestaurantTable(Long id, ReservationStatus status, String customerId, int capacity) {
			super();
			this.id = id;
			this.status = status;
			this.customerId = customerId;
			this.capacity = capacity;
		}

		public RestaurantTable(int capacity, String customerId) {
			this.status = ReservationStatus.PENDING ;
			this.customerId = customerId;
			this.capacity = capacity;
			
		}

       





		public Long getId() {
			return id;
		}

		public ReservationStatus getStatus() {
			return status;
		}

		public LocalDateTime getExpiresAt() {
			return expiresAt;
		}

		public Integer getTableNumber() {
			return tableNumber;
		}

		public String getCustomerId() {
			return customerId;
		}

		public void setId(Long id) {
			this.id = id;
		}


		public void setExpiresAt(LocalDateTime expiresAt) {
			this.expiresAt = expiresAt;
		}

		public void setTableNumber(Integer tableNumber) {
			this.tableNumber = tableNumber;
		}

		public void setCustomerId(String customerId) {
			this.customerId = customerId;
		}



		public int getCapacity() {
			return capacity;
		}



		public void setCapacity(int capacity) {
			this.capacity = capacity;
		}

		@Override
		public String toString() {
			return "RestaurantTable [id=" + id + ", status=" + status + ", expiresAt=" + expiresAt + ", tableNumber="
					+ tableNumber + ", customerId=" + customerId + ", capacity=" + capacity + "]";
		}



		
	    
	    

}
