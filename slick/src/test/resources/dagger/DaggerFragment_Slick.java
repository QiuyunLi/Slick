package test;

import android.app.Fragment;

import com.github.slick.OnDestroyListener;
import com.github.slick.SlickFragmentDelegate;

import java.lang.Override;
import java.lang.String;
import java.util.HashMap;

public class DaggerFragment_Slick implements OnDestroyListener {

    private static DaggerFragment_Slick hostInstance;
    private final HashMap<String, SlickFragmentDelegate<ExampleView, ExamplePresenter>> delegates = new HashMap<>();

    public static <T extends Fragment & ExampleView> SlickFragmentDelegate<ExampleView, ExamplePresenter> bind(T daggerFragment) {
        final String id = SlickFragmentDelegate.getId(daggerFragment);
        if (hostInstance == null) hostInstance = new DaggerFragment_Slick();
        SlickFragmentDelegate<ExampleView, ExamplePresenter> delegate = hostInstance.delegates.get(id)
        if (delegate == null) {
            final ExamplePresenter presenter = ((DaggerFragment) daggerFragment).provider.get();
            delegate = new SlickFragmentDelegate<>(presenter, daggerFragment.getClass(), id);
            delegate.setListener(hostInstance);
            hostInstance.delegates.put(id, delegate);
        }
        ((DaggerFragment) daggerFragment).presenter = delegate.getPresenter();
        return delegate;
    }

    public static <T extends Fragment & ExampleView> void onStart(T daggerFragment) {
        hostInstance.delegates.get(SlickFragmentDelegate.getId(daggerFragment)).onStart(daggerFragment);
    }
    public static <T extends Fragment & ExampleView> void onStop(T daggerFragment) {
        hostInstance.delegates.get(SlickFragmentDelegate.getId(daggerFragment)).onStop(daggerFragment);
    }
    public static <T extends Fragment & ExampleView> void onDestroy(T daggerFragment) {
        hostInstance.delegates.get(SlickFragmentDelegate.getId(daggerFragment)).onDestroy(daggerFragment);
    }

    @Override
    public void onDestroy(String id) {
        hostInstance.delegates.remove(id);
        if (hostInstance.delegates.size() == 0) {
            hostInstance = null;
        }
    }
}