package com.cube.lush.player.mobile.content;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.cube.lush.player.R;
import com.cube.lush.player.api.model.MediaContent;
import com.cube.lush.player.content.handler.ResponseHandler;
import com.cube.lush.player.content.manager.MediaManager;
import com.cube.lush.player.content.model.CategoryContentType;
import com.cube.lush.player.content.model.Channel;
import com.cube.lush.player.content.util.MediaSorter;
import com.cube.lush.player.mobile.MainActivity;
import com.cube.lush.player.mobile.base.FilterableListingFragment;
import com.cube.lush.player.mobile.base.RecyclerViewClickedListener;
import com.cube.lush.player.mobile.content.adapter.ContentAdapter;
import com.cube.lush.player.mobile.details.DetailsFragment;

import java.util.ArrayList;
import java.util.List;

import uk.co.jamiecruwys.contracts.ListingData;

public class ContentFragment extends FilterableListingFragment<MediaContent, CategoryContentType> implements RecyclerViewClickedListener<MediaContent>
{
	@SuppressWarnings("HardCodedStringLiteral")
	private static final String ARG_CHANNEL = "arg_channel";

	private Channel channel;

	public ContentFragment()
	{
		// Required empty public constructor
	}

	public static ContentFragment newInstance(@NonNull Channel channel)
	{
		ContentFragment fragment = new ContentFragment();
		Bundle args = new Bundle();
		args.putSerializable(ARG_CHANNEL, channel);
		fragment.setArguments(args);
		return fragment;
	}

	@Override public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		channel = (Channel)getArguments().getSerializable(ARG_CHANNEL);
	}

	@NonNull @Override protected RecyclerView.Adapter provideAdapter(@NonNull List<MediaContent> items)
	{
		return new ContentAdapter(items, this);
	}

	@NonNull @Override public List<CategoryContentType> provideFilterOptions()
	{
		ArrayList<CategoryContentType> options = new ArrayList<CategoryContentType>();

		for (CategoryContentType contentType : CategoryContentType.values())
		{
			options.add(contentType);
		}

		return options;
	}

	@Override public void getListDataForFilterOption(@NonNull CategoryContentType contentType, @NonNull final ListingData callback)
	{
		MediaManager.getInstance().getChannelContent(channel, contentType, new ResponseHandler<MediaContent>()
		{
			@Override public void onSuccess(@NonNull List<MediaContent> items)
			{
				MediaSorter.MOST_RECENT_FIRST.sort(items);
				callback.onListingDataRetrieved(items);
			}

			@Override public void onFailure(@Nullable Throwable t)
			{
				callback.onListingDataError(t);
			}
		});
	}

	@NonNull @Override public String getTitleForFilterOption(CategoryContentType contentType)
	{
		if (contentType == CategoryContentType.ALL)
		{
			return "All Episodes";
		}
		else if (contentType == CategoryContentType.TV)
		{
			return "TV";
		}
		else if (contentType == CategoryContentType.RADIO)
		{
			return "Radio";
		}

		return "Unknown";
	}

	@Override public int provideLoadingLayout()
	{
		return R.layout.content_loading;
	}

	@Override public int provideEmptyLayout()
	{
		return R.layout.content_empty;
	}

	@Override public int provideLoadedLayout()
	{
		return R.layout.content_loaded;
	}

	@Override public int provideErrorLayout()
	{
		return R.layout.content_error;
	}

	@Override public void onRecyclerViewItemClicked(@NonNull MediaContent mediaContent)
	{
		((MainActivity)getActivity()).showFragment(DetailsFragment.newInstance(mediaContent));
	}
}