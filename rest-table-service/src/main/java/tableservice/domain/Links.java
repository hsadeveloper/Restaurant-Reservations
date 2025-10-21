package tableservice.domain;

public class Links {
    private Link self;
    private Link confirm;
    private Link cancel;
    private Link update;

    public Links() {}

	public Link getCancel() {
		return cancel;
	}

	public void setCancel(Link cancel) {
		this.cancel = cancel;
	}

	public Link getSelf() {
		return self;
	}

	public Link getConfirm() {
		return confirm;
	}

	public Link getUpdate() {
		return update;
	}

	public void setSelf(Link self) {
		this.self = self;
	}

	public void setConfirm(Link confirm) {
		this.confirm = confirm;
	}

	public void setUpdate(Link update) {
		this.update = update;
	}

	@Override
	public String toString() {
		return "Links [self=" + self + ", confirm=" + confirm + ", cancel=" + cancel + ", update=" + update + "]";
	}

    
}
