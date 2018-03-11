<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:wrapper 
    pageTitle="Campleta - Login"
    activePage="login">
    <div class="container">
        <div class="col-lg-4 col-lg-offset-4 col-md-4 col-md-offset-4 col-sm-4 col-sm-offset-4">
            <c:if test="${not empty loginFailed}">
                <div class="alert alert-danger">
                    <h3><c:out value="${status}" /></h3>
                    <p><c:out value="${message}" /></p>
                </div>
            </c:if>
            
            <form name="loginForm" method="post" action="login">
                <div class="form-group">
                    <label for="email">Email address</label>
                    <input type="email" class="form-control" id="email" name="email" placeholder="Email" required>
                </div>
                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" class="form-control" id="password" name="password" placeholder="********" required>
                </div>
                <button type="submit" class="btn btn-success">Submit</button>
            </form>
        </div>
    </div>
</t:wrapper>