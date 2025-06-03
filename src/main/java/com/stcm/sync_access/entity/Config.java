package com.stcm.sync_access.entity;

public class Config {
	private int version;
	private long delay_token;
	private String server_url;
	private long delay_sync;
	
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public long getDelay_token() {
		return delay_token;
	}
	public void setDelay_token(long delay_token) {
		this.delay_token = delay_token;
	}
	public String getServer_url() {
		return server_url;
	}
	public void setServer_url(String server_url) {
		this.server_url = server_url;
	}
	public long getDelay_sync() {
		return delay_sync;
	}
	public void setDelay_sync(long delay_sync) {
		this.delay_sync = delay_sync;
	}
	
	
}
