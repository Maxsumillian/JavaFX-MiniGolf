package com.example.golf;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ModeManager {


    //this mode manager is a way for my controller classes to communicate to each other through a manager. with an enum
    // one controller is able to inform teh othee thta the mode has changed and when it is changes it causes all the apporpriate thigns to be updated as well
    public enum Mode {
        PLAY, EDIT
    }

    private final ObjectProperty<Mode> mode = new SimpleObjectProperty<>(Mode.PLAY);

    public Mode getMode() {
        return mode.get();
    }

    public void setMode(Mode newMode) {
        mode.set(newMode);
    }

    public ObjectProperty<Mode> modeProperty() {
        return mode;
    }
}
