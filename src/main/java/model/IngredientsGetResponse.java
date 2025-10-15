package model;

public class IngredientsGetResponse {
    private boolean success;
    private IngredientsData[] data;

    public String[] getIngredientsIds(){
        String[] ids = new String[data.length];
        for (int i = 0; i < data.length; i++){
            ids[i] = data[i].get_id();
        }
        return ids;
    }
}
