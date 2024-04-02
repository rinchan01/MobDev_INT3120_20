package com.example.planegame.gameplay;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.example.planegame.GameResultActivity;
import com.example.planegame.SettingsActivity;
import com.example.planegame.ShopActivity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Locale;

public class GameScreen implements Screen {
    private Camera camera;
    private Viewport viewport;
    private Context parentContext;
    private SpriteBatch batch;
    public TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("game_assets/game_assets.atlas"));
    private TextureRegion[] backgrounds = new TextureRegion[4];
    private TextureRegion playerShipTextureRegion, enemyShipTextureRegion, enemyBulletTextureRegion, playerBulletTextureRegion, enemyShip2TextureRegion, enemyBullet2TextureRegion;
    private float[] backgroundOffsets = {0, 0, 0, 0};
    private float backgroundMaxScrollingSpeed;
    private final int WORLD_WIDTH = 72;
    private final int WORLD_HEIGHT = 128;
    private boolean playSoundEffect = SettingsActivity.Companion.getPlaySoundEffect();
    private int score = 0;
    private int maxEnemies = SettingsActivity.Companion.getHardMode();
    private final float TOUCH_MOVEMENT_THRESHOLD = 0.5f;
    private float timeBetweenEnemySpawns = 2f;
    private float enemySpawnTimer = 0, enemySpawnTimer2 = 0;
    private Sound explosionSound;
    BitmapFont font;
    private PlayerShip playerShip;
    private LinkedList<EnemyShip> enemyShips = new LinkedList<>();
    private LinkedList<Bullet> playerBullets = new LinkedList<>();
    private LinkedList<Bullet> enemyBullets = new LinkedList<>();
    private LinkedList<Explosion> explosions = new LinkedList<>();

    GameScreen(Context context) {
        System.out.println("GameScreen created!");
        camera = new OrthographicCamera();
        this.parentContext = context;
        System.out.println("Address:" + parentContext);
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion_1.wav"));

        backgrounds[0] = textureAtlas.findRegion("Starscape00");
        backgrounds[1] = textureAtlas.findRegion("Starscape01");
        backgrounds[2] = textureAtlas.findRegion("Starscape02");
        backgrounds[3] = textureAtlas.findRegion("Starscape03");

        backgroundMaxScrollingSpeed = (float) (WORLD_HEIGHT) / 4;

        int currentSkinIdx = ShopActivity.Companion.getSkinIdx() + 1;
        playerShipTextureRegion = textureAtlas.findRegion("ship" + currentSkinIdx);
        playerBulletTextureRegion = textureAtlas.findRegion("bullet6");

        enemyShipTextureRegion = textureAtlas.findRegion("ship4");
        enemyBulletTextureRegion = textureAtlas.findRegion("bullet5");
        enemyShip2TextureRegion = textureAtlas.findRegion("ship1");
        enemyBullet2TextureRegion = textureAtlas.findRegion("shottype2");
        enemyShip2TextureRegion.flip(false, true);
        enemyShipTextureRegion.flip(false, true);

        playerShip = new PlayerShip(WORLD_WIDTH / 2, WORLD_HEIGHT/4, 15, 23.4f, 30, 20, 2, 12.8f, 40, 0.5f, playerShipTextureRegion, playerBulletTextureRegion);
        batch = new SpriteBatch();
        prepareHUD();
    }

    @Override
    public void render(float deltaTime) {
        batch.begin();
        detectInput(deltaTime);
        playerShip.update(deltaTime);
        renderBackground(deltaTime);
        spawnEnemies(deltaTime);
        for (EnemyShip enemyShip : enemyShips) {
            enemyShip.update(deltaTime);
            moveEnemyShips(enemyShip, deltaTime);
            enemyShip.draw(batch);
        }
        playerShip.draw(batch);
        renderBullets(deltaTime);
        detectCollision();
        renderExplosions(deltaTime);
        renderHUD();
        checkGameOver();
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
        for (EnemyShip enemyShip : enemyShips) {
            if (enemyShip.canFireBullet()) {
                Bullet[] bullets = enemyShip.fireBullets();
                Collections.addAll(enemyBullets, bullets);
            }
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
            if(bullet.type == 2 || bullet.type == 3) {
                int xMovementSpeed = (int) bullet.movementSpeed - 10;
                if(bullet.type == 2) {
                    bullet.boundingBox.x += xMovementSpeed * deltaTime;
                    if(bullet.boundingBox.x + bullet.boundingBox.width >= WORLD_WIDTH) bullet.type = 3;
                }else{
                    bullet.boundingBox.x -= xMovementSpeed * deltaTime;
                    if(bullet.boundingBox.x <= 0) bullet.type = 2;
                }
            }
            if(bullet.boundingBox.y + bullet.boundingBox.height < 0) {
                iterator.remove();
            }
        }
    }

    private void detectCollision() {
        ListIterator<Bullet> iterator = playerBullets.listIterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            ListIterator<EnemyShip> enemyShipListIterator = enemyShips.listIterator();
            while(enemyShipListIterator.hasNext()) {
                EnemyShip enemyShip = enemyShipListIterator.next();
                if (enemyShip.intersects(bullet.boundingBox)) {
                    enemyShip.hit(bullet);
                    if (enemyShip.health <= 0) {
                        Rectangle temp = enemyShip.boundingBox;
                        score += 100;
                        explosions.add(new Explosion(new Rectangle(temp.x - temp.width * 0.4f, temp.y + temp.height * 0.4f, 20, 20), 0.75f, textureAtlas));
                        playExplosionSound();
                        enemyShipListIterator.remove();
                    }
                    iterator.remove();
                }
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
        ListIterator<EnemyShip> enemyShipIterator = enemyShips.listIterator();
        while (enemyShipIterator.hasNext()) {
            EnemyShip enemyShip = enemyShipIterator.next();
            if (playerShip.intersects(enemyShip.boundingBox) || (enemyShip.boundingBox.y <= 0)) {
                Rectangle temp = enemyShip.boundingBox;
                explosions.add(new Explosion(new Rectangle(temp.x - temp.width * 0.4f, temp.y + temp.height * 0.4f, 20, 20), 0.75f, textureAtlas));
                enemyShipIterator.remove();
                playerShip.health -= 5;
            }
        }
    }

    private void renderExplosions(float deltaTime) {
        ListIterator<Explosion> iterator = explosions.listIterator();
        while(iterator.hasNext()) {
            Explosion explosion = iterator.next();
            explosion.update(deltaTime);
            if(explosion.isFinished()) {
                iterator.remove();
            } else {
                explosion.draw(batch);
            }
        }
    }

    private void renderHUD() {
        font.draw(batch, String.format(Locale.getDefault(), "%05d", score), (float) (WORLD_WIDTH * 2) /3, WORLD_HEIGHT - 1, (float) (WORLD_WIDTH) /3, Align.right, false);
        font.draw(batch, String.format(Locale.getDefault(), "%02d", playerShip.health), 0.5f, WORLD_HEIGHT - 1, (float) (WORLD_WIDTH) /3, Align.left, false);
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

        if(playerShip.boundingBox.x >= 0 && playerShip.boundingBox.x <= WORLD_WIDTH - playerShip.boundingBox.width
                && playerShip.boundingBox.y >= 0 && playerShip.boundingBox.y <= WORLD_HEIGHT - playerShip.boundingBox.height) {
            playerShip.translate(AndroidLauncher.xchange, AndroidLauncher.ychange);
        } else {
            float xChange = AndroidLauncher.xchange, yChange = AndroidLauncher.ychange;
            if(playerShip.boundingBox.x < 0)
                xChange = AndroidLauncher.xchange > 0 ? AndroidLauncher.xchange : 0;
            if(playerShip.boundingBox.x > WORLD_WIDTH - playerShip.boundingBox.width)
                xChange = AndroidLauncher.xchange < 0 ? AndroidLauncher.xchange : 0;
            if(playerShip.boundingBox.y < 0)
                yChange = AndroidLauncher.ychange > 0 ? AndroidLauncher.ychange : 0;
            if(playerShip.boundingBox.y > WORLD_HEIGHT - playerShip.boundingBox.height)
                yChange = AndroidLauncher.ychange < 0 ? AndroidLauncher.ychange : 0;
            playerShip.translate(xChange, yChange);
        }
    }

    private void spawnEnemies(float deltaTime) {
        enemySpawnTimer += deltaTime;
        enemySpawnTimer2 += deltaTime;
        int maxEnemyShip2 = maxEnemies - 2, timeBetweenEnemy2Spawns = (int) timeBetweenEnemySpawns + 1, enemy1 = 0, enemy2 = 0;
        for(EnemyShip enemyShip : enemyShips) {
            if(enemyShip.type == 1) enemy1++;
            else enemy2++;
        }
        if(enemySpawnTimer > timeBetweenEnemySpawns && enemy1 <= maxEnemies){
            enemyShips.add(new EnemyShip((WORLD_WIDTH-11.5f) * (float)Math.random(), WORLD_HEIGHT + 12, 11.5f, 24, 5, 10, 2, 12.8f, 30, 0.8f, enemyShipTextureRegion, enemyBulletTextureRegion, 1));
            enemySpawnTimer = 0;
        }
        if(maxEnemies == 3 || maxEnemies == 4) {
            if(enemySpawnTimer2 > timeBetweenEnemy2Spawns && enemy2 < maxEnemyShip2) {
                enemyShips.add(new EnemyShip((WORLD_WIDTH - 11.5f) * (float) Math.random(), WORLD_HEIGHT + 12, 11.5f, 24, 5, 10, 4, 4, 30, 1f, enemyShip2TextureRegion, enemyBullet2TextureRegion, 2));
                enemySpawnTimer2 = 0;
            }
        }
    }

    public void moveEnemyShips(EnemyShip enemyShip, float deltaTime) {
        enemyShip.boundingBox.setPosition(enemyShip.boundingBox.x, enemyShip.boundingBox.y - enemyShip.movementSpeed * deltaTime);
    }

    private void prepareHUD() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Honk.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 72;
        parameter.color = new Color(39, 119, 245, 0.7f);
        font = generator.generateFont(parameter);
        font.getData().setScale(0.15f);
        generator.dispose();
    }

    private void checkGameOver() {
        if(playerShip.health <= 0) {
            hide();
            Intent intent = new Intent(this.parentContext, GameResultActivity.class);
            intent.putExtra("score", score);
            this.parentContext.startActivity(intent);
        }
    }

    public void playExplosionSound() {
        if(playSoundEffect) {
            long id = explosionSound.play(0.5f);
            explosionSound.setPitch(id, 2);
            explosionSound.setLooping(id, false);
        }
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
        dispose();
    }

    @Override
    public void show() {

    }

    @Override
    public void dispose() {
        textureAtlas.dispose();
        batch.dispose();
        explosionSound.dispose();
        font.dispose();
    }
}
