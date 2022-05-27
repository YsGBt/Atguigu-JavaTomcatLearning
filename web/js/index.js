function delFruit(fid) {
    // window.confirm 弹出确认窗口，if检测如果用户选择确认
    if (window.confirm('是否确认删除?')) {
        // 用户地址栏重定向
        // before mvc
        // window.location.href = 'del.do?fid=' + fid;
        // after mvc
        window.location.href = 'fruit.do?fid=' + fid + '&operate=delete';
    }
}

function page(pageNo) {
    // before mvc
    // window.location.href = 'index?pageNo=' + pageNo;
    // after mvc
    window.location.href = 'fruit.do?pageNo=' + pageNo;
}