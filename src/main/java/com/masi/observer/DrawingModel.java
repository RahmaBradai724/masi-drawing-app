package com.masi.observer;
// DrawingModel.java
import java.util.ArrayList;
import java.util.List;

public class DrawingModel implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private String selectedShape = "None";

    public void setSelectedShape(String shape) {
        this.selectedShape = shape;
        notifyObservers();
    }

    public String getSelectedShape() {
        return selectedShape;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
