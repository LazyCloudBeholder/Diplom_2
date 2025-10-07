package model;

import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true)
public class Ingredients {
    private String[] ingredients;

}
