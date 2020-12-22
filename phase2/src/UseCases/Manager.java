package UseCases;

import org.bson.Document;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Manager {

    /**
     * Gets a HashMap of all information for an entity. Stores each field as the key and a list of Strings for the
     * value(s) corresponding to the field.
     *
     * @param id The id for the entity.
     * @return A HashMap representing an entity.
     */
    Map<String, List<String>> getInfo(String id);

    /**
     * Gets a list of HashMaps, each representing an entity and containing relevant information of that entity.
     * This list should represent all entities of that type existing in the program at that moment.
     *
     * @return A list of HashMaps, which represent an entity each.
     */
    List<Map<String, List<String>>> getInfoAsList();

    /**
     * Create a new Entity from the imported Document from the database and add it to the Manager's hashmap.
     *
     * @param entity The Document containing information about a single entity.
     */
    void importEntity(Document entity) throws ParseException;
}
