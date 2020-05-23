package com.team34.model.character;

class Association {
    long startCharacterUID; // UID to which character the starting point is attached to.
    long endCharacterUID; // UID to which character the ending point is attached to.
    double startX;
    double startY;
    double endX;
    double endY;
    String label;
    double labelX;
    double labelY;

    Association() {
        startCharacterUID = -1L;
        endCharacterUID = -1L;
        startX = 0.0;
        startY = 0.0;
        endX = 0.0;
        endY = 0.0;
        label = "";
        labelX = 0.0;
        labelY = 0.0;
    }
}