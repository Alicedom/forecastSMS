package multisave;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ExcelUtilsTest {

    private List<Person> persons;
    private List<User> users;
    private List<Contact> contacts;

    @Before
    public void setUp() throws Exception {
        persons = new ArrayList<>();
        Person p1 = new Person("A", "a@roytuts.com", "Kolkata");
        Person p2 = new Person("B", "b@roytuts.com", "Mumbai");
        Person p3 = new Person("C", "c@roytuts.com", "Delhi");
        Person p4 = new Person("D", "d@roytuts.com", "Chennai");
        Person p5 = new Person("E", "e@roytuts.com", "Bangalore");
        Person p6 = new Person("F", "f@roytuts.com", "Hyderabad");
        persons.add(p1);
        persons.add(p2);
        persons.add(p3);
        persons.add(p4);
        persons.add(p5);
        persons.add(p6);

        users = new ArrayList<>();
        User u1 = new User("u1", "pwd1");
        User u2 = new User("u2", "pwd2");
        User u3 = new User("u3", "pwd3");
        User u4 = new User("u4", "pwd4");
        User u5 = new User("u5", "pwd5");
        users.add(u1);
        users.add(u2);
        users.add(u3);
        users.add(u4);
        users.add(u5);

        contacts = new ArrayList<>();
        Contact c1 = new Contact("9478512354", "24157853", "24578613");
        Contact c2 = new Contact("9478512354", "24157853", "24578613");
        Contact c3 = new Contact("9478512354", "24157853", "24578613");
        Contact c4 = new Contact("9478512354", "24157853", "24578613");
        contacts.add(c1);
        contacts.add(c2);
        contacts.add(c3);
        contacts.add(c4);
    }

    @Test
    public void testWriteToExcel() {
        ExcelUtils.writeToExcel("person.xlsx", persons);
    }

    @Test
    public void testWriteToExcelInMultiSheets() {
        ExcelUtils.writeToExcelInMultiSheets("excel.xlsx", "Person Details", persons);
        ExcelUtils.writeToExcelInMultiSheets("excel.xlsx", "User Details", users);
        ExcelUtils.writeToExcelInMultiSheets("excel.xlsx", "Contact Details", contacts);
    }

}