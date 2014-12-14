Ext.Loader.setConfig({enabled: true});
Ext.application({
	name : 'gmq', // default namespace
	appFolder : 'app',
	controllers : ['HomeControl'], // controllers
	id : 'myviewport',
	
	launch : function() {

		this.viewPort=Ext.create('Ext.container.Viewport', { // viewport-resizes its child elements with browser size

			layout : {
				type : 'fit' // fit layout covers entire empty space
			},
			defaults : {
				split : false,
				padding:0
			},
			items : [{
				xtype : 'homeLayout'
			}]

		});		

	}

});