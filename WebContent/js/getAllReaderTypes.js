window.onload = new function(){
	ajax(
		  {
	    		url:"/Library/admin?method=getAllReaderTypes",
	    		type:"json",
	    		callback:function(data) {
					// 循环遍历每个读者分类，每个名称生成一个option对象，添加到<select>中
					for(var index in data) {
						var op = document.createElement("option");//创建一个指名名称元素
						op.value = data[index].readerTypeId;//设置op的实际值为当前的读者分类编号
						var textNode = document.createTextNode(data[index].readerTypeName);//创建文本节点
						op.appendChild(textNode);//把文本子节点添加到op元素中，指定其显示值
						
						document.getElementById("readerType").appendChild(op);
					}
				}
		   }
	);
};

function getAllReaderTypes(){
	ajax(
			  {
		    		url:"/Library/admin?method=getAllReaderTypes",
		    		type:"json",
		    		callback:function(data) {
						// 循环遍历每个读者分类，每个名称生成一个option对象，添加到<select>中
		    			//var select = document.getElementById("addreaderType").removeChild(op);
		    			
		    			//if($("select option:contains("+text+")").length > 1)
		    			//$("select option:contains("+text+"):gt(0)").remove();
						for(var index in data) {
							var op = document.createElement("option");//创建一个指名名称元素
							op.value = data[index].readerTypeId;//设置op的实际值为当前的读者分类编号
							var textNode = document.createTextNode(data[index].readerTypeName);//创建文本节点
							op.appendChild(textNode);//把文本子节点添加到op元素中，指定其显示值
							
							document.getElementById("addreaderType").appendChild(op);
						}
						if($("#addreaderType option:contains('学生')").length > 1){
							$("#addreaderType option:contains('学生'):gt(0)").remove();
						}
						if($("#addreaderType option:contains('老师')").length > 1){
							$("#addreaderType option:contains('老师'):gt(0)").remove();
						}
					}
			   }
		);
	
	
	
	
}
function getAllReaderType(){
	ajax(
			  {
		    		url:"/Library/admin?method=getAllReaderTypes",
		    		type:"json",
		    		callback:function(data) {
						// 循环遍历每个读者分类，每个名称生成一个option对象，添加到<select>中
		    			//var select = document.getElementById("addreaderType").removeChild(op);
		    			
		    			//if($("select option:contains("+text+")").length > 1)
		    			//$("select option:contains("+text+"):gt(0)").remove();
						for(var index in data) {
							var op = document.createElement("option");//创建一个指名名称元素
							op.value = data[index].readerTypeId;//设置op的实际值为当前的读者分类编号
							var textNode = document.createTextNode(data[index].readerTypeName);//创建文本节点
							op.appendChild(textNode);//把文本子节点添加到op元素中，指定其显示值
							
							document.getElementById("readerType").appendChild(op);
						}
						if($("#readerType option:contains('学生')").length > 1){
							$("#readerType option:contains('学生'):gt(0)").remove();
						}
						if($("#readerType option:contains('老师')").length > 1){
							$("#readerType option:contains('老师'):gt(0)").remove();
						}
					}
			   }
		);
	
	
	
	
}
 


