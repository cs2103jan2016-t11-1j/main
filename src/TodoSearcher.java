import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class TodoSearcher {
	private final static Logger LOGGER = Logger.getLogger(TodoSearcher.class.getName()); 
	
	public void searchDate(List<TodoItem> todos, Date toFind) {
    	try{
    		int i = 1;
    		for (TodoItem tdi: todos) {
    			if (tdi.getDueDate().equals(toFind)) {
    				System.out.println("Date " + toFind + "found in line " + i);
    			}
    			i++;
    		}
    	}catch (Exception e){
    		e.printStackTrace();
    		LOGGER.severe("Error searching by Date");
    	}
    }
	
    public void searchString(List<TodoItem> todos, String toFind) {
    	try{
    		int i = 1;
    		for (TodoItem tdi: todos) {
    			if (tdi.getContents().contains(toFind)){ //if it tdi contents has toFind
    				System.out.println(toFind + " found in line " + i + ". " + tdi.getContents());
    			}
    			i++;
    		}
    	}catch (Exception e){
    		e.printStackTrace();
    		LOGGER.severe("Error searching String");
    	}
    }
    
    public void searchTime(List<TodoItem> todos, Date toFind){
    	try{
    		int i = 1;
    		for (TodoItem tdi: todos) {
    			Date currDateTime = tdi.getDueDate();
    			boolean sameHour = currDateTime.getHours()==toFind.getHours();
    			boolean sameMinute = currDateTime.getMinutes()==toFind.getMinutes();
    			boolean sameSecond = currDateTime.getSeconds()==toFind.getSeconds();
            	if (sameHour && sameMinute && sameSecond){ // same time
            		assert(sameHour);
            		assert(sameMinute);
            		assert(sameSecond);
            		System.out.println(currDateTime.getHours()+ ":" + currDateTime.getMinutes() + ":" + currDateTime.getSeconds() + " found in line " + i + ". " + tdi.getContents());
            	}
            	i++;
            }
        }catch (Exception e){
    		e.printStackTrace();
    		LOGGER.severe("Error searching by Time");
    	}
    }
    
    public void searchHour(List<TodoItem> todos, Date toFind){
    	try{
    		int i = 1;
    		for (TodoItem tdi: todos) {
    			Date currDateTime = tdi.getDueDate();
    			boolean sameHour = currDateTime.getHours()==toFind.getHours();
            	if (sameHour){// same hour
            		assert(sameHour);
            		System.out.println(currDateTime.getHours()+ ":" + currDateTime.getMinutes() + ":" + currDateTime.getSeconds() + " found in line " + i + ". " + tdi.getContents());
            	}
            	i++;
            }
        }catch (Exception e){
    		e.printStackTrace();
    		LOGGER.severe("Error searching by Time");
    	}
    }
    
    public void searchAfterDate(List<TodoItem> todos, Date toFind){
    	try{
    		int i = 1;
    		for (TodoItem tdi: todos) {
    			Date currDateTime = tdi.getDueDate();
    			if (currDateTime.before(toFind)){ // after toFind time
            		System.out.println(i + ". " + tdi.getContents() + " is due after " + toFind);
            	}
            	i++;
            }
        }catch (Exception e){
    		e.printStackTrace();
    		LOGGER.severe("Error searching by Time");
    	}
    }
    
    public void searchAfterTime(List<TodoItem> todos, Date toFind){
    	try{
    		int i = 1;
    		for (TodoItem tdi: todos) {
    			Date currDateTime = tdi.getDueDate();
    			boolean sameHour = currDateTime.getHours()==toFind.getHours();
    			boolean sameMinute = currDateTime.getMinutes()==toFind.getMinutes();
    			boolean sameSecond = currDateTime.getSeconds()==toFind.getSeconds();
            	if (sameHour && sameMinute && sameSecond){ // same time
            		assert(sameHour);
            		assert(sameMinute);
            		assert(sameSecond);
            		System.out.println(currDateTime.getHours()+ ":" + currDateTime.getMinutes() + ":" + currDateTime.getSeconds() + " found in line " + i + ". " + tdi.getContents());
            	}
            	i++;
            }
        }catch (Exception e){
    		e.printStackTrace();
    		LOGGER.severe("Error searching by Time");
    	}
    }
}
