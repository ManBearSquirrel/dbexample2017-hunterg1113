package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.*;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RandomUserController extends Controller
{
    JPAApi jpaApi;

    @Inject
    public RandomUserController(JPAApi jpaApi)
    {
        this.jpaApi = jpaApi;
    }

    @Transactional
    public Result getRandomUsers()
    {
        try
        {
            URL url = new URL("https://randomuser.me/api/?nat=us&results=100");
            url.openConnection();

            ObjectMapper objectMapper = new ObjectMapper();
            RandomUser randomUser = objectMapper.readValue(url, RandomUser.class);

            return ok(views.html.randomusers.render(randomUser.getResults()));

        } catch (Exception e)
        {
            return ok("Unable to get users: " + e.getMessage());
        }
    }

    @Transactional
    public Result generateDb()
    {
        return ok("done");
    }
}




/*
//  Generate employees....

{
try
{
    URL url = new URL("https://randomuser.me/api/?nat=us&results=4&gender=male");
    url.openConnection();

    ObjectMapper objectMapper = new ObjectMapper();
    RandomUser randomUser = objectMapper.readValue(url, RandomUser.class);

    List<User> users = randomUser.getResults();

    int x = 9;

    for (User user : users)
    {
        Random random = new Random();
        Employee employee = new Employee();

        employee.setFirstName(user.getName().getFirst());
        employee.setLastName(user.getName().getLast());

        String hireDate = user.getDob().substring(0, 10);

        if (x == 9)
        {
            int year = random.nextInt(3) + 2008;
            employee.setHireDate("" + year + hireDate.substring(4));

            employee.setTitle("Foreman");
            employee.setHourlyWage(new BigDecimal("20.00"));
        }
        else if (x == 10)
        {
            int year = random.nextInt(3) + 2011;
            employee.setHireDate("" + year + hireDate.substring(4));

            employee.setTitle("Assistant Foreman");
            employee.setHourlyWage(new BigDecimal("17.50"));
        }
        else
        {
            int year = random.nextInt(3) + 2014;
            employee.setHireDate("" + year + hireDate.substring(4));

            employee.setTitle("Crewmember");
            employee.setHourlyWage(new BigDecimal("15.00"));
        }

        employee.setEmployeeId(x);

        jpaApi.em().persist(employee);

        x++;
    }
} catch (Exception e)
{
    return ok("Error: " + e.getMessage());
}

return ok("done");
}


// Generate equipment...
{
Random random = new Random();

for (
        int i = 1;
        i < 8; i++)

{
    Equipment equipment = new Equipment();

    equipment.setEquipmentHours(random.nextInt(500) + 75);
    equipment.setEquipmentId(i);
    equipment.setContractId(1);

    if (i < 3)
    {
        equipment.setEquipmentName("Mini-X 0" + i);
    }
    else if (i < 5)
    {
        equipment.setEquipmentName("Mini-Skid 0" + (i - 2));
    }
    else
    {
        equipment.setEquipmentName("Skid 0" + (i - 4));
    }

    jpaApi.em().persist(equipment);
}

return ok("done");
}


// Generate client
try
{
    URL url = new URL("https://randomuser.me/api/?nat=us&results=4");
    url.openConnection();

    ObjectMapper objectMapper = new ObjectMapper();
    RandomUser randomUser = objectMapper.readValue(url, RandomUser.class);

    List<User> users = randomUser.getResults();

    int x = 1;

    for (User user : users)
    {
        Random random = new Random();
        Client client = new Client();

        client.setFirstName(user.getName().getFirst());
        client.setLastName(user.getName().getLast());
        client.setAddress(user.getLocation().getStreet());
        client.setCity("Little Rock");
        client.setZipCode("7220" + random.nextInt(10));
        client.setEmail(user.getEmail());
        client.setClientId(x);
        client.setState("AR");

        client.setPhone(user.getPhone().replace(user.getPhone().subSequence(0,5),"(501)"));

        jpaApi.em().persist(client);

        x++;
    }

    return ok("ok");

} catch (Exception e)
{
    return ok("Unable to get users: " + e.getMessage());
}
}

//Clients' address capitalized
{
    List<Client> clients = jpaApi.em().createQuery("FROM Client c").getResultList();

    for(Client client : clients)
    {
        String[] words = client.getAddress().split(" ");

        for (int i = 1; i < words.length; i++)
        {
            words[i] = words[i].substring(0,1).toUpperCase() + words[i].substring(1).toLowerCase();
        }

        client.setAddress(String.join(" ", words));
    }

    return ok("ok");
}

// Generate contract
{
Random random = new Random();

for (int i = 1; i < 5; i++)
{
    Contract contract = new Contract();

    contract.setClientId(i);
    contract.setContractId(i);
    contract.setStartDate("" + (random.nextInt(2) + 2016) + "-" + (random.nextInt(12) + 1) + "-" + (random.nextInt(30) + 1));

    jpaApi.em().persist(contract);
}
return ok("done");
}

//Generate category
 {
for (int i = 1; i < 10; i++)
{
    Category category = new Category();
    category.setCategoryId(i);

    if (i == 1)
    {
        category.setCategoryName("Excavation/ Debris Removal");
    }
    else if (i == 2)
    {
        category.setCategoryName("Irrigation");
    }
    else if (i == 3)
    {
        category.setCategoryName("Patio");
    }
    else if (i == 4)
    {
        category.setCategoryName("Outdoor Chimney");
    }
    else if (i == 5)
    {
        category.setCategoryName("Fire Pit");
    }
    else if (i == 6)
    {
        category.setCategoryName("Retaining Wall");
    }
    else if (i == 7)
    {
        category.setCategoryName("Soil Preparation");
    }
    else if (i == 8)
    {
        category.setCategoryName("Plant Installation");
    }
    else
    {
        category.setCategoryName("Sod Installation");
    }

    jpaApi.em().persist(category);
}
return ok("done");
}

// Generate estimates...
List<Contract> contracts = jpaApi.em().createQuery("FROM Contract c").getResultList();

Random random  = new Random();

int x = 1;

for (Contract contract : contracts)
{

    for (int i = 1; i < 10; i++)
    {
        Estimate estimate = new Estimate();

        estimate.setEstimateId(x);
        estimate.setContractId(contract.getContractId());
        estimate.setCategoryId(i);

        int y = random.nextInt(75) + 25;

        estimate.setEstimateHours(new BigDecimal("" + y));

        jpaApi.em().persist(estimate);

        x++;
    }
}


return ok("done");
}

Generate actual....
 List<Estimate> estimates = jpaApi.em().createQuery("FROM Estimate e WHERE contractId = 4").getResultList();

Random random = new Random();

int x = 118;

for (Estimate estimate : estimates)
{
    Actual actual = new Actual();

    actual.setActualId(x);
    actual.setForemanId(1);
    actual.setEstimateId(estimate.getEstimateId());
    actual.setActualDate(estimate.getContract().getStartDate());

    int y = random.nextInt(3);

    if (y == 0)
    {
        BigDecimal bd = estimate.getEstimateHours().add(new BigDecimal("" + random.nextInt(18)));
        actual.setActualHours(bd);
    }
    else
    {
        BigDecimal bd = estimate.getEstimateHours().subtract(new BigDecimal("" + random.nextInt(12)));
        actual.setActualHours(bd);
    }

    jpaApi.em().persist(actual);

    x++;
}


return ok("done");
}

// Generate users
List<Employee> employees = jpaApi.em().createQuery("FROM Employee e").getResultList();

Random random = new Random();

int x = 1;

for (Employee employee : employees)
{
    if (employee.getTitle().equals("Foreman"))
    {
        ProjectUser projectUser = new ProjectUser();

        projectUser.setUserName(employee.getFirstName().substring(0, 1).toLowerCase() + "." + employee.getLastName().toLowerCase());
        projectUser.setEmployeeId(employee.getEmployeeId());
        projectUser.setUserId(x);

        try
        {
            String password = "password";

            byte[] salt = Password.getNewSalt();
            byte[] hashedPassword = Password.hashPassword(password.toCharArray(),salt);

            projectUser.setSalt(salt);
            projectUser.setPassword(hashedPassword);
        } catch (Exception e)
        {
            return ok("error");
        }

        jpaApi.em().persist(projectUser);

        x++;
    }
}


return ok("done");
}

 */