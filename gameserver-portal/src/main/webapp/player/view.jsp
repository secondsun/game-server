<%@page import="net.saga.sync.gameserver.portal.Player"%>
<%@page import="net.saga.sync.gameserver.portal.CreatePlayer"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ page import="org.keycloak.util.KeycloakUriBuilder" %>
<html>
    <head>
        <title>Product View Page</title>
    </head>
    <body bgcolor="#F5F6CE">
        <%
            String logoutUri = KeycloakUriBuilder.fromUri("https://auth-coffeeregister.rhcloud.com/auth/rest/realms/game-server/tokens/logout")
                    .queryParam("redirect_uri", "https://localhost:8443/gameserver-portal/").build().toString();
            String acctUri = "https://auth-coffeeregister.rhcloud.com/auth/rest/realms/game-server/account";
        %>

        | <a href="<%=logoutUri%>">logout</a> | <a href="<%=acctUri%>">manage acct</a></p>
       
    <h2>Player creation:</h2>
    <%
        Player player = null;
        try {
            player = CreatePlayer.createPlayer(request);
            {
                out.print("<p>");
                out.print(player.id);
                out.print("<br/>");
                out.print(player.name);
                out.println("</p>");

            }
        } catch (Exception failure) {
            out.println("There was a failure processing request.  You either didn't configure Keycloak properly, or maybe"
                    + "you just forgot to secure the database service?");
            out.println("Status from database service invocation was: " + failure.getMessage());
            return;

        }


    %>
    <br><br>
</body>
</html>