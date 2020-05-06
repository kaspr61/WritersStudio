package com.team34.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;

import javax.xml.stream.*;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.team34.model.event.*;
import com.team34.model.character.*;

/**
 * This class represents the top layer of the model/data.
 * <p>
 * All file loading and saving is handled by this class.
 * <p>
 * Writer's Studio uses a file called "preferences.xml" in order to allows settings to be made
 * and stored throughout and between sessions.
 * <p>
 * Writer's Studio stores each project a user creates as an individual wsp-file (Writer's Studio Project).
 * By calling the method {@link Project#loadProject(File)}, the project file will be loaded.
 * Likewise, if the method {@link Project#saveProject()} is called, the project file will be saved.
 * Any project that was already loaded when loading a new project, will be discarded.
 *
 * @author Kasper S. Skott
 */
public class Project {

    public final EventManager eventManager;

    private String workingDir;
    private Path workingPath;

    private UserPreferences userPrefs;
    private String currProjectName;
    private File currProjectFile;


    /**
     * Constructs the project, sets up the working directory, and loads the preferences file.
     */
    public Project() {
        eventManager = new EventManager();
        userPrefs = new UserPreferences();

        workingDir = System.getProperty("user.dir");
        workingPath = Paths.get(workingDir);
        currProjectName = "";
        currProjectFile = null;

        if(workingPath.endsWith("bin")) {
            System.setProperty("user.dir", workingPath.getRoot().resolve(workingPath.subpath(0, workingPath.getNameCount()-1)).toString());
            workingDir = System.getProperty("user.dir");
            workingPath = Paths.get(workingDir);
        }

        try {
            loadUserPrefs();
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the current project name.
     * @return {@link Project#currProjectName}
     */
    public String getProjectName() {
        return currProjectName;
    }

    /**
     * Sets the current project name.
     * @param name the new name
     */
    public void setProjectName(String name) {
        currProjectName = name;
    }

    /**
     * Returns the file of the current project.
     * @return {@link Project#currProjectFile}
     */
    public File getProjectFile() {
        return currProjectFile;
    }

    /**
     * Sets the file of the current project
     * @param file the new file
     */
    public void setProjectFile(File file) {
        currProjectFile = file;
    }

    /**
     * Loads the preferences file.
     * @throws IOException
     * @throws XMLStreamException
     */
    private void loadUserPrefs() throws IOException, XMLStreamException {
        File file = new File(workingDir, "preferences.xml");
        if(!file.exists())
            return;

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
        }
    }

    /**
     * Internal helper method to {@link Project#writeUserPrefs()}
     * @param factory
     * @param writer
     * @param localName
     * @param content
     * @throws XMLStreamException
     */
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

    /**
     * Internal helper method to {@link Project#loadProject(File)}
     * @param event
     * @param reader
     * @throws XMLStreamException
     */
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

    /**
     * Internal helper method to {@link Project#loadProject(File)}
     * @param event
     * @param reader
     * @throws XMLStreamException
     */
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
                    if(uid != -1L && name != null) {
                        if(event.isCharacters())
                            eventManager.addEvent(uid, name, event.asCharacters().getData());
                        else
                            eventManager.addEvent(uid, name, "");

                    }
                }

            }
            else if(event.isEndElement()) {
                if(event.asEndElement().getName().getLocalPart() == "events")
                    return;
            }
        }
    }

    /**
     * Internal helper method to {@link Project#loadProject(File)} ()}
     * @param event
     * @param reader
     * @throws XMLStreamException
     */
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

    /**
     * Internal helper method to {@link Project#saveProject()}
     * @param factory
     * @param writer
     * @throws XMLStreamException
     */
    private void writeUIDManager(XMLEventFactory factory, XMLEventWriter writer)
            throws XMLStreamException
    {
        Long[] uids = UIDManager.getUIDs();
        if(uids == null)
            return;

        for(int i = 0; i < uids.length; i++) {
            writer.add(factory.createStartElement("", "", "uid"));
            writer.add(factory.createCharacters(Long.toString(uids[i])));
            writer.add(factory.createEndElement("", "", "uid"));
            writer.add(factory.createCharacters(System.lineSeparator()));
        }
    }

    /**
     * Internal helper method to {@link Project#saveProject()}
     * @param factory
     * @param writer
     * @throws XMLStreamException
     */
    private void writeEvents(XMLEventFactory factory, XMLEventWriter writer)
            throws XMLStreamException
    {
        Object[][] event = eventManager.getEvents();
        if(event == null)
            return;

        for(int i = 0; i < event.length; i++) {
            writer.add(factory.createStartElement("", "", "event"));
            writer.add(factory.createAttribute("name", (String) event[i][1]));
            writer.add(factory.createAttribute("uid", Long.toString((Long) event[i][0])));

            writer.add(factory.createCharacters((String) event[i][2]));

            writer.add(factory.createEndElement("", "", "event"));
            writer.add(factory.createCharacters(System.lineSeparator()));
        }
    }

    /**
     * Internal helper method to {@link Project#saveProject()}
     * @param factory
     * @param writer
     * @throws XMLStreamException
     */
    private void writeEventOrderLists(XMLEventFactory factory, XMLEventWriter writer)
            throws XMLStreamException
    {
        int i = 0;
        Long[] orderList = eventManager.getEventOrder(i);
        if(orderList == null)
            return;

        while(orderList != null) {
            writer.add(factory.createStartElement("", "", "order_list"));
            writer.add(factory.createCharacters(System.lineSeparator()));

            for (int j = 0; j < orderList.length; j++) {
                writer.add(factory.createStartElement("", "", "li"));
                writer.add(factory.createCharacters(Long.toString(orderList[j])));
                writer.add(factory.createEndElement("", "", "li"));
                writer.add(factory.createCharacters(System.lineSeparator()));
            }

            writer.add(factory.createEndElement("", "", "order_list"));
            writer.add(factory.createCharacters(System.lineSeparator()));

            orderList = eventManager.getEventOrder(++i);
        }
    }

    /**
     * Saves the current state of {@link Project#userPrefs} to the preferences file
     * @throws IOException
     * @throws XMLStreamException
     */
    public void writeUserPrefs() throws IOException, XMLStreamException {
        File file = new File(workingDir, "preferences.xml");
        file.createNewFile();

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
        }
    }

    /**
     * Loads the given file as a project.
     * This will discard any existing data, present in the currently loaded project.
     * @param projectFile the project file to load
     * @throws IOException
     * @throws XMLStreamException
     */
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
        finally {
            currProjectFile = projectFile;
            eventManager.resetChanges();
        }

    }

    /**
     * Saves the current state of the project data into the file, {@link Project#currProjectFile}.
     * @throws IOException
     * @throws XMLStreamException
     */
    public void saveProject() throws IOException, XMLStreamException {
        if(currProjectFile == null)
            throw new NullPointerException("currProjectFile must not be null");

        currProjectFile.createNewFile();

        if(currProjectName.isEmpty())
            currProjectName = currProjectFile.getName();

        try(FileOutputStream fileStream = new FileOutputStream(currProjectFile, false)) {

            XMLEventFactory eventFactory = XMLEventFactory.newFactory();
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(fileStream);

            eventWriter.add(eventFactory.createStartDocument("UTF-8", "1.0"));
            eventWriter.add(eventFactory.createCharacters(System.lineSeparator()));

            eventWriter.add(eventFactory.createStartElement("", "", "project"));
            eventWriter.add(eventFactory.createAttribute("name", currProjectName));
            eventWriter.add(eventFactory.createCharacters(System.lineSeparator()));

            eventWriter.add(eventFactory.createStartElement("", "", "uid_manager"));
            eventWriter.add(eventFactory.createCharacters(System.lineSeparator()));
            writeUIDManager(eventFactory, eventWriter);
            eventWriter.add(eventFactory.createEndElement("", "", "uid_manager"));
            eventWriter.add(eventFactory.createCharacters(System.lineSeparator()));

            eventWriter.add(eventFactory.createStartElement("", "", "events"));
            eventWriter.add(eventFactory.createCharacters(System.lineSeparator()));
            writeEvents(eventFactory, eventWriter);
            eventWriter.add(eventFactory.createEndElement("", "", "events"));
            eventWriter.add(eventFactory.createCharacters(System.lineSeparator()));

            eventWriter.add(eventFactory.createStartElement("", "", "event_order"));
            eventWriter.add(eventFactory.createCharacters(System.lineSeparator()));
            writeEventOrderLists(eventFactory, eventWriter);
            eventWriter.add(eventFactory.createEndElement("", "", "event_order"));
            eventWriter.add(eventFactory.createCharacters(System.lineSeparator()));

            eventWriter.add(eventFactory.createEndElement("", "", "project"));
            eventWriter.add(eventFactory.createCharacters(System.lineSeparator()));

            eventWriter.add(eventFactory.createEndDocument());

            eventWriter.flush();

            eventManager.resetChanges();
        }
    }

    /**
     * Discards all data currently loaded. Does not overwrite or erase any file.
     * This only affects the data currently loaded in the application.
     */
    public void clearProject() {
        eventManager.clear();
        UIDManager.clear();
        currProjectName = "";
        currProjectFile = null;
    }

    /**
     * Returns whether the project contains any unsaved changes.
     * @return true if there are unsaved changed
     */
    public boolean hasUnsavedChanges() {
        return eventManager.hasChanged();
    }

    /**
     * Returns a reference to the internal user preferences {@link Project#userPrefs}.
     * @return {@link Project#userPrefs}
     */
    public UserPreferences getUserPreferences() {
        return userPrefs;
    }

    /////////////////////////////////////////////////////////////////////////

    /**
     * This class stores data for the user preferences.
     */
    public class UserPreferences {
        public String projectDir = "";
        public boolean windowMaximized = false;
        public int windowWidth = 1280;
        public int windowHeight = 720;

        /**
         * Default constructor
         */
        public UserPreferences() { }

        /**
         * Copy constructor.
         * @param ref the instance to copy from.
         */
        public UserPreferences(UserPreferences ref) {
            projectDir = ref.projectDir;
            windowMaximized = ref.windowMaximized;
            windowWidth = ref.windowWidth;
            windowHeight = ref.windowHeight;
        }
    }

}
