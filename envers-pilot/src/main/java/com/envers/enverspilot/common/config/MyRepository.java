package com.envers.enverspilot.common.config;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.history.RevisionRepository;

@NoRepositoryBean
public interface MyRepository<T, ID, N extends Number & Comparable<N>>
        extends CrudRepository<T, ID>, RevisionRepository<T, ID, N> {

}
