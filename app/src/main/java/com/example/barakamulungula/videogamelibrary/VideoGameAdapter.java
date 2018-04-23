package com.example.barakamulungula.videogamelibrary;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoGameAdapter extends RecyclerView.Adapter<VideoGameAdapter.ViewHolder> {

    private List<VideoGame> videoGameList;
    private AdapterCallback adapterCallback;

    public VideoGameAdapter(List<VideoGame> videoGameList, AdapterCallback adapterCallback) {
        this.videoGameList = videoGameList;
        this.adapterCallback = adapterCallback;
    }

    @NonNull
    @Override
    public VideoGameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_game, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoGameAdapter.ViewHolder holder, int position) {
        holder.bind(videoGameList.get(position));
        holder.itemView.setOnClickListener(holder.onClick(videoGameList.get(position)));
        holder.itemView.setOnLongClickListener(holder.onLongClick(videoGameList.get(position)));
    }

    @Override
    public int getItemCount() {
        return videoGameList.size();
    }

    public void updateList(List<VideoGame> videoGames) {
        this.videoGameList = videoGames;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_row_layout)
        protected ConstraintLayout rowLayout;
        @BindView(R.id.item_title)
        protected TextView gameTitle;
        @BindView(R.id.item_due_date)
        protected TextView gameDate;
        @BindView(R.id.item_genre)
        protected TextView gameGenre;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void bind(VideoGame videoGame) {
            gameTitle.setText(videoGame.getTitle());
            gameGenre.setText(adapterCallback.getContext().getString(R.string.genre, videoGame.getGenre()));
            if (videoGame.isCheckedOut()) {
                //Makes the due date visible
                gameDate.setVisibility(View.VISIBLE);
                //Show the day the game was checked out on
                videoGame.setDueDate(new Date());
                //Change background color to red
                rowLayout.setBackgroundResource(R.color.red);
                //Calculate check back in date
                int numberOfDays = 14;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(videoGame.getDueDate());
                calendar.add(Calendar.DAY_OF_YEAR, numberOfDays);
                Date date = calendar.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                gameDate.setText(adapterCallback.getContext().getString(R.string.due_date, formatter.format(date)));
            } else {
                gameDate.setVisibility(View.INVISIBLE);
                rowLayout.setBackgroundResource(R.color.green);
            }
        }

        public View.OnClickListener onClick(final VideoGame videoGame) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterCallback.rowClicked(videoGame);
                }
            };
        }

        public View.OnLongClickListener onLongClick(final VideoGame videoGame) {
            return new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    adapterCallback.rowLongClicked(videoGame);
                    return true;
                }
            };
        }


    }
}
