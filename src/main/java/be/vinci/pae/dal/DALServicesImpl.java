package be.vinci.pae.dal;

import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.utils.Config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * This class creates the connection to the database and generates PrepareStatements.
 */
public class DALServicesImpl implements DALServices, DALBackend {

  private final ThreadLocal<Connection> connection;
  private final BasicDataSource dataSource;

  /**
   * This is the constructor of DALServicesImpl.
   */
  public DALServicesImpl() {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      throw new FatalException(e.getMessage(), e);
    }
    this.connection = new ThreadLocal<>();
    this.dataSource = initDataSource();
  }

  /**
   * This method initializes the data source.
   *
   * @return the data source
   */
  private BasicDataSource initDataSource() {
    BasicDataSource ds = new BasicDataSource();
    ds.setDriverClassName("org.postgresql.Driver");
    ds.setUrl(Config.getProperty("UrlDb"));
    ds.setUsername(Config.getProperty("UserDb"));
    ds.setPassword(Config.getProperty("PasswordDb"));
    return ds;
  }

  @Override
  public void initTransaction() {
    try {
      if (connection.get() != null) {
        throw new FatalException("Une transaction est déjà en cours");
      }
      Connection conn = dataSource.getConnection();
      connection.set(conn);
      conn.setAutoCommit(false);
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
  }

  @Override
  public void commitTransaction() {
    Connection conn = connection.get();
    try {
      conn.commit();
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    } finally {
      closeConnection();
    }
  }

  @Override
  public void rollBackTransaction() {
    Connection conn = connection.get();
    try {
      conn.rollback();
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    } finally {
      closeConnection();
    }
  }

  @Override
  public PreparedStatement getPrepareStatement(String sql) {
    Connection conn = connection.get();
    if (conn == null) {
      throw new FatalException("Aucune transaction en cours");
    }

    try {
      return conn.prepareStatement(sql);
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
  }

  /**
   * This method checks if a transaction is in progress.
   */
  public boolean isTransactionInProgress() {
    return connection.get() != null;
  }

  /**
   * This method closes the connection and sets the ThreadLocal to null.
   */
  private void closeConnection() {
    Connection conn = connection.get();
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        throw new FatalException(e.getMessage(), e);
      } finally {
        if (connection.get() == conn) {
          connection.set(null);
        }
      }
    }
  }
}
