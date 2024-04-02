package com.example.planegame.gameplay;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EnemyShip extends Ship{
    int type;

    public EnemyShip(float xCentre, float yCentre, float width, float height, float movementSpeed, int health, float bulletWidth, float bulletHeight, float bulletSpeed, float bulletCooldown, TextureRegion shipTexture, TextureRegion bulletTexture, int type) {
        super(xCentre, yCentre, width, height, movementSpeed, health, bulletWidth, bulletHeight, bulletSpeed, bulletCooldown, shipTexture, bulletTexture);
        this.type = type;
    }

    @Override
    public Bullet[] fireBullets() {
        Bullet[] bullets = new Bullet[this.type];
        if(this.type == 1) {
            bullets[0] = new Bullet(boundingBox.x + boundingBox.width * 0.5f, boundingBox.y,
                    bulletWidth, bulletHeight, bulletSpeed, bulletTextureRegion, this.type);
        }else{
            bullets[0] = new Bullet(boundingBox.x + boundingBox.width * 0.5f, boundingBox.y,
                    bulletWidth, bulletHeight, bulletSpeed, bulletTextureRegion, this.type);
            bullets[1] = new Bullet(boundingBox.x + boundingBox.width * 0.5f, boundingBox.y,
                    bulletWidth, bulletHeight, bulletSpeed, bulletTextureRegion, this.type + 1);
        }
        timeSinceLastShot = 0;
        return bullets;
    }
}
