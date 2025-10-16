package tableservice.domain.model;

import java.time.LocalDateTime;

public class ReservationResponse {

    private String id;  // Change to String, if you want "r-42" as the ID
    private String status;
    private LocalDateTime expiresAt;
    private Links _links;

    // Constructor with String id
    public ReservationResponse(String id, String status, LocalDateTime expiresAt) {
        this.id = id;  // ID as String (e.g., "r-42")
        this.status = status;
        this.expiresAt = expiresAt;
        this._links = new Links(
            "/api/reservations/" + id, "GET",    // View Reservation
            "/api/reservations/" + id + "/confirm", "POST",  // Confirm Reservation
            "/api/reservations/" + id + "/cancel", "DELETE", // Cancel Reservation
            "/api/reservations/" + id, "PUT"      // Update Reservation
        );
    }

    // Default constructor
    public ReservationResponse() {
        super();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Links get_links() {
        return _links;
    }

    public void set_links(Links _links) {
        this._links = _links;
    }

    // Nested class for Links
    public static class Links {
        private Link self;
        private Link confirm;
        private Link cancel;
        private Link update;

        public Links(String selfHref, String selfMethod, 
                     String confirmHref, String confirmMethod,
                     String cancelHref, String cancelMethod,
                     String updateHref, String updateMethod) {
            this.self = new Link(selfHref, selfMethod);
            this.confirm = new Link(confirmHref, confirmMethod);
            this.cancel = new Link(cancelHref, cancelMethod);
            this.update = new Link(updateHref, updateMethod);
        }

        public Link getSelf() { return self; }
        public Link getConfirm() { return confirm; }
        public Link getCancel() { return cancel; }
        public Link getUpdate() { return update; }

        // Nested class for Link (individual link with method)
        public static class Link {
            private String href;
            private String method;

            public Link(String href, String method) {
                this.href = href;
                this.method = method;
            }

            public String getHref() {
                return href;
            }

            public String getMethod() {
                return method;
            }

            public void setHref(String href) {
                this.href = href;
            }

            public void setMethod(String method) {
                this.method = method;
            }
        }
    }
}
