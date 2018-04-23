package com.example.barakamulungula.videogamelibrary;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements AdapterCallback, AddGameFragment.ActivityCallBack {

    private VideoGameDatabase videoGameDatabase;
    private VideoGameAdapter videoGameAdapter;
    private AddGameFragment addGameFragment;

    @BindView(R.id.game_view)
    protected RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        videoGameDatabase = ((VideoGameApplication) getApplicationContext()).getDatabase();
        setUpRecyclerView();


    }

    @OnClick(R.id.add_game_button)
    protected void addVideoGame() {
        addGameFragment = AddGameFragment.newInstance();
        addGameFragment.attachParent(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, addGameFragment).commit();

    }

    public void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        videoGameAdapter = new VideoGameAdapter(videoGameDatabase.videoGameDao().getVideoGames(), this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(videoGameAdapter);
        videoGameAdapter.notifyDataSetChanged();


    }

    private void checkGameOut(final VideoGame videoGame) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Checkout Game?")
                .setMessage("Are you sure you would like to check out this game?")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        videoGame.setCheckedOut(true);
                        //Update Database record for this game
                        videoGameDatabase.videoGameDao().updateVideoGame(videoGame);
                        //tell our adapter that the database has been updated so it will update our view.
                        videoGameAdapter.updateList(videoGameDatabase.videoGameDao().getVideoGames());
                        //Tell users that this game has been checked out
                        Toast.makeText(MainActivity.this, "Game Checked Out!", Toast.LENGTH_LONG).show();

                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }

    private void checkGameBackIn(final VideoGame videoGame) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.check_in_game)
                .setMessage(R.string.check_in_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        videoGame.setCheckedOut(false);
                        //Update database with updated game information
                        videoGameDatabase.videoGameDao().updateVideoGame(videoGame);
                        //Let our adapter know that information in the database has changed to update our view accordingly
                        videoGameAdapter.updateList(videoGameDatabase.videoGameDao().getVideoGames());

                        Toast.makeText(MainActivity.this, R.string.game_checked_in, Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void rowClicked(VideoGame videoGame) {
        if (videoGame.isCheckedOut()) {
            checkGameBackIn(videoGame);
        } else {
            checkGameOut(videoGame);
        }
    }

    @Override
    public void rowLongClicked(final VideoGame videoGame) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Game?")
                .setMessage("Are you sure you would like to delete this game?")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        videoGameDatabase.videoGameDao().deleteVideoGame(videoGame);
                        videoGameAdapter.updateList(videoGameDatabase.videoGameDao().getVideoGames());
                        Toast.makeText(MainActivity.this, "Game Deleted!", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void addClicked() {
        getSupportFragmentManager().beginTransaction().remove(addGameFragment).commit();
        videoGameAdapter.updateList(videoGameDatabase.videoGameDao().getVideoGames());
    }
}
