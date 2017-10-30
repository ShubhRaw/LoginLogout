<%@page import="java.io.PrintWriter"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Homepage</title>
</head>
<body bgcolor="#8cff66">
	<img src="/uploads/Banner.jpg" style="width: 99vw; height: 15vh;" />
	<h3 align="center">
		Welcome
		<%=session.getAttribute("name")%>!
	</h3>
	<img src="/uploads/<%=session.getAttribute("photo")%>" align="right"
		alt="Image not available" height="50" width="50" />

	<form action="UpdateServlet" method="post" onsubmit="return val()">
		<table>
			<tr>
				<td>Name:</td>
				<td><input type="text" name="name" id="name"
					required="required" onfocusout="nameout()" disabled="disabled"
					value="<%=session.getAttribute("name")%>" />
					<p id="pname" style="font-size: 80%; color: red;"></p></td>
			</tr>

			<tr>
				<td>Email ID:</td>
				<td><input type="text" name="email" id="email"
					required="required" onfocusout="emailout()" disabled="disabled"
					value=<%=session.getAttribute("email")%> />
					<p id="pemail" style="font-size: 80%; color: red;"></p></td>
			</tr>

			<tr>
				<td>Course Name:</td>
				<td><b><%=session.getAttribute("course")%></b> &nbsp;&nbsp; <select
					id="courselist" name="courselist" required="required"
					disabled="disabled" hidden="hidden">
						<option value="MCA">MCA</option>
						<option value="MBA">MBA</option>
						<option value="BCA">BCA</option>
						<option value="BBA">BBA</option>
				</select></td>
			</tr>

			<tr>
				<td>Address:</td>
				<td><textarea rows="10" cols="50" name="address" id="address"
						maxlength="300" disabled="disabled"><%=session.getAttribute("address")%></textarea></td>
			</tr>

			<tr>
				<td><input type="submit" id="done" hidden="hidden" value="Done"></td>
			</tr>
		</table>
	</form>
	<br>
	<button id="edit" onclick="enableall()">Edit</button>
	<br>
	<br>

	<form action="LogoutServlet" method="post">
		<input type="text" name="uname" hidden="hidden"
			value=<%=session.getAttribute("name")%> /> <input type="text"
			name="email" hidden="hidden"
			value=<%=session.getAttribute("email")%> /> <input type="submit"
			value="Logout" />
	</form>
	<script>
		function enableall() {
			document.getElementById("name").removeAttribute("disabled");
			document.getElementById("courselist").removeAttribute("disabled");
			document.getElementById("address").removeAttribute("disabled");
			document.getElementById("done").removeAttribute("hidden");
			document.getElementById("courselist").removeAttribute("hidden");
			document.getElementById("edit").setAttribute("hidden", "hidden");
		}

		function nameout() {
			var x = document.getElementById("name");
			var format = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/;

			if (format.test(x.value) == true) {
				document.getElementById("pname").innerHTML = "No Special Characters";
				return false;
			} else {
				document.getElementById("pname").innerHTML = "";
				return true;
			}
		}

		function emailout() {
			var x = document.getElementById("email");
			var format = /\S+@\S+\.\S+/;

			if (format.test(x.value) == false) {
				document.getElementById("pemail").innerHTML = "Enter valid Email";
				return false;
			} else {
				document.getElementById("pemail").innerHTML = "";
				return true;
			}
		}

		function val() {
			document.getElementById("email").removeAttribute("disabled");
			var name = nameout();
			var email = emailout();
			if (name == false || email == false) {
				alert("Please enter correct details!");
				return false;
			}
		}
	</script>
</body>
</html>