package com.cricket.matchdata.deserializer;

import com.cricket.matchdata.entity.matchinfo.Person;
import com.cricket.matchdata.entity.matchinfo.Registry;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RegistryDeserializer extends StdDeserializer<Registry> {

    public RegistryDeserializer() {
        this(null);
    }

    public RegistryDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Registry deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode peopleNode = node.get("people");

        Registry registry = new Registry();
        List<Person> personList = new ArrayList<>();

        if (peopleNode != null && peopleNode.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = peopleNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                Person person = new Person();
                person.setName(entry.getKey());
                person.setUuid(entry.getValue().asText());
                person.setRegistry(registry);
                personList.add(person);
            }
        }

        registry.setPeople(personList);
        return registry;
    }
}