package com.mycompany.restaurant.utils;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * SceneManager - Centralized scene navigation with smooth transitions
 */
public class SceneManager {

    private static Stage primaryStage;
    private static Scene currentScene;

    /**
     * Initialize the scene manager with primary stage
     */
    public static void initialize(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Switch to a new scene with fade transition
     */
    public static void switchScene(Scene newScene) {
        switchScene(newScene, TransitionType.FADE);
    }

    /**
     * Switch to a new scene with specified transition
     */
    public static void switchScene(Scene newScene, TransitionType transitionType) {
        if (currentScene == null) {
            // First scene, no transition
            primaryStage.setScene(newScene);
            currentScene = newScene;
            return;
        }

        switch (transitionType) {
            case FADE:
                fadeTransition(newScene);
                break;
            case SLIDE_LEFT:
                slideTransition(newScene, -primaryStage.getWidth(), 0);
                break;
            case SLIDE_RIGHT:
                slideTransition(newScene, primaryStage.getWidth(), 0);
                break;
            case NONE:
                primaryStage.setScene(newScene);
                currentScene = newScene;
                break;
        }
    }

    /**
     * Fade transition between scenes
     */
    private static void fadeTransition(Scene newScene) {
        Node oldRoot = currentScene.getRoot();

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), oldRoot);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(e -> {
            primaryStage.setScene(newScene);
            currentScene = newScene;

            Node newRoot = newScene.getRoot();
            newRoot.setOpacity(0.0);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), newRoot);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
    }

    /**
     * Slide transition between scenes
     */
    private static void slideTransition(Scene newScene, double fromX, double fromY) {
        primaryStage.setScene(newScene);
        currentScene = newScene;

        Node newRoot = newScene.getRoot();
        newRoot.setTranslateX(fromX);
        newRoot.setTranslateY(fromY);

        TranslateTransition slide = new TranslateTransition(Duration.millis(400), newRoot);
        slide.setToX(0);
        slide.setToY(0);
        slide.setInterpolator(Interpolator.EASE_OUT);
        slide.play();
    }

    /**
     * Get the primary stage
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Get current scene
     */
    public static Scene getCurrentScene() {
        return currentScene;
    }

    /**
     * Load stylesheet to a scene
     */
    public static void applyStylesheet(Scene scene) {
        scene.getStylesheets().add(
                SceneManager.class.getResource("/styles/application.css").toExternalForm());
    }

    /**
     * Transition types
     */
    public enum TransitionType {
        FADE,
        SLIDE_LEFT,
        SLIDE_RIGHT,
        NONE
    }
}
