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
<h2>青岛流量自动挂机系统</h2>
<form action="/phone/pushPhone" method="post">
    手机号：<input type="text" id="phone" name="phone">
    <br>
    验证码：<input type="text" id="verify_code" name="verify_code">
    <br>
    <img src="/getVerify" alt="verify_code_image" id="verify_code_image">
    <br>
    <input type="submit" value="添加自动挂机" id="btn_add">
</form>
<script src="${root}/lib/axios.min.js"></script>
<script src="${root}/js/index.js"></script>
</body>
</html>