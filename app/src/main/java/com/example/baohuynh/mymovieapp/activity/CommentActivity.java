package com.example.baohuynh.mymovieapp.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.baohuynh.mymovieapp.R;
import com.example.baohuynh.mymovieapp.adapter.CommentAdapter;
import com.example.baohuynh.mymovieapp.model.Comment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.baohuynh.mymovieapp.activity.PlayTrailer.COMMENT_CHILD;
import static com.example.baohuynh.mymovieapp.activity.PlayTrailer.mAuth;
import static com.example.baohuynh.mymovieapp.activity.PlayTrailer.mDatabase;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEdtInputComment;
    private RecyclerView mRecyclerComment;
    private ArrayList<Comment> mComments = new ArrayList<>();
    private CommentAdapter mCommentAdapter;
    private int idMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initView();
        idMovie = getIntent().getIntExtra(MovieDetail.MOVIE_ID, 1);
        mCommentAdapter = new CommentAdapter(this, mComments);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase.child(COMMENT_CHILD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(String.valueOf(idMovie))) {
                    mDatabase.child(COMMENT_CHILD).child(String.valueOf(idMovie))
                            .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //clear comment list before run for loop
                            mComments.clear();
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                Comment comment = data.getValue(Comment.class);
                                mComments.add(comment);
                                mRecyclerComment.setAdapter(mCommentAdapter);
                                mRecyclerComment.setLayoutManager(new LinearLayoutManager
                                        (getApplicationContext()));
                                mRecyclerComment.setHasFixedSize(true);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_input_comment:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Comment comment = new Comment(Objects.requireNonNull(mAuth.getCurrentUser())
                            .getDisplayName(),
                            mEdtInputComment.getText().toString(),
                            Objects.requireNonNull(mAuth.getCurrentUser().getPhotoUrl()).toString());
                    mDatabase.child(COMMENT_CHILD)
                            .child(String.valueOf(idMovie))
                            .push()
                            .setValue(comment);
                    mComments.add(comment);
                    mCommentAdapter.notifyDataSetChanged();
                    mEdtInputComment.setText("");
                    break;
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        setTitle(getIntent().getStringExtra(MovieDetail.NAME));
        mEdtInputComment = findViewById(R.id.edt_input_comment);
        CircleImageView img = findViewById(R.id.img_input_comment);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Picasso.with(this).load(Objects.requireNonNull(mAuth.getCurrentUser()).getPhotoUrl())
                    .into(img);
        }
        Button btnSend = findViewById(R.id.btn_input_comment);
        btnSend.setOnClickListener(this);
        mRecyclerComment = findViewById(R.id.recycler_input_comment);
        RelativeLayout bottomSheetLayout = findViewById(R.id.bottom_sheet_comment);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mEdtInputComment.setFocusable(true);
    }
}
