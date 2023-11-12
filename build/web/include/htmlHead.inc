<meta charset="utf-8"/>

    <title>Progetto: <%=menuActiveLink%></title>
    <link rel="shortcut icon" href="images/tanadeigoblin.png" />
    <link rel="stylesheet" href="css/addressbook.css" type="text/css" media="screen">
    <script>
        var applicationMessage;
        <%if (applicationMessage != null) {%>
            applicationMessage="<%=applicationMessage%>";
        <%}%>
        function onLoadHandler() {
            try{
                mainOnLoadHandler();
            }catch(e){
            }
            
            if(applicationMessage!=undefined) alert(applicationMessage);
        }
  
    window.addEventListener("load", onLoadHandler);
</script> 
