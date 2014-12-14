Ext.define('gmq.store.DefaultConfigStore',{
	extend : 'Ext.data.Store',	
	storeId: 'DefaultConfiguration',	
	model : 'gmq.model.DefaultConfigModel',
	autoLoad : true,	
	proxy : {
		type : 'ajax',
		url : 'http://localhost:8080/mq-service/service/v1/rest/monitor/loadDefaultConfig',
		reader : {
			type : 'json',			
			successProperety: 'success'
		}
		
	}
});