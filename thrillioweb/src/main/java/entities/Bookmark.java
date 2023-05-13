package entities;

import constants.KidFriendlyStatus;

abstract public class Bookmark {

	private long id;
	private String title;
	private String imageUrl;
	private KidFriendlyStatus kidFriendlyStatus=KidFriendlyStatus.UNKNOWN;
    private User kidFriendlyMarkedBy;
    private User sharedBy;
    
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}



	@Override
	public String toString() {
		return "Bookmark [id=" + id + ", title=" + title + ", imageUrl=" + imageUrl + "]";
	}

	public KidFriendlyStatus getKidFriendlyStatus() {
		return kidFriendlyStatus;
	}

	public void setKidFriendlyStatus(KidFriendlyStatus kidFriendlyStatus2) {
		this.kidFriendlyStatus = kidFriendlyStatus2;
	}

	public User getKidFriendlyMarkedBy() {
		return kidFriendlyMarkedBy;
	}

	public void setKidFriendlyMarkedBy(User kidFriendlyMarkedBy) {
		this.kidFriendlyMarkedBy = kidFriendlyMarkedBy;
	}

	public User getSharedBy() {
		return sharedBy;
	}

	public void setSharedBy(User sharedBy) {
		this.sharedBy = sharedBy;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public boolean isKidFriendlyEligible() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
