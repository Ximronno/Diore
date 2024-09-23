package ximronno.diore.api;

import java.util.HashMap;

public enum SortingVariant {

    NO_SORT,
    SORT_OFFLINE_PLAYERS,
    SORT_ONLINE_PLAYERS;

    private static final SortingVariant[] variants = values();

    public static SortingVariant cycleVariants(SortingVariant currentVariant) {
        int index = currentVariant.ordinal();

        int nextIndex = (index + 1) % variants.length;
        return variants[nextIndex];
    }

    public static HashMap<String, SortingVariant> getTagsForArrows() {
        HashMap<String, SortingVariant> tags = new HashMap<>();

        for(SortingVariant variant : variants) {

            tags.put(String.format("{%s}", variant.name().toLowerCase()), variant);

        }

        return tags;
    }

}
