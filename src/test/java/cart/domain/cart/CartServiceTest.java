package cart.domain.cart;

import cart.dao.ProductDao;
import cart.domain.member.Member;
import cart.domain.member.MemberDao;
import cart.entity.ProductEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    MemberDao memberDao;

    @Mock
    ProductDao productDao;

    @Mock
    CartDao cartDao;

    @InjectMocks
    CartService cartService;

    @Test
    void 장바구니에_상품을_추가한다() {
        given(memberDao.findByEmail(any()))
                .willReturn(Optional.of(new Member(1L, "Cyh6099@gmail.com", "qwer1234")));

        given(productDao.findById(any()))
                .willReturn(Optional.of(new ProductEntity(1L, "치킨", "image", 10000)));

        given(cartDao.addProduct(any()))
                .willReturn(1L);

        Long cartId = cartService.addProductToCart("cyh6099@gmail.com", 1L);

        Assertions.assertThat(cartId).isPositive();
    }

    @Test
    void 존재하지_않는_멤버로_저장하려고_하면_예외발생() {
        given(memberDao.findByEmail(any()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.addProductToCart("cyh6099@gmail.com", 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 유저입니다.");
    }

    @Test
    void 존재하지_않는_상품을_저장하려고_하면_예외발생() {
        given(memberDao.findByEmail(any()))
                .willReturn(Optional.of(new Member(1L, "aa@aa.com", "qwer1234")));

        given(productDao.findById(any()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.addProductToCart("cyh6099@gmail.com", 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 상품입니다.");
    }

    @Test
    void 존재하지_않는_상품을_삭제하려고_하면_예외발생() {
        given(memberDao.findByEmail(any()))
                .willReturn(Optional.of(new Member(1L, "aa@aa.com", "qwer1234")));

        given(cartDao.deleteCartItem(any(), any()))
                .willReturn(0);

        assertThatThrownBy(() -> cartService.deleteCartItem("cyh6099@gmail.com", 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 삭제 요청입니다.");
    }
}
