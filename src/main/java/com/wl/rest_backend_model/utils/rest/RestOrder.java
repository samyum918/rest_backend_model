package com.wl.rest_backend_model.utils.rest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestOrder {
    private static final boolean DEFAULT_IGNORE_CASE = true;

    private String property;
    private RestOrder.Direction direction;
    private boolean ignoreCase;

    public RestOrder() {
        this.ignoreCase = true;
    }

    public RestOrder(String property) {
        this(property, null);
    }

    public RestOrder(String property, RestOrder.Direction direction) {
        this(property, direction, true);
    }

    public RestOrder(String property, RestOrder.Direction direction, boolean ignoreCase) {
        this.ignoreCase = true;
        this.property = property;
        this.direction = direction;
        this.ignoreCase = ignoreCase;
    }

    public String toString() {
        return String.format("%s: %s", this.property, this.direction == null ? "DEFAULT" : this.direction) + (this.ignoreCase ? " (case insensitive)" : "");
    }

    public enum Direction {
        ASC,
        DESC;
        Direction() { }
    }
}
