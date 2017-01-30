package com.cube.lush.player.details;

import android.app.Fragment;
import android.app.FragmentManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;

import com.cube.lush.player.R;
import com.cube.lush.player.browse.BasicMainFragmentAdapter;
import com.cube.lush.player.playback.PlaybackFragment;

/**
 * Displays details and a preview of the current live Lush playlist.
 *
 * @author Jamie Cruwys
 * @project lush-player-android-client
 */
public class OffAirFragment extends PlaybackFragment implements BrowseFragment.MainFragmentAdapterProvider
{
	private static final String OFFAIR_FRAGMENT_TAG = "offair";

	public static void show(FragmentManager fragmentManager, View parentView)
	{
		if (parentView != null && fragmentManager.findFragmentByTag(OFFAIR_FRAGMENT_TAG) == null)
		{
			fragmentManager.beginTransaction().add(parentView.getId(), new OffAirFragment(), OFFAIR_FRAGMENT_TAG).commit();
		}
	}

	public static void hide(FragmentManager fragmentManager)
	{
		if (fragmentManager.isDestroyed())
		{
			return;
		}

		fragmentManager.executePendingTransactions();
		Fragment offAirFragment = fragmentManager.findFragmentByTag(OFFAIR_FRAGMENT_TAG);
		if (offAirFragment != null)
		{
			fragmentManager.beginTransaction().remove(offAirFragment).commitAllowingStateLoss();
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		getBrightcoveVideoView().setMediaController((MediaController)null);
		playFile("asset:///videos/offair.mp4");
		getBrightcoveVideoView().setOnCompletionListener(new MediaPlayer.OnCompletionListener()
		{
			@Override
			public void onCompletion(MediaPlayer mediaPlayer)
			{
				if (getActivity() != null)
				{
					getBrightcoveVideoView().start();
				}
			}
		});

		getView().findViewById(R.id.text_overlay).setVisibility(View.VISIBLE);
		((TextView)getView().findViewById(R.id.text_overlay)).setText("There is no live broadcast right now.");
	}

	@Override
	public BrowseFragment.MainFragmentAdapter getMainFragmentAdapter()
	{
		return new BasicMainFragmentAdapter<>(this);
	}
}