package mvc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CommandReviewBbs {
	public String action(HttpServletRequest request, 
			 HttpServletResponse response) throws Exception;
}
