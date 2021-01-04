var identityCard = $('#identityCard').val();



$(function() {
	load();
});

function load() {
	
	$('#exampleTable1')
	.bootstrapTable(
			{
				method : 'get', // 服务器数据的请求方式 get or post
				url : "/xinshineng/users/list", // 服务器数据的加载地址
			//	showRefresh : true,
			//	showToggle : true,
			//	showColumns : true,
				iconSize : 'outline',
				toolbar : '#exampleToolbar',
				striped : true, // 设置为true会有隔行变色效果
				dataType : "json", // 服务器返回的数据类型
				pagination : true, // 设置为true会在底部显示分页条
				// queryParamsType : "limit",
				// //设置为limit则会发送符合RESTFull格式的参数
				singleSelect : false, // 设置为true将禁止多选
				// contentType : "application/x-www-form-urlencoded",
				// //发送到服务器的数据编码类型
				pageSize : 10, // 如果设置了分页，每页数据条数
				pageNumber : 1, // 如果设置了分布，首页页码
				//search : true, // 是否显示搜索框
				showColumns : false, // 是否显示内容下拉框（选择显示的列）
				sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
				queryParams : function(params) {
					return {
						//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
						limit: params.limit,
						offset:params.offset,
						//checkDate : $("#checkDate").find("option:selected").text()
						identityCard:$('#identityCard').val()
			           // name:$('#searchName').val(),
			           // username:$('#searchName').val()
					};
				},
				// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
				// queryParamsType = 'limit' ,返回参数必须包含
				// limit, offset, search, sort, order 否则, 需要包含:
				// pageSize, pageNumber, searchText, sortName,
				// sortOrder.
				// 返回false将会终止请求
				columns : [
							{
								checkbox : true
							},
							/*								{
								field : 'id', 
								title : 'id' 
							},
															{
								field : 'userId', 
								title : '用户ID' 
							},					{
								field : 'openId', 
								title : '微信id' 
							},
							*/								{
								field : 'nickname', 
								title : '昵称' 
							},
							/*							{
								field : 'password', 
								title : '密码' 
							},
							*/								{
								field : 'phone', 
								title : '手机号' 
							},
							/*								{
								field : 'heardUrl', 
								title : '头像' 
							},
							*/								{
								field : 'name', 
								title : '真实姓名' 
							},	
															{
								field : 'sex', 
								title : '性别', 
								align : 'center',
								formatter : function(value, row, index) {
								    if(value == '1'){
								    	return '<span class="label">男</span>';
								    }else if(value == '2'){
								    	return '<span class="label">女</span>';
								    	
								    }
								}		
							},
															{
								field : 'birthday', 
								title : '生日' 
								
							},
							/*								{
								field : 'identityCard', 
								title : '身份证号' 
							},
							*/							{
								field : 'registerTime', 
								title : '注册时间' 
							},
							/*								{
								field : 'payNum', 
								title : '消费金额' 
							},
															{
								field : 'serveNum', 
								title : '服务次数' 
							},
															{
								field : 'balance', 
								title : '余额' 
							},
															{
								field : 'restitution', 
								title : '返还' 
							},
															{
								field : 'payTime', 
								title : '缴费日期' 
							},
							*/								{
								field : 'loginTime', 
								title : '最后登录时间' 
							},
							/*								{
								field : 'addTime', 
								title : '添加时间' 
							},
															{
								field : 'updateTime', 
								title : '修改时间' 
							},
							
															{
								field : 'username', 
								title : '' 
							},
							*/
															{
								field : 'deleteFlag', 
								title : '状态' ,
								align : 'center',
							    formatter : function(value, row, index) {
							    	if(value == '0'){
							    		return '<span class="label label-danger">禁止</span>';
							    	}else if(value == '1'){
							    		return '<span class="label label-primary">正常</span>';
							    		
							    	}
							    }			
							}/*,
							
														{
								title : '操作',
								field : 'id',
								align : 'center',
								formatter : function(value, row, index) {
									
									var e = '<a class="btn btn-primary btn-sm '+s_edit_h+'" href="#" mce_href="#" title="编辑" onclick="edit(\''
											+ row.id
											+ '\')"><i class="fa fa-edit"></i></a> ';
									var d = '<a class="btn btn-warning btn-sm '+s_remove_h+'" href="#" title="状态"  mce_href="#" onclick="remove(\''
											+ row.id
											+ '\')"><i class="fa fa-remove"></i></a> ';
									var f = '<a class="btn btn-success btn-sm" title="详情"  mce_href="#" onclick="showdetail(\''
											+ row.id
											+ '\')"><i class="fa fa-list"></i></a> ';
									return f;
									
								}
							}*/ ]
				});
	
	$('#exampleTable2')
	.bootstrapTable(
			{
				method : 'get', // 服务器数据的请求方式 get or post
				url : "/xinshineng/jianhuyi/list", // 服务器数据的加载地址
			//	showRefresh : true,
			//	showToggle : true,
			//	showColumns : true,
				iconSize : 'outline',
				toolbar : '#exampleToolbar',
				striped : true, // 设置为true会有隔行变色效果
				dataType : "json", // 服务器返回的数据类型
				pagination : true, // 设置为true会在底部显示分页条
				// queryParamsType : "limit",
				// //设置为limit则会发送符合RESTFull格式的参数
				singleSelect : false, // 设置为true将禁止多选
				// contentType : "application/x-www-form-urlencoded",
				// //发送到服务器的数据编码类型
				pageSize : 10, // 如果设置了分页，每页数据条数
				pageNumber : 1, // 如果设置了分布，首页页码
				//search : true, // 是否显示搜索框
				showColumns : false, // 是否显示内容下拉框（选择显示的列）
				sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
				queryParams : function(params) {
					return {
						//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
						limit: params.limit,
						offset:params.offset,
						identityCard:$('#identityCard').val()
					};
				},
				// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
				// queryParamsType = 'limit' ,返回参数必须包含
				// limit, offset, search, sort, order 否则, 需要包含:
				// pageSize, pageNumber, searchText, sortName,
				// sortOrder.
				// 返回false将会终止请求
				columns : [
						{
							checkbox : true
						},
						/*								{
							field : 'id', 
							title : 'id' 
						},
														{
							field : 'userId', 
							title : '用户ID' 
						},					{
							field : 'openId', 
							title : '微信id' 
						},
						*/								{
							field : 'nickname', 
							title : '昵称' 
						},
						/*							{
							field : 'password', 
							title : '密码' 
						},
						*/								{
							field : 'phone', 
							title : '手机号' 
						},
						/*								{
							field : 'heardUrl', 
							title : '头像' 
						},
						*/								{
							field : 'name', 
							title : '真实姓名' 
						},	
														{
							field : 'sex', 
							title : '性别', 
							align : 'center',
							formatter : function(value, row, index) {
							    if(value == '1'){
							    	return '<span class="label">男</span>';
							    }else if(value == '2'){
							    	return '<span class="label">女</span>';
							    	
							    }
							}		
						},
														{
							field : 'birthday', 
							title : '生日' 
							
						},
						/*								{
							field : 'identityCard', 
							title : '身份证号' 
						},
						*/							{
							field : 'registerTime', 
							title : '注册时间' 
						},
						/*								{
							field : 'payNum', 
							title : '消费金额' 
						},
														{
							field : 'serveNum', 
							title : '服务次数' 
						},
														{
							field : 'balance', 
							title : '余额' 
						},
														{
							field : 'restitution', 
							title : '返还' 
						},
														{
							field : 'payTime', 
							title : '缴费日期' 
						},
						*/								{
							field : 'loginTime', 
							title : '最后登录时间' 
						},
						/*								{
							field : 'addTime', 
							title : '添加时间' 
						},
														{
							field : 'updateTime', 
							title : '修改时间' 
						},
						
														{
							field : 'username', 
							title : '' 
						},
						*/
														{
							field : 'deleteFlag', 
							title : '状态' ,
							align : 'center',
						    formatter : function(value, row, index) {
						    	if(value == '0'){
						    		return '<span class="label label-danger">禁止</span>';
						    	}else if(value == '1'){
						    		return '<span class="label label-primary">正常</span>';
						    		
						    	}
						    }			
						}/*,
						
													{
							title : '操作',
							field : 'id',
							align : 'center',
							formatter : function(value, row, index) {
								
								var e = '<a class="btn btn-primary btn-sm '+s_edit_h+'" href="#" mce_href="#" title="编辑" onclick="edit(\''
										+ row.id
										+ '\')"><i class="fa fa-edit"></i></a> ';
								var d = '<a class="btn btn-warning btn-sm '+s_remove_h+'" href="#" title="状态"  mce_href="#" onclick="remove(\''
										+ row.id
										+ '\')"><i class="fa fa-remove"></i></a> ';
								var f = '<a class="btn btn-success btn-sm" title="详情"  mce_href="#" onclick="showdetail(\''
										+ row.id
										+ '\')"><i class="fa fa-list"></i></a> ';
								return  f;
								
							}
						} */]
			});
	
	
	
	
	
	$('#exampleTable5')
	.bootstrapTable(
			{
				method : 'get', // 服务器数据的请求方式 get or post
				url : "/xinshineng/menzhen/list", // 服务器数据的加载地址
			//	showRefresh : true,
			//	showToggle : true,
			//	showColumns : true,
				iconSize : 'outline',
				toolbar : '#exampleToolbar',
				striped : true, // 设置为true会有隔行变色效果
				dataType : "json", // 服务器返回的数据类型
				pagination : true, // 设置为true会在底部显示分页条
				// queryParamsType : "limit",
				// //设置为limit则会发送符合RESTFull格式的参数
				singleSelect : false, // 设置为true将禁止多选
				// contentType : "application/x-www-form-urlencoded",
				// //发送到服务器的数据编码类型
				pageSize : 10, // 如果设置了分页，每页数据条数
				pageNumber : 1, // 如果设置了分布，首页页码
				//search : true, // 是否显示搜索框
				showColumns : false, // 是否显示内容下拉框（选择显示的列）
				sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
				
				queryParams : function(params) {
					return {
						//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
						limit: params.limit,
						offset:params.offset,
						uidcard:$('#identityCard').val()
			           // name:$('#searchName').val(),
			           // username:$('#searchName').val()
					};
				},
				// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
				// queryParamsType = 'limit' ,返回参数必须包含
				// limit, offset, search, sort, order 否则, 需要包含:
				// pageSize, pageNumber, searchText, sortName,
				// sortOrder.
				// 返回false将会终止请求
				columns : [
						{
							checkbox : true
						},
														/*{
							field : 'uid', 
							title : '<strong>id</strong>',
							align : 'center',
							width :  50
						},*/
														{
							field : 'uname', 
							title : '姓名',
							align : 'center',
							width :  50
							
						},
														{
							field : 'uorganization', 
							title : '单位/学校',
							align : 'center',
							width : 65
						},
														{
							field : 'ugender', 
							title : '性别',
							width : 60,
							formatter: function(value,row,index ){
								if(value == 0) return "男";
								if(value == 1) return "女";
							},
							align : 'center'
						},
														{
							field : 'ugrand', 
							title : '年级/职业',
							width : 65,
							align : 'center'
						},
														{
							field : 'uage', 
							title : '年龄' ,
							width : 40,
							align : 'center'
						},
														{
							field : 'ubirthday', 
							title : '出生日期',
							align : 'center',
							width : 65,
							formatter: function (value, row, index) {
								if(value != null){
									return value.split(' ')[0]
								}
			                }
						},
														{
							field : 'uidcard', 
							title : '身份证号',
							width : 80,
							align : 'center' 
						},
														{
							field : 'uphone', 
							title : '联系电话',
							width : 65,
							align : 'center' 
						},
														{
							field : 'uheight', 
							title : '身高cm',
							width : 60,
							align : 'center'
								
						},
														{
							field : 'uweight', 
							title : '体重kg',
							width : 60,
							align : 'center'
						},
														/*{
							field : 'ufolder', 
							title : '3D扫描数据目录',
							width : 200,
							align : 'center',
							
						},	*/							{
							field : 'uimg', 
							title : '用户头像',
							width : 65,
							formatter : function(value ,row , index){
								if(value!=null && value !=""){
									var e = '<div class="image"><a class = ""  href="javascript:void(0)"><img width="90" height="120"  alt="image" class="img-responsive pimg view" src="' + value + '"></ a></div>'
									return e;
								}
								else
									return "";
							},
							events: 'operateEvents',
							align : 'center'
						},
														{
							field : 'mname', 
							title : '医生',
							width : 60,
							align : 'center'
						},
														{
							field : 'uupdatedate', 
							title : '创建时间',
							width : 65,
							align : 'center'
						}
						/*								{
							title : '操作',
							field : 'id',
							align : 'center',
							width : 60,
							formatter : function(value, row, index) {
								var e = '<a class="btn btn-primary btn-block btn-sm '+s_edit_h+'" href="#" mce_href="#" title="编辑" onclick="edit(\''
										+ row.uid
										+ '\')"><i class="fa fa-edit">修改</i></a> ';
								
								var f = '<a class="btn btn-primary btn-block btn-sm" href="#" title="指定"  mce_href="#" onclick="resetfile(\''
										+ row.uid
										+ '\')"><i class="fa fa-key">指定</i></a> ';
								
								var d = '<a class="btn btn-primary btn-block btn-sm" href="#" title="解绑"  mce_href="#" onclick="unbind(\''
									+ row.uid
									+ '\')"><i class="fa fa-key">解绑</i></a> ';
								
								var a = '<a class="btn btn-primary btn-block btn-sm dowmL"   title="下载"  onclick="downfile(\''
									   + row.uid
									   + '\')" ><i class="fa fa-download">下载</i></ a> ';
								
							   var b = '<a class="btn btn-primary btn-block btn-sm dowmL" style="display:none"   title="下载"  onclick="downfile(\''
								   + row.uid
								   + '\')" ><i class="fa fa-download">下载</i></ a> ';
								var f = '<a class="btn btn-success btn-block btn-sm" href="#" title="二维码"  mce_href="#" onclick="code(\''
									+ row.uid
									+ '\')" style="text-decoration: none;">二维码</a>';	
								
								if((row.ufolder ===null || row.ufolder === "") && (row.uimg === "" || row.uimg === null)){
									return e + f + b ;
								}else if(row.uname != null && row.ufolder != null){
									return e + a + d;
								}else{
									return e + a ;
								}
								var n = '<a class="btn btn-primary btn-block btn-sm" href="#" title="二维码下载"  mce_href="#" onclick="erweimaxiazaibyliulanqi(\''
									+ row.uid
									+ '\')" style="text-decoration: none;">二维码打印</a>';
								return e+f+n;
								
							}
						}*/ ]
			});
	
	$('#exampleTable4')
	.bootstrapTable(
			{
				method : 'get', // 服务器数据的请求方式 get or post
				url : "/xinshineng/shaicha/list", // 服务器数据的加载地址
			//	showRefresh : true,
			//	showToggle : true,
			//	showColumns : true,
				iconSize : 'outline',
				toolbar : '#exampleToolbar',
				striped : true, // 设置为true会有隔行变色效果
				dataType : "json", // 服务器返回的数据类型
				pagination : true, // 设置为true会在底部显示分页条
				// queryParamsType : "limit",
				// //设置为limit则会发送符合RESTFull格式的参数
				singleSelect : false, // 设置为true将禁止多选
				// contentType : "application/x-www-form-urlencoded",
				// //发送到服务器的数据编码类型
				pageSize : 10, // 如果设置了分页，每页数据条数
				pageNumber : 1, // 如果设置了分布，首页页码
				//search : true, // 是否显示搜索框
				showColumns : false, // 是否显示内容下拉框（选择显示的列）
				sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
				queryParams : function(params) {
					return {
						//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
						limit: params.limit,
						offset:params.offset,
						identityCard:$('#identityCard').val()
						/*studentName:$("#studentName").val(),
						phone:$("#phone").val(),
						school:$("#school option:selected").val(),
						studentSex:$("#studentSex option:selected").val(),
						lastCheckTime:$("#lastCheckTime").val()*/
			           // name:$('#searchName').val(),
			           // username:$('#searchName').val()
					};
				},
				// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
				// queryParamsType = 'limit' ,返回参数必须包含
				// limit, offset, search, sort, order 否则, 需要包含:
				// pageSize, pageNumber, searchText, sortName,
				// sortOrder.
				// 返回false将会终止请求
				columns : [
						{
							checkbox : true
						},
														{
							field : 'id', 
							title : 'id' 
						},
														{
							field : 'studentName', 
							title : '学生姓名' 
						},
														{
							field : 'studentSex', 
							title : '性别 ' ,
							formatter : function(value, row, index) {
								if(value == '1'){
							   		return '<span class="label">男</span>';
							   	}else if(value == '2'){
							   		return '<span class="label">女</span>';
							   	}else if(value == '0'){
							   		return '<span class="label">未知</span>';
							   	}
							}
						},
														{
							field : 'nation', 
							title : '民族' 
						},
														{
							field : 'birthday', 
							title : '出生年月' 
						},
														{
							field : 'identityCard', 
							title : '身份证号' 
						},
														{
							field : 'school', 
							title : '学校' 
						},
														{
							field : 'grade', 
							title : '年级' 
						},
														{
							field : 'studentClass', 
							title : '班级' 
						},
														{
							field : 'phone', 
							title : '联系电话' 
						},
//														{
//							field : 'address', 
//							title : '联系地址' 
//						},
//														{
//							field : 'height', 
//							title : '身高' 
//						},
//														{
//							field : 'weight', 
//							title : '体重' 
//						},
														{
							field : 'addTime', 
							title : '添加时间' 
						},
						/*								{
							field : 'status', 
							title : '状态0：正常1：禁止' 
						},*/
						/*								{
							title : '操作',
							field : 'id',
							align : 'center',
							formatter : function(value, row, index) {
								var e = '<a class="btn btn-primary btn-sm '+s_edit_h+'" href="#" mce_href="#" title="编辑" onclick="edit(\''
										+ row.id
										+ '\')"><i class="fa fa-edit"></i></a> ';
								var d = '<a class="btn btn-warning btn-sm '+s_remove_h+'" href="#" title="删除"  mce_href="#" onclick="remove(\''
										+ row.id
										+ '\')"><i class="fa fa-remove"></i></a> ';
								var f = '<a class="btn btn-success btn-xs" href="#" title="二维码"  mce_href="#" onclick="code(\''
										+ row.id
										+ '\')" style="text-decoration: none;">二维码</a>';
								var g = '<a class="btn btn-primary btn-xs" href="#" title="详情"  mce_href="#" onclick="detail(\''
										+ row.id
										+ '\')" style="text-decoration: none;">详情</a>';
								var h = '<a class="btn btn-primary btn-xs" href="#" title="电脑验光数据"  mce_href="#" onclick="optometry(\''
										+ row.id
										+ '\')" style="text-decoration: none;">验光数据</a>';
								var p = '<a class="btn btn-primary btn-xs" href="#" title=""  mce_href="#" onclick="datijieguoR(\''
									+ row.identityCard
									+ '\')" style="text-decoration: none;">答题结果</a>';
								var n = '<a class="btn btn-primary btn-xs" href="#" title="二维码下载"  mce_href="#" onclick="erweimaxiazaibyliulanqi(\''
									+ row.id
									+ '\')" style="text-decoration: none;">二维码打印</a>';
								var q = '<a class="btn btn-primary btn-xs" href="#" title="筛查打印"  mce_href="#" onclick="putongshaichadayin(\''
									+ row.id
									+ '\',\''+row.lastCheckTime+'\')" style="text-decoration: none;">筛查打印</a>';
								return g + h ;
							}
						} */]
			});
	
	$('#exampleTable6')
	.bootstrapTable(
			{
				method : 'get', // 服务器数据的请求方式 get or post
				url : "/xinshineng/shaicha/listshifan", // 服务器数据的加载地址
			//	showRefresh : true,
			//	showToggle : true,
			//	showColumns : true,
				iconSize : 'outline',
				toolbar : '#exampleToolbar',
				striped : true, // 设置为true会有隔行变色效果
				dataType : "json", // 服务器返回的数据类型
				pagination : true, // 设置为true会在底部显示分页条
				// queryParamsType : "limit",
				// //设置为limit则会发送符合RESTFull格式的参数
				singleSelect : false, // 设置为true将禁止多选
				// contentType : "application/x-www-form-urlencoded",
				// //发送到服务器的数据编码类型
				pageSize : 10, // 如果设置了分页，每页数据条数
				pageNumber : 1, // 如果设置了分布，首页页码
				//search : true, // 是否显示搜索框
				showColumns : false, // 是否显示内容下拉框（选择显示的列）
				sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
				queryParams : function(params) {
					return {
						//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
						limit: params.limit,
						offset:params.offset,
						identityCard:$('#identityCard').val()
						/*studentName:$("#studentName").val(),
						phone:$("#phone").val(),
						school:$("#school option:selected").val(),
						studentSex:$("#studentSex option:selected").val()*/
			           // name:$('#searchName').val(),
			           // username:$('#searchName').val()
					};
				},
				// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
				// queryParamsType = 'limit' ,返回参数必须包含
				// limit, offset, search, sort, order 否则, 需要包含:
				// pageSize, pageNumber, searchText, sortName,
				// sortOrder.
				// 返回false将会终止请求
				columns : [
						{
							checkbox : true
						},
														{
							field : 'id', 
							title : 'id' 
						},
						{
							field : 'ideentityType', 
							title : '证件类型' 
						},
						{
							field : 'identityCard', 
							title : '证件号码' 
						},
														{
							field : 'studentName', 
							title : '姓名' 
						},
														{
							field : 'studentSex', 
							title : '性别 ' ,
							formatter : function(value, row, index) {
								if(value == '1'){
							   		return '<span class="label">男</span>';
							   	}else if(value == '2'){
							   		return '<span class="label">女</span>';
							   	}else if(value == '0'){
							   		return '<span class="label">未知</span>';
							   	}
							}
						},
						{
							field : 'birthday', 
							title : '出生日期' 
						},
														{
							field : 'nation', 
							title : '民族' 
						},
														
						{
							field : 'xueBu', 
							title : '学部' 
						},								
														{
							field : 'school', 
							title : '学校' 
						},
						{
							field : 'schoolCode', 
							title : '学校编码' 
						},
														{
							field : 'grade', 
							title : '年级' 
						},
														{
							field : 'studentClass', 
							title : '班级' 
						},
														{
							field : 'phone', 
							title : '联系人电话' 
						},
//														{
//							field : 'address', 
//							title : '联系地址' 
//						},
//														{
//							field : 'height', 
//							title : '身高' 
//						},
//														{
//							field : 'weight', 
//							title : '体重' 
//						},
														{
							field : 'addTime', 
							title : '添加时间' 
						},
						/*								{
							field : 'status', 
							title : '状态0：正常1：禁止' 
						},*/
						/*								{
							title : '操作',
							field : 'id',
							align : 'center',
							formatter : function(value, row, index) {
								var e = '<a class="btn btn-primary btn-sm '+s_edit_h+'" href="#" mce_href="#" title="编辑" onclick="edit(\''
										+ row.id
										+ '\')"><i class="fa fa-edit"></i></a> ';
								var d = '<a class="btn btn-warning btn-sm '+s_remove_h+'" href="#" title="删除"  mce_href="#" onclick="remove(\''
										+ row.id
										+ '\')"><i class="fa fa-remove"></i></a> ';
								var f = '<a class="btn btn-success btn-xs" href="#" title="二维码"  mce_href="#" onclick="code(\''
										+ row.id
										+ '\')" style="text-decoration: none;">二维码</a>';
								var g = '<a class="btn btn-primary btn-xs" href="#" title="详情"  mce_href="#" onclick="detail(\''
										+ row.id
										+ '\')" style="text-decoration: none;">详情</a>';
								var h = '<a class="btn btn-primary btn-xs" href="#" title="电脑验光数据"  mce_href="#" onclick="optometry(\''
										+ row.id
										+ '\')" style="text-decoration: none;">验光数据</a>';
								var p = '<a class="btn btn-primary btn-xs" href="#" title=""  mce_href="#" onclick="datijieguoR(\''
									+ row.identityCard
									+ '\')" style="text-decoration: none;">答题结果</a>';
								var n = '<a class="btn btn-primary btn-xs" href="#" title="二维码打印"  mce_href="#" onclick="erweimaxiazaibyliulanqi(\''
									+ row.id
									+ '\')" style="text-decoration: none;">二维码打印</a>';
								var q = '<a class="btn btn-primary btn-xs" href="#" title="筛查打印"  mce_href="#" onclick="shifanshaichadayin(\''
									+ row.id
									+ '\',\''+row.lastCheckTime+'\')" style="text-decoration: none;">筛查打印</a>';
								return g + h;
							}
						}*/ ]
			});
	
	$('#exampleTable3')
	.bootstrapTable(
			{
				method : 'get', // 服务器数据的请求方式 get or post
				url : "/xinshineng/yanke/list", // 服务器数据的加载地址
			//	showRefresh : true,
			//	showToggle : true,
			//	showColumns : true,
				iconSize : 'outline',
				toolbar : '#exampleToolbar',
				striped : true, // 设置为true会有隔行变色效果
				dataType : "json", // 服务器返回的数据类型
				pagination : true, // 设置为true会在底部显示分页条
				// queryParamsType : "limit",
				// //设置为limit则会发送符合RESTFull格式的参数
				singleSelect : false, // 设置为true将禁止多选
				// contentType : "application/x-www-form-urlencoded",
				// //发送到服务器的数据编码类型
				pageSize : 10, // 如果设置了分页，每页数据条数
				pageNumber : 1, // 如果设置了分布，首页页码
				//search : true, // 是否显示搜索框
				showColumns : false, // 是否显示内容下拉框（选择显示的列）
				sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
				queryParams : function(params) {
					return {
						//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
						limit: params.limit,
						offset:params.offset,
						identityCard:$('#identityCard').val()
						/*studentName:$("#studentName").val(),
						phone:$("#phone").val(),
						school:$("#school option:selected").val(),
						studentSex:$("#studentSex option:selected").val()*/
			           // name:$('#searchName').val(),
			           // username:$('#searchName').val()
					};
				},
				// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
				// queryParamsType = 'limit' ,返回参数必须包含
				// limit, offset, search, sort, order 否则, 需要包含:
				// pageSize, pageNumber, searchText, sortName,
				// sortOrder.
				// 返回false将会终止请求
				columns : [
						{
							checkbox : true
						},
														{
							field : 'id', 
							title : 'id' 
						},
														{
							field : 'studentName', 
							title : '学生姓名' 
						},
														{
							field : 'studentSex', 
							title : '性别 ' ,
							formatter : function(value, row, index) {
								if(value == '1'){
							   		return '<span class="label">男</span>';
							   	}else if(value == '2'){
							   		return '<span class="label">女</span>';
							   	}else if(value == '0'){
							   		return '<span class="label">未知</span>';
							   	}
							}
						},
														{
							field : 'nation', 
							title : '民族' 
						},
														{
							field : 'birthday', 
							title : '出生年月' 
						},
														{
							field : 'identityCard', 
							title : '身份证号' 
						},
														{
							field : 'school', 
							title : '学校' 
						},
														{
							field : 'grade', 
							title : '年级' 
						},
														{
							field : 'studentClass', 
							title : '班级' 
						},
														{
							field : 'phone', 
							title : '联系电话' 
						},
														{
							field : 'address', 
							title : '联系地址' 
						},
														{
							field : 'height', 
							title : '身高' 
						},
														{
							field : 'weight', 
							title : '体重' 
						},
														{
							field : 'addTime', 
							title : '添加时间' 
						}/*,*/
						/*								{
							field : 'status', 
							title : '状态0：正常1：禁止' 
						},*//*
														{
							title : '操作',
							field : 'id',
							align : 'center',
							formatter : function(value, row, index) {
								var e = '<a class="btn btn-primary btn-sm '+s_edit_h+'" href="#" mce_href="#" title="编辑" onclick="edit(\''
										+ row.id
										+ '\')"><i class="fa fa-edit"></i></a> ';
								var d = '<a class="btn btn-warning btn-sm '+s_remove_h+'" href="#" title="删除"  mce_href="#" onclick="remove(\''
										+ row.id
										+ '\')"><i class="fa fa-remove"></i></a> ';
								var f = '<a class="btn btn-success btn-xs" href="#" title="二维码"  mce_href="#" onclick="code(\''
										+ row.id
										+ '\')" style="text-decoration: none;">二维码</a>';
								var g = '<a class="btn btn-primary btn-xs" href="#" title="详情"  mce_href="#" onclick="detail(\''
										+ row.id
										+ '\')" style="text-decoration: none;">详情</a>';
								var h = '<a class="btn btn-primary btn-xs" href="#" title="电脑验光数据"  mce_href="#" onclick="optometry(\''
										+ row.id
										+ '\')" style="text-decoration: none;">验光数据</a>';
								var p = '<a class="btn btn-primary btn-xs" href="#" title=""  mce_href="#" onclick="datijieguoR(\''
									+ row.identityCard
									+ '\')" style="text-decoration: none;">答题结果</a>';
								return g + h+p;
							}
						} */]
			});
	
}

function reLoad() {
	
	$('#exampleTable1').bootstrapTable('refresh');
	$('#exampleTable2').bootstrapTable('refresh');
	$('#exampleTable3').bootstrapTable('refresh');
	$('#exampleTable4').bootstrapTable('refresh');
	$('#exampleTable5').bootstrapTable('refresh');
	$('#exampleTable6').bootstrapTable('refresh');

}