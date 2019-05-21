<!DOCTYPE html>
<html>
<head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <title>乱码头条 - 乱码集中营</title>
    <meta name="viewport" content="width=device-width, minimum-scale=1.0, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">


    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/font-awesome.min.css">
    <link rel="stylesheet" media="all" href="${contextPath}/styles/style.css">
    <script type="text/javascript" src="${contextPath}/scripts/jquery.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/main/base/base.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/main/base/util.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/main/base/event.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/main/base/upload.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/main/component/component.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/main/component/popup.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/main/component/popupLogin.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/main/component/upload.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/main/component/popupUpload.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/main/util/action.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/main/site/home.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/main/component/popupSendMsg.js"></script>

</head>
<body class="welcome_index">

<header class="navbar navbar-default navbar-static-top bs-docs-nav" id="top" role="banner">
    <div class="container">
        <div class="navbar-header">
            <button class="navbar-toggle collapsed" type="button" data-toggle="collapse" data-target=".bs-navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>

            <a href="/" class="navbar-brand logo">
                <h1>吃不下去</h1>
                <h3>看见乱码心好烦</h3>
            </a>
        </div>

        <nav class="collapse navbar-collapse bs-navbar-collapse" role="navigation">
            <ul class="nav navbar-nav navbar-right">
                <li class=""><a href="${contextPath}/hot/hotshare">最热分享</a></li>
                <li class=""><a href="${contextPath}/recentNews">最新分享</a></li>
                <#if user??>
                <li class="js-share"><a href="javascript:void(0);">分享</a></li>
                <li class=""><a href="${contextPath}/msg/list">站内信</a></li>
                <li class="top-nav-noti zu-top-nav-li ">
                    <a href="${contextPath}/user/tosendmsg">发私信 </a>
                </li>
                <li class=""><a href="${contextPath}/user/${user.id!}/">${user.name!}</a></li>
                <li class=""><a href="${contextPath}/logout/">注销</a></li>
                <#else>
                <li class="js-login"><a href="javascript:void(0);">登陆</a></li>
                </#if>
            </ul>
        </nav>
        <ul class="nav navbar-nav navbar-left">
            <li class=""><a href="${contextPath}/category/finance">财经</a></li>
            <li class=""><a href="${contextPath}/category/entertainment">娱乐</a></li>
            <li class=""><a href="${contextPath}/category/gaming">电竞</a></li>
            <li class=""><a href="${contextPath}/category/political">时政</a></li>
            <li class=""><a href="${contextPath}/category/technology">科技</a></li>
            <li class=""><a href="${contextPath}/category/sport">体育</a></li>
            <li class=""><a href="${contextPath}/category/car">汽车</a></li>
            <li class=""><a href="${contextPath}/category/history">历史</a></li>
        </ul>
    </div>

</header>