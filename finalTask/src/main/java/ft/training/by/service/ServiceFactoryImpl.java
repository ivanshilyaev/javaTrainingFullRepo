package ft.training.by.service;

import ft.training.by.dao.Transaction;
import ft.training.by.dao.TransactionFactory;
import ft.training.by.service.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceFactoryImpl implements ServiceFactory {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final Map<Class<? extends Service>, Class<? extends ServiceImpl>>
            repository = new ConcurrentHashMap<>();

    static {
        repository.put(FacultyService.class, FacultyServiceImpl.class);
        repository.put(GroupService.class, GroupServiceImpl.class);
        repository.put(SubgroupService.class, SubgroupServiceImpl.class);
        repository.put(UserService.class, UserServiceImpl.class);
        repository.put(StudentService.class, StudentServiceImpl.class);
    }

    private TransactionFactory factory;

    public ServiceFactoryImpl(TransactionFactory factory) {
        this.factory = factory;
    }

    @Override
    public <T extends Service> T createService(Class<T> key) throws ServiceException {
        Class<? extends ServiceImpl> value = repository.get(key);
        if (value != null) {
            try {
                Transaction transaction = factory.createTransaction();
                ServiceImpl service = value.getDeclaredConstructor().newInstance();
                service.setTransaction(transaction);
                return (T) service;
            } catch (InstantiationException | InvocationTargetException
                    | NoSuchMethodException | IllegalAccessException e) {
                LOGGER.error("Impossible to create service");
                throw new ServiceException(e);
            }
        }
        return null;
    }

    @Override
    public void close() {
        factory.close();
    }
}
