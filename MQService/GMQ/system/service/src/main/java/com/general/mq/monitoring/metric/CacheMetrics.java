package com.general.mq.monitoring.metric;

import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "name","redis_version", "ping" })
public class CacheMetrics extends BaseMetrics{
	

	//Server
	private String redis_version;
	private String process_id;
	private String tcp_port;
	private String uptime_in_seconds;
	private String uptime_in_days;
	//# Clients
	private String connected_clients;
	//Memory Info
	private String used_memory_human;
	private String used_memory_rss;
	private String used_memory_peak_human;
	private String used_memory_lua;
	private String mem_fragmentation_ratio;
	//Persistence
	private String rdb_last_bgsave_status;
	private String aof_enabled;
	private String aof_last_write_status;
	private String aof_current_size;
	//Stats
	private String total_connections_received;
	private String total_commands_processed;
	private String rejected_connections;
	private String expired_keys;
	private String evicted_keys;
	private String keyspace_hits;
	private String keyspace_misses;
	//Keyspace
	private String keyspaceInfo;
	
	
	public String getRedis_version() {
		return redis_version;
	}
	public void setRedis_version(String redis_version) {
		this.redis_version = redis_version;
	}
	public String getProcess_id() {
		return process_id;
	}
	public void setProcess_id(String process_id) {
		this.process_id = process_id;
	}
	public String getTcp_port() {
		return tcp_port;
	}
	public void setTcp_port(String tcp_port) {
		this.tcp_port = tcp_port;
	}
	public String getUptime_in_seconds() {
		return uptime_in_seconds;
	}
	public void setUptime_in_seconds(String uptime_in_seconds) {
		this.uptime_in_seconds = uptime_in_seconds;
	}
	public String getUptime_in_days() {
		return uptime_in_days;
	}
	public void setUptime_in_days(String uptime_in_days) {
		this.uptime_in_days = uptime_in_days;
	}
	public String getConnected_clients() {
		return connected_clients;
	}
	public void setConnected_clients(String connected_clients) {
		this.connected_clients = connected_clients;
	}
	public String getUsed_memory_human() {
		return used_memory_human;
	}
	public void setUsed_memory_human(String used_memory_human) {
		this.used_memory_human = used_memory_human;
	}
	public String getUsed_memory_rss() {
		return used_memory_rss;
	}
	public void setUsed_memory_rss(String used_memory_rss) {
		this.used_memory_rss = used_memory_rss;
	}
	public String getUsed_memory_peak_human() {
		return used_memory_peak_human;
	}
	public void setUsed_memory_peak_human(String used_memory_peak_human) {
		this.used_memory_peak_human = used_memory_peak_human;
	}
	public String getUsed_memory_lua() {
		return used_memory_lua;
	}
	public void setUsed_memory_lua(String used_memory_lua) {
		this.used_memory_lua = used_memory_lua;
	}
	public String getMem_fragmentation_ratio() {
		return mem_fragmentation_ratio;
	}
	public void setMem_fragmentation_ratio(String mem_fragmentation_ratio) {
		this.mem_fragmentation_ratio = mem_fragmentation_ratio;
	}
	public String getRdb_last_bgsave_status() {
		return rdb_last_bgsave_status;
	}
	public void setRdb_last_bgsave_status(String rdb_last_bgsave_status) {
		this.rdb_last_bgsave_status = rdb_last_bgsave_status;
	}
	public String getAof_enabled() {
		return aof_enabled;
	}
	public void setAof_enabled(String aof_enabled) {
		this.aof_enabled = aof_enabled;
	}
	public String getAof_last_write_status() {
		return aof_last_write_status;
	}
	public void setAof_last_write_status(String aof_last_write_status) {
		this.aof_last_write_status = aof_last_write_status;
	}
	public String getAof_current_size() {
		return aof_current_size;
	}
	public void setAof_current_size(String aof_current_size) {
		this.aof_current_size = aof_current_size;
	}
	public String getTotal_connections_received() {
		return total_connections_received;
	}
	public void setTotal_connections_received(String total_connections_received) {
		this.total_connections_received = total_connections_received;
	}
	public String getTotal_commands_processed() {
		return total_commands_processed;
	}
	public void setTotal_commands_processed(String total_commands_processed) {
		this.total_commands_processed = total_commands_processed;
	}
	public String getRejected_connections() {
		return rejected_connections;
	}
	public void setRejected_connections(String rejected_connections) {
		this.rejected_connections = rejected_connections;
	}
	public String getExpired_keys() {
		return expired_keys;
	}
	public void setExpired_keys(String expired_keys) {
		this.expired_keys = expired_keys;
	}
	public String getEvicted_keys() {
		return evicted_keys;
	}
	public void setEvicted_keys(String evicted_keys) {
		this.evicted_keys = evicted_keys;
	}
	public String getKeyspace_hits() {
		return keyspace_hits;
	}
	public void setKeyspace_hits(String keyspace_hits) {
		this.keyspace_hits = keyspace_hits;
	}
	public String getKeyspace_misses() {
		return keyspace_misses;
	}
	public void setKeyspace_misses(String keyspace_misses) {
		this.keyspace_misses = keyspace_misses;
	}
	public String getKeyspaceInfo() {
		return keyspaceInfo;
	}
	public void setKeyspaceInfo(String keyspaceInfo) {
		this.keyspaceInfo = keyspaceInfo;
	}
	
	
}
