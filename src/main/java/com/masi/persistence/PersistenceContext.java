package com.masi.persistence;

import com.masi.Controller.DrawingController.DrawnShape;
import java.util.List;

public class PersistenceContext {
    private DrawingPersistenceStrategy strategy;

    public PersistenceContext(DrawingPersistenceStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(DrawingPersistenceStrategy strategy) {
        this.strategy = strategy;
    }

    public void saveDrawing(String drawingName, List<DrawnShape> shapes) {
        strategy.saveDrawing(drawingName, shapes);
    }

    public List<DrawnShape> loadDrawing(int drawingId) {
        return strategy.loadDrawing(drawingId);
    }

    public List<String> listDrawings() {
        return strategy.listDrawings();
    }

    public int getDrawingIdByName(String drawingName) {
        return strategy.getDrawingIdByName(drawingName);
    }
}