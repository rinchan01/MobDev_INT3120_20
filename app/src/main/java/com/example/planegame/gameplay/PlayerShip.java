package com.example.planegame.gameplay;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerShip extends Ship{


    public PlayerShip(float xCentre, float yCentre, float width, float height, float movementSpeed, int health, float bulletWidth, float bulletHeight, float bulletSpeed, float bulletCooldown, TextureRegion shipTexture, TextureRegion bulletTexture) {
        super(xCentre, yCentre, width, height, movementSpeed, health, bulletWidth, bulletHeight, bulletSpeed, bulletCooldown, shipTexture, bulletTexture);
    }

    @Override
    public Bullet[] fireBullets() {
        Bullet[] bullets = new Bullet[2];
        bullets[0] = new Bullet(boundingBox.x+ boundingBox.width*0.3f, boundingBox.y+boundingBox.height*0.45f,
            bulletWidth, bulletHeight, bulletSpeed, bulletTextureRegion);

        bullets[1] = new Bullet(boundingBox.x + boundingBox.width*0.7f, boundingBox.y + boundingBox.height*0.45f,
            bulletWidth, bulletHeight, bulletSpeed, bulletTextureRegion);

        timeSinceLastShot = 0;
        return bullets;
    }
}
