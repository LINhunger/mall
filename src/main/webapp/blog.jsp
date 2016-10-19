<%--
  Created by IntelliJ IDEA.
  User: hunger
  Date: 2016/10/16
  Time: 17:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src='//cdn.tinymce.com/4/tinymce.min.js'></script>
    <script>
        tinymce.init({
            selector: '#mytextarea'
        });
    </script>
</head>

<body>
<h1>TinyMCE Quick Start Guide</h1>
<form method="post">
    <textarea id="mytextarea">Hello, World!</textarea>
</form>
</body>
</html>
