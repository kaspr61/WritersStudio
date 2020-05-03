package com.team34.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.stream.*;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.team34.model.event.*;
import com.team34.model.character.*;

// This class will hold the main load and save functions for the project file.
// This class will perhaps also hold information about project specific
// preferences and layout, in terms of character block coordinates, associations, and so forth.

/**
 * This class represents the top layer of the model/data.
 * @author Kasper S. Skott
 */
public class Project {

    public final EventManager eventManager;

    private String workingDir;
    private Path workingPath;

    private UserPreferences userPrefs;


    /**
     * Contructs the project.
     */
    public Project() {
        eventManager = new EventManager(0, 0);
        userPrefs = new UserPreferences();

        workingDir = System.getProperty("user.dir");
        workingPath = Paths.get(workingDir);

        if(workingPath.endsWith("bin")) {
            System.setProperty("user.dir", workingPath.getRoot().resolve(workingPath.subpath(0, workingPath.getNameCount()-1)).toString());
            workingDir = System.getProperty("user.dir");
            workingPath = Paths.get(workingDir);
        }

        loadUserPrefs();
        writeUserPrefs();
    }

    private void loadUserPrefs() {
        File file = new File(workingDir, "preferences.xml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try(FileInputStream fileStream = new FileInputStream(file)) {

            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(fileStream);

            XMLEvent event;
            while(eventReader.hasNext()) {
                event = eventReader.nextEvent();

                if(event.isStartElement()) { // Start window tag
                    StartElement startElement = event.asStartElement();
                    switch (startElement.getName().getLocalPart()) {
                        case "window_maximized":
                            event = eventReader.nextEvent();
                            userPrefs.windowMaximized = Boolean.parseBoolean(event.asCharacters().getData());
                            break;
                        case "window_width":
                            event = eventReader.nextEvent();
                            userPrefs.windowWidth = Integer.parseInt(event.asCharacters().getData());
                            break;
                        case "window_height":
                            event = eventReader.nextEvent();
                            userPrefs.windowHeight = Integer.parseInt(event.asCharacters().getData());
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private void addPreference(XMLEventFactory factory, XMLEventWriter writer, String localName, String content)
        throws XMLStreamException
    {
        XMLEvent event;
        event = factory.createStartElement("", "", localName);
        writer.add(event);
        event = factory.createCharacters(content);
        writer.add(event);
        event = factory.createEndElement("", "", localName);
        writer.add(event);
    }

    public void writeUserPrefs() {
        File file = new File(workingDir, "preferences.xml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try(FileOutputStream fileStream = new FileOutputStream(file, false)) {

            XMLEventFactory eventFactory = XMLEventFactory.newFactory();
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(fileStream);

            XMLEvent event = eventFactory.createStartDocument("UTF-8", "1.0");
            eventWriter.add(event);

            event = eventFactory.createStartElement("", "", "preferences");
            eventWriter.add(event);

            addPreference(eventFactory, eventWriter,
                    "window_maximized", Boolean.toString(userPrefs.windowMaximized));
            addPreference(eventFactory, eventWriter,
                    "window_width", Integer.toString(userPrefs.windowWidth));
            addPreference(eventFactory, eventWriter,
                    "window_height", Integer.toString(userPrefs.windowHeight));

            event = eventFactory.createEndElement("", "", "preferences");
            eventWriter.add(event);

            event = eventFactory.createEndDocument();
            eventWriter.add(event);

            eventWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }


        }

    public UserPreferences getUserPreferences() {
        return userPrefs;
    }

    public class UserPreferences {
        public boolean windowMaximized = false;
        public int windowWidth = 1280;
        public int windowHeight = 720;

        public UserPreferences() { }

        public UserPreferences(UserPreferences ref) {
            windowMaximized = ref.windowMaximized;
            windowWidth = ref.windowWidth;
            windowHeight = ref.windowHeight;
        }
    }

}
