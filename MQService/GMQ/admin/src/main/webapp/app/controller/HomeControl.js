Ext.define('gmq.controller.HomeControl',{

	extend : 'Ext.app.Controller',
	alias : 'widget.homeControl',
	models : ['queueModel','rKeyModel','queueDetailModel','DefaultConfigModel'],
	stores : ['HistoryTableStore','queueStore','rKeyStore','timeUnitStore','msgPriorityStore','queueDetailStore','DefaultConfigStore'],
	require : ['AdminLogin'],
	views : ['HomeView',
	         'AccordionMenuView',
	         'CenterTabPanel',
	         'queue.CreateQueue',
	        'producer.producerReg',
	         'consumer.consumerReg'		
	        		         ],

	         init: function() {
	        	 /*The init function is a special method that is called when your application boots.
					It is called before the Application's launch function is executed so gives a hook
					point to run any code before your Viewport is created.*/
	        	 this.control({                 
	        		 'producerRegForm #excName' :{
	        			 select: this.loadRKeyCombo
	        		 },
	        		 'consumerRegForm #con_excName' :{
	        			 select: this.loadRKeyCombo_con
	        		 }
	        		 
	        	 });
	         },
	         loadRKeyCombo: function(combo, records) {    	
	        	 var comboRKey1 = Ext.ComponentQuery.query('producerRegForm #rKey')[0];

	        	 comboRKey1.setDisabled(true);
	        	 comboRKey1.setValue(''); 
	        	 comboRKey1.store.removeAll();
	        	 comboRKey1.store.load({
	        		 params: { exchangeName: combo.getValue() }
	        	 });
	        	 comboRKey1.setDisabled(false);
	         },
	         loadRKeyCombo_con: function(combo, records) {    	
	        	 var comboRKey = Ext.ComponentQuery.query('consumerRegForm #con_rKey')[0];

	        	 comboRKey.setDisabled(true);
	        	 comboRKey.setValue('');
	        	 comboRKey.store.removeAll();
	        	 comboRKey.store.load({
	        		 params: { exchangeName: combo.getValue() }
	        	 });
	        	 comboRKey.setDisabled(false);
	         }
	         
	        
});