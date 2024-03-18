package com.example.planegame.gameplay;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

abstract public class Ship {
    float movementSpeed;
    int health;
    float bulletWidth, bulletHeight;
    float bulletCooldown;
    Rectangle boundingBox;
    float timeSinceLastShot = 0;
    float bulletSpeed;
    TextureRegion shipTextureRegion, bulletTextureRegion;

    public Ship(float xCentre, float yCentre, float width, float height, float movementSpeed, int health,
                float bulletWidth, float bulletHeight, float bulletSpeed, float bulletCooldown,
                TextureRegion shipTexture,
                TextureRegion bulletTexture) {
        this.movementSpeed = movementSpeed;
        this.health = health;
        this.shipTextureRegion = shipTexture;
        this.bulletTextureRegion = bulletTexture;
        this.bulletWidth = bulletWidth;
        this.bulletHeight = bulletHeight;
        this.bulletSpeed = bulletSpeed;
        this.bulletCooldown = bulletCooldown;
        this.boundingBox = new Rectangle(xCentre - width / 2, yCentre - height / 2, width, height);
    }

    public void update(float deltaTime) {
        timeSinceLastShot += deltaTime;
    }

    public boolean canFireBullet() {
        return timeSinceLastShot - bulletCooldown >= 0;
    }

    public abstract Bullet[] fireBullets();

    public void draw(Batch batch) {
        batch.draw(shipTextureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }

    public boolean intersects(Rectangle otherRectangle) {
        return boundingBox.overlaps(otherRectangle);
    }

    public void hit(Bullet bullet) {
        health--;
    }

    public void translate(float xChange, float yChange) {
        boundingBox.setPosition(boundingBox.x + xChange, boundingBox.y + yChange);
    }
}
