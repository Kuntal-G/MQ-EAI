Ext.define("gmq.view.AccordionMenuView", {

	extend : 'Ext.panel.Panel',
	xtype : 'menuView',
	title : 'Menu',
	layout : 'accordion',
	width : 200,
	margins : '0 0 2 0',
	collapsible : true,
	titleCollapse : true,
	layoutConfig : {
		animate : false,
		multi : true
	},
	items : [
{
	xtype : 'panel',
	hidden: true,
	collapsed: false
},
{
	title : 'Queue Management',
	xtype : 'panel',
	items : [
	         {
	        	 xtype : 'button',
	        	 text : 'Create Queue',
	        	 itemId : 'createQueue',
	        	 //cls : 'add',
	        	 width : 194,
	        	 height : 30,
	        	 margin :"2 2 0 2",
	        	 handler : function (){
	        		 var t6=Ext.getCmp("tabPanel");
	        		 var existingTab6 = t6.getComponent('queueCreation');
	        		 if(!existingTab6){
	        			 t6.add({xtype : 'queueCreate',itemId : 'queueCreation'});
	        			 t6.setActiveTab('queueCreation');

	        		 }else{
	        			 t6.setActiveTab('queueCreation');

	        		 }
	        	 }			

	         }
	         ]
},
{
	title : 'Client Management',
	xtype : 'panel',
	autoScroll : true,
	items : [
	         {
	        	 xtype : 'button',
	        	 text : 'Register Producer',
	        	 itemId : 'regProducer',
	        	 cls : 'register',
	        	 width : 194,
	        	 height : 30,
	        	 margin :"2 2 0 2",
	        	 handler : function (){
	        		 var t1=Ext.getCmp("tabPanel");
	        		 var existingTab1 = t1.getComponent('proRegistration');
	        		 if(!existingTab1){
	        			 t1.add({xtype : 'producerReg',itemId : 'proRegistration'});
	        			 t1.setActiveTab('proRegistration');

	        		 }else{
	        			 t1.setActiveTab('proRegistration');

	        		 }						
	        	 }
	         },
	         {
	        	 xtype : 'button',
	        	 text : 'Register Consumer',
	        	 itemId : 'regConsumer',
	        	 cls : 'register',
	        	 width : 194,
	        	 height : 30,

	        	 margin :"2 2 0 2",
	        	 handler : function (){
	        		 var t2=Ext.getCmp("tabPanel");
	        		 var existingTab2 = t2.getComponent('conRegistration');
	        		 if(!existingTab2){
	        			 t2.add({xtype : 'consumerReg',itemId : 'conRegistration'});
	        			 t2.setActiveTab('conRegistration');

	        		 }else{
	        			 t2.setActiveTab('conRegistration');

	        		 }						
	        	 }
	         }
	         
	         ]


}
]

});