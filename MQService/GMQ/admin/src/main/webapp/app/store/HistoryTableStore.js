Ext.define('gmq.store.HistoryTableStore',{
	extend : 'Ext.data.Store',
	model : 'gmq.model.HistoryTableModel',	
	autoLoad : {start: 0, limit: 100},
	pageSize : 100,
	remoteSort: true,
	proxy : {
		type : 'ajax',
		url : 'http://localhost:8080/mq-service/service/v1/rest/history/getAllData',
		reader : {
			type : 'json',
			root : 'historyData',
			successProperety: 'success',
			//Name of the property from which to retrieve the total number of records in the dataset.This is only
			//needed if the whole dataset is not passed in one go, but is being paged from the remote server
			totalProperty: 'totalCount',
		}

	}

});