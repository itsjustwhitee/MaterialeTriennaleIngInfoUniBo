package servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private Map<String, User> users;

    @Override
    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
        
        //dati con scope application
        users = new HashMap<>();
        
        users.put("admin", new User("admin", "admin", -1));
		users.get("admin").setAdmin(true);
        users.put("matteo", new User("matteo", "matteo", 20));
		users.get("matteo").setAdmin(true);
		users.put("sharon", new User("sharon", "sharon", 21));
		users.get("sharon").setAdmin(true);
		users.put("paolo", new User("paolo", "paolo", -1));
		users.get("paolo").setAdmin(true);
		users.put("andrea", new User("andrea", "andrea", 2));
		users.get("andrea").setAdmin(true);
		
		for(User u : users.values())
			System.out.println(u);
		
		this.getServletContext().setAttribute("users", users); //inizializzo il pool di users con scope application
    }

    @SuppressWarnings("unchecked")
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String action = request.getParameter("act");
        
        System.out.println("Richiesta arrivata alla servlet con username=" + username + " password=" + password + " action=" + action);
        
        request.setAttribute("loginError", "");
        request.setAttribute("registrationError", "");
        
        if("login".equals(action)) //login (utente giá registrato)
        {
        	synchronized (this.getServletContext()) //evito inconsistenze
        	{
        		users = (Map<String, User>) this.getServletContext().getAttribute("users");

        		//validazione utente
        		if(users.containsKey(username) && users.get(username).getPassword().equals(password) && users.get(username).isAdmin())
        		{
        			login(username, request.getSession());
        			System.out.println("Login con successo, reindirizzamento alla home.jsp");
        			users = (Map<String, User>) this.getServletContext().getAttribute("users");
        			System.out.println("User corrente: " + users.get(username).toString());
        			response.sendRedirect("home.jsp");
        		}
        		//credenziali non valide
	        	else
	        	{
	        		System.out.println("Login senza successo, reindirizzamento alla login.jsp");
	        		request.setAttribute("loginError", "Incorrect username or password." + 
	        				(users.containsKey(username) ? "" : "Are you a new user? Fill the form on the right!"));
	        		getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
	        		return;
	        	}
        	}
        }
        else if("guest".equals(action)) //accesso come guest
        {

        	login(action, request.getSession());
        	System.out.println("Accesso come guests, reindirizzamento alla home.jsp");
    		response.sendRedirect("home.jsp");
        }
        else if("logout".equals(action) && !request.getSession().getAttribute("username").equals("guest"))
        {
        	synchronized (this.getServletContext())
        	{
        		logout(request.getSession());
        	}
        	System.out.println("Logout con successo, reindirizzamento alla login.jsp");
        	//response.sendRedirect("login.jsp");
        }
        else //errore
        {
        	System.out.println("Pagina richiesta non esistente, reindirizzamento a notfound.html");
        	response.sendRedirect("/error/notfound.html");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
    	doPost(request, response);
    }
    
    @SuppressWarnings("unchecked")
	private void login(String username, HttpSession session)
    {
    	if(!"guest".equals(username))
    	{
    		users = (Map<String, User>) this.getServletContext().getAttribute("users");
        	users.get(username).setExpired(false);
    		users.get(username).setSessionId(session.getId());
    		this.getServletContext().setAttribute("users", users);
    	}
		session.setAttribute("username", username);
    }
    
    @SuppressWarnings("unchecked")
	private void logout(HttpSession session)
    {	
    	users = (Map<String, User>) this.getServletContext().getAttribute("users");
    	String username = (String) session.getAttribute("username");
    	if(!username.isBlank() && username != null && !users.containsKey(username))
    	{
    		users.get(username).setExpired(true);
    		users.get(username).setSessionId("");
    	}
    	this.getServletContext().setAttribute("users", users);
    	session.invalidate();
    }
}
