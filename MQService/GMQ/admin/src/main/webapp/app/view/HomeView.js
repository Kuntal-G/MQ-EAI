Ext.define("gmq.view.HomeView", {
		extend : 'Ext.panel.Panel',
		xtype : 'homeLayout',
		title : 'Message Queue',
		layout : 'border',
		items : [
			{
				region : 'north',			
				xtype : 'container',
				id : 'mainHeader',
				height:50,
				layout :'hbox',				
				items :[
				    {
						   xtype : 'container',
						   width : 200,
						   margin:"5 0 0 10 ",
						   html:'<img src="images/gmq-logo.gif" height="40" width="150" />'
					},
				    {
						   xtype : 'container',
						   width : 500,
						   margin:"0 0 0 300",
						   html:'<img src="images/header.gif" height="50" width="450" />'
					}
				]				
				
			},
			{
				region : 'west',
				xtype : 'menuView'
			},
			{
				region : 'south',
				xtype : 'container',
				id : 'mainFooter',
				items : [
					{
						xtype : 'toolbar',						
						items : [
							"->",							
							{
								xtype:"tbtext",
								text:"&#169&nbsp&nbsp&nbspGeneral&nbspMQ&nbspSolutions"
							},
							"->"
						]
					}
				]
			},
			{
				region : 'center',
				xtype : 'centerPanel'
			}
		]

});