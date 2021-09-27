package br.com.felipe.modernjava.optional;

import br.com.felipe.modernjava.optional.models.Car;
import br.com.felipe.modernjava.optional.models.Insurance;
import br.com.felipe.modernjava.optional.models.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OptionalTest {

    /**
     * Replace this kind of code:
     *
     * String name = null;
     * if(insurance != null){
     *      name = insurance.getName();
     * }
     *
     * */
    @Test
    void should_extract_and_transform_an_optional_value_using_map(){
        Optional<Insurance> insurance = Optional.ofNullable(new Insurance("Test"));
        Optional<String> name = insurance.map(Insurance::getName);
        Assertions.assertTrue(name.isPresent());
        Assertions.assertEquals("Test",name.get());
    }

    @Test
    void should_not_extract_and_transform_an_optional_with_null(){
        /**
         * If the Optional contains a value, the function
         * passed as argument to map transforms that value. If the Optional is empty, nothing
         * happens.
         * */
        Optional<Insurance> insurance = Optional.ofNullable(null);
        Optional<String> name = insurance.map(Insurance::getName);
        Assertions.assertFalse(name.isPresent());
    }


    /**
     * Replace this kind of code:
     *
     * public String getCarInsuranceName(Person person) {
     *    return person.getCar().getInsurance().getName();
     * }
     *
     * */
    @Test
    void should_chaining_optional_objects_with_flatMap(){
        Optional<Person> person = Optional.of(new Person(new Car(new Insurance("Test"))));
        String name = person.flatMap(Person::getCar)
                                      .flatMap(Car::getInsurance)
                                      .map(Insurance::getName)
                                      .orElse("Unknown");

        Assertions.assertEquals("Test", name);
    }

    @Test
    void given_a_list_of_persons_should_return_a_set_containing_all_distict_names_of_the_insurance_companies(){
        List<Person> persons = List.of(
             new Person(new Car(new Insurance("Test1"))),
             new Person(new Car(new Insurance("Test2"))),
             new Person(new Car(new Insurance("Test3")))
        );

        Set<String> names = persons.stream()
                .map(Person::getCar)
                .map(c -> c.flatMap(Car::getInsurance))
                .map(i -> i.map(Insurance::getName))
                .flatMap(Optional::stream) // this method transforms each Optional into a Stream with zero or one elements, depending on whether the transformed Optional is empty.
                .collect(Collectors.toSet());

        Assertions.assertEquals(3, names.size());
    }

    @Test
    void should_combine_two_optionals(){
        Optional<Car> car = Optional.of(new Car(new Insurance("Test")));
        Optional<Person> person = Optional.of(new Person(car.get()));
        Optional<Insurance> insurance = nullSafeFindCheapestInsurance(person, car);
        Assertions.assertEquals("Cheapest Insurance Company",insurance.map(Insurance::getName).orElse("Unknown"));
    }

    @Test
    void should_read_properties_using_optional(){
        Properties properties = new Properties();
        properties.setProperty("a","5");
        properties.setProperty("b","true");
        properties.setProperty("c","-3");
        Assertions.assertEquals(5, readProperty(properties, "a"));
        Assertions.assertEquals(0, readProperty(properties, "b"));
        Assertions.assertEquals(0, readProperty(properties, "c"));
        Assertions.assertEquals(0, readProperty(properties, "d"));
    }

    public int readProperty(Properties props, String name){
       return  Optional.ofNullable(props.getProperty(name))
                  .flatMap(OptionalUtility::stringToInt)
                  .filter(i -> i > 0)
                  .orElse(0);
    }

    /**
     *   Insurance insurance = ...;
     *   if(insurance != null && "CambridgeInsurance".equals(insurance.getName())){
     *         System.out.println("ok");
     *   }
     * */
    @Test
    void should_check_the_name_of_insurance(){
        Optional<Insurance> insurance = Optional.of(new Insurance("Test"));
         insurance.filter(i -> i.equals("Test"))
                  .ifPresent(i-> System.out.println("OK"));
    }


    public Insurance findCheapestInsurance(Person person, Car car) {
        // queries services provided by the different insurance companies
        // compare all those data
        return new Insurance("Cheapest Insurance Company");
    }


    public Optional<Insurance> nullSafeFindCheapestInsurance(Optional<Person> person, Optional<Car> car) {
        return person.flatMap(p -> car.map(c -> findCheapestInsurance(p, c)));
    }
}
