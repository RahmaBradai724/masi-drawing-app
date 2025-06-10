package com.masi.persistence;

import com.masi.Controller.DrawingController.DrawnShape;
import java.util.List;

public interface DrawingPersistenceStrategy {
    void saveDrawing(String drawingName, List<DrawnShape> shapes);
    List<DrawnShape> loadDrawing(int drawingId);
    List<String> listDrawings();
    int getDrawingIdByName(String drawingName);
}