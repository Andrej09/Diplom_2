package org.example;

import java.util.List;

public class Ingredients {

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    private List<String> ingredients;

    public Ingredients(List<String> ingredients){
        this.ingredients = ingredients;
    }

    public Ingredients(){
    }

}
