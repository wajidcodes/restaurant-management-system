package com.mycompany.restaurant.utils;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * AnimationUtils - Reusable animation helpers
 */
public class AnimationUtils {

    /**
     * Fade in animation
     */
    public static void fadeIn(Node node, double duration) {
        node.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.millis(duration), node);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }

    /**
     * Fade in with delay
     */
    public static void fadeInWithDelay(Node node, double duration, double delay) {
        node.setOpacity(0);
        PauseTransition pause = new PauseTransition(Duration.millis(delay));
        pause.setOnFinished(e -> fadeIn(node, duration));
        pause.play();
    }

    /**
     * Slide up animation
     */
    public static void slideUp(Node node, double duration) {
        node.setTranslateY(50);
        node.setOpacity(0);

        TranslateTransition slide = new TranslateTransition(Duration.millis(duration), node);
        slide.setToY(0);

        FadeTransition fade = new FadeTransition(Duration.millis(duration), node);
        fade.setToValue(1.0);

        ParallelTransition parallel = new ParallelTransition(slide, fade);
        parallel.play();
    }

    /**
     * Slide up with delay
     */
    public static void slideUpWithDelay(Node node, double duration, double delay) {
        node.setTranslateY(50);
        node.setOpacity(0);

        PauseTransition pause = new PauseTransition(Duration.millis(delay));
        pause.setOnFinished(e -> slideUp(node, duration));
        pause.play();
    }

    /**
     * Scale pulse effect
     */
    public static void pulse(Node node) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), node);
        scaleUp.setToX(1.1);
        scaleUp.setToY(1.1);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), node);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        SequentialTransition pulse = new SequentialTransition(scaleUp, scaleDown);
        pulse.play();
    }

    /**
     * Shake animation for errors
     */
    public static void shake(Node node) {
        TranslateTransition shake1 = new TranslateTransition(Duration.millis(50), node);
        shake1.setToX(10);

        TranslateTransition shake2 = new TranslateTransition(Duration.millis(50), node);
        shake2.setToX(-10);

        TranslateTransition shake3 = new TranslateTransition(Duration.millis(50), node);
        shake3.setToX(10);

        TranslateTransition shake4 = new TranslateTransition(Duration.millis(50), node);
        shake4.setToX(0);

        SequentialTransition shakeSeq = new SequentialTransition(shake1, shake2, shake3, shake4);
        shakeSeq.play();
    }

    /**
     * Number counting animation
     */
    public static void animateNumber(javafx.scene.control.Label label, int from, int to, double duration) {
        Timeline timeline = new Timeline();
        final int[] current = { from };
        int steps = 30; // Number of animation steps
        int diff = to - from;
        int increment = diff / steps;

        for (int i = 0; i <= steps; i++) {
            final int step = i;
            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis(duration / steps * i),
                    e -> {
                        if (step == steps) {
                            label.setText(String.valueOf(to));
                        } else {
                            current[0] += increment;
                            label.setText(String.valueOf(current[0]));
                        }
                    });
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.play();
    }

    /**
     * Hover scale effect
     */
    public static void addHoverEffect(Node node) {
        node.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), node);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();
        });

        node.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), node);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });
    }

    /**
     * Success checkmark animation
     */
    public static void showSuccessAnimation(Node node) {
        // Scale up and fade in
        node.setScaleX(0);
        node.setScaleY(0);
        node.setOpacity(0);

        ScaleTransition scale = new ScaleTransition(Duration.millis(300), node);
        scale.setToX(1.0);
        scale.setToY(1.0);

        FadeTransition fade = new FadeTransition(Duration.millis(300), node);
        fade.setToValue(1.0);

        ParallelTransition parallel = new ParallelTransition(scale, fade);
        parallel.play();
    }
}
