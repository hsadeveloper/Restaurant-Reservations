package orderservice.entity;

public class Links {
    private Link confirm;

    public Links() {}

    public Links(Link confirm) {
        this.confirm = confirm;
    }

    public Link getConfirm() {
        return confirm;
    }

    public void setConfirm(Link confirm) {
        this.confirm = confirm;
    }
}
