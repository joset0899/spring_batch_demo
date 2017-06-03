package com.demo.batch.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;



public interface ItemRepository extends CrudRepository<AlmcItemAlmc, Integer>{

	@Override
	public List<AlmcItemAlmc> findAll();
}
