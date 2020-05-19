package com.team34.model.character;

class Association {
    public long startCharacterUID; // UID to which character the starting point is attached to.
    public long endCharacterUID; // UID to which character the ending point is attached to.
    public double startX;
    public double startY;
    public double endX;
    public double endY;

    Association() {
        startCharacterUID = -1L;
        endCharacterUID = -1L;
        startX = 0.0;
        startY = 0.0;
        endX = 0.0;
        endY = 0.0;
    }
}