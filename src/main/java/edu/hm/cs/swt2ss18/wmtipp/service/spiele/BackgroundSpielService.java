package edu.hm.cs.swt2ss18.wmtipp.service.spiele;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.ManagedBean;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * HintergrundKlasse zu {@link SpielService}.
 * </p>
 * <p>
 * Ein Spring ManagedBean, welcher Methoden aus {@link SpielService} aufruft.
 * </p>
 * 
 * @author Jannis Ditterich
 *
 */
@ManagedBean
public class BackgroundSpielService implements ServletContextListener {
	private ScheduledExecutorService scheduler;
	@Autowired
	private SpielService spielService;

	/**
	 *  Wird gestartet wenn, der Server gestartet wird.
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		// https://stackoverflow.com/questions/15867069/easy-way-to-call-method-in-new-thread
		// Ruft jede Minute die Methode closesGameIf.... auf in einem scheduler
				scheduler.scheduleAtFixedRate((new Thread(() -> spielService.closesGamesIfStartAfterNow())), 0, 1,
				TimeUnit.MINUTES);

	}

	/* 
	 * Wenn der Server gestoppt wird, soll auch der Scheduler deaktiviert werden.
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		scheduler.shutdownNow();

	}

}
