package com.masi.decorator;

public class ShadowDecorator implements ShapeDecorator {
    private Shape shape;

    public ShadowDecorator(Shape shape) {
        this.shape = shape;
    }

    @Override
    public void draw() {
        shape.draw();
        System.out.println("Adding shadow to shape");
    }

    @Override
    public Shape applyDecorator(ShapeDecorator decorator) {
        return null;
    }

    @Override
    public Shape getShape() {
        return shape;
    }
}
