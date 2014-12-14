Ext.define('gmq.view.consumer.consumerReg', {
	extend : 'Ext.form.Panel',
	xtype : 'consumerReg',
	title : 'Consumer Registration',
	itemId : 'conRegistration',
	alias : 'widget.consumerRegForm',
	closable : true,
	autoScroll:true,
	margin :2,
	items: [
	        {
	        	xtype :'combo',
	        	margin : "50 0 0 100",
	        	labelWidth: 200,	       	
	        	name: 'queueName',
	        	allowBlank: false,
	        	id: 'con_excName',
	        	forceSelection : true,	        		       	
	        	fieldLabel : 'Queue Name <span style="color: rgb(255, 0, 0); padding-left: 2px;">*</span>',
	        	store: 'queueStore',
	        	displayField:'queueName',	        
	        	triggerAction:'all',
	        	mode:'local',
	        	emptyText:'Required',
	        	//emptyCls: 'myEmptyClass',
	        	invalidCls : '',

	        },
	        {
	        	xtype :'combo',
	        	margin : "20 0 0 100",
	        	labelWidth: 200,
	        	id: 'con_rKey',
	        	name: 'routingKey',
	        	allowBlank: false,
	        	forceSelection : true,	        		       	
	        	fieldLabel : 'Routing Key <span style="color: rgb(255, 0, 0); padding-left: 2px;">*</span>',
	        	store: 'rKeyStore',
	        	displayField:'routingKey',	        
	        	triggerAction:'all',
	        	mode:'local',
	        	emptyText:'Required',
	        	//emptyCls: 'myEmptyClass',
	        	invalidCls : '',
	        	disabled:true,
	        	lastQuery:''

	        },
	        {
	        	xtype: 'fieldcontainer',
	        	fieldLabel: 'Message Processing Time',
	        	listeners: {

	        		render: function(p) {
	        			var theElem = p.getEl();
	        			var theTip = Ext.create('Ext.tip.Tip', {
	        				html: 'Expected time required to process a message.',
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
	        	margin : "20 0 0 100",
	        	layout: "column",
	        	width:  600,
	        	items: [
	        	        {
	        	        	xtype :'numberfield',
	        	        	name: 'processTime',
	        	        	minValue: 0,
	        	        	allowBlank: false,
	        	        	width :90,
	        	        	listeners: {

	        	        		render: function(p) {
	        	        			p.setValue(Ext.getStore('DefaultConfigStore').collect("processTime"));
	        	        		}
	        	        	}
	        	        },
	        	        {
	        	        	xtype     : 'combo',
	        	        	name      : 'timeUnit',
	        	        	width : 60,
	        	        	itemId : 'con_tunit',
	        	        	forceSelection : true,
	        	        	emptyText:'select',
	        	        	queryMode:'local',
	        	        	store:'timeUnitStore',	        	        	
	        	        	displayField:'timeUnitName',
	        	        	valueField:'timeUnitValue',
	        	        	listeners: {
	        	        		render: function(combo) {	        	                
	        	        			combo.setValue(combo.getStore().getAt(Ext.getStore('DefaultConfigStore').collect("processTimeUnit")));
	        	        		}
	        	        	}	        	        	

	        	        }
	        	        ]

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
	        	        		this.up('form').getForm().reset();

	        	        	}
	        	        },
	        	        {
	        	        	xtype : 'button',
	        	        	text : 'Register',	        	        	        	
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
	        	        		var pan1=tpan.getComponent('conRegistration');
	        	        		var pan=pan1.getComponent('infoPanel')
	        	        		var lab1=pan.getComponent('message_label');
	        	        		var lab2=pan.getComponent('message_label2');
	        	        		var lab3=pan.getComponent('message_label3');
	        	        		if(f.isValid()){
	        	        			Ext.Ajax.request({

	        	        				url:'http://localhost:8080/mq-service/service/v1/rest/consumer/register',
	        	        				method: 'POST',
	        	        				headers :{'Content-Type': 'application/json','Accept':'application/json'},                   
	        	        				jsonData: f.getValues(),
	        	        				success: function(response) {
	        	        					var jsonResp = Ext.JSON.decode(response.responseText);
	        	        					if(jsonResp.clientId!=null){
	        	        						lab1.setText('Message  :  Registration Successful.');
	        	        						lab2.setText('Consumer Id  : '+jsonResp.clientId);
	        	        						lab3.setText('NOTE :  Please copy this Consumer Id and keep it safe as you will require this ID to consume messages.');

	        	        					}
	        	        				},
	        	        				failure: function(response) {
	        	        					var jsonResp = Ext.JSON.decode(response.responseText);			    	        	 
	        	        					Ext.Msg.alert(jsonResp.errorMsg.errorMsg);

	        	        				}
	        	        			});
	        	        		}else{
	        	        			lab1.setText('INFO : Please fill all the required fields.');

	        	        		}
	        	        		this.up('form').getForm().reset();
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

