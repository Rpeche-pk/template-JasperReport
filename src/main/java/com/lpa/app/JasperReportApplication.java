package com.lpa.app;

import com.lpa.app.model.ProductDetails;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SpringBootApplication
public class JasperReportApplication implements CommandLineRunner {

    @Autowired
    private ProductDetails product;
    public static void main(String[] args) {
        SpringApplication.run(JasperReportApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        String destinationPath= "src"+ File.separator+"main"+File.separator+"resources"+File.separator+"static"+File.separator+"ReportGenerated.pdf";
        String filePath= "src"+ File.separator+"main"+File.separator+"resources"+File.separator+"templates"+File.separator+"report"+File.separator+"Voucher.jrxml";
        //InputStream resourceAsStream = getClass().getResourceAsStream("/static/images/report.jasper v "

        List<ProductDetails> productDetails= Arrays.asList(
                ProductDetails.builder().amount(BigDecimal.ZERO).importe(BigDecimal.ZERO).nameProduct("").quantity(0).build(),
                ProductDetails.builder().nameProduct("Producto 1").quantity(1).amount(new BigDecimal("15.5")).build(),
                ProductDetails.builder().nameProduct("Producto 2").quantity(2).amount(new BigDecimal("15.8")).build(),
                ProductDetails.builder().nameProduct("Producto 3").quantity(3).amount(new BigDecimal("20.5")).build(),
                ProductDetails.builder().nameProduct("Producto 4").quantity(4).amount(new BigDecimal("11.8")).build(),
                ProductDetails.builder().nameProduct("Producto 5").quantity(5).amount(new BigDecimal("10.9")).build()
        );

        BigDecimal totalAmount = product.getTotalAmount(productDetails);
        JRBeanArrayDataSource dataSource = new JRBeanArrayDataSource(productDetails.toArray());

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("voucherId", UUID.randomUUID().toString());
        parameters.put("dateNow", formatter.format(localDateTime) );
        parameters.put("cellPhone", "987456123");
        parameters.put("clienteName", "Luis Peche Aparcana");
        parameters.put("clienteDireccion", "Calle 1 # 2-3");
        parameters.put("dataSource", dataSource);
        parameters.put("totalAmount", totalAmount);
        parameters.put("imageDir", "classpath:/static/images/");


        JasperReport report = JasperCompileManager.compileReport(filePath);
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource);

        JasperExportManager.exportReportToPdfFile(jasperPrint, destinationPath);
        System.out.println("Report generated at " + destinationPath);

    }
}
