import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/SampleServlet")
public class SampleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Get the DataSource using JNDI
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/sampledb");

            // Get a connection from the DataSource
            try (Connection conn = ds.getConnection()) {
                // Execute a sample query
                String sql = "SELECT * FROM employees";
                try (PreparedStatement pstmt = conn.prepareStatement(sql);
                     ResultSet rs = pstmt.executeQuery()) {

                    // Display the results
                    out.println("<html><body><h2>Employee List</h2><ul>");
                    while (rs.next()) {
                        out.println("<li>ID: " + rs.getInt("id") + ", Name: " + rs.getString("name") + ", Age: " + rs.getInt("age") + "</li>");
                    }
                    out.println("</ul></body></html>");
                }
            }
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }
}
