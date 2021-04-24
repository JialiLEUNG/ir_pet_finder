package com.ir.dataPreProcess;

import java.io.IOException;
import java.util.Map;

/**
 * DocCollection is an interface for reading individual xml file
 */

public interface DocCollection {
    /**
     * Read and return the next document stored in the collection.
     * Each document is stored as a Map:
     * key is the document id, and value is document content.
     * Get the document's id by calling doc.keySet().iterator().next()
     * Get the document's content by map.get(doc id)
     *
     * @return The next document stored in the collection;
     *         or null if it is the end of the collection file.
     */
    public abstract Map<String, Map<String, String>> nextDoc() throws IOException, Exception;
}