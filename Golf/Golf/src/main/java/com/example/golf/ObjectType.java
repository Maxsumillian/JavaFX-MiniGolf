package com.example.golf;


// an enum that dictates the type of behavoir the object will have based on collision
//public enum ObjectType {
//    WALL,//an outwarding collision check
//    FLOOR,//an inside collisionCheck
//    RESET,//On overlap resets the balls position to starign position
//    TRAP,
//    BALL
//}

public enum ObjectType {
    FLOOR(0),//floor is redundant i haven't implemented it yet
    TRAP(1),
    WALL(2),
    RESET(3),
    HOLE(4),
    BALL(5);

    public final int Priority;

    ObjectType(int p) {
        this.Priority = p;
    }
}