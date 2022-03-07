<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html ng-app="microprofileio-main">
<head><title>microprofile.io</title>
    <script data-cfasync="false">
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
    <script>
        // Google Analytics
        (function() {
            try {
                (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
                            (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
                        m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
                })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');
                ga('create', 'UA-79802118-1', 'auto');
                ga('send', 'pageview');
            } catch(e) {
                // no-op
            }
        }());
    </script>
    <link rel="stylesheet" href="app/third-party/styles/_.css"/>
    <link rel="stylesheet" href="app/styles/_.css"/>
    <link rel="stylesheet" href="github-markdown.css">
    <style>
        .markdown-body {
            box-sizing: border-box;
            min-width: 200px;
            max-width: 980px;
            margin: 0 auto;
            padding: 45px;
        }

        @media (max-width: 767px) {
            .markdown-body {
                padding: 15px;
            }
        }
    </style>
    <link rel="icon" href="app/images/favicon.png">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width">
</head>
<body>
<div ng-view class="app-body">
    <div class="app-loading"></div>
</div>
<script data-cfasync="false" type="text/javascript" src="app/third-party/_.js"></script>
<script data-cfasync="false" type="text/javascript" src="app/scripts/_.js"></script>
<script data-cfasync="false" type="text/javascript" src="app/scripts/_templates.js"></script>
</body>
</html>
