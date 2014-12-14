Ext.define('gmq.store.rKeyStore',{
	extend : 'Ext.data.Store',	
	storeId: 'keyStore',
	
	model : 'gmq.model.rKeyModel',
	//autoLoad : true,	
	proxy : {
		type : 'ajax',
		url : 'http://localhost:8080/mq-service/service/v1/rest/queue/getKeysByExchange',
		reader : {
			type : 'json',
			root : 'queueDetails',
			successProperety: 'success'
		}
		
	}
});