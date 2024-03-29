package NguoiDung;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.appxemphim.R;


import com.example.appxemphim.databinding.FragmentNguoiDungBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class NguoiDungFragment extends Fragment
{
    FragmentNguoiDungBinding binding;
    FirebaseAuth firebaseAuth;
    UserInfo profile;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment__nguoi_dung,container,false);
        View view=binding.getRoot();
        BottomNavigationView bottomNavigationView=getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        Glide.with(getContext()).load(R.drawable.logomovieapp).into(binding.imgProfile);
        firebaseAuth=FirebaseAuth.getInstance();
        checkUser();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        login();
        onRefresh();
        register();
    }

    private void checkUser() {
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        String email="";
        Uri photo = null;
        if(firebaseUser==null)
        {
            Glide.with(getContext()).load(R.drawable.logomovieapp).circleCrop().into(binding.imgProfile);
            binding.btnDangXuat.setVisibility(View.GONE);
            binding.btnHistory.setVisibility(View.GONE);
            binding.btnnoAdver.setBackgroundResource(R.drawable.shapelinearbtnup);

        }
        else
        {
            binding.btnHistory.setVisibility(View.VISIBLE);
            binding.linearSignInUp.setVisibility(View.INVISIBLE);
            binding.txtUserID.setText(firebaseUser.getUid());
            binding.txtEmail.setText(firebaseUser.getEmail());
            if(firebaseUser.getPhotoUrl()==null)
            {
                Glide.with(getContext()).load(R.drawable.logomovieapp).circleCrop().into(binding.imgProfile);
            }
            else
                Glide.with(getContext()).load(firebaseUser.getPhotoUrl()).circleCrop().into(binding.imgProfile);
            binding.btnDangXuat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseAuth.signOut();
                    getActivity().finish();
                    startActivity(getActivity().getIntent());

                }
            });
        }
    }
    private void login()
    {
        binding.btndangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
    }
    private void register()
    {
        binding.btndangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SignUpActivity.class));
            }
        });
    }
    private void onRefresh()
    {
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment,new NguoiDungFragment()).commit();
                        binding.refreshLayout.setRefreshing(false);
                    }
                },1000);
            }
        });

    }
}