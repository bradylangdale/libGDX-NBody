package com.nbody.libgdx;

import java.util.ArrayList;
import java.lang.Math;
import java.util.Random;

public class NBodyManager {
    private float centerX, centerY;
    private float scale = 5;
    ArrayList<Body> bodies;
    Random ran;

    public NBodyManager(float centerX, float centerY, float scale) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.scale = scale;

        bodies = new ArrayList<Body>();
        Random ran = new Random();

        for (int i = 0; i < 3000; i++)
        {
            bodies.add(new Body(20 * (ran.nextFloat() - 0.5f),
                    20 * (ran.nextFloat() - 0.5f),
                    0.15f * (ran.nextFloat() - 0.5f),
                    0.15f * (ran.nextFloat() - 0.5f),
                    0.0000010f));
        }
    }

    public void update(float dt) {

        for (Body body1: bodies)
        {
            for (Body body2: bodies)
            {
                if (body1 == body2) continue;

                float dx = body1.posX - body2.posX;
                float dy = body1.posY - body2.posY;
                float dist = (float)Math.sqrt((dx * dx) + (dy * dy));

                if (dist <= 0.2f) continue;

                float f = body2.mass / (float)Math.pow(dist, 2);
                body1.velX -= dx / dist * f;
                body1.velY -= dy / dist * f;
            }
        }

        for (Body body: bodies)
        {
            body.posX += body.velX * dt;
            body.posY += body.velY * dt;
        }
    }

    public ArrayList<float[]> getRenderData()
    {
        ArrayList<float[]> data = new ArrayList<float[]>();
        for (Body body: bodies)
        {
            data.add(new float[] { body.posX / scale + centerX, body.posY / scale + centerY,
                    (float)Math.sqrt(Math.pow(body.velX, 2) + Math.pow(body.velY, 2)), body.mass});
        }
        return data;
    }

    public ArrayList<float[]> getSetupData()
    {
        ArrayList<float[]> data = new ArrayList<float[]>();
        for (Body body: bodies)
        {
            data.add(new float[] { body.posX / scale + centerX, body.posY / scale + centerY, body.velX, body.velY,
                    body.mass});
        }
        return data;
    }

    public ArrayList<float[]> getPhysicsData()
    {
        ArrayList<float[]> data = new ArrayList<float[]>();
        for (Body body: bodies)
        {
            data.add(new float[] { body.posX / scale + centerX, body.posY / scale + centerY, body.velX, body.velY});
        }
        return data;
    }

    public void setPhysicsData(ArrayList<float[]> data)
    {
        for (int i = 0; i < data.size(); i++)
        {
            bodies.get(i).posX = (data.get(i)[0] - centerX) * scale;
            bodies.get(i).posY = (data.get(i)[1] - centerY) * scale;
            bodies.get(i).velX = data.get(i)[2];
            bodies.get(i).velY = data.get(i)[3];
        }
    }

    public static class Body
    {
        public float posX, posY, velX, velY, mass;

        public Body(float posX, float posY, float velX, float velY, float mass)
        {
            this.posX = posX;
            this.posY = posY;
            this.velX = velX;
            this.velY = velY;
            this.mass = mass;
        }
    }
}
