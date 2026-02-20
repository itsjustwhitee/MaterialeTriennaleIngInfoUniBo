package servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.google.gson.Gson;

import beans.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;


public class Shop extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    Map<Integer, Cart> carts;
    Map<Integer, Set<HttpSession>> sessions;
    Map<Item, Integer> items; 
    

    @Override
    public void init(ServletConfig conf) throws ServletException 
    {
        super.init(conf);
        carts = new HashMap<>();
        sessions = new HashMap<>();
        items = new HashMap<>();
        Random r = new Random();
        
        items.put(new Item("BNN001", "Banana Chiquita", "Frutto del famosissimo brand Chiquita", 0.30), r.nextInt(0, 200) + 1);
        items.put(new Item("ARC001", "Arancia Sicilia", "Frutto DOP origine Palermo", 0.23), r.nextInt(0, 200) + 1);
        items.put(new Item("ARC002", "Arancia Makumba", "Frutto origine Africa", 0.19), r.nextInt(0, 200) + 1);
        items.put(new Item("FRG001", "Cassetta Fragole Asiago", "Cassetta da 0.5kg del frutto origine Asiago (VI)", 10.98), r.nextInt(0, 200) + 1);
        items.put(new Item("ACT001", "Aceto Modena DOP", "Aceto DOP di Modena IGP", 7.45), r.nextInt(0, 200) + 1);
        items.put(new Item("CCL001", "Cioccolato Latte Milka", "Barretta cioccolato Milka 150g", 2.19), r.nextInt(0, 200) + 1);
        items.put(new Item("CCL002", "Cioccolato Latte Novi", "Barretta cioccolato Novi 250g", 2.99), r.nextInt(0, 200) + 1);
        items.put(new Item("CCF001", "Cioccolato Fondente Milka", "Barretta cioccolato Milka 149g", 2.20), r.nextInt(0, 200) + 1);
        
        System.out.println("Avviata servlet Shop con items:\n" + items.toString());
        
        this.getServletContext().setAttribute("carts", carts);
        this.getServletContext().setAttribute("sessions", sessions);
        this.getServletContext().setAttribute("items", items);
    }

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
		this.doGet(request, response);
        
    }
    
    @SuppressWarnings({ "unchecked"})
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
    	String act = request.getParameter("act");
    	String what = request.getParameter("what");
    	int group = (int) request.getSession().getAttribute("group");
    	
    	synchronized(this.getServletContext())
    	{
	    	if("get".equals(act))
	    	{
	    		if("cart".equals(what))
	    		{
    				carts = (Map<Integer, Cart>) this.getServletContext().getAttribute("carts");
    				if(carts.containsKey(group))
    				{
    					this.createAndSendJson(response, carts.get(group).getItems());
    				}
    				else
    				{
    					carts.put(group, new Cart(group));
    					this.getServletContext().setAttribute("carts", carts);
    					this.createAndSendJson(response, "empty");
    				}
    			
	    		}
	    		else if("items".equals(what))
	    		{
	    			items = (Map<Item, Integer>) this.getServletContext().getAttribute("items");
	    			this.createAndSendJson(response, items);
	    		}
	    		else
	    		{
	    			this.createAndSendJson(response, "error");
	    		}
	    			
	    	}
	    	else if("add".equals(act))
	    	{
	    		carts = (Map<Integer, Cart>) this.getServletContext().getAttribute("carts");
	    		items = (Map<Item, Integer>) this.getServletContext().getAttribute("items");
	    		Item item = null;
	    		
	    		for(Entry<Item, Integer> e : items.entrySet())
	    		{
	    			if(e.getKey().getId().equals(what))
	    			{
	    				item = e.getKey();
	    				break;
	    			}
	    		}
	    		
	    		if(item != null)
	    		{
	    			carts.get(group).add(item);
	    			this.getServletContext().setAttribute("carts", carts);
	    			this.createAndSendJson(response, carts.get(group).getItems());
	    		}
	    		else
	    		{
	    			this.createAndSendJson(response, "error");
	    		}
	    	}
	    	else if("remove".equals(act))
	    	{
	    		carts = (Map<Integer, Cart>) this.getServletContext().getAttribute("carts");
	    		items = (Map<Item, Integer>) this.getServletContext().getAttribute("items");
	    		Item item = null;
	    		
	    		for(Entry<Item, Integer> e : items.entrySet())
	    		{
	    			if(e.getKey().getId().equals(what))
	    			{
	    				item = e.getKey();
	    				break;
	    			}
	    		}
	    		
	    		if(item != null)
	    		{
	    			carts.get((int) request.getSession().getAttribute("group")).remove(item);
	    			this.getServletContext().setAttribute("carts", carts);
	    			this.createAndSendJson(response, carts.get(group).getItems());
	    		}
	    		else
	    		{
	    			this.createAndSendJson(response, "error");
	    		}
	    	}
	    	else if("checkout".equals(act))
	    	{
	    		if(this.checkout(request.getSession()))
	    		{
	    			this.createAndSendJson(response, carts.get(group).getItems());
	    		}
	    		else
	    		{
	    			this.createAndSendJson(response, "error");
	    		}
	    	}
	    	else
    		{
    			this.createAndSendJson(response, "error");
    		}
    	}
    }
    
    //----------------------------------------------------------------------------------------------------------------------------
    //									METODI UTILI PER CALCOLI/OPERAZIONI (SERVER-SIDE)
    //----------------------------------------------------------------------------------------------------------------------------
    
    @SuppressWarnings("unchecked")
	private boolean checkout(HttpSession session)
    {
    	synchronized(this.getServletContext())
    	{
    		carts = (Map<Integer, Cart>) this.getServletContext().getAttribute("carts");
    		items = (Map<Item, Integer>) this.getServletContext().getAttribute("items");
    		
    		//check degli item disponibili
    		for(Entry<Item, Integer> item : carts.get((int) session.getAttribute("group")).getItems().entrySet())
    		{
    			if(item.getValue() > items.get(item.getKey()))
    				return false;
    		}
    		
    		//update degli item disponibili
    		for(Entry<Item, Integer> item : carts.get((int) session.getAttribute("group")).getItems().entrySet())
    		{
    			items.replace(item.getKey(), items.get(item.getKey()) - item.getValue());
    			if(items.get(item.getKey()) < 1)
    				items.remove(item.getKey());
    		}
    		
    		this.carts.put((int) session.getAttribute("group"), new Cart((int) session.getAttribute("group"))); //carrello vuoto
    		
    		//update scope application
    		this.getServletContext().setAttribute("carts", carts);
            this.getServletContext().setAttribute("items", items);
            
    		return true;
    	}
    }
    
    //----------------------------------------------------------------------------------------------------------------------------
    //									METODI PER PARSING/CREAZIONE JSON e XML
    //----------------------------------------------------------------------------------------------------------------------------
    
    private void createAndSendJson(HttpServletResponse response, Map<Item,Integer> item) throws IOException
    {
    	response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("{\"data\":[");
		
		for(Entry<Item,Integer> i : item.entrySet())
		{
			sb.append("{\"id\":\"");
			sb.append(i.getKey().getId());
			sb.append("\",\"name\":\"");
			sb.append(i.getKey().getName());
			sb.append("\",\"description\":\"");
			sb.append(i.getKey().getDescription());
			sb.append("\",\"price\":\"");
			sb.append(i.getKey().getPrice());
			sb.append("\",\"value\":\"");
			sb.append(i.getValue());
			sb.append("\"},");
		}
		
		if(sb.length() > 1)
			sb.replace(sb.length() -1, sb.length(), "");
		sb.append("]}");
		
		String json = sb.toString();

		System.out.println("Pronto json: " + json);
		
		try {
			response.getWriter().write(json);
			response.getWriter().flush();
		} catch (IOException e) {
			throw new IOException("Errore nell'invio del json", e);
		}
    }
    
    private void createAndSendJson(HttpServletResponse response, String item) throws IOException
    {
    	response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		Gson gson = new Gson();
		String json = gson.toJson(item, item.getClass());

		System.out.println("Pronto json: " + json);
		
		try {
			response.getWriter().write(json);
			response.getWriter().flush();
		} catch (IOException e) {
			throw new IOException("Errore nell'invio del json", e);
		}
    }
    
    @SuppressWarnings("unchecked")
	private <T> T parseJson(HttpServletRequest request, Class<T> clazz) throws IOException
    {
    	T response = null;
    	
    	StringBuilder jsonString = new StringBuilder();
        String line;
        try (var reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
        } catch (IOException e) {
        	throw new IOException("Errore nel parsing del json", e);
		}
        Gson gson = new Gson(); 
        
        response = (T) gson.fromJson(jsonString.toString(), clazz);
        
    	return response;
    }
    
    private <T> void sendXml(HttpServletResponse response, T item) throws IOException {
        try {
            // Crea il documento XML
            Document document = DocumentHelper.createDocument();
            
            Element root = document.addElement("output"); //elemento in cui aggiungere i risultati
            
            //Parti ti item da aggiungere
            //root.addElement("name").addText(item.getId());

            // Configura l'output
            response.setContentType("application/xml");
            response.setCharacterEncoding("UTF-8");

            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(response.getWriter(), format);

            // Scrive il documento XML nella risposta
            writer.write(document);
        } catch (Exception e) {
            throw new IOException("Errore nella generazione XML", e);
        }
    }
    
    public static <T> T parseXml(HttpServletRequest request, Class<T> clazz) throws DocumentException, IOException 
    {
        T response = null;
        
        try {
            // Creare una istanza di SAXReader
            SAXReader reader = new SAXReader();
            // Leggere e parsare il documento XML
            Document document = reader.read(request.getInputStream());

            // Conversione del documento XML in un oggetto specifico (se richiesto)
            if (clazz.equals(Document.class)) {
                response = (T) document;
            } else {
                throw new UnsupportedOperationException("La conversione in " + clazz.getSimpleName() + " non è supportata.");
            }

        } catch (DocumentException e) {
            throw new IOException("Errore nel parsing del XML", e);
        }
        
        return response;
    }
}
