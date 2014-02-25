package Restrauant;

/**
 * provide a enum class to hold menu category
 */
public enum MenuCategory {
    STARTER,MAIN,DESSERT,DRINKS,NOVALUE;

    public static MenuCategory toMenuCategory(String category){
        try {
            return valueOf(category);
        }catch (IllegalArgumentException iae){
            System.out.println("No such category" + iae);
            return NOVALUE;
        } catch (NullPointerException npe){
            System.out.println("Category is null" + npe);
            return  NOVALUE;
        }
    }
}
