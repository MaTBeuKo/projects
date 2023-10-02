package net.automator;

import net.automator.Events.IOEvent;

import java.io.Serializable;
import java.util.ArrayList;

public class Macros implements Serializable {

    private String name;
    private ArrayList<IOEvent> events;
    private double startX;
    private double startY;
    private double startZ;
    private double endX;
    private double endY;
    private double endZ;
    private float startYaw;
    private float startPitch;
    private float endYaw;
    private float endPitch;

    @Override
    public String toString() {
        return "Macros{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEvents(ArrayList<IOEvent> events) {
        this.events = events;
    }

    public ArrayList<IOEvent> getEvents() {
        return events;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public double getStartZ() {
        return startZ;
    }

    public void setStartZ(float startZ) {
        this.startZ = startZ;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(float endX) {
        this.endX = endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(float endY) {
        this.endY = endY;
    }

    public double getEndZ() {
        return endZ;
    }

    public void setEndZ(float endZ) {
        this.endZ = endZ;
    }

    public float getStartYaw() {
        return startYaw;
    }

    public void setStartYaw(float startYaw) {
        this.startYaw = startYaw;
    }

    public float getStartPitch() {
        return startPitch;
    }

    public void setStartPitch(float startPitch) {
        this.startPitch = startPitch;
    }

    public float getEndYaw() {
        return endYaw;
    }

    public void setEndYaw(float endYaw) {
        this.endYaw = endYaw;
    }

    public float getEndPitch() {
        return endPitch;
    }

    public void setEndPitch(float endPitch) {
        this.endPitch = endPitch;
    }




    public Macros(String name, ArrayList<IOEvent> events, double startX, double startY, double startZ, double endX, double endY, double endZ,
                  float startYaw, float startPitch, float endYaw, float endPitch) {
        this.name = name;
        this.events = events;
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.endX = endX;
        this.endY = endY;
        this.endZ = endZ;
        this.startYaw = startYaw;
        this.startPitch = startPitch;
        this.endYaw = endYaw;
        this.endPitch = endPitch;
    }
}
