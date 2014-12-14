Ext.define('gmq.store.msgPriorityStore',{
	extend : 'Ext.data.Store',
	fields: ['priorityName','priorityValue'],
	data : [
		{"priorityName":"Decrease Priority","priorityValue":"1"},
		{"priorityName":"Same Priority","priorityValue":"2"},
		{"priorityName":"Increase Priority","priorityValue":"3"}		
	]
	
});