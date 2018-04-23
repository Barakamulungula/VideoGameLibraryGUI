package com.example.barakamulungula.videogamelibrary;

import android.content.Context;

public interface AdapterCallback {
    Context getContext();

    void rowClicked(VideoGame videoGame);

    void rowLongClicked(VideoGame videoGame);
}
