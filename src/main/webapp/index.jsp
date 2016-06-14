<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html ng-app="javaeeio-main">
<head><title>javaee.io</title>
    <script>
        // doc base
        (function () {
            var contextPath = '<%=request.getContextPath()%>';
            var result = '';
            if (document.location.href === '<%=request.getRequestURL()%>') {
                if (document.location.port) {
                    result = "//" + document.location.hostname + ":" + document.location.port + contextPath + "/";
                } else {
                    result = "//" + document.location.hostname + contextPath + "/";
                }
            } else {
                var reqUrl = '<%=request.getRequestURL()%>'
                        .replace(/^http:/, '')
                        .replace(/^https:/, '')
                        .replace(/^\/\//, '')
                        .replace(/^[^\/]*/, '')
                        .replace(new RegExp('^' + contextPath, "i"), '');
                var baseUrl = document.location.pathname.replace(new RegExp(reqUrl + '$', 'i'), '');
                if (document.location.port) {
                    result = "//" + document.location.hostname + ":" + document.location.port + baseUrl + "/";
                } else {
                    result = "//" + document.location.hostname + baseUrl + "/";
                }
            }
            document.write("<base href='" + result + "' />");
        }());
    </script>
    <link rel="stylesheet" href="app/third-party/styles/_.css"/>
    <link rel="stylesheet" href="app/styles/_.css"/>
    <link rel="icon" href="app/images/favicon.png">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width">
</head>
<body>
<div ng-view class="app-body">
    <div class="app-loading"></div>
</div>
<script type="text/javascript" src="app/third-party/_.js"></script>
<script type="text/javascript" src="app/scripts/_.js"></script>
<script type="text/javascript" src="app/scripts/_templates.js"></script>
</body>
</html>
