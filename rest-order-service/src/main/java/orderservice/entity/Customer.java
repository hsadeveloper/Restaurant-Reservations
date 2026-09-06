package orderservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String phoneNumber;
	    
		public Customer() {
			super();
			// TODO Auto-generated constructor stub
		}

		public Customer(String customerId, String phoneNumber) {
			super();
			this.phoneNumber = phoneNumber;
		}

		
		public String getPhoneNumber() {
			return phoneNumber;
		}

		

		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
		
		
	    
		
	    

}
