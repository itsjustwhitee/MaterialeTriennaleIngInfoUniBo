package servlets;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.User;
import beans.UserGroup;

public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public SignUpServlet() {
		super();
	}
	 @Override
	    public void init(ServletConfig config) throws ServletException {
		 super.init(config);
	    }

	 public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			response.getWriter().append("Served at: ").append(request.getContextPath());
		}
	 
	    @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	    	ServletContext ctx = this.getServletContext();
	    	UserGroup usr = (UserGroup) ctx.getAttribute("listaUtenti");
	    	HttpSession session = request.getSession(); 
	    	int error = 0;
	        User u = new User(request.getParameter("Username"), request.getParameter("Pwd")); //preparo il nuovo user
	        
	        if(usr.checkUser(u)) { //Utente gia' registrato
	        	error = 1;
	        	session.setAttribute("error", error);
	        }else {
	        	error = -1;
	        	session.setAttribute("error", error);
	        	usr.addUser(u);
	        	ctx.removeAttribute("listaUtenti");
	        	ctx.setAttribute("listaUtenti", usr);
	        }
        	response.sendRedirect("registrazione.jsp");
        	return;
	    }
	    
}