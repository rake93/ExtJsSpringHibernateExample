<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<html>
<head>
<title><s:message code="appname" /></title>
<link rel="stylesheet" type="text/css"
	href="http://cdn.sencha.io/ext-4.2.0-gpl/resources/css/ext-all.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/css/style.css">
<script type="text/javascript" charset="utf-8"
	src="http://cdn.sencha.io/ext-4.2.0-gpl/ext-all.js"></script>
<script type="text/javascript"
	src="http://cdn.sencha.io/ext-4.2.0-gpl/locale/ext-lang-ru.js"></script>

<script type="text/javascript">
	Ext.onReady(function() {
		var dictionary = {
				'docNumber': '<s:message code="letter.number" />',
				'creationDate': '<s:message code="letter.date" />',
				'title': '<s:message code="letter.theme" />',
				'published': '<s:message code="letter.published" />',
				'attachedFile': '<s:message code="letter.file" />',
				'textContent': '<s:message code="letter.notation" />'
		};
		
		Ext.create('Ext.form.Panel', {
			title : '<s:message code="form.name" />',
			width : 555,
			bodyPadding : 10,
			frame : true,
			renderTo : 'letter-form',
			items : [ {
				xtype : 'textfield',
				name : 'docNumber',
				fieldLabel : dictionary['docNumber'],
				labelWidth : 120,
				anchor : '100%',
				msgTarget : 'side',
				allowBlank : false
			}, {
				xtype : 'datefield',
				fieldLabel : dictionary['creationDate'],
				labelWidth : 120,
				msgTarget : 'side',
				name : 'creationDate',
				format : 'd.m.Y',
				allowBlank : false
			}, {
				xtype : 'textfield',
				name : 'title',
				fieldLabel : dictionary['title'],
				labelWidth : 120,
				anchor : '100%',
				msgTarget : 'side',
				allowBlank : false
			}, {
				xtype : 'checkbox',
				boxLabel : dictionary['published'],
				name : 'published',
			}, {
				xtype : 'filefield',
				name : 'attachedFile',
				fieldLabel : dictionary['attachedFile'],
				labelWidth : 120,
				msgTarget : 'side',
				allowBlank : false,
				anchor : '100%',
				buttonText : '<s:message code="letter.file.button" />',
			}, {
				xtype : 'textareafield',
				grow : true,
				name : 'textContent',
				fieldLabel : dictionary['textContent'],
				labelWidth : 120,
				anchor : '100%'
			} ],
			buttons : [ {
				text : '<s:message code="addletter" />',
				handler : function() {
					var form = this.up('form').getForm();
					if (form.isValid()) {
						form.submit({
							url : 'add.json',
							waitMsg : '<s:message code="form.upload.msg" />',
							success: function (form, action) {
		                        Ext.Msg.alert('Success', action.result.message);
		                        store.load();
		                        form.reset();
		                    },
		                    failure: function (form, action) {
		                    	var msg = action.result ? action.result.message : 'No response';
		                    	msg += '<br/><br/>';
		                    	var i=1;
		                    	Ext.each(action.result.errors, function(error) {
		                    		msg += '<div>' + (i++) + '. '  +'<s:message code="words.field" /> «' + dictionary[error.field] + '»: ' +  error.errorText + '</div>';
		                    	});
		                        Ext.Msg.alert('Failed', msg);
		                    }
						});
					}
				}
			} ]
		});

		Ext.define('Letter', {
			extend : 'Ext.data.Model',

			idProperty : 'id',

			fields : [ {
				name : 'id',
				type : 'int'
			}, {
				name : 'docNumber',
				type : 'string'
			}, {
				name : 'title',
				type : 'string'
			}, {
				name : 'creationDate',
				type : 'date',
				convert : function(v, j) {
					return new Date(v);
				}
			}, {
				name : 'textContent',
				type : 'string'
			}, {
				name : 'published',
				type : 'bool',
				convert : function(v, record) {
					if (v) {
						return '<s:message code="yes" />';
					} else {
						return '<s:message code="no" />, <a class="publish" lid="' + record.data['id'] + '"><s:message code="publish" /></a>';
					}
				}
			}, {
				name: 'file',
				mapping: 'id',
				convert: function(v, record) {
					return '<a target="blank" href="file/'+ record.data['id'] +'"><s:message code="download" /></a>';
				}
			} ]
		});

		var store = Ext.create('Ext.data.Store', {
			model : 'Letter',
			autoLoad : true,
			proxy : {
				type : 'ajax',
				url : 'list.json',
				reader : {
					type : 'json'
				}
			},
			listeners: {
		        load: function() {
		        	var pub = Ext.select('a.publish');
		        	Ext.each(pub, function(link) {
		        		link.on('click', function(e, t) {
		        			var lid = t.getAttribute("lid");

		        			Ext.Ajax.request({
		        				url: 'publish.json',
		                        method: 'GET',
		                        params: {
		                            "lid": lid
		                        },
		                        failure: function(){
		                            Ext.MessageBox.show({
		                            	title: '<s:message code="words.error" />',
		                            	msg: '<s:message code="form.result.errors.request" />',
		                            	buttons: Ext.MessageBox.OK
		                            }); 
		                        },
		                        success: function(response){
		                            if(Boolean(response)) {
		                            	store.load();
		                            } else {
		                            	Ext.MessageBox.show({
			                            	title: '<s:message code="words.error" />',
			                            	msg: '<s:message code="form.result.errors.serv" />',
			                            	buttons: Ext.MessageBox.OK
			                            }); 
		                            } 
		                        }
		        			});
		        		});
		        	});
		        },
		        scope: this
		    }
		});

		var table = Ext.create('Ext.grid.Panel', {
			title : '<s:message code="table.name" />',
			height : 333,
			width : 999,
			columns : [ {
				header : '<s:message code="letter.number" />',
				dataIndex : 'docNumber'
			}, {
				header : '<s:message code="letter.date" />',
				dataIndex : 'creationDate',
				xtype : 'datecolumn',
				renderer : Ext.util.Format.dateRenderer('d.m.Y'),
				flex : 1
			}, {
				header : '<s:message code="letter.theme" />',
				dataIndex : 'title',
				flex : 1
			}, {
				header : '<s:message code="letter.published" />',
				dataIndex : 'published',
				flex : 1
			}, {
				header : '<s:message code="letter.file" />',
				dataIndex : 'file',
				flex : 1
			}, {
				header : '<s:message code="letter.notation" />',
				dataIndex : 'textContent',
				width : 300
			} ],
			renderTo : 'letters-table',
			store : store
		});
});
</script>
</head>
<body>
	<h1>
		<s:message code="appname" />
	</h1>

	<div id="letter-form"></div>

	<div id="letters-table" style="padding: 25px;"></div>
</body>
</html>
