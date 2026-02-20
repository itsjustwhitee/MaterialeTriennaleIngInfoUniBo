package servlets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import beans.Risultato;

public class CruciverbaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private char[][] cruciverba;
	private List<Integer> righeComplete;
	private List<Integer> colonneComplete;
	private List<Integer> righeCorrette;
	private List<Integer> colonneCorrette;
	private char[][] paroleCorrette;

	public CruciverbaServlet() {
		super();
	}
	 @Override
	    public void init(ServletConfig config) throws ServletException {
			super.init(config);
			this.cruciverba = new char[10][10];
			this.paroleCorrette = new char[10][10];
			for (int i = 0; i < cruciverba.length; i++) {
					for (int j = 0; j < cruciverba[i].length; j++) {
						cruciverba[i][j] = ' '; // Inizializza con uno spazio vuoto
					}
			}
			this.colonneComplete = new ArrayList<Integer>();
			this.righeComplete = new ArrayList<Integer>();
			this.colonneCorrette = new ArrayList<Integer>();
			this.righeCorrette = new ArrayList<Integer>();

			this.getServletContext().setAttribute("cruciverba", cruciverba);
			this.getServletContext().setAttribute("righeComplete", righeComplete);
			this.getServletContext().setAttribute("colonneComplete", colonneComplete);
			this.getServletContext().setAttribute("righeCorrette", righeCorrette);
			this.getServletContext().setAttribute("colonneCorrette", colonneCorrette);
			File file = new File(getServletContext().getRealPath("./cruciverba.txt"));
			Scanner s;
			int dim = 0;
			try {
				s = new Scanner(file);
				while(s.hasNext()){
					paroleCorrette[dim++] = s.nextLine().toCharArray();
				}
				s.close();
			} catch (FileNotFoundException e) {
				System.out.println("File insesistente");
				e.printStackTrace();
			}
			catch (Exception e) {
				System.out.println("Errore lettura file");
				e.printStackTrace();
			}
			System.out.println("Parole corrette");
			for(int i=0;i<dim;i++){
				System.out.println(paroleCorrette[i]);
			}
			
	    }

	 @Override
	 public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 	response.setContentType("application/json");
		 	PrintWriter out = response.getWriter();
		 	ArrayList<Integer> fullRows = (ArrayList<Integer>)this.getServletContext().getAttribute("righeComplete"); 
		 	ArrayList<Integer> fullCols = (ArrayList<Integer>)this.getServletContext().getAttribute("colonneComplete"); 
			ArrayList<Integer> correctRows = (ArrayList<Integer>)this.getServletContext().getAttribute("righeCorrette"); 
		 	ArrayList<Integer> correctCols = (ArrayList<Integer>)this.getServletContext().getAttribute("colonneCorrette"); 
		 	char[][] matrice = (char[][]) this.getServletContext().getAttribute("cruciverba");
		 	 JsonObject jsonResponse = new JsonObject();
		     jsonResponse.add("matrice", new Gson().toJsonTree(matrice));
		     jsonResponse.add("righeComplete", new Gson().toJsonTree(fullRows));
		     jsonResponse.add("colonneComplete", new Gson().toJsonTree(fullCols));
			 jsonResponse.add("righeCorrette", new Gson().toJsonTree(correctRows));
		     jsonResponse.add("colonneCorrette", new Gson().toJsonTree(correctCols));
		 	out.print(jsonResponse.toString());
		 	out.flush();
		}
	 
	    @Override
	    protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	    		int riga = Integer.parseInt(request.getParameter("riga"));
	    		int colonna = Integer.parseInt(request.getParameter("colonna"));
	    		Integer rigaCompleta = null;
	    		Integer colonnaCompleta =null;
	    		String carattere = request.getParameter("carattere");
	    		if(carattere != null && Character.isUpperCase(carattere.charAt(0)) && carattere.length() == 1)
	    		{
	    			this.cruciverba[riga][colonna] = carattere.charAt(0);
	    			if(this.controllaRiga(riga)) {
						System.out.println("Completata riga " + (riga+1));
	    				rigaCompleta = riga;
	    				ArrayList<Integer> fullRows = (ArrayList<Integer>)this.getServletContext().getAttribute("righeComplete"); 
	    				fullRows.add(riga);
						this.getServletContext().setAttribute("righeComplete", fullRows);

						if(isRowCorrect(riga)){
							ArrayList<Integer> correctRows = (ArrayList<Integer>)this.getServletContext().getAttribute("righeCorrette");
							correctRows.add(riga);
							this.getServletContext().setAttribute("righeCorrette", correctRows);
							System.out.println("La riga " + (riga + 1) + " è corretta");
						}
						else System.out.println("La riga " + (riga + 1) + " non è corretta");

						//DEBUG
						ArrayList<Integer> correctRows = (ArrayList<Integer>)this.getServletContext().getAttribute("righeCorrette");
						System.out.print("Righe corrette: ");
						for(Integer row : correctRows){
							System.out.print(" " + row + " ");
						}
						System.out.print("\n");
						//
						
	    				
	    			}
	    			if(this.controllaColonna(colonna)) {
						System.out.println("Completata colonna " + colonna);
	    				colonnaCompleta = colonna;
	    				ArrayList<Integer> fullCols = (ArrayList<Integer>)this.getServletContext().getAttribute("colonneComplete"); 
	    				fullCols.add(colonna);
	    				this.getServletContext().setAttribute("colonneComplete", fullCols);

						if(isColCorrect(colonna)){
							ArrayList<Integer> correctCols = (ArrayList<Integer>)this.getServletContext().getAttribute("colonneCorrette");
							correctCols.add(colonna);
							this.getServletContext().setAttribute("colonneCorrette", correctCols);
							System.out.println("La colonna " + (colonna+1) + " è corretta");
						}
						else System.out.println("La colonna " + (colonna+1) + " non è corretta");

						//DEBUG
						ArrayList<Integer> correctCols = (ArrayList<Integer>)this.getServletContext().getAttribute("colonneCorrette");
						System.out.print("Colonne corrette: ");
						for(Integer col : correctCols){
							System.out.print(" " + col + " ");
						}
						System.out.print("\n");
						//
	    			}
	    			Risultato result = new Risultato(rigaCompleta, colonnaCompleta);
	    			Gson g = new Gson();
	    			String jsonResponse = g.toJson(result);
	    			 response.setContentType("application/json");
	    		     response.setCharacterEncoding("UTF-8");
	    		     PrintWriter out = response.getWriter();
	    		     out.print(jsonResponse);
	    		     out.flush();
	    			this.getServletContext().removeAttribute("cruciverba");
	    			this.getServletContext().setAttribute("cruciverba", cruciverba);
	    		}
	    		return;
	    }
	    
	    
	    private boolean controllaRiga(int i) {
			//ritorna true se non è stata completata o se è stata completata ora
	    	boolean check = true;
	    	for(Integer j : (ArrayList<Integer>)this.getServletContext().getAttribute("righeComplete")) //controlla se e' gia' stata completata concorrentemente da qualcuno la riga
	    	{
	    		if(j.intValue() == i) {
	    			check = false;
	    			return check;
	    		}
	    	}
	    	for(char c: this.cruciverba[i]) //questo invece per controllare se e' stata completata ora
	    		if(c == ' ' ) {
	    			check  = false;
	    			break;
	    		}
	    	return check;
	    }
	    
	    private boolean controllaColonna(int j) {
	    	boolean check = true;
	    	for(Integer i : (ArrayList<Integer>)this.getServletContext().getAttribute("colonneComplete")) //stesso discorso delle righe
	    	{
	    		if(i.intValue() == j) {
	    			check = false;
	    			return check;
	    		}
	    	}
	    	for(int i = 0; i < 10; i++) //questo invece per controllare se e' stata completata ora
	    		if(this.cruciverba[i][j] == ' ') {
	    			check = false;
	    			break;
	    		}
	    	return check;
	    }

		private boolean isRowCorrect(int row) {
			boolean res = true;
			for(int j=0;j<10;j++){
				System.out.println("Confronto " + cruciverba[row][j] + " e " + paroleCorrette[row][j]);
				if(cruciverba[row][j] != paroleCorrette[row][j]){
					res = false;
					break;
				}
					
			}

			return res;
		}

		private boolean isColCorrect(int col) {
			boolean res = true;
			for(int i=0;i<10;i++){
				System.out.println("Confronto " + cruciverba[i][col] + " e " + paroleCorrette[i][col]);
				if(cruciverba[i][col] != paroleCorrette[i][col]){
					res = false;
					break;
				}
			}

			return res;
		}
}
