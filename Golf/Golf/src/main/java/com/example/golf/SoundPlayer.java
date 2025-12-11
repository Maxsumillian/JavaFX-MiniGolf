package com.example.golf;

import javafx.scene.media.AudioClip;


public class SoundPlayer {
    private static final AudioClip waterSound =
            new AudioClip(SoundPlayer.class.getResource("/sounds/WaterSplash.wav").toString());

    private static final AudioClip hitCanvas1 =
            new AudioClip(SoundPlayer.class.getResource("/sounds/HitCanvas1.wav").toString());

    private static final AudioClip hitCanvas2 =
            new AudioClip(SoundPlayer.class.getResource("/sounds/HitCanvas2.wav").toString());

    private static final AudioClip HitWall =
            new AudioClip(SoundPlayer.class.getResource("/sounds/Bounce.wav").toString());

    private static final AudioClip Victory =
            new AudioClip(SoundPlayer.class.getResource("/sounds/Victory.wav").toString());

    public static double amp = 1;


    public static void playWaterSplash() {
        waterSound.setVolume(0.5 * amp);
        waterSound.play();
    }

    public static void playHitCanvas1() {
        hitCanvas1.play();
    }

    public static void playHitCanvas2() {
        hitCanvas2.play();
    }

    public static void playHitWall() {
        HitWall.setVolume(0.5 * amp);
        HitWall.play();
    }

    public static void playVictory() {
        Victory.play();
    }


    //Based on velocity of ball it modifies how loud this sound plays
    public static void playHitWall(double volume) {
        HitWall.setVolume(volume * .05 * amp);
        HitWall.play();
    }

    public static void playHitCanvas1(double volume) {
        hitCanvas1.setVolume(volume * .05 * amp);
        hitCanvas1.play();
//        System.out.println(volume * .05 * amp);
//        !!!weird on school computers it all sounds the same,!!!
//        but it may work on other computers not having na audio restrictions
    }

    public static void playHitCanvas2(double volume) {
        hitCanvas2.setVolume(volume * .05 * amp);
        hitCanvas2.play();
    }
}
