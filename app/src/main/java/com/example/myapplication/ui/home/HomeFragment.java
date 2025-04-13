package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.AppDatabase;
import com.example.myapplication.R;
import com.example.myapplication.Subject;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    AppDatabase db;
    List<Subject> list;

    String subjects;
    TextView sub_text;
    FloatingActionButton floatingActionButton;
    private FragmentHomeBinding binding;
    RecyclerView subject_recycler;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        floatingActionButton=binding.floatingButton;

        //implementation starts from here

        db=Room.databaseBuilder(getContext(),AppDatabase.class,"notes-latest").allowMainThreadQueries().fallbackToDestructiveMigrationFrom(1).build();



        list=db.subjectDao().getAllSubjects();
        subjects="";
        for (Subject s:list)
        {
            subjects+="\n"+s.name;
            Log.d("Subject",s.name);
        }

        subject_recycler=binding.subjectRecyclerView;
        subject_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        Subject_adapter subjectAdapter=new Subject_adapter(getContext(),list,db);
        subject_recycler.setAdapter(subjectAdapter);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View dialogView=LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_dialog_subject,null);
                BottomSheetDialog dialog=new BottomSheetDialog(requireContext());
                EditText subjectInput = dialogView.findViewById(R.id.subject_name_input);
                Button submitBtn = dialogView.findViewById(R.id.submit_subject);

                dialog.setContentView(dialogView);
                dialog.show();

                submitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s_name=subjectInput.getText().toString();
                        if((s_name!=null || s_name!="") && s_name.matches("(?i)[a-z0-9]{2,}"))
                        {
                            db.subjectDao().insert(new Subject(s_name));
                            list=db.subjectDao().getAllSubjects();
                            subjectAdapter.updateList(list);
                            dialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Enter valid Subject Name", Toast.LENGTH_SHORT).show();

                        }
                    }
                });



                //Toast.makeText(getContext(), "subject clicked", Toast.LENGTH_SHORT).show();
                /*
                db.subjectDao().insert(new Subject("Ravi"));
                list=db.subjectDao().getAllSubjects();
                subjectAdapter.updateList(list);
                */

            }
        });











        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}