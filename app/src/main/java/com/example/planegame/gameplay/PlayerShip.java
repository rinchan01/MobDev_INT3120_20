package com.example.planegame.gameplay;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class PlayerShip extends Ship{
    private Explosion exhaust;


    public PlayerShip(float xCentre, float yCentre, float width, float height, float movementSpeed, int health, float bulletWidth, float bulletHeight, float bulletSpeed, float bulletCooldown, TextureRegion shipTexture, TextureRegion bulletTexture, TextureAtlas textureAtlas) {
        super(xCentre, yCentre, width, height, movementSpeed, health, bulletWidth, bulletHeight, bulletSpeed, bulletCooldown, shipTexture, bulletTexture);
        Rectangle exhaustRectangle = new Rectangle(boundingBox.x + boundingBox.width*0.35f, boundingBox.y - 6f, 4f, 6f);
        exhaust = new Explosion(exhaustRectangle, 0.5f, textureAtlas, "exhaust");
    }

    @Override
    public Bullet[] fireBullets() {
        Bullet[] bullets = new Bullet[2];
        bullets[0] = new Bullet(boundingBox.x + boundingBox.width*0.3f, boundingBox.y+boundingBox.height*0.45f,
            bulletWidth, bulletHeight, bulletSpeed, bulletTextureRegion, 1);

        bullets[1] = new Bullet(boundingBox.x + boundingBox.width*0.7f, boundingBox.y + boundingBox.height*0.45f,
            bulletWidth, bulletHeight, bulletSpeed, bulletTextureRegion, 1);

        timeSinceLastShot = 0;
        return bullets;
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        exhaust.draw(batch);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        exhaust.update(deltaTime);
    }

    public void translate(float xChange, float yChange) {
        boundingBox.setPosition(boundingBox.x + xChange, boundingBox.y + yChange);
        exhaust.moveExplosion(xChange, yChange);
    }
}
