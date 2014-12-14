Ext.define('gmq.model.HistoryTableModel',{	
	extend : 'Ext.data.Model',
	fields : [{name:"id"},{name:"clientId"},{name:"queueName"},{name:"message"},{name:"status"},{name:"parentId"},{name:"loggingTime"},{name:"msgAttrName"},{name:"remark"},{name:"msgAttrValue"},{name:"loggingTime",type: 'datetime',dateFormat:'Y-m-d H:i:s'}],
	idProperty : 'id'
	
});