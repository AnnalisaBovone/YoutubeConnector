package com.example.youtubeconnector;

import java.util.Comparator;

public class SortByTimestamp implements Comparator<YoutubeAnswer> {

	@Override
	public int compare(YoutubeAnswer o1, YoutubeAnswer o2) {
		return Long.compare(o1.timestamp, o2.timestamp);
	}

}
