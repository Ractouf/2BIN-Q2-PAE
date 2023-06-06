package be.vinci.pae.jobs;

import be.vinci.pae.business.ucc.NotificationUCC;
import be.vinci.pae.utils.ApplicationBinder;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * This class represents a job that will be executed by the scheduler.
 */
public class NotificationExpiredJob implements Job {

  /**
   * This method will be executed by the scheduler.
   *
   * @param jobExecutionContext the context of the job
   */
  @Override
  public void execute(JobExecutionContext jobExecutionContext) {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinder());
    NotificationUCC notificationUCC = locator.getService(NotificationUCC.class);
    notificationUCC.sendNotificationsForExpiredObjects();
  }
}
