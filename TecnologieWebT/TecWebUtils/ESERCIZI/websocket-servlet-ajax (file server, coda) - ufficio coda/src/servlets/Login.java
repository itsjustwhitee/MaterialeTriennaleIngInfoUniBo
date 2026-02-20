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
        
        users.put("matteo", new User("matteo", "matteo", 20));
		users.put("sharon", new User("sharon", "sharon", 21));
		users.put("paolo", new User("paolo", "paolo", -1));
		users.put("andrea", new User("andrea", "andrea", 2));
		users.put("pippo", new User("pippo", "pippo", 12));
		users.put("pluto", new User("pluto", "pluto", 12));
		users.put("paperino", new User("paperino", "paperino", 12));
		users.put("giuseppe", new User("giuseppe", "giuseppe", -1));
		users.put("power", new User("power", "power", -1));
		users.put("federico", new User("federico", "federico", 2));
		users.put("paola", new User("paola", "paola", 2));
		users.put("enrico", new User("enrico", "enrico", 2));
		users.put("ambra", new User("ambra", "ambra", 2));
		users.put("professore", new User("professore", "vip", 2));
		
		for(User u : users.values())
			System.out.println(u.getUsername() + " [" + u.getGroup() + "]" + (u.isAdmin() ? " é admin" : ""));
		
		this.getServletContext().setAttribute("users", users); //inizializzo il pool di users con scope application

		this.getServletContext().setAttribute("sessions", new HashMap<String, HttpSession>()); //inizializzo il pool per le sessioni degli utenti
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
        	request.getSession().setAttribute("priority", false);
        	request.getSession().setAttribute("username", "");
        	synchronized (this.getServletContext()) //evito inconsistenze
        	{
        		users = (Map<String, User>) this.getServletContext().getAttribute("users");

        		//validazione utente
        		if(users.containsKey(username) && users.get(username).getPassword().equals(password))
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
        else if("registration".equals(action)) //registrazione e login (utente non registrato)
        {
        	String g = request.getParameter("group");
        	int group;
        	
			try {
				group = (g == null || g.isBlank() ? -1 : Integer.parseInt(g)); //valore riservato -1 = no gruppo
			} catch (NumberFormatException e) {
				e.printStackTrace();
				request.setAttribute("registrationError", "Group must be a positive (integer) number");
				getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
				return;
			}
        	
        	synchronized (this.getServletContext()) //evito inconsistenze
        	{
        		users = (Map<String, User>) this.getServletContext().getAttribute("users");
        		
        		if(users.containsKey(username)) //caso username giá registrato
        		{
        			System.out.println("Impossibile registrare l'utente, username giá in uso");
        			request.setAttribute("registrationError", "Username already taken");
        			getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
        			return;
        		}
        		
        		User user = new User(username, password, group);
        		users.put(username, user);
        		this.getServletContext().setAttribute("users", users);
        		login(username, request.getSession());
        		System.out.println("Registrazione e login con successo, reindirizzamento alla home.jsp");
    			response.sendRedirect("home.jsp");
        	}
        }
        else if("logout".equals(action))
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
        
        System.out.println("Verifica attributi di sessione: username=" + request.getSession().getAttribute("username")
        		+ " priority=" + request.getSession().getAttribute("priority"));
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
    	users = (Map<String, User>) this.getServletContext().getAttribute("users");
    	users.get(username).setExpired(false);
		users.get(username).setSessionId(session.getId());
		this.getServletContext().setAttribute("users", users);
		Map<String, HttpSession> sessions = (HashMap<String, HttpSession>) this.getServletContext().getAttribute("sessions");
		sessions.put(session.getId(), session);
		this.getServletContext().setAttribute("sessions", sessions);
		session.setAttribute("username", username);
		session.setAttribute("group", users.get(username).getGroup());
		
		if(username.equals("professore") && users.get(username).getPassword().equals("vip"))
			session.setAttribute("priority", true);
		else
			session.setAttribute("priority", false);
    }
    
    @SuppressWarnings("unchecked")
	private void logout(HttpSession session)
    {	
		Map<String, HttpSession> sessions = (HashMap<String, HttpSession>) this.getServletContext().getAttribute("sessions");
    	sessions.remove(session.getId());
    	this.getServletContext().setAttribute("sessions", sessions);
    	users = (Map<String, User>) this.getServletContext().getAttribute("users");
    	String username = "";
    	for(User u : users.values())
    		if(u.getSessionId().equals(session.getId()))
    		{
    			username = u.getUsername();
    			break;
    		}
    	if(!username.isBlank())
    	{
    		users.get(username).setExpired(true);
    		users.get(username).setSessionId("");
    	}
    	this.getServletContext().setAttribute("users", users);
    	session.invalidate();
    }
}
