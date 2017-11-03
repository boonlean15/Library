window.onload = new function(){
	ajax(
		  {
	    		url:"/Library/book?method=getAllBookTypes",
	    		type:"json",
	    		callback:function(data) {
					// 循环遍历每个图书分类，每个名称生成一个option对象，添加到<select>中
					for(var index in data) {
						var op = document.createElement("option");//创建一个指名名称元素
						op.value = data[index].typeId;//设置op的实际值为当前的图书分类编号
						var textNode = document.createTextNode(data[index].typeName);//创建文本节点
						op.appendChild(textNode);//把文本子节点添加到op元素中，指定其显示值
						
						document.getElementById("bookTypeId").appendChild(op);
					}
				}
		   }
	);
};

function getAllBookTypes(){
	ajax(
			  {
		    		url:"/Library/book?method=getAllBookTypes",
		    		type:"json",
		    		callback:function(data) {
		    			// 循环遍历每个图书分类，每个名称生成一个option对象，添加到<select>中
						for(var index in data) {
							var op = document.createElement("option");//创建一个指名名称元素
							op.value = data[index].typeId;//设置op的实际值为当前的图书分类编号
							var textNode = document.createTextNode(data[index].typeName);//创建文本节点
							op.appendChild(textNode);//把文本子节点添加到op元素中，指定其显示值
							
							document.getElementById("bookTypeId").appendChild(op);
						}
						$("select option").each(function() {
				            text = $(this).text();
				            if($("select option:contains("+text+")").length > 1)
				                $("select option:contains("+text+"):gt(0)").remove();
				        });
					}
			   }
		);
	
	
	
	
}
function getAllBookTypeInAddBook(){
	ajax(
			  {
		    		url:"/Library/book?method=getAllBookTypes",
		    		type:"json",
		    		callback:function(data) {
		    			// 循环遍历每个图书分类，每个名称生成一个option对象，添加到<select>中
						for(var index in data) {
							var op = document.createElement("option");//创建一个指名名称元素
							op.value = data[index].typeId;//设置op的实际值为当前的图书分类编号
							var textNode = document.createTextNode(data[index].typeName);//创建文本节点
							op.appendChild(textNode);//把文本子节点添加到op元素中，指定其显示值
							
							document.getElementById("addBookType").appendChild(op);
						}
						$("select option").each(function() {
				            text = $(this).text();
				            if($("select option:contains("+text+")").length > 1)
				                $("select option:contains("+text+"):gt(0)").remove();
				        });
					}
			   }
		);
	
	
	
	
}