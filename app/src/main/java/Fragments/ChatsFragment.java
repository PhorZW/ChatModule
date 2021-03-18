package Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moc.chatmodule.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.ReaderAdapter;
import Model.Chat;
import Model.Reader;


public class ChatsFragment extends Fragment {
    private RecyclerView recyclerView;

    private ReaderAdapter readerAdapter;

    private List<Reader> mReaders;

    FirebaseUser fuser;
    DatabaseReference reference;



    private List<String> readersList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        // Inflate the layout for this fragment

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        readersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                readersList.clear();

                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                  Chat chat = snapshot1.getValue(Chat.class);

                  if (chat.getSender().equals(fuser.getUid())) {
                      readersList.add(chat.getReceiver());
                  }
                  if (chat.getReceiver().equals(fuser.getUid())) {
                      readersList.add(chat.getSender());
                  }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    private void readChats() {
        mReaders = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Meter Readers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mReaders.clear();

                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    Reader reader = snapshot1.getValue(Reader.class);
                    //display users with chats
                    for (String id: readersList) {
                        if (reader.getId().equals(id)) {
                            if (mReaders.size() != 0) {
                                for (Reader reader1 :mReaders) {
                                    if (!reader.getId().equals(reader1.getId())) {
                                        mReaders.add(reader);
                                    }
                                }

                            }else {
                                mReaders.add(reader);
                            }

                        }
                    }
                }
                readerAdapter = new ReaderAdapter(getContext(), mReaders);
                recyclerView.setAdapter(readerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}