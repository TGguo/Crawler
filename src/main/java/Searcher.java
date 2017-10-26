import javax.naming.directory.SearchResult;
import java.util.List;

public interface Searcher {
    public List<Webpage> search(String keyword);
    public SearchResult search(String keyword, int page);
}
