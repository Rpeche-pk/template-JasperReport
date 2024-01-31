package com.lpa.app.model;

import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author ROMULO
 * @package com.lpa.app.model
 * @license Lrpa, zephyr cygnus
 * @since 6/11/2023
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Component
public class ProductDetails {
    private String nameProduct;
    private Integer quantity;
    private BigDecimal amount;
    private BigDecimal importe;

    public BigDecimal getTotalAmount(List<ProductDetails> productDetails){
        BigDecimal total= BigDecimal.ZERO;
        for (ProductDetails productDetail : productDetails) {
            total= total.add(productDetail.getImporte());
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getImporte(){
        setImporte(this.amount.multiply(new BigDecimal(this.quantity)));
        return this.amount.multiply(new BigDecimal(this.quantity));
    }

}
