package ximronno.diore.api.menu;

public enum MenuSizes {

    ONE_ROW(9),
    TWO_ROWS(18),
    THREE_ROWS(27),
    FOUR_ROWS(36),
    FIVE_ROWS(45),
    SIX_ROWS(54);


    private final int size;

    MenuSizes(int size) {

        this.size = size;

    }

    public int size() {
        return size;
    }
}
