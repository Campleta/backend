<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="put the tag description here" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="pageTitle"%>
<%@attribute name="activePage" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${pageTitle}</title>

        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
        <link rel="stylesheet" href="../../view/style/main.css">
        <link rel="stylesheet" href="../../view/style/navigation.css">
    </head>
    <body>
        <nav class="navbar navbar-fixed-top">
            <div class="container-fluid">
                <!-- Brand and toggle get grouped for better mobile display -->
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
                            aria-expanded="false">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="/Portal">Campleta</a>
                </div>

                <!-- Collect the nav links, forms, and other content for toggling -->
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav">
                      <li><a href="#">Dashboard</a></li>
                    </ul>
                    
                    <ul class="nav navbar-nav navbar-right">
                        <c:choose>
                            <c:when test="${empty User}">
                                <li <c:if test="${activePage == 'register'}"> class="active"</c:if>><a href="/register">Register</a></li>
                                <li <c:if test="${activePage == 'login'}"> class="active"</c:if>><a href="/login">Login</a></li>
                            </c:when>
                            <c:otherwise>
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                                    <c:choose>
                                        <c:when test="${empty Firstname}">Profile</c:when>
                                        <c:otherwise>${Firstname}</c:otherwise>
                                    </c:choose>
                                    <span class="caret"></span>
                                </a>
                                <ul class="dropdown-menu">
                                    <li><a href="#">Profile</a></li>
                                    <c:if test="${not empty HasOwnerRole || not empty HasAdminRole}"><li><a href="/Portal">Portal</a></li></c:if>
                                    <c:if test="${not empty HasAdminRole}"><li><a href="#">Admin</a></li></c:if>
                                </ul>
                            </li>
                                <li <c:if test="${activePage == 'logout'}"> class="active"</c:if>><a href="/logout">Logout</a></li>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </div>
                <!-- /.navbar-collapse -->
            </div>
            <!-- /.container-fluid -->
        </nav>

        <div class="container">
            <jsp:doBody />
        </div>

        <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
    </body>
</html>