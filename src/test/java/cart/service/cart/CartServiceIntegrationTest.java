package cart.service.cart;

import cart.service.cart.dto.ProductResponse;
import cart.service.member.Member;
import cart.service.member.MemberDao;
import cart.service.product.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
public class CartServiceIntegrationTest {
    @Autowired
    CartService cartService;
    @Autowired
    MemberDao memberDao;

    @Autowired
    ProductDao productDao;

    @Test
    void 장바구니에_존재하는_유저의_상품을_조회한다() {
        String email = "cyh6099@gmail.com";
        Member member = memberDao.save(new Member(email, "Qwer1234"));
        Member memberNoHaveCartItem = memberDao.save(new Member("cyh6099@wooteco.com", "qwer1234"));

        List<Product> products = List.of(
                new Product(new ProductName("chicken"), new ProductImage("image"), new ProductPrice(20000)),
                new Product(new ProductName("pizza"), new ProductImage("image"), new ProductPrice(20000))
        );


        Long chickenId = productDao.save(products.get(0));
        Long pizzaId = productDao.save(products.get(1));

        cartService.createCartItem(email, chickenId);
        cartService.createCartItem(email, pizzaId);

        List<ProductResponse> productResponses = cartService.findProductsByUserIdOnCart(member.getEmail());

        assertAll(
                () -> assertThat(cartService.findProductsByUserIdOnCart(member.getEmail())).hasSize(2),
                () -> assertThat(cartService.findProductsByUserIdOnCart(memberNoHaveCartItem.getEmail())).hasSize(0),
                () -> assertThat(productResponses.get(0).getName()).isEqualTo(products.get(0).getName())
        );
    }

    @Test
    void 장바구니_아이템을_삭제한다() {
        String email = "cyh6099@gmail.com";
        memberDao.save(new Member(email, "Qwer1234"));

        List<Product> products = List.of(
                new Product(new ProductName("chicken"), new ProductImage("image"), new ProductPrice(20000)),
                new Product(new ProductName("pizza"), new ProductImage("image"), new ProductPrice(20000))
        );


        Long chickenId = productDao.save(products.get(0));
        Long pizzaId = productDao.save(products.get(1));

        cartService.createCartItem(email, chickenId);
        cartService.createCartItem(email, pizzaId);

        Assertions.assertDoesNotThrow(() -> cartService.deleteCartItem(email, pizzaId));
    }
}
