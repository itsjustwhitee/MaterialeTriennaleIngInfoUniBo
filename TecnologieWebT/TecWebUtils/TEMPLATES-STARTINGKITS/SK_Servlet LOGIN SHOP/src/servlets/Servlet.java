package servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;


public class Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void init(ServletConfig conf) throws ServletException 
    {
        super.init(conf);
        
        
    }

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {

        
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
    	doPost(request, response);
    }
    
    //----------------------------------------------------------------------------------------------------------------------------
    //									METODI UTILI PER CALCOLI/OPERAZIONI (SERVER-SIDE)
    //----------------------------------------------------------------------------------------------------------------------------
    
    
    
    //----------------------------------------------------------------------------------------------------------------------------
    //									METODI PER PARSING/CREAZIONE JSON e XML
    //----------------------------------------------------------------------------------------------------------------------------
    
    private <T> void createAndSendJson(HttpServletResponse response, T item) throws IOException
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
