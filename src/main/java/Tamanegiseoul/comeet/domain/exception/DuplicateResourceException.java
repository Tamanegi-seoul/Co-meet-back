package Tamanegiseoul.comeet.domain.exception;

import lombok.Getter;

@Getter
public class DuplicateResourceException extends RuntimeException {
    @Getter
    private String fieldValue;

    public DuplicateResourceException(String fieldValue) {
        super(String.format("%s is already exists", fieldValue));
        this.fieldValue = fieldValue;
    }
}
