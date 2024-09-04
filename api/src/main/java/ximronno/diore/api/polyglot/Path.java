package ximronno.diore.api.polyglot;

public interface Path {

    String path();

    static Path of(String path) {
        return new SimplePath(path);
    }

}
