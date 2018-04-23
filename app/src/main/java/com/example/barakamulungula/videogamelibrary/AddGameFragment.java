package com.example.barakamulungula.videogamelibrary;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddGameFragment extends Fragment {

    private VideoGameDatabase videoGameDatabase;
    private ActivityCallBack activityCallBack;
    @BindView(R.id.game_title)
    protected EditText gameTitle;
    @BindView(R.id.game_genre)
    protected EditText gameGenre;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_add_game, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public static AddGameFragment newInstance() {

        Bundle args = new Bundle();

        AddGameFragment fragment = new AddGameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        videoGameDatabase = ((VideoGameApplication) Objects.requireNonNull(getActivity()).getApplicationContext()).getDatabase();
    }

    private void addGameToDatabase(final VideoGame videoGame) {
        videoGameDatabase.videoGameDao().addVideoGame(videoGame);
        activityCallBack.addClicked();
    }


    @OnClick(R.id.save_game_button)
    protected void saveGame(){
        if(gameTitle.getText().toString().isEmpty() || gameGenre.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "All Fields are required", Toast.LENGTH_LONG).show();
        } else {
            VideoGame videoGame = new VideoGame(gameTitle.getText().toString(), gameGenre.getText().toString(), new Date());

            addGameToDatabase(videoGame);
            Toast.makeText(getActivity(), "Game Added!!", Toast.LENGTH_LONG).show();

        }
    }

    public void attachParent(ActivityCallBack activityCallBack){
        this.activityCallBack = activityCallBack;
    }

    public interface ActivityCallBack{
        void addClicked();
    }
}
