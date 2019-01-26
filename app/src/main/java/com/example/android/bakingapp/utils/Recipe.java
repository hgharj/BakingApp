package com.example.android.bakingapp.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Recipe implements Parcelable {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("servings")
    @Expose
    private int servings;

    @SerializedName("ingredients")
    @Expose
    private ArrayList<Ingredient> ingredients = new ArrayList<>();

    @SerializedName("steps")
    @Expose
    private ArrayList<Step> steps = new ArrayList<>();

    public Recipe(int id, String name, int servings) {
        this.id = id;
        this.name = name;
        this.servings = servings;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public ArrayList<Ingredient> getIngredients(){return ingredients;}

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients=ingredients;
    }

    public ArrayList<Step> getSteps(){return steps;}

    public void setSteps(ArrayList<Step> steps) {
        this.steps=steps;
    }

    public Recipe(Parcel parcel) {
        this.id = parcel.readInt();
        this.name = parcel.readString();
        this.servings = parcel.readInt();
        this.ingredients = parcel.readArrayList(Ingredient.class.getClassLoader());
        this.steps = parcel.readArrayList(Step.class.getClassLoader());
    }

    //creator - used when un-parceling our parcel (creating the object)
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Recipe createFromParcel(Parcel parcel) {
            return new Recipe(parcel);
        }

        @Override
        public Recipe[] newArray(int i) {
            return new Recipe[0];
        }
    };

    //return hashcode of object
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(servings);
        dest.writeList(ingredients);
        dest.writeList(steps);
    }
}
