package ximronno.diore.api.polyglot;

public record SimplePath(String path) implements Path {

    @Override
    public int hashCode() {
        return path().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Path other)) return false;
        return this.path().equals(other.path());
    }
}
