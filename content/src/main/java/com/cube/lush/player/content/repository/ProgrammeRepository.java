package com.cube.lush.player.content.repository;

import android.support.annotation.NonNull;

import com.cube.lush.player.api.model.ContentType;
import com.cube.lush.player.api.model.Programme;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;

import static com.cube.lush.player.api.model.ContentType.RADIO;
import static com.cube.lush.player.api.model.ContentType.TV;

/**
 * Base Programme respository to provide filtering by content type
 *
 * @author Jamie Cruwys
 */
abstract class ProgrammeRepository extends Repository<Programme>
{
	@Getter protected Set<Programme> videos = new HashSet<>();
	@Getter protected Set<Programme> radios = new HashSet<>();

	@Override protected void updateItems(@NonNull List<Programme> latestItems)
	{
		super.updateItems(latestItems);

		if (latestItems.isEmpty())
		{
			return;
		}

		for (Programme programme : latestItems)
		{
			ContentType type = programme.getType();

			if (type == TV)
			{
				videos.add(programme);
			}
			else if (type == RADIO)
			{
				radios.add(programme);
			}
		}
	}
}