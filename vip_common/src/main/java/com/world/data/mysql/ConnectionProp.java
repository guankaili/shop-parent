package com.world.data.mysql;

public class ConnectionProp {
	public ConnectionProp(String ip, String dbName, String dbUserName,String dbPwd , boolean isOk, 
			String jdbcUrl, String driverClassName) {
		super();
		this.ip = ip;
		this.dbName = dbName;
		this.dbUserName = dbUserName;
		this.dbPwd = dbPwd;
		this.isOk = isOk;
		this.jdbcUrl = jdbcUrl;
		this.driverClassName = driverClassName;
	}
	public String ip;
	public String dbName;
	public String dbUserName;
	public String dbPwd;
	public boolean isOk;
	public String jdbcUrl;
	public String driverClassName;
}
