package com.luckyend.test.infinispan.wildfly10;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/test")
public class TestDataServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@EJB
	private TestService service;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");

		try( PrintWriter writer = resp.getWriter() ) {
			System.out.println("configuration:");
			writer.println("configuration:");
			writer.println(service.testConfig());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("\n\ndata:");
			writer.println("\n\ndata:");
			System.out.println("inserting item");
			writer.println("inserting item");
			writer.println(service.put());
			
			System.out.println("\n\nsleeping 0.5 seconds\n");
			writer.println("\n\nsleeping 0.5 seconds\n");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("reading item [expecting(x found, y found)]");
			writer.println("reading item [expecting(x found, y found)]");
			writer.println(service.read());
			
			System.out.println("\n\nsleeping 2 seconds\n");
			writer.println("\n\nsleeping 2 seconds\n");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("reading item [expecting(x found, y not found)]");
			writer.println("reading item [expecting(x found, y found)]");
			writer.println(service.read());
			
			System.out.println("\n\nsleeping 10 seconds\n");
			writer.println("\n\nsleeping 10 seconds\n");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("reading item [expecting(x not found, y not found)]");
			writer.println("reading item [expecting(x found, y found)]");
			writer.println(service.read());
			
			System.out.println("completed.");
			writer.println("completed.");
			writer.flush();
		}
		
	}
}
