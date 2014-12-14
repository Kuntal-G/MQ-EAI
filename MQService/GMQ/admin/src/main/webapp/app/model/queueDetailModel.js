Ext.define('gmq.model.queueDetailModel',{	
	extend : 'Ext.data.Model',
	fields : ["queueId","queueName","qName","maxAttmpt","nxtAttmptDly","msgPriorityAttmpt","routingKey","timeUnit"]	
	
});