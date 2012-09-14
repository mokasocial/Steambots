package com.tacticalautomata.androidclient;

public class StoreItem {

	// product types
	static final String TYPE_BOOSTERS = "BoosterPackPrime";
	static final String TYPE_DECKS = "AlphaDeck";
	static final String TYPE_ROBOTS = "RobotItem";

	private int cost;
	private String description;
	private int id;
	private String imageCode;
	private String name;
	private String productType;

	public int getCost() {
		return cost;
	}

	public String getDescription() {
		return description;
	}

	public int getId() {
		return id;
	}

	public String getImageCode() {
		return imageCode;
	}

	public String getName() {
		return name;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setImageCode(String imageCode) {
		this.imageCode = imageCode;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProductType() {
		return productType;
	}

	public String getHumanProductType() {
		if (productType.contentEquals(TYPE_BOOSTERS)) {
			return "Booster Pack";
		} else if (productType.contentEquals(TYPE_DECKS)) {
			return "Starter Deck";
		} else if (productType.contentEquals(TYPE_ROBOTS)) {
			return "Robot";
		}
		return "Unknown";
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
}