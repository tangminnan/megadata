
var prefix = "/xinshineng/menzhen"
$(function() {
	load();
});

function load() {
	$('#exampleTable')
			.bootstrapTable(
					{
						method : 'get', // 服务器数据的请求方式 get or post
						url : prefix + "/list", // 服务器数据的加载地址
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
								offset:params.offset
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
}

/*function getData(date){
	
	var y = (date.getFullYear() + '').padStart(2, '0') // 年
	var m = (date.getMonth() + 1 + '').padStart(2, '0') // 月
	var d = (date.getDate() + '').padStart(2, '0') // 日
	return y + '-' + m + '-' + d
}*/
(function (){
	console.log('fixed-table-body',$('.fixed-table-body'))
})()
function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}

window.operateEvents = {
		    'click .view': function (e, value, row, index) {
			 console.log(row.uimg)
		        layer.open({
		          type: 1,
		          title: false,
		          closeBtn: 0,
				  area: 'auto',
		          skin: 'layui-layer-nobg', //没有背景色
		          shadeClose: true,
		          content: '<img width="300px" height="400px" src="'+row.uimg+'"/>'
		        });
		  },
};


function selectlist() {
	$('#exampleTable').bootstrapTable('destroy');	//清除原来界面
	$('#exampleTable')
			.bootstrapTable(
					{
						method : 'get', // 服务器数据的请求方式 get or post
						url :prefix + "/selectlist", // 服务器数据的加载地址
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
								uname:$('#uname').val(),
								uorganization:$('#uorganization').val(),
								ugender: (function(){
									 if($('#ugender').val() === '男'){
									  return 0
									 }else if($('#ugender').val() === '女'){
									  return 1
									 }
								})(),
								uage:$('#uage').val(),
								mname:$('#mname').val()
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
										field : 'uid', 
										title : 'id',
										align : 'center'
									},
																	{
										field : 'uname', 
										title : '姓名',
										align : 'center'
										
									},
																	{
										field : 'uorganization', 
										title : '单位或学校',
										align : 'center'
									},
																	{
										field : 'ugender', 
										title : '性别',
										formatter: function(value,row,index ){
											if(value == 0) return "男";
											if(value == 1) return "女";
										},
										align : 'center'
									},
																	{
										field : 'ugrand', 
										title : '年级或职业',
										align : 'center'
									},
																	{
										field : 'uage', 
										title : '年龄' ,
										align : 'center'
									},
																	{
										field : 'ubirthday', 
										title : '出生日期',
										align : 'center',
										formatter: function (value, row, index) {
											if(value != null){
												return value.split(' ')[0]
											}
						                }
									},
																	{
										field : 'uidcard', 
										title : '身份证号',
										align : 'center' 
									},
																	{
										field : 'uphone', 
										title : '联系电话',
										align : 'center' 
									},
																	{
										field : 'uheight', 
										title : '身高cm',
										align : 'center'
											
									},
																	{
										field : 'uweight', 
										title : '体重kg',
										align : 'center'
									},
																	{
										field : 'ufolder', 
										title : '3D扫描数据目录',
										align : 'center'
									},								{
										field : 'uimg', 
										title : '用户头像',
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
										title : '负责人',
										align : 'center'
									},
																	{
										field : 'uupdatedate', 
										title : '创建时间',
										align : 'center'
									}
									/*								{
										title : '操作',
										field : 'id',
										align : 'center',
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
}



function downfile(id){
		$.ajax({
			url : "/information/user/download", 
			type : "get",
			data : {
				'uid' : id  
			},
			success : function(data) {
					window.open(data[0].ufolder)
			}
		});
		
		
}

function add(type) {
	console.log(type)
	if(type == 1){
		layer.open({
			type : 2,
			title : '手动添加',
			maxmin : true,
			shadeClose : false, // 点击遮罩关闭层
			area : [ '800px', '520px' ],
			content : prefix + '/add' // iframe的url
		});
	}else if(type == 2){
		layer.open({
			type : 2,
			title : '批量添加',
			maxmin : true,
			shadeClose : false, // 点击遮罩关闭层
			area : [ '800px', '520px' ],
			content : prefix + '/batchAdd' // iframe的url
		});
	}else if(type == 3){
		layer.open({
			type : 2,
			title : '导入扫描数据',
			maxmin : true,
			shadeClose : false, // 点击遮罩关闭层
			area : [ '800px', '520px' ],
			content : prefix + '/batchAdds' // iframe的url
		});
	}else{
		alert("请输入type1或2或3")
	}
	
}

function edit(id) {
	layer.open({
		type : 2,
		title : '编辑',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/edit/' + id // iframe的url
	});
}

function unbind(id){
	$.ajax({
		url : "/information/user/unbind", 
		type : "get",
		data : {
			'uid' : id  
		},
		success : function(data) {
			if (data.code==0) {
				layer.msg(data.msg);
				reLoad();
			}
		}
	});
}

function remove(id) {
	layer.confirm('确定要删除选中的记录？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix+"/remove",
			type : "post",
			data : {
				'uid' : id
			},
			success : function(r) {
				if (r.code==0) {
					layer.msg(r.msg);
					reLoad();
				}else{
					layer.msg(r.msg);
				}
			}
		});
	})
}

function resetfile(id) {
	layer.open({
		type : 2,
		title : '指定',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content :'/information/user/point/' + id // iframe的url
	});
	
}


function batchRemove() {
	var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
	if (rows.length == 0) {
		layer.msg("请选择要删除的数据");
		return;
	}
	layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		var ids = new Array();
		// 遍历所有选择的行数据，取每条数据对应的ID
		$.each(rows, function(i, row) {
			ids[i] = row['uid'];
		});
		$.ajax({
			type : 'POST',
			data : {
				"ids" : ids
			},
			url : prefix + '/batchRemove',
			success : function(r) {
				if (r.code == 0) {
					layer.msg(r.msg);
					reLoad();
				} else {
					layer.msg(r.msg);
				}
			}
		});
	}, function() {

	});
	

}

function code(uid) {
	layer.open({
		type : 2,
		title : '二维码',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '450px', '520px' ],
		content : prefix + '/code/' + uid // iframe的url
	});
}

function erweimaxiazai(){
	var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
	if (rows.length == 0) {
		layer.msg("选择要下载的二维码");
		return;
	}
	var ids=[];
	$.each(rows, function(i, row) {
		ids[i] = row['uid'];
	});
	
	window.location.href="/information/user/downloadErweima?ids="+ids
}

function erweimaxiazaibyliulanqi(uid){
	//window.location.href="/information/student/downLoadErWeiMaByBrower?id="+id
	window.open("/information/user/downLoadErWeiMaByBrower?uid="+uid);
}