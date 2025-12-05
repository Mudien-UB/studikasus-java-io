package hehe_com.order;

import hehe_com.user.Users;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderRepository {
    
    private static final String FILE_NAME = "ORDERS.CSV";
    private static final String ID = "id";
    private static final String ORDER_TIME = "order_time";
    private static final String PRODUCTS = "products";
    private static final String TOTAL_PRICE = "total_price";
    private static final String USER = "user";
    
    private final Path PATH_DIR;
    private final Path PATH_FILE;
    
    public OrderRepository(Path dirPath) {
        this.PATH_DIR = dirPath;
        this.PATH_FILE = PATH_DIR.resolve(FILE_NAME);
    }
    
    public void save(Order order) {
        try {
            boolean writeHeader =
                    Files.notExists(PATH_FILE) || Files.size(PATH_FILE) == 0;
            
            try (
                    FileWriter writer = new FileWriter(PATH_FILE.toFile(), true);
                    CSVPrinter printer = new CSVPrinter(
                            writer,
                            writeHeader
                                    ? CSVFormat.DEFAULT.builder()
                                    .setHeader(ID, ORDER_TIME, PRODUCTS, TOTAL_PRICE, USER)
                                    .get()
                                    : CSVFormat.DEFAULT
                    )
            ) {
                
                String productList = order.getItems().stream()
                        .map(od -> od.getProduct().getId() + ":" + od.getAmount())
                        .reduce((a, b) -> a + ";" + b)
                        .orElse("");
                
                printer.printRecord(
                        order.getId(),
                        order.getOrderTime(),
                        productList,
                        order.getTotalPrice(),
                        order.getUser().getUsername()
                );
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Gagal menyimpan order", e);
        }
    }
    
    public List<Map<String, Object>> loadAll() {
        List<Map<String,Object>> rawDataList = new ArrayList<>();
        
        if (Files.notExists(PATH_FILE)) {
            return null;
        }
        
        try (
                FileReader reader = new FileReader(PATH_FILE.toFile());
                CSVParser parser = new CSVParser(
                        reader,
                        CSVFormat.DEFAULT.builder()
                                .setHeader()
                                .setSkipHeaderRecord(true)
                                .get()
                )
        ) {
            
            for (CSVRecord r : parser) {
                try {
                    String id = r.get(ID);
                    String productsRaw = r.get(PRODUCTS);
                    String totalPrice = r.get(TOTAL_PRICE);
                    String username = r.get(USER);
                    String orderTime = r.get(ORDER_TIME);
                    
                    List<Map<String,String>> rawDataDetails = new ArrayList<>();
                    
                    if (productsRaw != null && !productsRaw.isBlank()) {
                        for (String p : productsRaw.split(";")) {
                            if (p.isBlank()) continue;
                            
                            String[] d = p.split(":");
                            if (d.length != 2) continue;
                            
                            rawDataDetails.add(Map.of(
                                    "product",d[0],
                                    "amount",d[1])
                            );
                        }
                    }
                    
                    rawDataList.add(Map.of(
                            "id",id,
                            "items", rawDataDetails,
                        "totalPrice",totalPrice,
                            "user",username,
                            "orderTime",orderTime
                    ));
                    
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        return rawDataList;
    }
    
    public List<Map<String, Object>> loadAllWithUser(Users user) {
        try{
        
        List<Map<String, Object>> maps = this.loadAll();
        return maps.stream()
                .filter(d -> d.get("user").toString().equals(user.getUsername()))
                .toList();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
