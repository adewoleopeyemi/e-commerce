package com.foodies.amatfoodies.Chat_Module.ViewHolders;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.foodies.amatfoodies.Chat_Module.ChatAdapter;
import com.foodies.amatfoodies.Chat_Module.ChatModel;
import com.foodies.amatfoodies.R;


public  class ChatAudio_viewholder extends RecyclerView.ViewHolder{

    public TextView datetxt,message_seen,total_time ,msg_date;

    public ProgressBar p_bar;
    public SeekBar seekBar;
    private LinearLayout audio_bubble;
    public ImageView play_btn;
    private View view;

    public ChatAudio_viewholder(View itemView) {
        super(itemView);
        view = itemView;

        audio_bubble=view.findViewById(R.id.audio_bubble);
        datetxt=view.findViewById(R.id.datetxt);
        message_seen=view.findViewById(R.id.message_seen);
        p_bar=view.findViewById(R.id.p_bar);
        msg_date=view.findViewById(R.id.msg_date);
        play_btn = view.findViewById(R.id.play_btn);
        this.seekBar=(SeekBar) view.findViewById(R.id.seek_bar);
        this.total_time=(TextView)view.findViewById(R.id.total_time);

    }





    public void bind(final int pos,final ChatModel item,
                     final ChatAdapter.OnItemClickListener listener,
                     final ChatAdapter.OnLongClickListener long_listener) {

        audio_bubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(pos,item,v);
            }
        });

        audio_bubble.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                long_listener.onLongclick(item,v);
                return false;
            }
        });

        seekBar.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }




}

