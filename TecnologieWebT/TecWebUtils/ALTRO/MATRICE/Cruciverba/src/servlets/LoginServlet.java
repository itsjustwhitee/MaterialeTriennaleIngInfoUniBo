package servlets;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.User;
import beans.UserGroup;

//inizializzata dentro web.xml tramite <load-on-startup>1</load-on-startup> per far si che l'UserGroup venisse inizializzato una sola volta

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserGroup users;
	
	public LoginServlet() {
		super();
	}
	
    @Override
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);
    	users = new UserGroup();
    	this.getServletContext().setAttribute("listaUtenti", users);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
	        String username = request.getParameter("username");
	        String password = request.getParameter("password");
	        HttpSession session = request.getSession();
	        this.users = (UserGroup) this.getServletContext().getAttribute("listaUtenti");
	        User user = this.users.getUser(username);
	        int error= 0;
	        String action = request.getParameter("action");
	        if(action.equals("login")) {
		       this.login(request, response);
		       return;
		 } else if (action.equals("logout")) {
			 this.logout(request, response);
			 return;
		 } 
    }
    
    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	  	String username = request.getParameter("username");
	        String password = request.getParameter("password");
	        HttpSession session = request.getSession();
	        User user = this.users.getUser(username);
	        int error = 0;
    	if (user != null && user.getPassword().equals(password)) {//LOGIN CORRETTO
            session.setAttribute("user", user);
            error = -1; 
            session.setAttribute("login", true); //per evitare che qualcuno acceda tramite URL
            user.login(request.getSession());
            session.setAttribute("user", user); //Passo i dati dell'utente alla sua pagina
            this.getServletContext().setAttribute("listaUtenti", users);
            response.sendRedirect("user.jsp");
        return ;
        } else {
        	if(user == null) 
        		error = 2;  // Utente non registrato
        	else
        		error = 3; //Password errata
        	session.setAttribute("error", error);
    		response.sendRedirect("loginPage.jsp"); //Lo faccio fuori dai due if else per risparmiare codice
    		return;
      }
    }
    
    private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	HttpSession session = request.getSession();
    	try {
			 User u = (User) session.getAttribute("user");
			 for (User checker : this.users.getLista()) {
				 if(checker.getUsername().equals(u.getUsername()))
					 u.logout();
				 break;
			 }
			 this.getServletContext().removeAttribute("listaUtenti");
			 this.getServletContext().setAttribute("listaUtenti", this.users);
		 }catch(Exception e) {
			 session.invalidate();
		 }
    	//Rimozione cookie altrimenti una volta effettuato il logout non posso fare il login con lo stesso account
    	Cookie jsessionidCookie = new Cookie("JSESSIONID", null); 
    	jsessionidCookie.setMaxAge(0);  // Elimina il cookie
    	jsessionidCookie.setPath(request.getContextPath());  // Assicurati che il path sia corretto
    	response.addCookie(jsessionidCookie);   	
		request.getSession().setAttribute("error", 4);
		response.sendRedirect("loginPage.jsp");
    }
    
}