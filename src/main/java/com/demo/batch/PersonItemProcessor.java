package com.demo.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.demo.batch.domain.ItemRepository;
import com.demo.batch.message.Person;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {

	@Autowired
	private ItemRepository itemRepository;
	
	private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

	
	@Override
	public Person process(final Person persona) throws Exception {
		
		String nombre= persona.getFirstName().toUpperCase();
		String apellido= persona.getLastName().toUpperCase();
		
		final Person personTranform = new Person(nombre,apellido);
		log.info("tranformando "+persona+" a "+personTranform);
		return personTranform;
	}

	
	
}
