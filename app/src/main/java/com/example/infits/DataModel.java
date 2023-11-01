package com.example.infits;

public class DataModel {
    private String dietitianId;
    private String recipeName;
    private String ingredients;
    private String recipeInstructions;
    private String nutritionalInfo;
    private String recipeImage;
    private String recipeCategory;

    public DataModel(String dietitianId, String recipeName, String ingredients,
                     String recipeInstructions, String nutritionalInfo,
                     String recipeImage, String recipeCategory) {
        this.dietitianId = dietitianId;
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.recipeInstructions = recipeInstructions;
        this.nutritionalInfo = nutritionalInfo;
        this.recipeImage = recipeImage;
        this.recipeCategory = recipeCategory;
    }

    public String getDietitianId() {
        return dietitianId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getRecipeInstructions() {
        return recipeInstructions;
    }

    public String getNutritionalInfo() {
        return nutritionalInfo;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public String getRecipeCategory() {
        return recipeCategory;
    }
}
