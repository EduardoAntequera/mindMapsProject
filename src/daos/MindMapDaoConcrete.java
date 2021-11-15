package daos;

import models.MindMap;
import org.sqlite.util.StringUtils;
import sqlconnection.ConnectionFactory;

import java.sql.*;
import java.util.*;

public class MindMapDaoConcrete implements MindMapDao{

    public MindMapDaoConcrete() {

    }

    /*
    * Searches a given MindMap by a given keyword, limits the
    * search to the root, child root and level 2 topic by default
    * can be searched deeper by changing limit from "2" to other value.
    * If the input map is not a root node it will go to the root and
    * perform the search from there.
    */
    public Set searchMindMapByKeyword(String keyword, MindMap map){
        return searchMindMapByKeyword(keyword, map, 2);
    }

    public Set searchMindMapByKeyword(String keyword, MindMap map, int limit){
        if (map.getParentId() > 0) {
            // Goes to root and performs search from root
            while (map.getParentId() > 0) { map = getParent(map);}
            return searchMindMapByKeyword(keyword, map, limit);
        } else {
            Set result = new HashSet();
            Set explored = new HashSet();

            // Adds root node to explored, so it can later be checked for the keyword
            explored.add(map);

            // search for all childs of given map and add them to the explored set
            // Recursively goes down the tree doing so until explored contains all nodes
            explored.addAll(searchMindMapByKeywordHelper(map, limit));

            // Check for keyword in each node of tree, if it is there add node to result
            Iterator it = explored.iterator();
            while (it.hasNext()) {
                MindMap nextMap = (MindMap) it.next();
                if (nextMap.getTopic().contains(keyword.substring(0, 1).toUpperCase() + keyword.substring(1))
                        | nextMap.getTopic().contains(keyword.toUpperCase())
                        | nextMap.getTopic().contains(keyword.toLowerCase())
                        | nextMap.getTopic().contains(keyword)) {
                    result.add(nextMap);
                }
            }
            // return all matching nodes in given mindmap tree
            return result;
        }
    }

    private Set searchMindMapByKeywordHelper(MindMap map, int limit){
        Connection connection = ConnectionFactory.getConnection();
        Set explored = new HashSet();

        try {
            // selects child nodes of current mindmap node
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM maps WHERE maps.parent_id=?");
            ps.setInt(1, map.getId());
            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                // Create a mind map from each result
                MindMap rsmm = extractMindMapFromResultSet(rs);
                // Add the mind map to the explored set
                explored.add( rsmm );
                // Perform recursive search down the tree
                if (limit > 1) {
                    explored.addAll(searchMindMapByKeywordHelper(rsmm, limit - 1));
                }
            }
            return explored;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private MindMap getParent(MindMap map){
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM maps WHERE maps.id=?" );
            ps.setInt(1, map.getParentId());
            ResultSet rs = ps.executeQuery();

            if(rs.next()) { return extractMindMapFromResultSet(rs);}

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public MindMap getMindMap(Integer id) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM maps WHERE id=" + id);

            if(rs.next())
            {
                return extractMindMapFromResultSet(rs);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public Set<MindMap> getAllMindMaps() {
        Connection connection = ConnectionFactory.getConnection();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM maps");

            Set mind_maps = new HashSet();

            while(rs.next())
            {
                MindMap mind_map = extractMindMapFromResultSet(rs);
                mind_maps.add(mind_map);
            }

            return mind_maps;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public MindMap getMindMapByTopic(String topic) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM maps WHERE maps.topic=?");
            ps.setString(1, topic);
            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                return extractMindMapFromResultSet(rs);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean insertMindMap(MindMap mind_map) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO maps VALUES (?, ?, ?, ?)");
            ps.setInt(1, mind_map.getId());
            ps.setInt(2, mind_map.getParentId());
            ps.setInt(3, mind_map.getUserId());
            ps.setString(4, mind_map.getTopic());
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
    public boolean updateMindMap(MindMap mind_map) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE maps SET topic=?, parent_id=?, user_id=? WHERE id=?");
            ps.setString(1, mind_map.getTopic());
            ps.setInt(2, mind_map.getParentId());
            ps.setInt(3, mind_map.getUserId());
            ps.setInt(4, mind_map.getId());
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
    public boolean deleteMindMap(Integer id) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            Statement stmt = connection.createStatement();
            int i = stmt.executeUpdate("DELETE FROM maps WHERE id=" + id);

            if(i == 1) {
                return true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    private MindMap extractMindMapFromResultSet(ResultSet rs) throws SQLException {
        MindMap mind_Map = new MindMap();

        mind_Map.setId( rs.getInt("id") );
        mind_Map.setParentId( rs.getInt("parent_id") );
        mind_Map.setUserId( rs.getInt("user_id") );
        mind_Map.setTopic( rs.getString("topic"));

        return mind_Map;
    }
}
