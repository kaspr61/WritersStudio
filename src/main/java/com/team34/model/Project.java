package com.team34.model;

import com.team34.model.event.*;
import com.team34.model.character.*;

// This class will hold the main load and save functions for the project file.
// This class will also eventually hold information about project specific
// preferences and layout, in terms of character block coordinates, associations, and so forth.

/**
 * @author Kasper S. Skott
 */
public class Project {

    public final EventManager eventManager;

    public Project() {
        eventManager = new EventManager(0, 0);
    }

}
