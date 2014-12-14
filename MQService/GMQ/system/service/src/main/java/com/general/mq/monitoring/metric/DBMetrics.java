package com.general.mq.monitoring.metric;

import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "name", "ping" })
public class DBMetrics extends BaseMetrics {
	
	private String tot_conn_created;
	private String tot_conn_free;
	private String tot_conn_leased;
	private String conn_wait_time_avg;
	private String stmt_exec_time_avg;
	private String stmt_exec_time_cum;
	private String prep_exec_time_cum;
	
	
	public String getTot_conn_created() {
		return tot_conn_created;
	}
	public void setTot_conn_created(String tot_conn_created) {
		this.tot_conn_created = tot_conn_created;
	}
	public String getTot_conn_free() {
		return tot_conn_free;
	}
	public void setTot_conn_free(String tot_conn_free) {
		this.tot_conn_free = tot_conn_free;
	}
	public String getTot_conn_leased() {
		return tot_conn_leased;
	}
	public void setTot_conn_leased(String tot_conn_leased) {
		this.tot_conn_leased = tot_conn_leased;
	}
	public String getConn_wait_time_avg() {
		return conn_wait_time_avg;
	}
	public void setConn_wait_time_avg(String conn_wait_time_avg) {
		this.conn_wait_time_avg = conn_wait_time_avg;
	}
	public String getStmt_exec_time_avg() {
		return stmt_exec_time_avg;
	}
	public void setStmt_exec_time_avg(String stmt_exec_time_avg) {
		this.stmt_exec_time_avg = stmt_exec_time_avg;
	}
	public String getStmt_exec_time_cum() {
		return stmt_exec_time_cum;
	}
	public void setStmt_exec_time_cum(String stmt_exec_time_cum) {
		this.stmt_exec_time_cum = stmt_exec_time_cum;
	}
	public String getPrep_exec_time_cum() {
		return prep_exec_time_cum;
	}
	public void setPrep_exec_time_cum(String prep_exec_time_cum) {
		this.prep_exec_time_cum = prep_exec_time_cum;
	}
	
}
