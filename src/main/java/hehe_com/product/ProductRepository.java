package hehe_com.product;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class ProductRepository {
    
    private final Path PRODUCTS_DIR;
    private final Path PRODUCTS_FILE;
    private static final ObjectMapper mapper = new ObjectMapper()
            // serialization feature
            .configure(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN, true)
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            
            // deserialization feature
            .configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    
    
    public ProductRepository(Path PRODUCTS_DIR) {
        this.PRODUCTS_DIR = PRODUCTS_DIR;
        this.PRODUCTS_FILE = PRODUCTS_DIR.resolve("products.json");
        this.initStorage();
    }
    
    private void initStorage() {
        try {
            
            if (!Files.exists(PRODUCTS_DIR)) {
                Files.createDirectory(PRODUCTS_DIR);
            }
            if (!Files.exists(PRODUCTS_FILE)) {
                Files.writeString(PRODUCTS_FILE, "[]", StandardOpenOption.CREATE);
            }
        } catch (RuntimeException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void save(List<Product> products) {
        try {
            mapper.writeValue(PRODUCTS_FILE.toFile(), products);
        } catch (StreamWriteException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private List<Product> load() {
        try {
            return mapper.readValue(PRODUCTS_FILE.toFile(), new TypeReference<List<Product>>() {
            });
        } catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public long generateIDProduct() {
        try {
            List<Product> products = this.load();
            return products.stream()
                    .map(Product::getId)
                    .max(Comparator.naturalOrder())
                    .orElse(0L) + 1;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    
    public boolean isExistById(long id) {
        try {
            List<Product> products = this.load();
            return products.stream().anyMatch(product -> product.getId() == id);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    
    public boolean isExistByName(String name) {
        try {
            List<Product> products = this.load();
            return products.stream().anyMatch(product -> product.getName().equals(name));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    
    public Product saveNew(Product newProduct) {
        try {
            List<Product> products = this.load();
            
            if (products.contains(newProduct)) throw new RuntimeException();
            
            products.add(newProduct);
            save(products);
            return newProduct;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    
    public Product saveUpdated(Product productUpdated) {
        try {
            List<Product> products = this.load();
            
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getId() == productUpdated.getId()) {
                    products.set(i, productUpdated);
                    break;
                }
            }
            
            save(products);
            return productUpdated;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    
    public Product findById(long id) {
        try {
            List<Product> products = this.load();
            return  products.stream().filter(product -> product.getId() == id).findFirst().orElse(null);
        }catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    
    public List<Product> loadAll() {
        try {
            return this.load();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    
    public List<Product> loadAll(Predicate<Product> filter) {
        try {
            List<Product> products = this.loadAll();
            return products.stream().filter(filter).toList();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    
    public boolean delete(Product product) {
        try {
            List<Product> products = this.loadAll();
            products.remove(product);
            save(products);
            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}