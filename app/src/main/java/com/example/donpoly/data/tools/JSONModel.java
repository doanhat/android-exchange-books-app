package com.example.donpoly.data.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;

public class JSONModel implements Serializable{
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> String serialize(T object) {
        try {
            return getMapper().writeValueAsString(object);
        } catch(JsonProcessingException ex) {
            System.err.println("Unable to serialize" + object.getClass().getCanonicalName() + " object");
            ex.printStackTrace();
            return null;
        }
    }

    public static <T extends JSONModel> T deserialize(String serial, Class<T> c) {
        try {
            return getMapper().readValue(serial, c);
        } catch (IOException e) {
            System.err.println("Unable to deserialize" + c.getCanonicalName() + " object");
            e.printStackTrace();
            return null;
        }
    }

    private static ObjectMapper getMapper() {
        return mapper;
    }
}
