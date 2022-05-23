// window : 当前窗口
// window.onload 当当前页面加载完成后
window.onload = function () {
    updateSum();
    // 当页面加载完成后，我们需要绑定各种事件
    // 根据id获取表格
    var fruitTbl = document.getElementById("tbl_fruit");

    // 获取表格中的所有行
    var rows = fruitTbl.rows;
    for (let i = 1; i < rows.length - 1; ++i) {
        var tr = rows[i];
        // 1. 绑定鼠标悬浮设置背景颜色事件
        tr.onmouseover = showBGColor;
        tr.onmouseout = clearBGColor;

        var cells = tr.cells;
        var priceTD = cells[1];
        // 2. 绑定鼠标悬浮在单价单元格改变手势的事件
        priceTD.onmouseover = showHand;
        // 3. 绑定鼠标点击单价单元格的事件
        priceTD.onclick = editPrice;

        // 7. 绑定删除小图标的点击事件
        var img = cells[4].firstChild;
        img.style.cursor = "pointer";
        if (img && img.tagName == "IMG") {
            // 绑定点击事件
            img.onclick = delFruit;
        }
    }

    // 限制单价数量的输入
    document.getElementById("fprice").onkeydown = checkInput;
    document.getElementById("fcount").onkeydown = checkInput;
    // 提交输入后增加表格行
    var submit = document.getElementById("fbtn");
    submit.onclick = addRow;
    // 点击重置后清除输入
    var reset = document.getElementById("freset");
    reset.onclick = resetInput;
}


// 当鼠标悬浮时，显示背景颜色
function showBGColor(event) {
    // event: 当前发生的事件
    // event.srcElement: 事件源
    // alert(event.srcElement);
    // event.srcElement.tagName: 标签名
    // alert(event.srcElement.tagName); --> TD
    if (event && event.srcElement && event.srcElement.tagName == 'TD') {
        var td = event.srcElement;
        var tr = td.parentElement;
        // 如果想要通过js代码设置某节点的样式，则需要加上 .style
        tr.style.backgroundColor = "navy";

        // tr.cells表示获取这个tr中的所有单元格
        var tds = tr.cells;
        for (let i = 0; i < tds.length; ++i) {
            tds[i].style.color = "white";
        }
    }
}

// 当鼠标离开时，恢复原始样式
function clearBGColor(event) {
    if (event && event.srcElement && event.srcElement.tagName == 'TD') {
        var td = event.srcElement;
        var tr = td.parentElement;

        tr.style.backgroundColor = "transparent";

        var tds = tr.cells;
        for (let i = 0; i < tds.length; ++i) {
            tds[i].style.color = "threeddarkshadow"
        }
    }
}

// 当鼠标悬浮在单价单元格时，显示手
function showHand(event) {
    if (event && event.srcElement && event.srcElement.tagName == 'TD') {
        var td = event.srcElement;
        td.style.cursor = "pointer";
    }
}

// 当鼠标点击单价单元格时，进行价格编辑
function editPrice(event) {
    if (event && event.srcElement && event.srcElement.tagName == 'TD') {
        var priceTD = event.srcElement;
        // 目的是判断当前priceTD有字节点，并且第一个子节点是文本节点。 TextNode对应3 ElementNode对应1
        // 检测如果是Text才进行反应 nodeType == 3 -> Text Node
        if (priceTD.firstChild && priceTD.firstChild.nodeType == 3) {
            // innerText 表示设置或者获取当前节点的内部文本
            var oldPrice = priceTD.innerText;
            // innerHTML 表示设置当前节点的内部HTML
            priceTD.innerHTML = "<input type='text' size='4' />";
            // <td><input type='text' size='4' value='/></td> --> 注意之前的5（单价）被替换掉了
            var input = priceTD.firstChild;
            if (input.tagName == "INPUT") {
                input.value = oldPrice;
                // 选中输入框内部的文本
                input.select();
                // 4. 绑定输入框失去焦点事件，失去焦点，更新单价
                input.onblur = updatePrice;
                // input.addEventListener("keypress", function(event) {
                //     if (event.key === "Enter") {
                //         updatePrice(event);
                //     }
                // });
                // 8. 在输入框上绑定键盘摁下事件，此处我们需要保证用户输入的是数字
                input.onkeydown = checkInput;
            }
        }
    }
}

// 更新单价价格
function updatePrice(event) {
    if (event && event.srcElement && event.srcElement.tagName == 'INPUT') {
        var input = event.srcElement;
        var newPrice = input.value;
        // input节点的父节点是td
        var priceTD = input.parentElement;
        priceTD.innerText = newPrice;

        // 5. 更新当前行小记这一个格子的值
        // priceTD.parentElement td的父元素是tr
        updateXJ(event, priceTD.parentElement);
    }
}

// 更新指定行的小记
function updateXJ(event, tr) {
    if (tr && tr.tagName == "TR") {
        var cells = tr.cells;
        var price = cells[1].innerText;
        var count = cells[2].innerText;
        var xj = cells[3];
        // innerText 获取到的值时字符串类型，需要类型转换
        xj.innerText = parseInt(price) * parseInt(count);
        updateSum();
    }
}

// 6. 更新总计
function updateSum(event) {
    var fruitTbl = document.getElementById("tbl_fruit");
    var rows = fruitTbl.rows;
    var sum = 0;
    for (let i = 1; i < rows.length - 1; ++i) {
        var tr = rows[i];
        var xj = parseInt(tr.cells[3].innerText);
        sum += xj;
    }
    rows[rows.length - 1].cells[1].innerText = sum;
}

// 点击删除按钮后删除整行
function delFruit(event) {
    if (event && event.srcElement && event.srcElement.tagName == 'IMG') {
        // alert 表示弹出一个对话框，只有确定按钮
        // confirm 表示弹出一个对话框，有确定和取消按钮。当点击确定，返回true，否则返回false
        if (window.confirm()) {
            var img = event.srcElement;
            var tr = img.parentElement.parentElement;
            var fruitTbl = document.getElementById("tbl_fruit");
            fruitTbl.deleteRow(tr.rowIndex);

            updateSum();
        }
    }
}

// 检验键盘摁下的值的方法
function checkInput(event) {
    var kc = event.keyCode;
    // 0 - 9 --> 48 - 57
    // delete --> 8
    // return --> 13
    // console.log(kc);

    if (!((kc >= 48 && kc <= 57) || kc == 8 || kc == 13)) {
        event.returnValue = false;
    }

    if (kc == 13) {
        event.srcElement.blur();
    }
}


// 基于目前输入对表格增加一行 (添加水果信息)
function addRow(event) {
    var table = document.getElementById("tbl_fruit");
    var name = document.getElementById("fname").value;
    var price = document.getElementById("fprice").value;
    var count = document.getElementById("fcount").value;

    var newRow = table.insertRow(1);
    newRow.onmouseover = showBGColor;
    newRow.onmouseout = clearBGColor;

    var nameCell = newRow.insertCell();
    nameCell.innerText = name;

    var priceCell = newRow.insertCell();
    priceCell.innerText = price;
    priceCell.onmouseover = showHand;
    priceCell.onclick = editPrice;

    var countCell = newRow.insertCell();
    countCell.innerText = count;

    newRow.insertCell();

    var delCell = newRow.insertCell();
    delCell.innerHTML = "<img id='del_img' src='imgs/wastebasket.png'/>";
    delCell.onclick = delFruit;

    updateXJ(event, newRow);
}

// 清除所有输入 
function resetInput() {
    var name = document.getElementById("fname");
    name.value = '';
    var price = document.getElementById("fprice");
    price.value = '';
    var count = document.getElementById("fcount");
    count.value = '';
}
