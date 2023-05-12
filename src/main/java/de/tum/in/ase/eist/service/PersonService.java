package de.tum.in.ase.eist.service;

import de.tum.in.ase.eist.model.Person;
import de.tum.in.ase.eist.repository.PersonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person save(Person person) {
        if (person.getBirthday().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Birthday may not be in the future");
        }
        return personRepository.save(person);
    }

    public void delete(Person person) {
        personRepository.delete(person);
    }

    public Optional<Person> getById(Long id) {
        return personRepository.findWithParentsAndChildrenById(id);
    }

    public List<Person> getAll() {
        return personRepository.findAll();
    }

    public Person addParent(Person person, Person parent) {
        // TODO: Implement
        Set<Person> p=person.getParents();
        p.add(parent);
        if(p.size()>2)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Over 2 parents.");
        person.setParents(p);
        save(person);
        return person;
    }

    public Person addChild(Person person, Person child) {
        // TODO: Implement
        Set<Person> c_p=child.getParents();
        Set<Person> p=person.getChildren();
        if(c_p.size()==2)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Over 2 parents.");
        c_p.add(person);
        child.setParents(c_p);
        p.add(child);
        person.setChildren(p);
        save(person);
        save(child);
        return person;
    }

    public Person removeParent(Person person, Person parent) {
        // TODO: Implement
        if(person.getParents.size()<=1)
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parents less than 2");
         Set<Person> p=person.getParents();
        p.remove(parent);
        person.setParents(p);
        save(person);
        return person;
    }

    public Person removeChild(Person person, Person child) {
        // TODO: Implement
        if(child.getParents.size()<=1)
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parents less than 2");
         Set<Person> p=person.getChildren();
        p.remove(child);
        person.setChildren(p);
        save(person);
        return person;
    }
}
