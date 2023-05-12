package de.tum.in.ase.eist;

import de.tum.in.ase.eist.model.Person;
import de.tum.in.ase.eist.repository.PersonRepository;
import de.tum.in.ase.eist.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class PersonServiceTest {
    @Autowired
    private PersonService personService;
    @Autowired
    private PersonRepository personRepository;

    @Test
    void testAddPerson() {
        var person = new Person();
        person.setFirstName("Max");
        person.setLastName("Mustermann");
        person.setBirthday(LocalDate.now());

        personService.save(person);

        assertEquals(1, personRepository.findAll().size());
    }

    @Test
    void testDeletePerson() {
        var person = new Person();
        person.setFirstName("Max");
        person.setLastName("Mustermann");
        person.setBirthday(LocalDate.now());

        person = personRepository.save(person);

        personService.delete(person);

        assertTrue(personRepository.findAll().isEmpty());
    }

    // TODO: Add more test cases here
    @Test
    boolean testAddParent() {
        var person = new Person();
        person.setFirstName("M");
        person.setLastName("A");
        person.setBirthday(LocalDate.now());

        personService.save(person);

        assertTrue(personService.getAll().contains(person));
        var person2 = new Person();
        person2.setFirstName("M");
        person2.setLastName("B");
        person2.setBirthday(LocalDate.now());
        personService.save(person2);

        assertTrue(personService.getAll().contains(person2));
        
        assertEquals(2, personRepository.findAll().size());
    }

    @Test
    void testAddThreeParents() {
        var person = new Person();
        person.setFirstName("M");
        person.setLastName("A");
        person.setBirthday(LocalDate.now());

        personService.save(person);
        var person2 = new Person();
        person2.setFirstName("M");
        person2.setLastName("B");
        person2.setBirthday(LocalDate.now());
        personService.save(person2);
        var person3 = new Person();
        person3.setFirstName("M");
        person3.setLastName("C");
        person3.setBirthday(LocalDate.now());

        personService.save(person3);
        var person4 = new Person();
        person4.setFirstName("M");
        person4.setLastName("D");
        person4.setBirthday(LocalDate.now());
        personService.save(person4);
        
    }
}
