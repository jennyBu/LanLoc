package com.example.usi.lanloc;

import java.util.Observable;

/**
 * Created by Jennifer Busta on 13.12.17.
 * Source: https://gist.github.com/alexfu/5797429
 */

public class FragmentObserver extends Observable {
    @Override
    public void notifyObservers() {
        setChanged(); // Set the changed flag to true, otherwise observers won't be notified.
        super.notifyObservers();
    }
}
