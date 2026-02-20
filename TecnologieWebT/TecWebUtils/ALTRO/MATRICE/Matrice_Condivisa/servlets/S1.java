package servlets;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import beans.Matrice;

public class S1 extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private Matrice m;
	private Gson gson;
	
	public void init(ServletConfig config) throws ServletException{
		super.init(config);
		int righe=5,colonne=5;
		String [][] temp = new String[righe][colonne];
		for(int i = 0 ; i < righe ; i++) {
			for(int j = 0 ; j < colonne ; j++) {
				temp[i][j] = "";
			}
		}
		m = new Matrice (temp);
		gson = new Gson();
	}
	
	
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
		response.setContentType("application/json");
		response.setStatus(200);
		response.getWriter().println(gson.toJson(m));
	}
	
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
		response.setContentType("application/json");
		response.setStatus(200);
		if(request.getParameter("i") != null && request.getParameter("j") != null  && request.getParameter("valore") != null) {
			try {
				int i = Integer.parseInt(request.getParameter("i").trim());
				int j = Integer.parseInt(request.getParameter("j").trim());
				String [][] temp = m.getMatrice();
				String valore = request.getParameter("valore").trim();
				temp [i][j] = valore;
				m.setMatrice(temp);
			}
			catch(Exception e) {
				System.out.println(e.getLocalizedMessage());
				response.getWriter().println(gson.toJson(false));
				return;
			}
			response.getWriter().println(gson.toJson(true));
			return;
		}
		System.out.println("Richiesta Post non gradita");
		response.getWriter().println(gson.toJson(false));
		return;
	}
}
