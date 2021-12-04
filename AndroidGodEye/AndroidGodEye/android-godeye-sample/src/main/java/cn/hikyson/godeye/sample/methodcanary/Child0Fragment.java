package cn.hikyson.godeye.sample.methodcanary;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.hikyson.android.godeye.sample.R;

public class Child0Fragment extends BaseFragment {
	Child0Fragment(){}//FDS fix at least one constructor
    private TextView mTv;

    @Override
    public void onCreate(final @Nullable Bundle savedIState) {//FDS fix method arg could be final
        super.onCreate(savedIState);//FDS fix long var
        int value = 1;//FDS fix short var
    }

    @Override
    public void onAttach(final Activity activity) {//FDS fix method arg could be final
        super.onAttach(activity);
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {//FDS fix method arg could be final
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(final @NonNull LayoutInflater inflater,final  @Nullable ViewGroup container,final @Nullable Bundle savedIState) {//FDS fix method arg could be final and long var
        View view = inflater.inflate(R.layout.fragment_blank_fragment1, container, false);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
