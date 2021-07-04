package com.wl.rest_backend_model.utils.rest;

import com.google.common.collect.Lists;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@Getter
@Setter
@EqualsAndHashCode
public class Filter implements Iterable<Criteria> {
    private static final Filter NOFILTER = new Filter(Lists.newArrayList());
    private final List<Criteria> criterias;

    private Filter(List<Criteria> criterias) {
        this.criterias = Collections.unmodifiableList(criterias);
    }

    public static Filter by(Criteria criteria) {
        return new Filter(Lists.newArrayList(new Criteria[]{criteria}));
    }

    public static Filter by(Criteria... criterias) {
        return new Filter(Lists.newArrayList(criterias));
    }

    public static Filter by(List<Criteria> criterias) {
        return new Filter(criterias);
    }

    public static Filter nofilter() {
        return NOFILTER;
    }

    public List<Criteria> getCriterias() {
        return this.criterias;
    }

    public Filter and(Filter filter) {
        List<Criteria> these = new ArrayList(this.criterias);
        Iterator var3 = filter.iterator();

        while(var3.hasNext()) {
            Criteria criteria = (Criteria)var3.next();
            these.add(criteria);
        }

        return by((List)these);
    }

    public Iterator<Criteria> iterator() {
        return this.criterias.iterator();
    }

    public Stream<Criteria> stream() {
        return this.criterias.stream();
    }
}
