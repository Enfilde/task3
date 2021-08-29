package com.epam.ld.ilya.entity;

public class CustomEntity {

    private String value;

    public CustomEntity(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object){
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        CustomEntity entity = (CustomEntity) object;
        return value.equals(entity.value);
    }
}
