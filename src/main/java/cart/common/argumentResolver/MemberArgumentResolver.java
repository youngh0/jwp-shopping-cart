package cart.common.argumentResolver;

import cart.exception.AuthException;
import cart.service.member.dto.MemberRequest;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String BASIC_TYPE = "Basic";
    private static final String DELIMITER = ":";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(MemberRequest.class) && parameter.hasParameterAnnotation(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String header = webRequest.getHeader("Authorization");

        if (header == null) {
            throw new AuthException("올바르지 않은 인증 정보입니다.");
        }
        if (!header.toLowerCase().startsWith(BASIC_TYPE.toLowerCase())) {
            throw new AuthException("올바르지 않은 인증 방식입니다.");

        }
        String authValue = header.substring(BASIC_TYPE.length()).trim();
        byte[] decodedAuthByteValue = Base64.decodeBase64(authValue);
        String decodedAuthString = new String(decodedAuthByteValue);

        String[] credentials = decodedAuthString.split(DELIMITER);
        String email = credentials[0];
        String password = credentials[1];

        return new MemberRequest(email, password);
    }
}
