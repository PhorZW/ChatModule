package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moc.chatmodule.MessageActivity;
import com.moc.chatmodule.R;

import java.util.List;

import Model.Reader;

public class ReaderAdapter extends RecyclerView.Adapter<ReaderAdapter.ViewHolder> {
    private Context mContext;
    private List<Reader> mReaders;


    public ReaderAdapter(Context mContext, List<Reader> mReaders) {

        this.mReaders = mReaders;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.reader_item, parent, false );
        return new ReaderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Reader reader = mReaders.get(position);
        holder.username.setText(reader.getUsername());

        if (reader.getImageURL().equals("default")){

            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }
        else {

            Glide.with(mContext).load(reader.getImageURL()).into(holder.profile_image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("readerid", reader.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReaders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public ImageView profile_image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);

        }
    }

}
