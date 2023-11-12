<%@page session="false"%>
<!DOCTYPE HTML>
<html lang="it-IT">
  <head>
    <meta charset="UTF-8">
    <meta http-equiv="refresh" content="0; url=/Progetto/Dispatcher">
    <script type="text/javascript">
      function onLoadHandler() {
        window.location.href = "/Progetto/Dispatcher";
      }
      window.addEventListener("load", onLoadHandler);
    </script>
    <title>Page Redirection</title>
  </head>
  <body>
    If you are not redirected automatically, follow the <a href='/Progetto/Dispatcher'>link</a>
  </body>
</html>
