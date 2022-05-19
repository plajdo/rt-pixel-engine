package sk.bytecode.bludisko.rt.game.window.screens.components;

import sk.bytecode.bludisko.rt.game.util.NullSafe;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class View {

    private final float maximumDragDistanceFromOriginalTouchDown = 40f;
    private Point touchLocation;

    protected Rectangle frame;
    protected boolean isVisible = true;

    protected WeakReference<View> superview;
    protected ArrayList<View> subviews = new ArrayList<>();

    // MARK: - Appearance

    public void draw(Graphics graphics) {
        if(isVisible) { subviews.forEach(view -> view.draw(graphics)); }
    }

    // MARK: - Touch handling

    public void touchesBegan(Point point) {
        touchLocation = point;
        subviews.forEach(subview -> {
            if(subview.frame.contains(point)) {
                subview.touchesBegan(point);
            }
        });
    }

    public void touchesEnded(Point point) {
        NullSafe.accept(touchLocation, touchPoint -> {
            if(point.distance(touchLocation) < maximumDragDistanceFromOriginalTouchDown) {
                subviews.forEach(subview -> {
                    if(subview.frame.contains(touchLocation)) {
                        subview.touchesEnded(point);
                    }
                });
            } else {
                touchesCancelled(point);
            }
        });
    }

    public void touchesCancelled(Point point) {
        NullSafe.accept(touchLocation, touchPoint -> {
            if(point.distance(touchLocation) >= maximumDragDistanceFromOriginalTouchDown) {
                subviews.forEach(subview -> subview.touchesCancelled(point));
            }
        });
    }

    // MARK: - Subview manipulation

    public void addSubview(View view) {
        view.superview = new WeakReference<>(this);
        subviews.add(view);
    }

    public void removeFromSuperview() {
        NullSafe.acceptWeak(superview, this::removeFromSubviews);
    }

    private void removeFromSubviews(View view) {
        subviews.remove(view);
    }

    // MARK: - Getters and Setters

    public Rectangle getFrame() {
        return new Rectangle(frame.x, frame.y, frame.width, frame.height);
    }

    public void setFrame(Rectangle frame) {
        this.frame = frame;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

}
