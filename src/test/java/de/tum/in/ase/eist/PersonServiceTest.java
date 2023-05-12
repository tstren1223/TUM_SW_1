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
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

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
    @Test
    void testAddParent(){
        var person = new Person();
        person.setFirstName("M");
        person.setLastName("A");
        person.setBirthday(LocalDate.now());

        personRepository.save(person);

        assertTrue(personRepository.findAll().contains(person));
        var person2 = new Person();
        person2.setFirstName("M");
        person2.setLastName("B");
        person2.setBirthday(LocalDate.now());
        personRepository.save(person2);
        personService.addParent(person,person2);
        assertEquals(2, personRepository.findAll().size());
    }
    @Test
    void testAddThreeParents(){
        var person = new Person();
        person.setFirstName("M");
        person.setLastName("AA");
        person.setBirthday(LocalDate.now());

        personRepository.save(person);
        var person2 = new Person();
        person2.setFirstName("M");
        person2.setLastName("BB");
        person2.setBirthday(LocalDate.now());
        personRepository.save(person2);
        var person3 = new Person();
        person3.setFirstName("M");
        person3.setLastName("C");
        person3.setBirthday(LocalDate.now());

        personRepository.save(person3);
        var person4 = new Person();
        person4.setFirstName("M");
        person4.setLastName("D");
        person4.setBirthday(LocalDate.now());
        personRepository.save(person4);

        personService.addParent(person,person2);
        personService.addParent(person,person3);
        assertThrows(ResponseStatusException.class,()->{
            personService.addParent(person,person4);
        });
    }
}
