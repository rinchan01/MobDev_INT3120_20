package com.example.planegame.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;

public class GameScreen implements Screen {
    private Camera camera;
    private Viewport viewport;
    private SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private TextureRegion[] backgrounds = new TextureRegion[4];
    private TextureRegion playerShipTextureRegion, enemyShipTextureRegion, enemyBulletTextureRegion, playerBulletTextureRegion;
    private float[] backgroundOffsets = {0, 0, 0, 0};
    private float backgroundMaxScrollingSpeed;
    private final int WORLD_WIDTH = 72;
    private final int WORLD_HEIGHT = 128;
    private final float TOUCH_MOVEMENT_THRESHOLD = 0.5f;
    private Ship playerShip;
    private Ship enemyShip;
    private LinkedList<Bullet> playerBullets = new LinkedList<>();
    private LinkedList<Bullet> enemyBullets = new LinkedList<>();
    GameScreen() {
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        textureAtlas = new TextureAtlas(Gdx.files.internal("game_assets/game_assets.atlas"));

        backgrounds[0] = textureAtlas.findRegion("Starscape00");
        backgrounds[1] = textureAtlas.findRegion("Starscape01");
        backgrounds[2] = textureAtlas.findRegion("Starscape02");
        backgrounds[3] = textureAtlas.findRegion("Starscape03");

        backgroundMaxScrollingSpeed = (float) (WORLD_HEIGHT) / 4;

        playerShipTextureRegion = textureAtlas.findRegion("ship6_slice");
        playerBulletTextureRegion = textureAtlas.findRegion("shot61_slice");

        enemyShipTextureRegion = textureAtlas.findRegion("ship4slice");
        enemyBulletTextureRegion = textureAtlas.findRegion("shot5slice");
        enemyShipTextureRegion.flip(false, true);

        playerShip = new PlayerShip(WORLD_WIDTH / 2, WORLD_HEIGHT/4, 15, 23.4f, 30, 100, 2, 12.8f, 40, 0.5f, playerShipTextureRegion, playerBulletTextureRegion);
        enemyShip = new EnemyShip(WORLD_WIDTH / 2, WORLD_HEIGHT*5/6, 11.5f, 24, 5, 10, 2, 12.8f, 30, 0.8f, enemyShipTextureRegion, enemyBulletTextureRegion);
        batch = new SpriteBatch();
    }

    @Override
    public void render(float deltaTime) {
        batch.begin();
        detectInput(deltaTime);
        playerShip.update(deltaTime);
        enemyShip.update(deltaTime);
        renderBackground(deltaTime);
        enemyShip.draw(batch);
        playerShip.draw(batch);
        renderBullets(deltaTime);
        renderEnemyShip(deltaTime);
        detectCollision();
        batch.end();
    }

    private void renderBackground(float deltaTime) {
        backgroundOffsets[0] += deltaTime * backgroundMaxScrollingSpeed / 8;
        backgroundOffsets[1] += deltaTime * backgroundMaxScrollingSpeed / 4;
        backgroundOffsets[2] += deltaTime * backgroundMaxScrollingSpeed / 2;
        backgroundOffsets[3] += deltaTime * backgroundMaxScrollingSpeed;

        for (int layer = 0; layer < backgroundOffsets.length; layer++) {
            if (backgroundOffsets[layer] > WORLD_HEIGHT) {
                backgroundOffsets[layer] = 0;
            }
            batch.draw(backgrounds[layer], 0, -backgroundOffsets[layer], WORLD_WIDTH, WORLD_HEIGHT);
            batch.draw(backgrounds[layer], 0, -backgroundOffsets[layer] + WORLD_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT);
        }
    }

    private void renderBullets(float deltaTime) {
        if(playerShip.canFireBullet()) {
            Bullet[] bullets = playerShip.fireBullets();
            Collections.addAll(playerBullets, bullets);
        }
        if(enemyShip.canFireBullet()) {
            Bullet[] bullets = enemyShip.fireBullets();
            Collections.addAll(enemyBullets, bullets);
        }

        ListIterator<Bullet> iterator = playerBullets.listIterator();
        while(iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.draw(batch);
            bullet.boundingBox.y += bullet.movementSpeed * deltaTime;
            if(bullet.boundingBox.y > WORLD_HEIGHT) {
                iterator.remove();
            }
        }
        iterator = enemyBullets.listIterator();
        while(iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.draw(batch);
            bullet.boundingBox.y -= bullet.movementSpeed * deltaTime;
            if(bullet.boundingBox.y + bullet.boundingBox.height < 0) {
                iterator.remove();
            }
        }
    }

    private void detectCollision() {
        ListIterator<Bullet> iterator = playerBullets.listIterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            if (enemyShip.intersects(bullet.boundingBox)) {
                enemyShip.hit(bullet);
                iterator.remove();
            }
        }
        iterator = enemyBullets.listIterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            if (playerShip.intersects(bullet.boundingBox)) {
                playerShip.hit(bullet);
                iterator.remove();
            }
        }
    }

    public void detectInput(float deltaTime) {
        float leftLimit, rightLimit, upLimit, downLimit;
        leftLimit = -playerShip.boundingBox.x;
        downLimit = -playerShip.boundingBox.y;
        rightLimit = WORLD_WIDTH - playerShip.boundingBox.x - playerShip.boundingBox.width;
        upLimit = WORLD_HEIGHT/2 - playerShip.boundingBox.y - playerShip.boundingBox.height;

        if(Gdx.input.isTouched()) {
            float xTouch = Gdx.input.getX();
            float yTouch = Gdx.input.getY();

            Vector2 touchPoint = new Vector2(xTouch, yTouch);
            touchPoint = viewport.unproject(touchPoint);

            Vector2 playerShipCentre = new Vector2(playerShip.boundingBox.x + playerShip.boundingBox.width/2,
                                                playerShip.boundingBox.y + playerShip.boundingBox.height/2);

            float touchDistance = touchPoint.dst(playerShipCentre);
            if(touchDistance > TOUCH_MOVEMENT_THRESHOLD) {
                float xTouchDifference = touchPoint.x - playerShipCentre.x;
                float yTouchDifference = touchPoint.y - playerShipCentre.y;
                float xMove = xTouchDifference / touchDistance * playerShip.movementSpeed * deltaTime;
                float yMove = yTouchDifference / touchDistance * playerShip.movementSpeed * deltaTime;
                if(xMove > 0) xMove = Math.min(xMove, rightLimit);
                else xMove = Math.max(xMove, leftLimit);
                if(yMove > 0) yMove = Math.min(yMove, upLimit);
                else yMove = Math.max(yMove, downLimit);
                playerShip.translate(xMove, yMove);
            }
        }
    }

    public void renderEnemyShip(float deltaTime) {
        enemyShip.boundingBox.setPosition(enemyShip.boundingBox.x, enemyShip.boundingBox.y - enemyShip.movementSpeed * deltaTime);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    @Override
    public void dispose() {

    }
}
