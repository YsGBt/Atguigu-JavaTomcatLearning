function delFruit(fid) {
    // window.confirm 弹出确认窗口，if检测如果用户选择确认
    if (window.confirm('是否确认删除?')) {
        // 用户地址栏重定向
        window.location.href = 'del.do?fid=' + fid;
    }
}