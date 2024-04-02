package com.example.planegame.gameplay;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Bullet {

    Rectangle boundingBox;
    float movementSpeed;
    TextureRegion bulletTexture;
    int type;

    public Bullet(float xPosition, float yPosition, float width, float height, float movementSpeed, TextureRegion bulletTexture, int type) {
        this.boundingBox = new Rectangle(xPosition - width/2, yPosition, width, height);
        this.movementSpeed = movementSpeed;
        this.bulletTexture = bulletTexture;
        this.type = type;
    }

    public void draw(Batch batch) {
        batch.draw(bulletTexture, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }
}
