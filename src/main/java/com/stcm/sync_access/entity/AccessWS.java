package com.stcm.sync_access.entity;

import javax.persistence.Entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
public class AccessWS {
	private long id;
    private int routeId;
    private int stationId;
    private int unitId;
    private String equipmentCode;
    @NotNull
    @Size(min = 4, max = 4)
    private String typeTitleCode;
    private String uidCard;
    private String logicNumber;
    private String uidSam;
    private float fare;
    private int paymentSeq;
    private float balanceEWallet;
    private float finalBalanceEWallet;
    private int balanceTrips;
    private int finalBalanceTrips;
    private String ticketingValidationCode;
    private String date;
    
    public AccessWS (Access access){
    	this.id = access.getCde();
    	this.routeId = access.getRouteId();
    	this.stationId = access.getStationId() ;
    	this.unitId = access.getUnitId() ;
    	this.equipmentCode = access.getEquipmentCode() ;
    	this.typeTitleCode = access.getTypeTitleCode() ;
    	this.uidCard = access.getUidCard() ;
    	this.logicNumber = access.getLogicNumber() ;
    	this.uidSam = access.getUidSam() ;
    	this.fare = access.getFare() ;
    	this.paymentSeq = access.getPaymentSeq() ;
    	this.balanceEWallet = access.getBalanceEWallet() ;
    	this.finalBalanceEWallet = access.getFinalBalanceEWallet() ;
    	this.balanceTrips = access.getBalanceTrips() ;
    	this.finalBalanceTrips = access.getBalanceTrips() ;
    	this.ticketingValidationCode = access.getTicketingValidationCode();
    	this.date = access.getDate() ;
    }
    
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getRouteId() {
		return routeId;
	}
	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}
	public int getStationId() {
		return stationId;
	}
	public void setStationId(int stationId) {
		this.stationId = stationId;
	}
	public int getUnitId() {
		return unitId;
	}
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	public String getEquipmentCode() {
		return equipmentCode;
	}
	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}
	
	public String getTypeTitleCode() {
		return typeTitleCode;
	}
	public void setTypeTitleCode(String typeTitleCode) {
		this.typeTitleCode = typeTitleCode;
	}
	public String getUidCard() {
		return uidCard;
	}
	public void setUidCard(String uidCard) {
		this.uidCard = uidCard;
	}
	public String getLogicNumber() {
		return logicNumber;
	}
	public void setLogicNumber(String logicNumber) {
		this.logicNumber = logicNumber;
	}
	public String getUidSam() {
		return uidSam;
	}
	public void setUidSam(String uidSam) {
		this.uidSam = uidSam;
	}
	public float getFare() {
		return fare;
	}
	public void setFare(float fare) {
		this.fare = fare;
	}
	public int getPaymentSeq() {
		return paymentSeq;
	}
	public void setPaymentSeq(int paymentSeq) {
		this.paymentSeq = paymentSeq;
	}
	public float getBalanceEWallet() {
		return balanceEWallet;
	}
	public void setBalanceEWallet(float balanceEWallet) {
		this.balanceEWallet = balanceEWallet;
	}
	public float getFinalBalanceEWallet() {
		return finalBalanceEWallet;
	}
	public void setFinalBalanceEWallet(float finalBalanceEWallet) {
		this.finalBalanceEWallet = finalBalanceEWallet;
	}
	public int getBalanceTrips() {
		return balanceTrips;
	}
	public void setBalanceTrips(int balanceTrips) {
		this.balanceTrips = balanceTrips;
	}
	public int getFinalBalanceTrips() {
		return finalBalanceTrips;
	}
	public void setFinalBalanceTrips(int finalBalanceTrips) {
		this.finalBalanceTrips = finalBalanceTrips;
	}
	public String getTicketingValidationCode() {
		return ticketingValidationCode;
	}
	public void setTicketingValidationCode(String ticketingValidationCode) {
		this.ticketingValidationCode = ticketingValidationCode;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}


}
