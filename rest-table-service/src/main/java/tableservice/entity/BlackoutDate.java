package tableservice.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table; // ✅ For Jakarta EE (Spring Boot 3+)

@Entity
@Table(name = "blackout_dates")
public class BlackoutDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String reason;

	public BlackoutDate() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BlackoutDate( LocalDate date, String reason) {
		super();
		
		this.date = date;
		this.reason = reason;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getReason() {
		return reason;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
    
    
    
}