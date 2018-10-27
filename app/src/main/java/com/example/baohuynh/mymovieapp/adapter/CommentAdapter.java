package com.example.baohuynh.mymovieapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baohuynh.mymovieapp.R;
import com.example.baohuynh.mymovieapp.model.Comment;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private Context mContext;
    private ArrayList<Comment> mComments;

    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        mContext = context;
        mComments = comments;
    }

    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentAdapter.CommentViewHolder holder, int position) {
        holder.bindView(mContext, mComments.get(position));
    }

    @Override
    public int getItemCount() {
        if (mComments != null) {
            return mComments.size();
        } else {
            return 0;
        }
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView commentImg;
        private TextView tvContent, tvUserName;

        private CommentViewHolder(View itemView) {
            super(itemView);
            commentImg = itemView.findViewById(R.id.img_user_comment);
            tvContent = itemView.findViewById(R.id.tv_comment);
            tvUserName = itemView.findViewById(R.id.tv_username);
        }

        private void bindView(Context context, Comment comment) {
            Picasso.with(context).load(comment.getUserImage()).placeholder(R.drawable.ic_empty_user).into(commentImg);
            tvUserName.setText(comment.getUserName());
            tvContent.setText(comment.getUserConent());
        }
    }
}