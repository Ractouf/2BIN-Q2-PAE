package be.vinci.pae.main;

import be.vinci.pae.exceptions.WebExceptionMapper;
import be.vinci.pae.jobs.NotificationExpiredJob;
import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.Config;
import java.io.IOException;
import java.net.URI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * This class starts the web server.
 */
public class Main {

  private static final Logger LOGGER = LogManager.getLogger(Main.class);

  /**
   * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
   *
   * @return Grizzly HTTP server.
   */
  public static HttpServer startServer() {
    // create a resource config that scans for JAX-RS resources and providers
    // in be.vinci.pae.ihm package
    final ResourceConfig rc = new ResourceConfig().packages("be.vinci.pae.ihm")
        .register(ApplicationBinder.class)
        .register(MultiPartFeature.class)
        .register(WebExceptionMapper.class);

    try {
      Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

      JobDetail job = JobBuilder.newJob(NotificationExpiredJob.class)
          .withIdentity("objectsTrigger", "group1")
          .build();

      Trigger trigger = TriggerBuilder.newTrigger()
          .withIdentity("objectsTrigger", "group1")
          .startNow()
          .withSchedule(SimpleScheduleBuilder.simpleSchedule()
              .withIntervalInHours(24)
              .repeatForever())
          .build();

      scheduler.scheduleJob(job, trigger);
      scheduler.start();
    } catch (SchedulerException se) {
      se.printStackTrace();
    }

    // create and start a new instance of grizzly http server
    // exposing the Jersey application at BASE_URI
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(Config.getProperty("BaseUri")), rc);
  }

  /**
   * Main method.
   *
   * @param args the argument given with the execution of the code
   * @throws IOException throws IOException
   */
  public static void main(String[] args) throws IOException {
    Config.load("dev.properties");

    final HttpServer server = startServer();
    LOGGER.info("Jersey app started with endpoints available at {}",
        Config.getProperty("BaseUri"));
    System.in.read();
    server.stop();
  }
}
