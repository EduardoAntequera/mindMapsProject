package daos;

import models.MindMap;
import models.User;
import sqlconnection.ConnectionFactory;

import java.sql.*;
import java.util.*;

public class UserDaoConcrete implements UserDao {


    public UserDaoConcrete() {
    }

    /*
    * Listing user maps, defaults to only show root nodes
    * can show all nodes by changing just_roots to false
    * */
    public Set listUserMaps(Integer user_id){
        return listUserMaps(user_id, true);
    }

    public Set listUserMaps(Integer user_id, boolean just_roots){
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("Select * FROM maps");
            if (just_roots) {
                ps = connection.prepareStatement("SELECT * FROM maps WHERE maps.user_id = ? AND maps.parent_id = ?");
                ps.setInt(1, user_id);
                ps.setInt(2, 0);
            } else {
                ps = connection.prepareStatement("SELECT * FROM maps WHERE maps.user_id = ?");
                ps.setInt(1, user_id);
            }
            ResultSet rs = ps.executeQuery();


            Set mind_maps = new HashSet();

            while(rs.next())
            {
                MindMap mind_Map = new MindMap();
                mind_Map.setId( rs.getInt("id") );
                mind_Map.setParentId( rs.getInt("parent_id") );
                mind_Map.setUserId( rs.getInt("user_id") );
                mind_Map.setTopic( rs.getString("topic"));

                mind_maps.add(mind_Map);
            }
            return mind_maps;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }


    @Override
    public User getUser(Integer id) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE id=" + id);

            if(rs.next())
            {
                return extractUserFromResultSet(rs);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public Set getAllUsers() {
        Connection connection = ConnectionFactory.getConnection();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");

            Set users = new HashSet();

            while(rs.next())
            {
                User user = extractUserFromResultSet(rs);
                users.add(user);
            }

            return users;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public User getUserByUserName(String user) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE users=?");
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                return extractUserFromResultSet(rs);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean insertUser(User user) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO users VALUES (?, ?)");
            ps.setInt(1, user.getId());
            ps.setString(2, user.getUserName());
            int i = ps.executeUpdate();

            if(i == 1) {
                return true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateUser(User user) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE users SET user_name=? WHERE id=?");
            ps.setString(1, user.getUserName());
            ps.setInt(2, user.getId());
            int i = ps.executeUpdate();

            if(i == 1) {
                return true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteUser(Integer id) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            Statement stmt = connection.createStatement();
            int i = stmt.executeUpdate("DELETE FROM users WHERE id=" + id);

            if(i == 1) {
                return true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();

        user.setId( rs.getInt("id") );
        user.setUserName( rs.getString("user_name") );

        return user;
    }
}
