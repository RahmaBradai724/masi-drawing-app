package com.masi.decorator;

public interface Shape {
    void draw();
    Shape applyDecorator(ShapeDecorator decorator);
}
