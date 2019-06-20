package com.invengo.entity;

import java.util.Date;

public class TracingInfoEntity{
	
	private String tagUid;
	private String productName;
	private String productOrign;
	private Date producerDate;
	
	private Date productInDate;
	private String personInCharge;
	private String personPhone;
	
	private String salePlace;
	private Date saleDate;
	private String seller;
	private String sellerPhone;
	
	private int result;
	
	public String getTagUid() {
		return tagUid;
	}
	public void setTagUid(String tagUid) {
		this.tagUid = tagUid;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductOrign() {
		return productOrign;
	}
	public void setProductOrign(String productOrign) {
		this.productOrign = productOrign;
	}
	public Date getProducerDate() {
		return producerDate;
	}
	public void setProducerDate(Date producerDate) {
		this.producerDate = producerDate;
	}
	public Date getProductInDate() {
		return productInDate;
	}
	public void setProductInDate(Date productInDate) {
		this.productInDate = productInDate;
	}
	public String getPersonInCharge() {
		return personInCharge;
	}
	public void setPersonInCharge(String personInCharge) {
		this.personInCharge = personInCharge;
	}
	public String getPersonPhone() {
		return personPhone;
	}
	public void setPersonPhone(String personPhone) {
		this.personPhone = personPhone;
	}
	public String getSalePlace() {
		return salePlace;
	}
	public void setSalePlace(String salePlace) {
		this.salePlace = salePlace;
	}
	public Date getSaleDate() {
		return saleDate;
	}
	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public String getSellerPhone() {
		return sellerPhone;
	}
	public void setSellerPhone(String sellerPhone) {
		this.sellerPhone = sellerPhone;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	
}
