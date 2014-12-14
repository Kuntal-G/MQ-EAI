package com.general.mq.monitoring.metric;

import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "name", "ping" })
public class JvmMetrics extends BaseMetrics {
	
	private String jvm_maxm_mem;
	private String jvm_tot_mem;
	private String jvm_used_mem;
	private String jvm_thread_no;
	
	
	public String getJvm_maxm_mem() {
		return jvm_maxm_mem;
	}
	public void setJvm_maxm_mem(String jvm_maxm_mem) {
		this.jvm_maxm_mem = jvm_maxm_mem;
	}
	public String getJvm_tot_mem() {
		return jvm_tot_mem;
	}
	public void setJvm_tot_mem(String jvm_tot_mem) {
		this.jvm_tot_mem = jvm_tot_mem;
	}
	public String getJvm_used_mem() {
		return jvm_used_mem;
	}
	public void setJvm_used_mem(String jvm_used_mem) {
		this.jvm_used_mem = jvm_used_mem;
	}
	public String getJvm_thread_no() {
		return jvm_thread_no;
	}
	public void setJvm_thread_no(String jvm_thread_no) {
		this.jvm_thread_no = jvm_thread_no;
	}
	

}
