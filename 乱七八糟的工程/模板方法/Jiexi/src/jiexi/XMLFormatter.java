package jiexi;

public class XMLFormatter extends Formatter {

	@Override  
    protected String formating(Book book) {  
        String result = "";  
        result += "<book_name>" + book.getBookName() + "</book_name>\n";  
        result += "<pages>" + book.getPages() + "</pages>\n";  
        result += "<price>" + book.getPrice() + "</price>\n";  
        result += "<author>" + book.getAuthor() + "</author>\n";  
        result += "<isbn>" + book.getIsbn() + "</isbn>\n";  
        return result;  
    }  

}
