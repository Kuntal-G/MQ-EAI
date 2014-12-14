Ext.define('gmq.view.queue.CreateQueue', {
	extend : 'Ext.form.Panel',
	xtype : 'queueCreate',
	title : 'Queue Creation',
	itemId : 'queueCreation',
	closable : true,
	autoScroll:true,
	margin :2,
	store:'queueStore',

	items: [
	        {
	        	xtype :'textfield',
	        	margin : "50 0 0 100",
	        	fieldLabel : 'Queue Name <span style="color: rgb(255, 0, 0); padding-left: 2px;">*</span>',	
	        	name: 'queueName',
	        	labelWidth: 200,
	        	itemId :'qName',
	        	emptyText:'Required',
	        	//emptyCls: 'myEmptyClass',
	        	invalidCls : '',	        	
	        	allowBlank: false,
	        	maskRe: /[a-z0-9_@]/i,
	        	regex: /^[a-zA-Z0-9_@]+$/,
	        	listeners: {

	        		render: function(p) {
	        			
	        			var theElem = p.getEl();
	        			var theTip = Ext.create('Ext.tip.Tip', {
	        				html: 'Enter unique name for the queue.',
	        				margin: '0 0 0 400',
	        				shadow: false
	        			});

	        			p.getEl().on('mouseover', function(){
	        				theTip.showAt(theElem.getX(), theElem.getY());
	        			});

	        			p.getEl().on('mouseleave', function(){
	        				theTip.hide();
	        			});

	        		}
	        	}

	        },
	        {
	        	xtype :'textfield',
	        	margin : "10 0 0 100",
	        	labelWidth: 200,
	        	name: 'routingKey',
	        	allowBlank: true,
	        	emptyText:'Default',
	        	maskRe: /[a-z0-9_@]/i,
	        	regex: /^[a-zA-Z0-9_@]+$/,
	        	fieldLabel : 'Routing Key',
	        	listeners: {

	        		render: function(p) {	        			

	        			var theElem = p.getEl();
	        			var theTip = Ext.create('Ext.tip.Tip', {
	        				html: 'This Key will be used to route message to the appropriate queue during Publish. If left Blank ,\'Default\' routing key will be automatically generated',
	        				margin: '0 0 0 400',
	        				shadow: false
	        			});

	        			p.getEl().on('mouseover', function(){
	        				theTip.showAt(theElem.getX(), theElem.getY());
	        			});

	        			p.getEl().on('mouseleave', function(){
	        				theTip.hide();
	        			});

	        		}
	        	}
	        },
	        {
	        	xtype :'numberfield',
	        	margin : "10 0 0 100",
	        	labelWidth: 200,
	        	minValue: 1,
	        	itemId: 'maxNoAttempt',
	        	name: 'maxAttmpt',
	        	allowBlank: false,
	        	maskRe: /[0-9]/i,
	        	regex: /^[0-9]+$/,
	        	fieldLabel : 'Maximum No. of Attempts',
	        	listeners: {	        	        		

	        		render: function(p) {
	        			var theElem = p.getEl();
	        			var theTip = Ext.create('Ext.tip.Tip', {
	        				html: 'Maximum no. of times a message can be delivered',
	        				margin: '0 0 0 400',
	        				shadow: false
	        			});

	        			p.getEl().on('mouseover', function(){
	        				theTip.showAt(theElem.getX(), theElem.getY());
	        			});

	        			p.getEl().on('mouseleave', function(){
	        				theTip.hide();
	        			});
	        			p.setValue(Ext.getStore('DefaultConfigStore').collect("maxAtmpt"));
	        		}
	        	}
	        },
	        {
	        	xtype: 'fieldcontainer',
	        	itemId :'timeContainer',
	        	fieldLabel: 'Delay for next Attempt',
	        	listeners: {

	        		render: function(p) {
	        			var theElem = p.getEl();
	        			var theTip = Ext.create('Ext.tip.Tip', {
	        				html: 'Delay in milliseconds after which the next delivery is attempted.',
	        				margin: '0 0 0 400',
	        				shadow: false
	        			});

	        			p.getEl().on('mouseover', function(){
	        				theTip.showAt(theElem.getX(), theElem.getY());
	        			});

	        			p.getEl().on('mouseleave', function(){
	        				theTip.hide();
	        			});
	        		}
	        	},
	        	labelWidth: 200,
	        	margin : "10 0 0 100",
	        	layout: "column",
	        	width:  600,
	        	items: [
	        	        {
	        	        	xtype :'numberfield',
	        	        	name: 'nxtAttmptDly',
	        	        	minValue: 0,
	        	        	allowBlank: false,
	        	        	width :90,
	        	        	maskRe: /[0-9]/i,
	        	        	regex: /^[0-9]+$/,
	        	        	value :0
	        	        },
	        	        {
	        	        	xtype     : 'combo',
	        	        	name      : 'timeUnit',
	        	        	width : 60,
	        	        	itemId : 'tunitCreateQueue',
	        	        	forceSelection : true,
	        	        	emptyText:'select',
	        	        	queryMode:'local',
	        	        	store:'timeUnitStore',	        	        	
	        	        	displayField:'timeUnitName',
	        	        	valueField:'timeUnitValue',
	        	        	listeners: {
	        	        		render: function(combo) {	        	                
	        	        			combo.setValue(combo.getStore().getAt('1'));
	        	        		}
	        	        	}	        	        	

	        	        }
	        	        ]

	        },	        
	        {
	        	xtype :'combo',	        	        	
	        	margin : "10 0 20 100",
	        	labelWidth: 200,
	        	forceSelection : true,
	        	queryMode:'local',
	        	itemId :'priorityComboBox',
	        	name: 'msgPriorityAttmpt',
	        	fieldLabel : 'Message Priority on next Attempt',
	        	store: 'msgPriorityStore',
	        	displayField:'priorityName',
	        	valueField:'priorityValue',
	        	emptyText:'Select a value...',
	        	//emptyCls: 'myEmptyClass',
	        	invalidCls : '',		        	
	        	allowBlank: false,
	        	listeners: {

	        		render: function(p) {

	        			p.setValue(p.getStore().getAt('1'));

	        			var theElem = p.getEl();
	        			var theTip = Ext.create('Ext.tip.Tip', {
	        				html: 'You can increase/decrease message priority with each retry or can keep it unchanged.',
	        				margin: '0 0 0 400',
	        				shadow: false
	        			});

	        			p.getEl().on('mouseover', function(){
	        				theTip.showAt(theElem.getX(), theElem.getY());
	        			});

	        			p.getEl().on('mouseleave', function(){
	        				theTip.hide();
	        			});

	        		}
	        	}
	        },
	        {
	        	xtype : 'fieldcontainer',
	        	margin : "50 0 20 100",
	        	layout: "column",
	        	width:  600,
	        	items :[
	        	        {
	        	        	xtype : 'button',
	        	        	text : 'Reset',	        	        	        	
	        	        	width : 120,
	        	        	margin:'0 0 0 50',
	        	        	height :30,
	        	        	handler : function(){
	        	        		var f=this.up('form').getForm();	        	        		
	        	        		f.reset();
	        	        		var priority=this.up('form').getComponent("priorityComboBox");
	        	        		priority.setValue(priority.getStore().getAt('1'));	        	        			        	        		
	        	        		var tunit=this.up('form').getComponent("timeContainer").getComponent("tunitCreateQueue");	        	        		
	        	        		tunit.setValue(tunit.getStore().getAt('1'));
	        	        		var maxAtmpt=this.up('form').getComponent("maxNoAttempt");
	        	        		maxAtmpt.setValue(Ext.getStore('DefaultConfigStore').collect("maxAtmpt"));
	        	        	}
	        	        },
	        	        {
	        	        	xtype : 'button',
	        	        	text : 'Create Queue',	        	        	        	
	        	        	width : 120,
	        	        	margin:'0 0 0 20',
	        	        	height :30,
	        	        	formBind: true,
	        	        	handler : function(){
	        	        		var f=this.up('form').getForm();
	        	        		console.log(f);	    		
	        	        		var values=f.getValues();
	        	        		console.log(values);
	        	        		var formData = Ext.JSON.encode(values);
	        	        		console.log(formData);
	        	        		var tpan=Ext.getCmp('tabPanel');
	        	        		var pan1=tpan.getComponent('queueCreation');
	        	        		var pan=pan1.getComponent('infoPanel')
	        	        		var lab=pan.getComponent('message_label');
	        	        		var component=pan.getComponent('message');   
	        	        		if(f.isValid()){
	        	        			Ext.Ajax.request({

	        	        				url:'http://localhost:8080/mq-service/service/v1/rest/queue/createQueue',
	        	        				method: 'POST',
	        	        				headers :{'Content-Type': 'application/json','Accept':'application/json'},                   
	        	        				jsonData: {
	        	        					queueDetail:f.getValues()
	        	        				},
	        	        				success: function(response) {
	        	        					var jsonResp = Ext.JSON.decode(response.responseText);
	        	        					if(jsonResp.status!=null){
	        	        						lab.setText('Message  : '+jsonResp.status);
	        	        						Ext.Msg.alert('Message  : '+jsonResp.status);
	        	        					}
	        	        				},
	        	        				failure: function(response) {
	        	        					var jsonResp = Ext.JSON.decode(response.responseText);			    	        	 
	        	        					Ext.Msg.alert(jsonResp.error.errorDetail);

	        	        				}
	        	        			});
	        	        		}else{
	        	        			lab.setText('INFO : Please fill all the required fields.');
	        	        			
	        	        		}
	        	        		f.reset();
	        	        		var priority=this.up('form').getComponent("priorityComboBox");
	        	        		priority.setValue(priority.getStore().getAt('1'));
	        	        		var tunit=this.up('form').getComponent("timeContainer").getComponent("tunitCreateQueue");	        	        		
	        	        		tunit.setValue(tunit.getStore().getAt('1'));
	        	        		Ext.getStore('queueStore').reload();
	        	        		
	        	        	}
	        	        }
	        	        ]
	        }
	        ],
	        dockedItems:[
	                     {
	                    	 xtype :'panel',
	                    	 dock: 'bottom',
	                    	 itemId :'infoPanel',
	                    	 height : 100,
	                    	 title : 'Information Panel',
	                    	 layout :{
	                    		 type : 'vbox',
	                    		 align : 'stretch',
	                    		 pack  : 'start'
	                    	 },
	                    	 items :[
	                    	         {
	                    	        	 xtype : 'label',
	                    	        	 text : '',
	                    	        	 itemId:'message_label',
	                    	        	 margin :'10 0 0 20'
	                    	         },
	                    	         {
	                    	        	 xtype : 'label',
	                    	        	 text : '',
	                    	        	 itemId:'message_label2',
	                    	        	 margin :'5 0 0 20'
	                    	         },
	                    	         {
	                    	        	 xtype : 'label',
	                    	        	 text : '',
	                    	        	 itemId:'message_label3',
	                    	        	 margin :'5 0 0 20'
	                    	         }
	                    	         ]

	                     }
	                     ]

});