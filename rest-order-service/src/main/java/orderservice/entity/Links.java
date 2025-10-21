package orderservice.entity;

public class Links {
 
    private Link confirm;
 

    public Links() {}


	public Link getConfirm() {
		return confirm;
	}

	
	
	public void setConfirm(Link confirm) {
		this.confirm = confirm;
	}


	@Override
	public String toString() {
		return "Links [confirm=" + confirm + ", cancel="  + "]";
	}

    
}
