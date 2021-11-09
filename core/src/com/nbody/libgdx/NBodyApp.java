package com.nbody.libgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class NBodyApp extends ApplicationAdapter implements InputProcessor {
	ShapeRenderer shapeRenderer;
	NBodyManager nBody;
	Box2DManager box2DManager;
	float size = 1f;
	boolean leftClick, rightClick, middleClick;
	Vector2 mouseStart;
	OrthographicCamera camera;
	
	@Override
	public void create () {
		Gdx.input.setInputProcessor(this);
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		shapeRenderer = new ShapeRenderer();

		box2DManager = new Box2DManager();

		nBody = new NBodyManager(0, 0, 0.01f);

		box2DManager.initialize(nBody.getSetupData());
	}

	@Override
	public void render () {
		if (leftClick) ScreenUtils.clear(0, 0, 0, 0);

		nBody.update(Gdx.graphics.getDeltaTime());
		box2DManager.setPhysicsData(nBody.getPhysicsData());
		box2DManager.update(Gdx.graphics.getDeltaTime());
		nBody.setPhysicsData(box2DManager.getPhysicsData());

		shapeRenderer.setProjectionMatrix(camera.combined);

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0, 0.1f);
		shapeRenderer.box(camera.position.x - (Gdx.graphics.getWidth() * camera.zoom) / 2f, camera.position.y - (Gdx.graphics.getHeight() * camera.zoom) / 2f,
				0, Gdx.graphics.getWidth() * camera.zoom, Gdx.graphics.getHeight() * camera.zoom, 1);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		for (float[] position: nBody.getRenderData()) {
			float r = position[2] / 0.4f + 0.3f;
			float g = position[2] / 0.4f + 0.05f;
			float b = position[2] / 0.4f + 0.01f;
			float s = size * position[3] * 2000000f;
			shapeRenderer.setColor(r, g, b, 1);
			shapeRenderer.circle(position[0], position[1], s);
		}
		shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		shapeRenderer.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		switch (button)
		{
			case 0:
				leftClick = true;
				ScreenUtils.clear(0, 0, 0, 0);
				mouseStart = new Vector2(screenX, screenY);
				break;
			case 1:
				rightClick = true;
				break;
			case 2:
				middleClick = true;
				break;
			default:
				return false;
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		switch (button)
		{
			case 0:
				leftClick = false;
				ScreenUtils.clear(0, 0, 0, 0);
				break;
			case 1:
				rightClick = false;
				break;
			case 2:
				middleClick = false;
				break;
			default:
				return false;
		}

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (leftClick)
		{
			camera.translate((mouseStart.x - screenX) * camera.zoom, (screenY - mouseStart.y) * camera.zoom);
			mouseStart = new Vector2(screenX, screenY);
			ScreenUtils.clear(0, 0, 0, 0);
			camera.update();
		}

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		camera.zoom += amountY / 25f;
		ScreenUtils.clear(0, 0, 0, 0);
		camera.update();
		return false;
	}
}
