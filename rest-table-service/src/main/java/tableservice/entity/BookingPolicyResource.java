package tableservice.entity;

import java.time.LocalTime;

import org.springframework.hateoas.RepresentationModel;

public class BookingPolicyResource extends RepresentationModel<BookingPolicyResource> {
    private Integer maxReservationsPerDayPerCustomer;
    private Integer turnoverBufferMinutes;
    private LocalTime bookingStartTime;
    private LocalTime bookingEndTime;
    
    public BookingPolicyResource() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
	public BookingPolicyResource(Integer maxReservationsPerDayPerCustomer, Integer turnoverBufferMinutes,
			LocalTime bookingStartTime, LocalTime bookingEndTime) {
		super();
		this.maxReservationsPerDayPerCustomer = maxReservationsPerDayPerCustomer;
		this.turnoverBufferMinutes = turnoverBufferMinutes;
		this.bookingStartTime = bookingStartTime;
		this.bookingEndTime = bookingEndTime;
	}


	public Integer getMaxReservationsPerDayPerCustomer() {
		return maxReservationsPerDayPerCustomer;
	}
	public Integer getTurnoverBufferMinutes() {
		return turnoverBufferMinutes;
	}
	public LocalTime getBookingStartTime() {
		return bookingStartTime;
	}
	public LocalTime getBookingEndTime() {
		return bookingEndTime;
	}
	public void setMaxReservationsPerDayPerCustomer(Integer maxReservationsPerDayPerCustomer) {
		this.maxReservationsPerDayPerCustomer = maxReservationsPerDayPerCustomer;
	}
	public void setTurnoverBufferMinutes(Integer turnoverBufferMinutes) {
		this.turnoverBufferMinutes = turnoverBufferMinutes;
	}
	public void setBookingStartTime(LocalTime bookingStartTime) {
		this.bookingStartTime = bookingStartTime;
	}
	public void setBookingEndTime(LocalTime bookingEndTime) {
		this.bookingEndTime = bookingEndTime;
	}
	
    
    
    
}