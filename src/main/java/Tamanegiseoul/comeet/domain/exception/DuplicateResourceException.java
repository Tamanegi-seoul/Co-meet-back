package Tamanegiseoul.comeet.domain.exception;

import lombok.Getter;

@Getter
public class DuplicateResourceException extends RuntimeException {
    @Getter
    private String valueType;
    private String fieldValue;

    public DuplicateResourceException(String valueType, String fieldValue) {
        super(String.format("%s's %s is already exists", fieldValue, fieldValue));
        this.fieldValue = fieldValue;
        this.valueType = valueType;
    }
}
