package org.executequery.databaseobjects.impl;

import org.executequery.databasemediators.spi.DefaultStatementExecutor;
import org.executequery.databaseobjects.DatabaseMetaTag;
import org.executequery.databaseobjects.NamedObject;
import org.underworldlabs.jdbc.DataSourceException;
import org.underworldlabs.util.SQLUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DefaultDatabaseUser extends AbstractDatabaseObject {

    private String plugin;
    private boolean active;
    private boolean admin;
    private Map<String, String> tags;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private String comment;

    public DefaultDatabaseUser(DatabaseMetaTag metaTagParent, String name) {
        super(metaTagParent, name);
        active = true;
        tags = new HashMap<>();
        admin = false;
        plugin = "";
    }

    @Override
    protected String queryForInfo() {
        String query = "select * from SEC$USERS \n" +
                "where SEC$USER_NAME = '" + getName() + "'";
        return query;
    }

    @Override
    protected void setInfoFromResultSet(ResultSet result) throws SQLException {
        if (result.next()) {
            try {
                setFirstName(result.getString(2).trim());
            } catch (NullPointerException e) {
                setFirstName("");
            }
            try {
                setMiddleName(result.getString(3).trim());
            } catch (NullPointerException e) {
                setMiddleName("");
            }
            try {
                setLastName(result.getString(4).trim());
            } catch (NullPointerException e) {
                setLastName("");
            }
            try {
                setActive(result.getBoolean(5));
            } catch (NullPointerException e) {
                setActive(false);
            }
            try {
                setAdministrator(result.getBoolean(6));
            } catch (NullPointerException e) {
                setAdministrator(false);
            }
            try {
                setComment(result.getString(7));
            } catch (NullPointerException e) {
                setComment("");
            }
            try {
                setPlugin(result.getString(8).trim());
            } catch (NullPointerException e) {
                setPlugin("");
            }
        }
    }

    @Override
    public int getType() {
        return NamedObject.USER;
    }

    @Override
    public String getMetaDataKey() {
        return META_TYPES[USER];
    }

    @Override
    public boolean allowsChildren() {
        return false;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setTag(String tag, String value) {
        tags.put(tag, value);
    }

    public void dropTag(String tag) {
        tags.remove(tag);
    }

    public String getTag(String tag) {
        return tags.get(tag);
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public Boolean getAdministrator() {
        return admin;
    }

    public void setAdministrator(boolean administrator) {
        admin = administrator;
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public DefaultDatabaseUser getCopy() {
        DefaultDatabaseUser user = new DefaultDatabaseUser((DatabaseMetaTag) getParent(), getName());
        user.setFirstName(firstName);
        user.setMiddleName(middleName);
        user.setLastName(lastName);
        user.setActive(active);
        user.setAdministrator(admin);
        user.setComment(comment);
        //user.setPassword(password);
        user.setPlugin(plugin);
        user.tags = new HashMap<>();
        for (String key : tags.keySet()) {
            user.tags.put(key, tags.get(key));
        }
        return user;
    }

    void loadTags() {
        DefaultStatementExecutor querySender = new DefaultStatementExecutor(getHost().getDatabaseConnection());
        try {
            String query = "SELECT * FROM SEC$USER_ATTRIBUTES WHERE SEC$USER_NAME = '" + getName() + "' and SEC$PLUGIN = '" + getPlugin() + "'";
            ResultSet rs = querySender.getResultSet(query).getResultSet();
            while (rs.next()) {
                tags.put(rs.getString(2), rs.getString(3));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            querySender.releaseResources();
        }
    }

    public void loadData() {
        getObjectInfo();
        loadTags();
    }

    public String getCreateSQLText() throws DataSourceException {

        return SQLUtils.generateCreateUser(this);
    }

}
