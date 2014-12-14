Ext.define('gmq.store.queueStore',{
	extend : 'Ext.data.Store',	
	storeId: 'qStore',
	
	model : 'gmq.model.queueModel',
	autoLoad : true,	
	proxy : {
		type : 'ajax',
		url : 'http://localhost:8080/mq-service/service/v1/rest/queue/loadAllExchange',
		reader : {
			type : 'json',
			root : 'queueDetails',
			successProperety: 'success'
		}
		
	}
	
});