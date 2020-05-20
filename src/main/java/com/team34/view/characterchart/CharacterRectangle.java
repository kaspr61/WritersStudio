package com.team34.view.characterchart;

import com.team34.view.LabeledRectangle;

import java.util.ArrayList;

class CharacterRectangle extends LabeledRectangle {

    private ArrayList<Long> assocPointUIDs; // UIDs to association points that are attached.

    /**
     * Creates a new instance of CharacterRectangle with the given text label and width.
     * Also creates a tooltip, but doesn't install it. Tooltip installation is managed
     * by the {@link com.team34.view.characterchart.CharacterChart} when adding and clearing events.
     *
     * @param label  the text to be displayed within the rectangle
     * @param width  the width of the rectangle. Set to 0.0 to use default
     * @param height the height of the rectangle. Set to 0.0 to use default
     */
    CharacterRectangle(String label, double width, double height) {
        super(label, width, height);
        assocPointUIDs = new ArrayList<>();
    }

    void addAssociationPoint(long uid) {
        assocPointUIDs.add(uid);
    }

    void removeAssociationPoint(long uid) {
        assocPointUIDs.remove(uid);
    }

    Long[] getAssociationPoints() {
        if(assocPointUIDs.isEmpty())
            return null;

        return assocPointUIDs.toArray(new Long[assocPointUIDs.size()]);
    }

}
