package com.wl.rest_backend_model.utils.rest;

import lombok.AllArgsConstructor;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

@AllArgsConstructor
public class FilterSpecification<T> implements Specification<T> {
    private Filter filter;
    private Set<String> grouping;

    public FilterSpecification(Filter filter) {
        this(filter, null);
    }

    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Map<String, From<?, ?>> joinMap = Maps.newHashMap();
        if (this.grouping != null) {
            List<Expression<?>> expressions = Lists.newArrayList();
            this.grouping.forEach((key) -> {
                expressions.add(this.getPropPath(root, key, joinMap));
            });
            query.groupBy(expressions);
            return criteriaBuilder.and(this.filter.getCriterias().stream()
                    .map((c) -> this.criteriaToPredicate(c, root, criteriaBuilder, joinMap))
                    .filter(Objects::nonNull).toArray(Predicate[]::new));
        } else {
            return criteriaBuilder.and(this.filter.getCriterias().stream()
                    .map((c) -> this.criteriaToPredicate(c, root, criteriaBuilder, joinMap))
                    .filter(Objects::nonNull)
                    .toArray(Predicate[]::new));
        }
    }

    protected Path<?> getPropPath(Root<T> root, String propPath, Map<String, From<?, ?>> joinMap) {
        if (StringUtils.isBlank(propPath)) {
            return null;
        } else {
            ArrayList paths;
            if (StringUtils.indexOf(propPath, ".") != -1) {
                paths = Lists.newArrayList(StringUtils.split(propPath, "."));
            } else {
                paths = Lists.newArrayList(new String[]{propPath});
            }

            Preconditions.checkArgument(!CollectionUtils.isEmpty(paths), String.format("Invalid property path: %s", propPath));
            List<String> currPaths = Lists.newArrayList();
            Object lastJoin = root;

            String part;
            while(paths.size() > 1) {
                part = (String)paths.remove(0);
                currPaths.add(part);
                String currPath = StringUtils.join(currPaths, ".");
                if (joinMap.containsKey(currPath)) {
                    lastJoin = (From)joinMap.get(currPath);
                } else {
                    lastJoin = ((From)lastJoin).join(part, JoinType.LEFT);
                    joinMap.put(currPath, (From<?, ?>) lastJoin);
                }
            }

            part = (String)paths.remove(0);
            return ((From)lastJoin).get(part);
        }
    }

    protected Object coerceToEnum(Object value, Class<?> propType) {
        if (value == null) {
            return null;
        } else if (propType.isAssignableFrom(value.getClass())) {
            return value;
        } else {
            String name = value instanceof Enum ? ((Enum)value).name() : Objects.toString(value, null);
            Optional<Enum> optFound = Arrays.asList(propType.getEnumConstants()).stream()
                    .map((item) -> (Enum)item)
                    .filter((item) -> StringUtils.equalsIgnoreCase(item.name(), name)).findAny();
            Preconditions.checkArgument(optFound.isPresent(), String.format("Unable to find enum constant for class (%s): %s", propType.getName(), name));
            return optFound.get();
        }
    }

    protected Predicate criteriaToPredicate(Criteria criteria, Root<T> root, CriteriaBuilder criteriaBuilder, Map<String, From<?, ?>> joinMap) {
        Path propPath = this.getPropPath(root, criteria.getProperty(), joinMap);
        Object propValue;
        if (criteria.getValue() != null && criteria.getValue() instanceof String && propPath.getJavaType().isAssignableFrom(UUID.class)) {
            propValue = UUID.fromString(Objects.toString(criteria.getValue(), null));
        } else if (criteria.getValue() != null && propPath.getJavaType().isEnum()) {
            propValue = this.coerceToEnum(criteria.getValue(), propPath.getJavaType());
        } else {
            propValue = criteria.getValue();
        }

        Object propValues;
        if (criteria.getValues() != null && !criteria.getValues().isEmpty()) {
            propValues = Lists.newArrayList();
            Iterator var8 = criteria.getValues().iterator();

            label137:
            while(true) {
                while(true) {
                    if (!var8.hasNext()) {
                        break label137;
                    }

                    Object value = var8.next();
                    if (value instanceof String && propPath.getJavaType().isAssignableFrom(UUID.class)) {
                        ((Collection)propValues).add(UUID.fromString(Objects.toString(value, (String)null)));
                    } else if (propPath.getJavaType().isEnum()) {
                        ((Collection)propValues).add(this.coerceToEnum(value, propPath.getJavaType()));
                    } else {
                        ((Collection)propValues).add(value);
                    }
                }
            }
        } else {
            propValues = criteria.getValues();
        }

        if (criteria.isEmpty()) {
            return null;
        } else if (criteria.isEq()) {
            return criteriaBuilder.equal(propPath, propValue);
        } else if (criteria.isIeq()) {
            return criteriaBuilder.equal(criteriaBuilder.lower(propPath), propValue instanceof String ? ((String)propValue).toLowerCase() : propValue);
        } else if (criteria.isEqOrIsNull()) {
            return criteriaBuilder.or(criteriaBuilder.equal(propPath, propValue), criteriaBuilder.isNull(propPath));
        } else if (criteria.isGe()) {
            return criteriaBuilder.greaterThanOrEqualTo(propPath, (Comparable)propValue);
        } else if (criteria.isGt()) {
            return criteriaBuilder.greaterThan(propPath, (Comparable)propValue);
        } else if (criteria.isIn()) {
            return criteriaBuilder.in(propPath).value(propValues);
        } else if (criteria.isNotIn()) {
            return criteriaBuilder.in(propPath).value(propValues).not();
        } else if (criteria.isLe()) {
            return criteriaBuilder.lessThanOrEqualTo(propPath, (Comparable)propValue);
        } else if (criteria.isLike()) {
            return criteriaBuilder.like(propPath, (String)propValue);
        } else if (criteria.isIlike()) {
            return criteriaBuilder.like(criteriaBuilder.upper(propPath), StringUtils.upperCase((String)propValue));
        } else if (criteria.isLt()) {
            return criteriaBuilder.lessThan(propPath, (Comparable)propValue);
        } else if (criteria.isNe()) {
            return criteriaBuilder.notEqual(propPath, propValue);
        } else if (criteria.isNotNull()) {
            return criteriaBuilder.isNotNull(propPath);
        } else if (criteria.isNull()) {
            return criteriaBuilder.isNull(propPath);
        } else {
            Predicate lPredicate;
            Predicate rPredicate;
            if (criteria.isAnd()) {
                lPredicate = this.criteriaToPredicate(criteria.getLCriteria(), root, criteriaBuilder, joinMap);
                rPredicate = this.criteriaToPredicate(criteria.getRCriteria(), root, criteriaBuilder, joinMap);
                if (lPredicate != null && rPredicate != null) {
                    return criteriaBuilder.and(lPredicate, rPredicate);
                } else if (lPredicate != null) {
                    return lPredicate;
                } else {
                    return rPredicate != null ? rPredicate : null;
                }
            } else if (criteria.isOr()) {
                lPredicate = this.criteriaToPredicate(criteria.getLCriteria(), root, criteriaBuilder, joinMap);
                rPredicate = this.criteriaToPredicate(criteria.getRCriteria(), root, criteriaBuilder, joinMap);
                if (lPredicate != null && rPredicate != null) {
                    return criteriaBuilder.or(lPredicate, rPredicate);
                } else if (lPredicate != null) {
                    return lPredicate;
                } else {
                    return rPredicate != null ? rPredicate : null;
                }
            } else {
                throw new IllegalArgumentException("unknown criteria:" + criteria);
            }
        }
    }
}
