package net.jimblacker.jsongenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Random;
import net.jimblackler.jsonschemafriend.GenerationException;
import net.jimblackler.jsonschemafriend.Schema;
import net.jimblackler.jsonschemafriend.SchemaStore;
import org.json.JSONArray;
import org.json.JSONObject;

public class Writer {
  private final URI defaultMetaSchema = URI.create("http://json-schema.org/draft-07/schema#");

  public void build(URI uri, Path out) throws GenerationException, IOException {
    SchemaStore schemaStore = new SchemaStore();
    Schema schema = schemaStore.loadSchema(uri, defaultMetaSchema);

    Object obj = Generator.generate(new Random(), schema);
    try (BufferedWriter writer =
             new BufferedWriter(new FileWriter(out.toFile(), StandardCharsets.UTF_8))) {
      if (obj instanceof JSONObject) {
        writer.write(((JSONObject) obj).toString(2));
      } else {
        writer.write(((JSONArray) obj).toString(2));
      }
    }
  }
}
