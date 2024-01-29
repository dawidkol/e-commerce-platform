package pl.dk.ecommerceplatform.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

import static pl.dk.ecommerceplatform.constant.UserRoleConstant.ADMIN_ROLE;

@Component
@AllArgsConstructor
public class UtilsService {

    private final ObjectMapper objectMapper;

    public <T, R> R applyPatch(T t, JsonMergePatch jsonMergePatch, Class<R> var) throws JsonPatchException, JsonProcessingException {
        JsonNode jsonNode = objectMapper.valueToTree(t);
        JsonNode apply = jsonMergePatch.apply(jsonNode);
        return objectMapper.treeToValue(apply, var);
    }

    public static Logger getLogger(Class<?> var) {
        return LoggerFactory.getLogger(var);
    }

    public static boolean isAdmin(List<String> credentials) {
        return credentials.stream()
                .anyMatch(c -> c.substring(5).equalsIgnoreCase(ADMIN_ROLE));
    }
}

