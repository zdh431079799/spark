/***************************************************************
 *      _______  ______      ___       ______       __  ___    *
 *     /       ||   _  \    /   \     |   _  \     |  |/  /    *
 *    |   (----`|  |_)  |  /  ^  \    |  |_)  |    |  '  /     *
 *     \   \    |   ___/  /  /_\  \   |      /     |    <      *
 * .----)   |   |  |     /  _____  \  |  |\  \----.|  .  \     *
 * |_______/    | _|    /__/     \__\ | _| `._____||__|\__\    *  
 *                                                             *
 **************************************************************/
package spark.webserver;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.log.Log;

/**
 * 
 *
 * @author Per Wendel
 */
class JettyHandler extends AbstractHandler {
    
    private List<Filter> filters;
    
    public JettyHandler(Filter... filters) {
        this.filters = Arrays.asList(filters);
    }

    public boolean handle(String target, HttpServletRequest request, HttpServletResponse response,
                    int arg3) throws IOException, ServletException {
        Log.info("jettyhandler, handle();");
        FilterChain chain = new FilterChainImpl(filters.iterator());
        chain.doFilter(request, response);
        return true;
    }
    
    /**
     * Simple FilterChain implementation
     */
    private static class FilterChainImpl implements FilterChain {
        private Iterator<Filter> filters;
        public FilterChainImpl(Iterator<Filter> filters) {
            this.filters = filters;
        }
        
        @Override
        public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
            if (filters.hasNext()) {
                filters.next().doFilter(request, response, this);
            }
        }
        
    }

}