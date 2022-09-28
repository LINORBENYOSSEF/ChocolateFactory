package chocolate.factory.db;

import chocolate.factory.db.dao.Dao;
import chocolate.factory.db.dao.DaoException;
import chocolate.factory.model.Chocolate;
import chocolate.factory.model.Client;
import chocolate.factory.model.Employee;
import chocolate.factory.model.Ingredient;
import chocolate.factory.model.IngredientUsageInChocolate;
import chocolate.factory.model.WorkLog;
import chocolate.factory.model.call.AddEmployee;
import chocolate.factory.model.call.AddNewChocolateOrderEntry;
import chocolate.factory.model.call.CalcWorkingTime;
import chocolate.factory.model.call.DeleteIngredientFromChocolate;
import chocolate.factory.model.call.MarkChocolateOrderComplete;
import chocolate.factory.model.call.MarkIngredientOrderComplete;
import chocolate.factory.model.call.OpenNewChocolateOrder;
import chocolate.factory.model.call.OpenNewIngredientOrder;
import chocolate.factory.model.schemas.ChocolateOrderEntrySchema;
import chocolate.factory.model.schemas.NewEmployeeSchema;
import chocolate.factory.model.views.ChocolateOrderEntryView;
import chocolate.factory.model.views.ChocolateOrderView;
import chocolate.factory.model.views.ClosedChocolateOrderView;
import chocolate.factory.model.views.IngredientInChocolateView;
import chocolate.factory.model.views.IngredientOrderView;
import chocolate.factory.model.views.OpenChocolateOrderView;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbOperations {

    private final DbConnector connector;

    public DbOperations(DbConnector connector) {
        this.connector = connector;
    }

    public List<Chocolate> getAllChocolates() throws DbOpsException {
        try {
            Dao<Chocolate> dao = connector.getConnection().newDao(Chocolate.class);
            return dao.getAll();
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }

    public List<Client> getAllClients() throws DbOpsException {
        try {
            Dao<Client> dao = connector.getConnection().newDao(Client.class);
            return dao.getAll();
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }

    public Optional<Client> getClientByName(String name) throws DbOpsException {
        try {
            Dao<Client> dao = connector.getConnection().newDao(Client.class);
            List<Client> clients = dao.findByColumnValue("name", name);

            if (clients.size() > 1) {
                throw new ExpectedOneResultException(clients.size());
            } else if (clients.isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(clients.get(0));
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }

    public List<OpenChocolateOrderView> getOpenChocolateOrdersForClient() throws DbOpsException {
        try {
            Dao<OpenChocolateOrderView> dao = connector.getConnection().newDao(OpenChocolateOrderView.class);
            return dao.getAll();
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }

    public List<OpenChocolateOrderView> getOpenChocolateOrdersForClient(Client client) throws DbOpsException {
        try {
            Dao<OpenChocolateOrderView> dao = connector.getConnection().newDao(OpenChocolateOrderView.class);
            return dao.findByColumnValue("client_id", client.getId());
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }

    public List<ClosedChocolateOrderView> getFulfilledChocolateOrdersForClient() throws DbOpsException {
        try {
            Dao<ClosedChocolateOrderView> dao = connector.getConnection().newDao(ClosedChocolateOrderView.class);
            return dao.getAll();
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }

    public List<ClosedChocolateOrderView> getFulfilledChocolateOrdersForClient(Client client) throws DbOpsException {
        try {
            Dao<ClosedChocolateOrderView> dao = connector.getConnection().newDao(ClosedChocolateOrderView.class);
            return dao.findByColumnValue("client_id", client.getId());
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }

    public List<? extends ChocolateOrderView> getChocolateOrders() throws DbOpsException {
        try {
            List<ChocolateOrderView> all = new ArrayList<>();

            Dao<OpenChocolateOrderView> dao = connector.getConnection().newDao(OpenChocolateOrderView.class);
            all.addAll(dao.getAll());
            Dao<ClosedChocolateOrderView> dao2 = connector.getConnection().newDao(ClosedChocolateOrderView.class);
            all.addAll(dao2.getAll());

            return all;
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }

    public List<ChocolateOrderEntryView> getChocolateOrderEntries(Integer orderId) throws DbOpsException {
        try {
            Dao<ChocolateOrderEntryView> dao = connector.getConnection().newDao(ChocolateOrderEntryView.class);
            return dao.findByColumnValue("order_id", orderId);
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }

    public void createChocolateOrder(Client client, List<ChocolateOrderEntrySchema> entries) throws DbOpsException {
        try {
            OpenNewChocolateOrder newChocolateOrder = new OpenNewChocolateOrder();
            newChocolateOrder.setClientId(client.getId());
            newChocolateOrder.setDate(LocalDate.now());

            connector.getConnection().executeProcedure(newChocolateOrder);

            for (ChocolateOrderEntrySchema entry : entries) {
                AddNewChocolateOrderEntry st = new AddNewChocolateOrderEntry();
                st.setOrderId(newChocolateOrder.getOrderId());
                st.setChocolateId(entry.getChocolate().getId());
                st.setAmount(entry.getAmount());

                connector.getConnection().executeProcedure(st);
            }
        } catch (SQLException e) {
            handleSqlException(e);
            throw new DbOpsException(e);
        }
    }

    public List<IngredientOrderView> getIngredientOrders() throws DbOpsException {
        try {
            Dao<IngredientOrderView> dao = connector.getConnection().newDao(IngredientOrderView.class);
            return dao.getAll();
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }

    public void createIngredientOrder(Ingredient ingredient, int amount) throws DbOpsException {
        try {
            OpenNewIngredientOrder newIngredientOrder = new OpenNewIngredientOrder();
            newIngredientOrder.setIngredientId(ingredient.getId());
            newIngredientOrder.setDate(LocalDate.now());
            newIngredientOrder.setAmount(amount);

            connector.getConnection().executeProcedure(newIngredientOrder);
        } catch (SQLException e) {
            handleSqlException(e);
            throw new DbOpsException(e);
        }
    }

    public Optional<Employee> getEmployeeByName(String name) throws DbOpsException {
        try {
            Dao<Employee> dao = connector.getConnection().newDao(Employee.class);
            List<Employee> employees = dao.findByColumnValue("employee_name", name);

            if (employees.size() > 1) {
                throw new ExpectedOneResultException(employees.size());
            } else if (employees.isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(employees.get(0));
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }

    public int getWorkHoursCompletedThisMonth(Employee employee) throws DbOpsException {
        try {
            LocalDate today = LocalDate.now();

            CalcWorkingTime workingTime = new CalcWorkingTime();
            workingTime.setEmployeeId(employee.getId());
            workingTime.setMonth(today.getMonthValue());
            workingTime.setYear(today.getYear());

            connector.getConnection().executeProcedure(workingTime);

            return workingTime.getWorkHours().intValue();
        } catch (SQLException e) {
            handleSqlException(e);
            throw new DbOpsException(e);
        }
    }

    public List<WorkLog> getEmployeeWorkLogs() throws DbOpsException {
        try {
            Dao<WorkLog> dao = connector.getConnection().newDao(WorkLog.class);
            return dao.getAll();
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }

    public List<WorkLog> getEmployeeWorkLogs(Employee employee) throws DbOpsException {
        try {
            Dao<WorkLog> dao = connector.getConnection().newDao(WorkLog.class);
            return dao.findByColumnValue("employee_id", employee.getId());
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }

    public List<Employee> getEmployees() throws DbOpsException {
        try {
            Dao<Employee> dao = connector.getConnection().newDao(Employee.class);
            return dao.getAll();
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }

    private void handleSqlException(SQLException e) throws DbOpsException {
        Optional<SqlError> errorOptional = SqlError.getForCode(e.getErrorCode());
        if (errorOptional.isEmpty()) {
            return;
        }

        SqlError error = errorOptional.get();
        throw error.newException();
    }

    public List<IngredientInChocolateView> getIngredientsIn(Chocolate chocolate) throws DbOpsException {
        try {
            Dao<IngredientInChocolateView> dao = connector.getConnection().newDao(IngredientInChocolateView.class);
            return dao.findByColumnValue("chocolate_id", chocolate.getId());
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }

    public List<Ingredient> getAllIngredients() throws DbOpsException {
        try {
            Dao<Ingredient> dao = connector.getConnection().newDao(Ingredient.class);
            return dao.getAll();
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }

    public void setIngredientOrderComplete(int orderId) throws DbOpsException {
        try {
            MarkIngredientOrderComplete procedure = new MarkIngredientOrderComplete();
            procedure.setOrderId(orderId);

            connector.getConnection().executeProcedure(procedure);
        } catch (SQLException e) {
            handleSqlException(e);
            throw new DbOpsException(e);
        }
    }

    public void setChocolateOrderComplete(int orderId) throws DbOpsException {
        try {
            MarkChocolateOrderComplete procedure = new MarkChocolateOrderComplete();
            procedure.setOrderId(orderId);

            connector.getConnection().executeProcedure(procedure);
        } catch (SQLException e) {
            handleSqlException(e);
            throw new DbOpsException(e);
        }
    }

    public void addIngredient(Ingredient ingredient) throws DbOpsException {
        try {
            Dao<Ingredient> dao = connector.getConnection().newDao(Ingredient.class);
            dao.insert(ingredient);
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }

    public void deleteIngredient(Ingredient ingredient) throws DbOpsException {
        try {
            Dao<Ingredient> dao = connector.getConnection().newDao(Ingredient.class);
            dao.delete(ingredient);
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }

    public void addChocolate(Chocolate chocolate) throws DbOpsException {
        try {
            Dao<Chocolate> dao = connector.getConnection().newDao(Chocolate.class);
            dao.insert(chocolate);
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }

    public void deleteChocolate(Chocolate chocolate) throws DbOpsException {
        try {
            Dao<Chocolate> dao = connector.getConnection().newDao(Chocolate.class);
            dao.delete(chocolate);
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }

    public void addIngredientToChocolate(int chocolateId, Ingredient ingredient, int amount) throws DbOpsException {
        try {
            IngredientUsageInChocolate ingredientUsageInChocolate = new IngredientUsageInChocolate();
            ingredientUsageInChocolate.setChocolateId(chocolateId);
            ingredientUsageInChocolate.setIngredientId(ingredient.getId());
            ingredientUsageInChocolate.setAmount(amount);

            Dao<IngredientUsageInChocolate> dao = connector.getConnection().newDao(IngredientUsageInChocolate.class);
            dao.insert(ingredientUsageInChocolate);
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }

    public void removeIngredientFromChocolate(int chocolateId, int ingredientId) throws DbOpsException {
        try {
            DeleteIngredientFromChocolate procedure = new DeleteIngredientFromChocolate();
            procedure.setChocolateId(chocolateId);
            procedure.setIngredientId(ingredientId);

            connector.getConnection().executeProcedure(procedure);
        } catch (SQLException e) {
            handleSqlException(e);
            throw new DbOpsException(e);
        }
    }

    public void addWorkLog(WorkLog workLog) throws DbOpsException {
        try {
            Dao<WorkLog> dao = connector.getConnection().newDao(WorkLog.class);
            dao.insert(workLog);
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }

    public void deleteEmployee(Employee employee) throws DbOpsException {
        try {
            Dao<Employee> dao = connector.getConnection().newDao(Employee.class);
            dao.delete(employee);
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }

    public void addEmployee(NewEmployeeSchema employee) throws DbOpsException {
        try {
            AddEmployee procedure = new AddEmployee();
            procedure.setName(employee.getName());
            procedure.setPosition(employee.getPosition().name());

            connector.getConnection().executeProcedure(procedure);
        } catch (SQLException e) {
            handleSqlException(e);
            throw new DbOpsException(e);
        }
    }

    public void addClient(Client client) throws DbOpsException {
        try {
            Dao<Client> dao = connector.getConnection().newDao(Client.class);
            dao.insert(client);
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }

    public void deleteClient(Client client) throws DbOpsException {
        try {
            Dao<Client> dao = connector.getConnection().newDao(Client.class);
            dao.delete(client);
        } catch (DaoException e) {
            throw new DbOpsException(e);
        }
    }
}
