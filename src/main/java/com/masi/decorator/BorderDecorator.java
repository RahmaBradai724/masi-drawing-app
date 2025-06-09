package com.masi.decorator;

public class BorderDecorator implements ShapeDecorator {
    private Shape shape;

    public BorderDecorator(Shape shape) {
        this.shape = shape;
    }

    @Override
    public void draw() {
        shape.draw();
        System.out.println("Adding border to shape");
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
