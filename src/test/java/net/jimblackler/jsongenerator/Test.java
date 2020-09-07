package net.jimblackler.jsongenerator;

import static net.jimblackler.jsonschemafriend.Validator.validate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Random;
import net.jimblacker.jsongenerator.Configuration;
import net.jimblacker.jsongenerator.Generator;
import net.jimblackler.jsonschemafriend.Schema;
import net.jimblackler.jsonschemafriend.SchemaException;
import net.jimblackler.jsonschemafriend.SchemaStore;
import org.json.JSONArray;
import org.json.JSONObject;

public class Test {
  private static final URI DEFAULT_METASCHEMA =
      URI.create("http://json-schema.org/draft-07/schema#");
  public static final FileSystem FILE_SYSTEM = FileSystems.getDefault();

  public static void main(String[] args) throws URISyntaxException, SchemaException, IOException {
    Path outDir = FILE_SYSTEM.getPath("out");
    Path base = FILE_SYSTEM.getPath("/examples");
    Path file = base.resolve("longread").resolve("schemas").resolve("schema.json");
    Path out = outDir.resolve("example.json");
    SchemaStore schemaStore = new SchemaStore();
    Schema schema =
        schemaStore.loadSchema(Test.class.getResource(file.toString()).toURI(), DEFAULT_METASCHEMA);

    Object object = new Generator(new Configuration() {
      @Override
      public boolean isPedanticTypes() {
        return false;
      }

      @Override
      public boolean isGenerateNulls() {
        return false;
      }

      @Override
      public boolean isGenerateMinimal() {
        return true;
      }
    }, schemaStore, new Random(1)).generate(schema, 250);

    validate(schema, object);
    try (BufferedWriter writer =
             new BufferedWriter(new FileWriter(out.toFile(), StandardCharsets.UTF_8))) {
      if (object instanceof JSONObject) {
        writer.write(((JSONObject) object).toString(2));
      } else if (object instanceof JSONArray) {
        writer.write(((JSONArray) object).toString(2));
      }
    }
  }
}
