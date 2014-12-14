Ext.define('gmq.view.CenterTabPanel',{
	extend : 'Ext.tab.Panel',
	xtype : 'centerPanel',
	id : 'tabPanel',
	margins : '0 2 2 2',
	layout : 'fit',
	store:'queueStore',
	defaults: {
        listeners: {
            activate: function(tab, eOpts) {
            	Ext.getStore('queueStore').reload();
            }
        }
    },
	items : [ ]
});