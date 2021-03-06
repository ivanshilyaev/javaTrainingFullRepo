package ft.training.by.service.impl;

import ft.training.by.dao.interfaces.Transaction;
import ft.training.by.dao.interfaces.TransactionFactory;
import ft.training.by.dao.exception.DAOException;
import ft.training.by.dao.mysql.TransactionFactoryImpl;
import ft.training.by.service.exception.ServiceException;
import ft.training.by.service.interfaces.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ServiceFactoryImpl implements ServiceFactory {
    private static final Map<Class<? extends Service>, ServiceImpl>
            repository = new ConcurrentHashMap<>();

    static {
        repository.put(GroupService.class, new GroupServiceImpl());
        repository.put(SubgroupService.class, new SubgroupServiceImpl());
        repository.put(UserService.class, new UserServiceImpl());
        repository.put(StudentService.class, new StudentServiceImpl());
        repository.put(TutorService.class, new TutorServiceImpl());
        repository.put(AdministratorService.class, new AdministratorServiceImpl());
        repository.put(SubjectService.class, new SubjectServiceImpl());
        repository.put(ClassroomService.class, new ClassroomServiceImpl());
        repository.put(TimetableService.class, new TimetableServiceImpl());
        repository.put(TimetableGroupService.class, new TimetableGroupServiceImpl());
        repository.put(PerformanceService.class, new PerformanceServiceImpl());
    }

    private TransactionFactory factory;

    public ServiceFactoryImpl() throws ServiceException {
        try {
            factory = new TransactionFactoryImpl();
        } catch (DAOException e) {
            throw new ServiceException("Couldn't create service factory", e);
        }
    }

    @Override
    public <T extends Service> T createService(Class<T> key) throws ServiceException {
        ServiceImpl service = repository.get(key);
        if (service != null) {
            Transaction transaction = factory.createTransaction();
            service.setTransaction(transaction);
            InvocationHandler handler = new ServiceInvocationHandler(service);
            return (T) Proxy.newProxyInstance(service.getClass().getClassLoader(),
                    new Class[]{key}, handler);
        }
        return null;
    }

    @Override
    public void close() {
        factory.close();
    }
}
