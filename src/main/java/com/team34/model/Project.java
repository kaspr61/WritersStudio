package com.team34.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.xml.stream.*;
import javax.xml.stream.events.Attribute;
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
    private String currProjectName;


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

    public String getProjectName() {
        return currProjectName;
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

                if(event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    switch (startElement.getName().getLocalPart()) {
                        case "project_directory":
                            event = eventReader.nextEvent();
                            if(event.isCharacters())
                                userPrefs.projectDir = event.asCharacters().getData();
                            break;
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
        writer.add(factory.createCharacters(System.lineSeparator()));
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

            eventWriter.add(eventFactory.createStartDocument("UTF-8", "1.0"));
            eventWriter.add(eventFactory.createCharacters(System.lineSeparator()));

            eventWriter.add(eventFactory.createStartElement("", "", "preferences"));
            eventWriter.add(eventFactory.createCharacters(System.lineSeparator()));

            addPreference(eventFactory, eventWriter,
                    "project_directory", userPrefs.projectDir);
            addPreference(eventFactory, eventWriter,
                    "window_maximized", Boolean.toString(userPrefs.windowMaximized));
            addPreference(eventFactory, eventWriter,
                    "window_width", Integer.toString(userPrefs.windowWidth));
            addPreference(eventFactory, eventWriter,
                    "window_height", Integer.toString(userPrefs.windowHeight));

            eventWriter.add(eventFactory.createEndElement("", "", "preferences"));
            eventWriter.add(eventFactory.createCharacters(System.lineSeparator()));

            eventWriter.add(eventFactory.createEndDocument());

            eventWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }


    }

    private void loadUIDManager(XMLEvent event, XMLEventReader reader)
            throws XMLStreamException
    {
        while(reader.hasNext()) {
            event = reader.nextEvent();

            if(event.isStartElement()) {
                StartElement startElement = event.asStartElement();

                if(startElement.getName().getLocalPart() == "uid"){
                    event = reader.nextEvent();
                    if(event.isCharacters()) {
                        UIDManager.addUID(Long.parseLong(event.asCharacters().getData()));
                    }
                    event = reader.nextEvent();
                }

            }
            else if(event.isEndElement()) {
                if(event.asEndElement().getName().getLocalPart() == "uid_manager")
                    return;
            }
        }
    }

    private void loadEvents(XMLEvent event, XMLEventReader reader)
            throws XMLStreamException
    {
        long uid = -1L;
        String name = null;

        while(reader.hasNext()) {
            event = reader.nextEvent();

            if(event.isStartElement()) {
                StartElement startElement = event.asStartElement();

                if(startElement.getName().getLocalPart() == "event"){
                    Iterator<Attribute> attrIt = startElement.getAttributes();
                    while(attrIt.hasNext()) {
                        Attribute attr = attrIt.next();
                        switch(attr.getName().getLocalPart()) {
                            case "uid":
                                uid = Long.parseLong(attr.getValue());
                                break;
                            case "name":
                                name = attr.getValue();
                                break;
                        }
                    }

                    event = reader.nextEvent();
                    if(event.isCharacters() && uid != -1L && name != null) {
                        eventManager.addEvent(uid, name, event.asCharacters().getData());
                    }
                }

            }
            else if(event.isEndElement()) {
                if(event.asEndElement().getName().getLocalPart() == "events")
                    return;
            }
        }
    }

    private void loadEventOrderLists(XMLEvent event, XMLEventReader reader)
            throws XMLStreamException
    {
        LinkedList<Long> orderList = new LinkedList<>();

        while(reader.hasNext()) {
            event = reader.nextEvent();

            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();

                if (startElement.getName().getLocalPart() == "order_list") {
                    orderList = new LinkedList<Long>();

                    while (reader.hasNext()) {
                        event = reader.nextEvent();

                        if (event.isStartElement()) {
                            startElement = event.asStartElement();
                            if (startElement.getName().getLocalPart() == "li") {
                                event = reader.nextEvent();
                                if (event.isCharacters())
                                    orderList.add(Long.parseLong(event.asCharacters().getData()));
                            }
                        }
                        else if(event.isEndElement()) {
                            if (event.asEndElement().getName().getLocalPart() == "order_list")
                                eventManager.addOrderList(orderList);
                            if (event.asEndElement().getName().getLocalPart() == "event_order")
                                    return;
                        }
                    }
                }

            }

        }
    }

    public void clearProject() {
        eventManager.clear();
        UIDManager.clear();
        currProjectName = "";
    }

    public void loadProject(File projectFile) throws IOException, XMLStreamException {
        clearProject();

        try(FileInputStream fileStream = new FileInputStream(projectFile)) {

            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(fileStream);

            XMLEvent event;
            while(eventReader.hasNext()) {
                event = eventReader.nextEvent();

                if(event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    switch (startElement.getName().getLocalPart()) {
                        case "project":
                            Attribute projName = startElement.getAttributes().next();
                            if(projName.getName().getLocalPart() == "name")
                                currProjectName = projName.getValue();
                            break;
                        case "uid_manager":
                            loadUIDManager(event, eventReader);
                            break;
                        case "events":
                            loadEvents(event, eventReader);
                            break;
                        case "event_order":
                            loadEventOrderLists(event, eventReader);
                            break;
                    }
                }
            }
        }

    }

    public UserPreferences getUserPreferences() {
        return userPrefs;
    }

    public class UserPreferences {
        public String projectDir = "";
        public boolean windowMaximized = false;
        public int windowWidth = 1280;
        public int windowHeight = 720;

        public UserPreferences() { }

        public UserPreferences(UserPreferences ref) {
            projectDir = ref.projectDir;
            windowMaximized = ref.windowMaximized;
            windowWidth = ref.windowWidth;
            windowHeight = ref.windowHeight;
        }
    }

}
