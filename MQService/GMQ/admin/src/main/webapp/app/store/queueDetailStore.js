Ext.define('gmq.store.queueDetailStore',{
	extend : 'Ext.data.Store',	
	storeId: 'qDetailStore',
	
	model : 'gmq.model.queueDetailModel',
	autoLoad : false,	
	proxy : {
		type : 'ajax',
		url : 'http://localhost:8080/mq-service/service/v1/rest/queue/getQueue?',
		reader : {
			type : 'json',
			root : 'queueDetail',
			successProperety: 'success'
		}
		
	}
	
});