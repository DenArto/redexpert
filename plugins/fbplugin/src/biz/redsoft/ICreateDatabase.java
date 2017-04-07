package biz.redsoft;

import java.sql.SQLException;

/**
 * Created by Vasiliy on 05.04.2017.
 */
public interface ICreateDatabase {

    void setServer(String server);

    void setPort(int port);

    void setUser(String user);

    void setPassword(String password);

    void setDatabaseName(String databaseName);

    void setEncoding(String encoding);

    void setPageSize(int pageSize);

    void exec() throws SQLException;
}
