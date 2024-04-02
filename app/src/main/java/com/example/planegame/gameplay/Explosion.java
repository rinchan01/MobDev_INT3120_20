package com.example.planegame.gameplay;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Explosion {
    private Animation<TextureRegion> explosionAnimation;
    private float explosionTimer;
    private Rectangle boundingBox;
    private String type;
    Explosion(Rectangle boundingBox, float frameDuration, TextureAtlas textureAtlas, String type) {
        this.boundingBox = boundingBox;
        if (type.equals("explosion")) {
            TextureRegion[] textureRegions = new TextureRegion[10];
            for (int i = 0; i < 10; i++) {
                String filename = "shot6_exp" + (i + 1);
                textureRegions[i] = textureAtlas.findRegion(filename);
            }
            explosionAnimation = new Animation<>(frameDuration / 10, textureRegions);
        } else {
            TextureRegion[] textureRegions = new TextureRegion[4];
            for (int i = 0; i < 4; i++) {
                String filename = "exhaust" + (i+1);
                textureRegions[i] = textureAtlas.findRegion(filename);
            }
            explosionAnimation = new Animation<>(frameDuration / 4, textureRegions);
            explosionAnimation.setPlayMode(Animation.PlayMode.LOOP);
        }
    }

    public void update(float deltaTime) {
        explosionTimer += deltaTime;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(explosionAnimation.getKeyFrame(explosionTimer), boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }

    public boolean isFinished() {
        return explosionAnimation.isAnimationFinished(explosionTimer);
    }

    public void moveExplosion(float xChange, float yChange) {
        boundingBox.setPosition(boundingBox.x + xChange, boundingBox.y + yChange);
    }
}
