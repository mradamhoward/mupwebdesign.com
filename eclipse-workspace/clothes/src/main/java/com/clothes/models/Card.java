package com.clothes.models;

public class Card {
	long visaDebitNumber;
	int cvv;
	String nameOnCard;
	String expiry;
	
	
	public long getVisaDebitNumber() {
		return visaDebitNumber;
	}
	public void setVisaDebitNumber(long visaDebitNumber) {
		this.visaDebitNumber = visaDebitNumber;
	}
	public int getCvv() {
		return cvv;
	}
	public void setCvv(int cvv) {
		this.cvv = cvv;
	}
	public String getNameOnCard() {
		return nameOnCard;
	}
	public void setNameOnCard(String nameOnCard) {
		this.nameOnCard = nameOnCard;
	}
	public String getExpiry() {
		return expiry;
	}
	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}
	
}
