package com.wl.rest_backend_model.utils.rest;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode
public class Criteria {
    private String property;
    private Object value;
    private Collection values;
    private boolean eq;
    private boolean ieq;
    private boolean lt;
    private boolean gt;
    private boolean eqOrIsNull;
    private boolean ne;
    private boolean like;
    private boolean ilike;
    private boolean le;
    private boolean ge;
    private boolean in;
    private boolean notIn;
    private boolean isNull;
    private boolean isNotNull;
    private Criteria lCriteria;
    private Criteria rCriteria;
    private boolean and;
    private boolean or;
    private boolean empty;

    private Criteria() {
        this.empty = true;
    }

    private Criteria(Criteria lCriteria, Criteria rCriteria, boolean and, boolean or) {
        this.and = and;
        this.or = or;
        this.lCriteria = lCriteria;
        this.rCriteria = rCriteria;
    }

    private Criteria(String property, Collection values, boolean in, boolean notIn) {
        this.property = property;
        this.values = values;
        this.in = in;
        this.notIn = notIn;
        this.value = null;
    }

    private Criteria(String property, Object value, boolean eq, boolean ieq, boolean lt, boolean gt, boolean eqOrIsNull, boolean ne, boolean like, boolean ilike, boolean le, boolean ge, boolean isNull, boolean isNotNull) {
        this.property = property;
        this.value = value;
        this.eq = eq;
        this.ieq = ieq;
        this.lt = lt;
        this.gt = gt;
        this.eqOrIsNull = eqOrIsNull;
        this.ne = ne;
        this.like = like;
        this.ilike = ilike;
        this.le = le;
        this.ge = ge;
        this.isNull = isNull;
        this.isNotNull = isNotNull;
        this.values = null;
    }

    public static Criteria empty() {
        return new Criteria();
    }

    public static Criteria eq(String property, Object value) {
        return new Criteria(property, value, true, false, false, false, false, false, false, false, false, false, false, false);
    }

    public static Criteria ieq(String property, Object value) {
        return new Criteria(property, value, false, true, false, false, false, false, false, false, false, false, false, false);
    }

    public static Criteria lt(String property, Object value) {
        return new Criteria(property, value, false, false, true, false, false, false, false, false, false, false, false, false);
    }

    public static Criteria gt(String property, Object value) {
        return new Criteria(property, value, false, false, false, true, false, false, false, false, false, false, false, false);
    }

    public static Criteria eqOrIsNull(String property, Object value) {
        return new Criteria(property, value, false, false, false, false, true, false, false, false, false, false, false, false);
    }

    public static Criteria ne(String property, Object value) {
        return new Criteria(property, value, false, false, false, false, false, true, false, false, false, false, false, false);
    }

    public static Criteria like(String property, Object value) {
        return new Criteria(property, value, false, false, false, false, false, false, true, false, false, false, false, false);
    }

    public static Criteria ilike(String property, Object value) {
        return new Criteria(property, value, false, false, false, false, false, false, false, true, false, false, false, false);
    }

    public static Criteria le(String property, Object value) {
        return new Criteria(property, value, false, false, false, false, false, false, false, false, true, false, false, false);
    }

    public static Criteria ge(String property, Object value) {
        return new Criteria(property, value, false, false, false, false, false, false, false, false, false, true, false, false);
    }

    public static Criteria isNull(String property) {
        return new Criteria(property, (Object)null, false, false, false, false, false, false, false, false, false, false, true, false);
    }

    public static Criteria isNotNull(String property) {
        return new Criteria(property, (Object)null, false, false, false, false, false, false, false, false, false, false, false, true);
    }

    public static Criteria in(String property, Collection values) {
        return values.isEmpty() ? empty() : new Criteria(property, values, true, false);
    }

    public static Criteria notIn(String property, Collection values) {
        return values.isEmpty() ? empty() : new Criteria(property, values, false, true);
    }

    public static Criteria and(Criteria lCriteria, Criteria rCriteria) {
        if ((!lCriteria.isEmpty() || !rCriteria.isEmpty()) && !rCriteria.isEmpty()) {
            return lCriteria.isEmpty() ? rCriteria : new Criteria(lCriteria, rCriteria, true, false);
        } else {
            return lCriteria;
        }
    }

    public static Criteria or(Criteria lCriteria, Criteria rCriteria) {
        if ((!lCriteria.isEmpty() || !rCriteria.isEmpty()) && !rCriteria.isEmpty()) {
            return lCriteria.isEmpty() ? rCriteria : new Criteria(lCriteria, rCriteria, false, true);
        } else {
            return lCriteria;
        }
    }
}
