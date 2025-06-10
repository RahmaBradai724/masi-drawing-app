package com.masi.decorator;

public class Rectangle implements Shape {
    @Override
    public void draw() {
        System.out.println("Drawing Rectangle");
    }

    @Override
    public Shape applyDecorator(ShapeDecorator decorator) {
        return decorator; // Return the decorated shape
    }
}
