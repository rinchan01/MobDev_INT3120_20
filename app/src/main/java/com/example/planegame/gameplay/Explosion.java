package com.example.planegame.gameplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Explosion {
    private Animation<TextureRegion> explosionAnimation;
    private float explosionTimer;
    private Rectangle boundingBox;
    Explosion(Rectangle boundingBox, float frameDuration) {
        this.boundingBox = boundingBox;
        TextureRegion[] textureRegions = new TextureRegion[10];
        for(int i = 0; i < 10; i++) {
            String filename = "shot6_exp" + (i+1);
            textureRegions[i] = GameScreen.textureAtlas.findRegion(filename);
        }
        explosionAnimation = new Animation<>(frameDuration/10, textureRegions);
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
}
