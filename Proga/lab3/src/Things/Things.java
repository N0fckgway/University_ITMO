package Things;




public abstract class Things  {
    private String name;


    public Things(String name) {
        setName(name);
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public abstract void use(String context);


}
