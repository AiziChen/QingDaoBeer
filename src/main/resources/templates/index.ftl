<#assign root=ctx.contextPath />
<!doctype html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>青岛流量自动挂机系统</title>
    <link rel="stylesheet" href="${root}/css/index.css">
</head>
<body>
<div id="content-main">
    <h2>青岛流量自动挂机系统</h2>
    <form action="/phone/pushPhone" method="post">
        <label for="phone">手机号：
            <input type="text" id="phone" name="phone">
        </label>
        <br>
        <label for="verify-code">验证码：
            <input type="text" id="verify-code" name="verify-code">
        </label>
        <br>
        <img src="/getVerify" alt="verify-code-image" id="verify-code-image">
        <br>
        <label><input type="checkbox" id="is-del">取消挂机？</label>
        <br>
        <br>
        <input type="submit" value="添加自动挂机" id="btn-add">
    </form>
</div>
<p id="sys-info">
    <b>关于本系统</b><br>
    作者：Quanyec <br>
    源码：<a href="https://github.com/aizichen/QingDaoBeer">青岛啤酒自动挂机</a><br>
</p>
<script src="${root}/lib/axios.min.js"></script>
<script src="${root}/js/index.js"></script>
</body>
</html>