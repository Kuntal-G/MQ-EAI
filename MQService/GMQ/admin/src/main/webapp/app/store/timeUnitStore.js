Ext.define('gmq.store.timeUnitStore',{
	extend : 'Ext.data.Store',
	fields: ['timeUnitName','timeUnitValue'],
	data : [
		{"timeUnitName":"mSec","timeUnitValue":"1"},
		{"timeUnitName":"Sec","timeUnitValue":"2"},
		{"timeUnitName":"Min","timeUnitValue":"3"}		
	]
	
});
