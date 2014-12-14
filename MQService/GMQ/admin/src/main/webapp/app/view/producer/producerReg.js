Ext.define('gmq.view.producer.producerReg', {
	extend : 'Ext.form.Panel',
	xtype : 'producerReg',
	title : 'Producer Registration',
	itemId : 'proRegistration',
	alias : 'widget.producerRegForm',
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
	        	forceSelection : true,
	        	id: 'excName',	       		       	
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
	        	id: 'rKey',
	        	name: 'routingKey',
	        	allowBlank: true,		       		       	
	        	fieldLabel : 'Routing Key',
	        	store: 'rKeyStore',
	        	displayField:'routingKey',	        
	        	triggerAction:'all',
	        	mode:'local',
	        	disabled:true,
	        	lastQuery:''

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
	        	        		var pan1=tpan.getComponent('proRegistration');
	        	        		var pan=pan1.getComponent('infoPanel')
	        	        		var lab1=pan.getComponent('message_label');
	        	        		var lab2=pan.getComponent('message_label2');
	        	        		var lab3=pan.getComponent('message_label3');   
	        	        		if(f.isValid()){
	        	        			Ext.Ajax.request({

	        	        				url:'http://localhost:8080/mq-service/service/v1/rest/producer/register',
	        	        				method: 'POST',
	        	        				headers :{'Content-Type': 'application/json','Accept':'application/json'},                   
	        	        				jsonData: f.getValues(),
	        	        				success: function(response) {
	        	        					var jsonResp = Ext.JSON.decode(response.responseText);
	        	        					if(jsonResp.clientId!=null){
	        	        						lab1.setText('Message  :  Registration Successful.');
	        	        						lab2.setText('Producer Id  : '+jsonResp.clientId);
	        	        						lab3.setText('NOTE :  Please copy this Producer Id and keep it safe as you will require this ID to publish messages.');

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

