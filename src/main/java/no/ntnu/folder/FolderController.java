package no.ntnu.folder;


import no.ntnu.mysql.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;

public class FolderController {
    private ConnectionManager connectionManager;

    public FolderController(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void createFolder(String name, int courseId, Integer parentFolderId) throws SQLException, NullPointerException {
        if (name == null) {
            throw new NullPointerException("Please provide a name!");
        }
        Folder folder = new Folder(name, courseId, parentFolderId);
        Connection conn = connectionManager.getConnection();
        folder.save(conn);
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
}
