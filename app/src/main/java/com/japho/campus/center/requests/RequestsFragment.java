package com.japho.campus.center.requests;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.japho.campus.center.Common.Constants;
import com.japho.campus.center.Common.NodeNames;
import com.japho.campus.center.ProfileActivity;
import com.japho.campus.center.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.japho.campus.center.UserProfileActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {

    private RecyclerView rvRequests;
    private RequestAdapter adapter;
    private List<RequestModel> requestModelList;
    private List<RequestModel> requestModelList2;
    private TextView tvEmptyRequestsList;

    private DatabaseReference databaseReferenceRequests, databaseReferenceUsers;
    private FirebaseUser currentUser;
    private  View progressBar;


    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvRequests = view.findViewById(R.id.rvRequests);
        tvEmptyRequestsList = view.findViewById(R.id.tvEmptyRequestsList);

        progressBar = view.findViewById(R.id.progressBar);

        rvRequests.setLayoutManager( new LinearLayoutManager(getActivity()));
        requestModelList = new ArrayList<>();
        requestModelList2 = new ArrayList<>();
        adapter = new RequestAdapter(getActivity(), requestModelList);
        rvRequests.setAdapter(adapter);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference().child(NodeNames.USERS);

        databaseReferenceRequests = FirebaseDatabase.getInstance().getReference().child(NodeNames.FRIEND_REQUESTS).child(currentUser.getUid());

        tvEmptyRequestsList.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        databaseReferenceRequests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                requestModelList.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.exists()){
                        String requestType = ds.child(NodeNames.REQUEST_TYPE).getValue().toString();
                        if(requestType.equals(Constants.REQUEST_STATUS_RECEIVED))
                        {
                            final String userId= ds.getKey();
                            databaseReferenceUsers.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String userName = dataSnapshot.child(NodeNames.NAME).getValue().toString();

                                    String photoName="";
                                    if(dataSnapshot.child(NodeNames.PHOTO).getValue()!=null)
                                    {
                                        photoName = dataSnapshot.child(NodeNames.PHOTO).getValue().toString();
                                    }

                                    RequestModel requestModel = new RequestModel(userId, userName, photoName);
                                    requestModelList.add(requestModel);
                                    requestModelList2.add(requestModel);
                                    adapter.notifyDataSetChanged();
                                    tvEmptyRequestsList.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getActivity(),  getActivity().getString( R.string.failed_to_fetch_friend_requests, databaseError.getMessage())
                                            , Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),  getActivity().getString( R.string.failed_to_fetch_friend_requests, databaseError.getMessage())
                        , Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });


    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.mnuProfile);
    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(@NonNull MenuItem item) {
            int id = item.getItemId();
            if(id==R.id.mnuProfile)
            {
                Intent mainIntent = new Intent(getContext(), UserProfileActivity.class);
                mainIntent.putExtra("userid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                mainIntent.putExtra("visitor","me");
                startActivity(mainIntent);
            }
            return true;
        }});




            MenuItem mSearchMenuItem = menu.findItem(R.id.action_search_query);
            SearchView searchView = (SearchView) mSearchMenuItem.getActionView();

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {


                    return false;
                }

                @Override
                // Responsible for displaying all possible string from the list based on each additionnal character input made by user
                public boolean onQueryTextChange(String newText) {
                    newText = newText.toLowerCase();

                    requestModelList.clear();
                    for (RequestModel list : requestModelList2) {
                        final String text = list.getUserName().toLowerCase();

                        if (text.contains(newText)) {
                            requestModelList.add(list);
                        }
                    }

                    adapter.notifyDataSetChanged(); //notify the adapter that the dataset was changed
                    return true;
                }
            });


        //chatListAdapter.notifyDataSetChanged();
    }

}















