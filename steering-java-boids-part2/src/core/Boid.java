package core;

import java.awt.Color;
import java.util.ArrayList;

import javax.vecmath.Vector2d;

import board.Board;


public class Boid {
	public static final Float MASS = 1f;
	public static final double MAX_SPEED = 2;
	public static final int MAX_ACCELERATION = 2;
	double distance = 10;
	
	private Board board;
	private Vector2d centerPosition;
	private Vector2d velocity;
	private Vector2d steerForce;
	private ArrayList<Vector2d> checkpoints;
	private ArrayList<Line> rays;
	private Vector2d currentCheckpoint;
	private int radius;
	private int currentCheckpointIndex;
	private double distanceToTarget;
	private CollisonCircle collisionCircle;
	
	public Boid(Board b, Vector2d initialPosition, int Radius, ArrayList<Vector2d> checkpoints) {
		board = b;
		centerPosition = initialPosition;
		radius = Radius;
		velocity = new Vector2d();
		steerForce = new Vector2d();
		distanceToTarget = 0d;
		this.checkpoints = checkpoints;
		currentCheckpoint = checkpoints.get(currentCheckpointIndex);
		collisionCircle = new CollisonCircle(centerPosition, Radius);
		rays = new ArrayList<Line>();
	}
	
	public void objectAvoidance() {
		
	}
	
	public void update() {
		ArrayList<Object> objects = board.getObjects();
		// check for collisions:
	    // note: this is an awfully inefficient method, if you ever do a real
	    //       game engine, you need to use a quad-tree or oct-tree to speed
	    //       this up.
		for (int i = 0; i < objects.size(); i++) 
			if (collisionCircle.collision(objects.get(i).getCollisonCircle())) 
				return;
		
		objectAvoidance();
		// acceleration = force / mass
		Vector2d acceleration = new Vector2d();
		if (steerForce.length()>0) {
			steerForce.normalize();
			acceleration.setX(steerForce.getX() / MASS);
			acceleration.setY(steerForce.getY() / MASS);
				
		}
		
		centerPosition.add(velocity);
		velocity.add(acceleration);
		if (velocity.length()>MAX_SPEED) {
			velocity.normalize();
			velocity.scale(MAX_SPEED);
		}
		
		// update the collision circle:
		collisionCircle.move(velocity);
	}
	
	public Vector2d getCenterPosition() {
		return centerPosition;
	}
	
	public Vector2d getSteerForce() {
		return (Vector2d)steerForce.clone();
	}
	
	public int getX() {
		return (int) centerPosition.getX();
	}

	public int getY() {
		return (int) centerPosition.getY();
	}

	public int getDistanceToTarget() {
		return (int) distanceToTarget;
	}
	
	public int getRadius() {
		return radius;
	}
	
	public ArrayList<Line> getRays() {
		return rays;
	}
	
	public CollisonCircle getCollisionCircle() {
		return collisionCircle;
	}
}
