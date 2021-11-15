package daos;

import models.MindMap;

import java.util.Set;

public interface MindMapDao {

    MindMap getMindMap(Integer id);
    Set<MindMap> getAllMindMaps();
    MindMap getMindMapByTopic(String topic);
    boolean insertMindMap(MindMap mind_map);
    boolean updateMindMap(MindMap mind_map);
    boolean deleteMindMap(Integer id);
}
