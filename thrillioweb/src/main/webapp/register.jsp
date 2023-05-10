<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
</head>
<body>
	<div style="height:65px;align: center;background: #DB5227;font-family: Arial;color: white;">
		<br><b>
		<a href="" style="font-family:garamond;font-size:34px;margin:0 0 0 10px;color:white;text-decoration: none;">thrill.io</a></b>          
	</div>
	<div style="height:25px;background: #DB5227;font-family: Arial;color: white;">
			<b>
			<a href="<%=request.getContextPath()%>/login.jsp" style="font-size:16px;color:white;margin-left:1150px;text-decoration:none;">Login</a>	
			</b>
		</div> 
	<br><br>
	<form name="myForm" action="<%=request.getContextPath()%>/auth/register" onsubmit="return validate()"  method="POST">
      <fieldset>
	    <legend>Register</legend>	    
	    <table>
	        <tr>
	    		<td><label>First Name:</label></td>
        		<td>
        			<input type="text" name="firstname" required><br>        			
        		</td>
        	</tr>
        	<tr>
	    		<td><label>Last Name:</label></td>
        		<td>
        			<input type="text" name="lastname" required><br>        			
        		</td>
        	</tr>
        	<tr>
	    		<td><label>Gender:</label></td>
        		<td>
        			<input type="radio" name="gender" value="0"> Male
                    <input type="radio" name="gender" value="1"> Female   
                    <input type="radio" name="gender" value="2"> Others   			
        		</td>
        	</tr>
	    	<tr>
	    		<td><label>Email:</label></td>
        		<td>
        			<input type="email" name="email" required><br>        			
        		</td>
        	</tr>
        	<tr>
        		<td><label>Password:</label></td>
        		<td>
        			<input type="password" name="password" required minlength="6" maxlength="20"><br>
        		</td>        
        	</tr>
        	<tr>
        		<td><label>Re-enter Password:</label></td>
        		<td>
        			<input type="password" name="repassword" required minlength="6" maxlength="20"><br>
        		</td>        
        	</tr>
        	<tr>
        		<td>&nbsp;</td>
        		<td><input type="submit" name="submitLoginForm" value="Log In"></td>
        	</tr>
        </table>
	  </fieldset>      
    </form>
</body>
<script>
function onValidate(){
	 let password = document.forms["myForm"]["password"].value;
	 let repassword= document.forms["myForm"]["repassword"].value;
	 if(password!=repassword){
		 alert("Password and Re entered Password do not match!");
		 return false;
	 }
	 var format = /[ `!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?~]/;
	 else if(format.test(password[0])){
		 alert("Password cannot start with sppecial character");
		 return false;
	 }
	 else{
		 return true;
	 }
}
</script>
</html>