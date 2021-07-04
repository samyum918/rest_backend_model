package com.wl.rest_backend_model.utils;

import com.fasterxml.jackson.databind.JsonNode;
import java.beans.FeatureDescriptor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import com.wl.rest_backend_model.utils.rest.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public class RestControllerUtils {
    public static Pageable toPageable(RestPageRequest restPageRequest, Sort sort) {
        return restPageRequest != null ? PageRequest.of(restPageRequest.getPage(), restPageRequest.getSize(), sort) : Pageable.unpaged();
    }

    public static Sort toSort(RestSort restSort) {
        return restSort != null && restSort.getOrders() != null ?
                Sort.by(restSort.getOrders().stream().map(RestControllerUtils::createOrder).collect(Collectors.toList())) : Sort.unsorted();
    }

    private static Order createOrder(RestOrder o) {
        Order ret = new Order(o.getDirection() == null ? null : Direction.fromString(o.getDirection().toString()), o.getProperty());
        if (o.isIgnoreCase()) {
            ret = ret.ignoreCase();
        }

        return ret;
    }

    public static Filter toFilter(RestFilter restFilter) {
        return restFilter != null && restFilter.getCriterias() != null ?
                Filter.by(restFilter.getCriterias().stream().map(RestControllerUtils::toCriteria).collect(Collectors.toList())) : Filter.nofilter();
    }

    public static Criteria toCriteria(RestCriteria restCriteria) {
        Object value = restCriteria.getValue();
        boolean isBoolean = false;
        if (restCriteria.getValueType() != null) {
            setValueByValueType(restCriteria, value);
        } else if (value instanceof String) {
            String value1 = (String)value;
            isBoolean = StringUtils.isNotBlank(value1) ? StringUtils.equalsIgnoreCase(value1, "true") || StringUtils.equalsIgnoreCase(value1, "false") : false;
        }

        switch(restCriteria.getOperator()) {
            case AND:
                return Criteria.and(toCriteria(restCriteria.getLeftCriteria()), toCriteria(restCriteria.getRightCriteria()));
            case OR:
                return Criteria.or(toCriteria(restCriteria.getLeftCriteria()), toCriteria(restCriteria.getRightCriteria()));
            case EQ:
                return Criteria.eq(restCriteria.getProperty(), isBoolean ? Boolean.valueOf((String)value) : restCriteria.getValue());
            case IEQ:
                return Criteria.ieq(restCriteria.getProperty(), isBoolean ? Boolean.valueOf((String)value) : restCriteria.getValue());
            case NE:
                return Criteria.ne(restCriteria.getProperty(), isBoolean ? Boolean.valueOf((String)value) : restCriteria.getValue());
            case GT:
                return Criteria.gt(restCriteria.getProperty(), restCriteria.getValue());
            case GE:
                return Criteria.ge(restCriteria.getProperty(), restCriteria.getValue());
            case LT:
                return Criteria.lt(restCriteria.getProperty(), restCriteria.getValue());
            case LE:
                return Criteria.le(restCriteria.getProperty(), restCriteria.getValue());
            case LIKE:
                return Criteria.like(restCriteria.getProperty(), restCriteria.getValue());
            case ILIKE:
                return Criteria.ilike(restCriteria.getProperty(), restCriteria.getValue());
            case IN:
                return Criteria.in(restCriteria.getProperty(), restCriteria.getValues());
            case NI:
                return Criteria.notIn(restCriteria.getProperty(), restCriteria.getValues());
            case ISNULL:
                return Criteria.isNull(restCriteria.getProperty());
            case ISNOTNULL:
                return Criteria.isNotNull(restCriteria.getProperty());
            case EQORISNULL:
                return Criteria.eqOrIsNull(restCriteria.getProperty(), restCriteria.getValues());
            default:
                throw new IllegalArgumentException();
        }
    }

    private static void setValueByValueType(RestCriteria restCriteria, Object value) {
        String format = restCriteria.getFormat();
        if (restCriteria.getValueType() == RestCriteria.ValueType.DATE) {
            SimpleDateFormat sf = StringUtils.isNotEmpty(format) ? new SimpleDateFormat(format) : new SimpleDateFormat();

            try {
                restCriteria.setValue(sf.parse((String)value));
            } catch (ParseException var5) {
                throw new RuntimeException(var5);
            }
        } else if(restCriteria.getValueType() == RestCriteria.ValueType.LOCALDATE) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
            restCriteria.setValue(LocalDate.parse((String)value, df));
        } else if(restCriteria.getValueType() == RestCriteria.ValueType.LOCALDATETIME) {
            if (StringUtils.isNotEmpty(format)) {
                DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
                restCriteria.setValue(LocalDateTime.parse((String) value, df));
            } else {
                restCriteria.setValue(LocalDateTime.parse((String) value));
            }
        }

    }

    public static <U> RestPage<U> toRestPage(Page<U> page) {
        return new RestPage(new RestPageInfo(page.getNumber(), page.getTotalPages(), page.getSize(), page.getTotalElements()), page.getContent());
    }

    public static void copyRequiredProperties(Object src, Object target, Set<String> requiredProperties) {
        BeanUtils.copyProperties(src, target, getIgnoredPropertyNames(src, requiredProperties));
    }

    public static Set<String> getRequiredPropertyNames(JsonNode jsonNode) {
        Iterator<String> it = jsonNode.fieldNames();
        Set<String> requiredProperties = new HashSet();
        it.forEachRemaining(requiredProperties::add);
        return requiredProperties;
    }

    public static String[] getIgnoredPropertyNames(Object source, Set<String> requiredProperties) {
        return Arrays.stream(BeanUtils.getPropertyDescriptors(source.getClass()))
                .map(FeatureDescriptor::getName)
                .filter((name) ->  !requiredProperties.contains(name))
                .toArray(String[]::new);
    }
}
