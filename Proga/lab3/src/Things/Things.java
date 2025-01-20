package Things;


import java.util.Objects;

public abstract class Things  {
    private String title;


    public Things(String title) {
        setTitle(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Things things = (Things) o;
        return Objects.equals(title, things.title);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(title);
    }

    @Override
    public String toString() {
        return "Things{" +
                "title='" + title + '\'' +
                '}';
    }

    public abstract void use();


}
