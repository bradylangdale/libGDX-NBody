package com.nbody.libgdx;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class Box2DManager {

    World world;
    Body body;
    float PPM = 1f;

    public Box2DManager()
    {
        world = new World(new Vector2(0, 0), false);
    }

    public void initialize(ArrayList<float[]> bodies)
    {
        for (float[] nbody: bodies) {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(nbody[0] * PPM, nbody[1] * PPM);
            bodyDef.linearVelocity.set(nbody[2] * PPM, nbody[3] * PPM);
            bodyDef.linearDamping = 0;
            bodyDef.angularDamping = 0;
            bodyDef.fixedRotation = true;

            // Create a body in the world using our definition
            body = world.createBody(bodyDef);

            // Now define the dimensions of the physics shape
            CircleShape shape = new CircleShape();
            shape.setRadius(1f * nbody[4] * 1000000f);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 100;
            fixtureDef.restitution = 1;
            fixtureDef.friction = 0;
            body.createFixture(fixtureDef);
            shape.dispose();
        }
    }

    public void update(float dt)
    {
        world.step(dt, 6, 2);
    }

    public void setPhysicsData(ArrayList<float[]> data)
    {
        Array<Body> bodyArray = new Array<>();
        world.getBodies(bodyArray);

        for (int i = 0; i < world.getBodyCount(); i++)
        {
            bodyArray.get(i).setTransform(data.get(i)[0] * PPM, data.get(i)[1] * PPM, body.getAngle());
            bodyArray.get(i).setLinearVelocity(data.get(i)[2] * PPM, data.get(i)[3] * PPM);
        }
    }

    public ArrayList<float[]> getPhysicsData()
    {
        ArrayList<float[]> data = new ArrayList<float[]>();
        Array<Body> bodyArray = new Array<>();
        world.getBodies(bodyArray);

        for (Body body: bodyArray)
        {
            Vector2 velocity = body.getLinearVelocity();
            Vector2 position = body.getPosition();
            data.add(new float[] { position.x / PPM, position.y / PPM, velocity.x / PPM, velocity.y / PPM});
        }

        return data;
    }

    public void dispose()
    {
        world.dispose();
    }
}
