import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class TodoSearcher {
	private final static Logger LOGGER = Logger.getLogger(TodoSearcher.class.getName()); 
	
	public void searchDate(List<TodoItem> todos, Date toFind) {
    	try{
    		for (TodoItem tdi: todos) {
    			if (tdi.getDueDate().equals(toFind)) {
    				System.out.println(tdi.getContents());
    			}
    		}
    	}catch (Exception e){
    		e.printStackTrace();
    		LOGGER.severe("Error searching by Date");
    	}
    }
    public void searchString(List<TodoItem> todos, String toFind) {
    	try{
    		for (TodoItem tdi: todos) {
    			if (tdi.getContents().equals(toFind)){
    				System.out.println(tdi.getContents());
    			}
    		}
    	}catch (Exception e){
    		e.printStackTrace();
    		LOGGER.severe("Error searching String");
    	}
    }
    public void searchTime(List<TodoItem> todos, Date toFind){
    	try{
    		for (TodoItem tdi: todos) {
    			Date currDateTime = tdi.getDueDate();
    			boolean sameHour = currDateTime.getHours()==toFind.getHours();
    			boolean sameMinute = currDateTime.getMinutes()==toFind.getMinutes();
    			boolean sameSecond = currDateTime.getSeconds()==toFind.getSeconds();
            	if (sameHour && sameMinute && sameSecond){
            		assert(sameHour);
            		assert(sameMinute);
            		assert(sameSecond);
            		System.out.println(tdi.getContents());
            	}
            }
        }catch (Exception e){
    		e.printStackTrace();
    		LOGGER.severe("Error searching by Time");
    	}
    }
}
