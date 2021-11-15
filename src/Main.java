import daos.MindMapDaoConcrete;
import daos.UserDaoConcrete;
import models.MindMap;
import sqlconnection.ConnectionFactory;

import java.util.Iterator;
import java.util.Set;

public class Main {
    public static void main(String [] args){

        ConnectionFactory.getConnection();

        // Load all users from the database
        UserDaoConcrete user_dao = new UserDaoConcrete();
        user_dao.getAllUsers();

        // Load all mind maps from the database
        MindMapDaoConcrete mind_map_dao = new MindMapDaoConcrete();
        mind_map_dao.getAllMindMaps();


        /*
        Now we will simulate a user requesting his/her maps.
        We then print the results.
        I have made the function listUserMaps return only the root nodes from the maps
        If you want to see all child nodes too then change:
        Set userMaps = userDao.listUserMaps(user_id: Num); where Num is a user id (only 2 users in database)
        Set userMaps = userDao.listUserMaps(user_id: Num, just_root: false);

        */
        Set userMaps = user_dao.listUserMaps(2);
        Iterator it = userMaps.iterator();

        System.out.println(" User requests his/her maps: ");
        while (it.hasNext()){
            MindMap currentMap = (MindMap) it.next();
            System.out.println("-------Next map------");
            System.out.print("ID: ");
            System.out.println( currentMap.getId() );
            System.out.print("Topic: ");
            System.out.println( currentMap.getTopic() );
            System.out.print("Parent ID: ");
            System.out.println( currentMap.getParentId() );
            System.out.print("User ID: ");
            System.out.println( currentMap.getUserId() );
        }

        System.out.println("");
        System.out.println("");

        /*
        Now we will simulate a search on a user selected map.
        We then print the results.
        The function searchMindMapByKeyword accepts three parameters:
           - keyword: keyword
           - map: a user selected map
           - limit: How deep should the search be in the tree (optional - defaults to 2)
        The search always starts from the root of the map, even
        if the user selects a child or grandchild node it will do
        it from the root.
        */

        MindMap user_selected_map = mind_map_dao.getMindMap(19);
        Set keyword_maps = mind_map_dao.searchMindMapByKeyword("root", user_selected_map);
        Iterator iter = keyword_maps.iterator();

        System.out.println(" User searches one of his/her maps by keyword: ");
        while (iter.hasNext()){
            MindMap current_map = (MindMap) iter.next();
            System.out.println("-------Next map------");
            System.out.print("ID: ");
            System.out.println( current_map.getId() );
            System.out.print("Topic: ");
            System.out.println( current_map.getTopic() );
            System.out.print("Parent ID: ");
            System.out.println( current_map.getParentId() );
            System.out.print("User ID: ");
            System.out.println( current_map.getUserId() );

        }
    }
}
