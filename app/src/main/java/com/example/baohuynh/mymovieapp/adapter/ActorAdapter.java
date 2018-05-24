package com.example.baohuynh.mymovieapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.baohuynh.mymovieapp.R;
import com.example.baohuynh.mymovieapp.data.MovieAPI;
import com.example.baohuynh.mymovieapp.handler.CallBackOnClickItem;
import com.example.baohuynh.mymovieapp.model.Actor;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by baohuynh on 29/03/2018.
 */

public class ActorAdapter extends RecyclerView.Adapter<ActorAdapter.ActorHolder> {
    private Context mContext;
    private ArrayList<Actor> mActors;
    private CallBackOnClickItem mOnClickItem;

    public ActorAdapter(Context context, ArrayList<Actor> actors, CallBackOnClickItem onClickItem) {
        mContext = context;
        mActors = actors;
        mOnClickItem = onClickItem;
    }

    @Override
    public ActorAdapter.ActorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.display_actor, parent, false);
        return new ActorHolder(v, mOnClickItem);
    }

    @Override
    public void onBindViewHolder(ActorAdapter.ActorHolder holder, int position) {
        holder.addData(mContext, mActors.get(position));
    }

    @Override
    public int getItemCount() {
        return mActors.size();
    }

    static class ActorHolder extends RecyclerView.ViewHolder {
        private ImageView imgActor;
        private TextView tvName, tvCharacter;
        private CallBackOnClickItem mOnClickItem;

        private ActorHolder(View itemView, CallBackOnClickItem onClickItem) {
            super(itemView);
            imgActor = itemView.findViewById(R.id.img_actor);
            tvName = itemView.findViewById(R.id.tv_name_actor);
            tvCharacter = itemView.findViewById(R.id.tv_character_actor);
            mOnClickItem = onClickItem;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnClickItem.onClick(getAdapterPosition());
                }
            });
        }

        private void addData(Context context, Actor actor) {
            if (actor.getImgActor().equals(MovieAPI.GET_IMAGE_PATH + "null")) {
                Picasso.with(context).load(R.drawable.ic_empty_img).into(imgActor);
            } else {
                Picasso.with(context).load(actor.getImgActor()).into(imgActor);
            }
            tvName.setText(actor.getNameActor());
            tvCharacter.setText(actor.getNameCharacter());
        }
    }
}
