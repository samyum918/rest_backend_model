package com.wl.rest_backend_model.utils.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
public class RestCriteria {
    private RestCriteria.Operator operator;
    private RestCriteria leftCriteria;
    private RestCriteria rightCriteria;
    private String property;
    private Object value;
    private RestCriteria.ValueType valueType;
    private String format;
    private HashSet<String> values;

    public RestCriteria(RestCriteria.Operator operator, RestCriteria leftCriteria, RestCriteria rightCriteria) {
        this.operator = operator;
        this.leftCriteria = leftCriteria;
        this.rightCriteria = rightCriteria;
    }

    public RestCriteria(RestCriteria.Operator operator, String property, Object value) {
        this(operator, property, value, null, null);
    }

    public RestCriteria(RestCriteria.Operator operator, String property, Object value, RestCriteria.ValueType valueType, String format) {
        this.operator = operator;
        this.property = property;
        this.value = value;
        this.valueType = valueType;
        this.format = format;
    }

    public RestCriteria(RestCriteria.Operator operator, String property, HashSet<String> values) {
        this.operator = operator;
        this.property = property;
        this.values = values;
    }

    public static enum ValueType {
        LOCALDATETIME,
        LOCALDATE,
        DATE;

        private ValueType() {
        }
    }

    public static enum Operator {
        EQ,
        IEQ,
        NE,
        GT,
        GE,
        LT,
        LE,
        IN,
        NI,
        LIKE,
        ILIKE,
        OR,
        AND,
        ISNULL,
        ISNOTNULL,
        EQORISNULL;

        Operator() {
        }
    }
}
