package ft.training.by.dao.mysql;

import ft.training.by.bean.Student;
import ft.training.by.bean.Subgroup;
import ft.training.by.bean.User;
import ft.training.by.dao.interfaces.StudentDao;
import ft.training.by.dao.exception.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentDaoImpl extends DaoImpl implements StudentDao {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String SQL_SELECT_ALL_STUDENTS =
            "SELECT id, subgroup_id, user_id FROM student;";

    private static final String SQL_SELECT_STUDENT_BY_ID =
            "SELECT id, subgroup_id, user_id FROM student WHERE id = ?;";

    private static final String SQL_SELECT_STUDENT_BY_USER_ID =
            "SELECT id, subgroup_id, user_id FROM student WHERE user_id = ?;";

    private static final String SQL_INSERT =
            "INSERT INTO student (subgroup_id, user_id) VALUES (?, ?);";

    private static final String SQL_DELETE_STUDENT_BY_ID =
            "DELETE FROM student WHERE id = ?;";

    @Override
    public List<Student> read() throws DAOException {
        List<Student> students = new ArrayList<>();
        try {
            Statement statement = null;
            try {
                statement = connection.createStatement();
                ResultSet resultSet = null;
                try {
                    resultSet = statement.executeQuery(SQL_SELECT_ALL_STUDENTS);
                    while (resultSet.next()) {
                        Student student = new Student();
                        fillStudent(student, resultSet);
                        students.add(student);
                    }
                } finally {
                    if (resultSet != null) {
                        resultSet.close();
                    }
                }
            } finally {

            }
        } catch (SQLException e) {
            LOGGER.error("DB connection error", e);
        } finally {
        }
        return students;
    }

    @Override
    public Optional<Student> read(Integer id) throws DAOException {
        Student student = null;
        PreparedStatement statement = null;
        ResultSet resultSet;
        try {
            statement = connection.prepareStatement(SQL_SELECT_STUDENT_BY_ID);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                student = new Student();
                fillStudent(student, resultSet);
            }
        } catch (SQLException throwables) {
            LOGGER.error("DB connection error", throwables);
        } finally {
        }
        return Optional.ofNullable(student);
    }

    @Override
    public boolean delete(Integer id) throws DAOException {
        boolean deleted = false;
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(SQL_DELETE_STUDENT_BY_ID);
            statement.setInt(1, id);
            statement.executeUpdate();
            deleted = true;

        } catch (SQLException throwables) {
            LOGGER.error("DB connection error", throwables);
        } finally {
        }
        return deleted;
    }

    @Override
    public boolean delete(Student entity) throws DAOException {
        return delete(entity.getId());
    }

    @Override
    public Integer create(Student entity) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, entity.getSubgroup().getId());
            statement.setInt(2, entity.getUser().getId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                LOGGER.error("No autoincremented index after trying to add record into table student");
                return BAD_CREATION_CODE;
            }
        } catch (SQLException throwables) {
            LOGGER.error("DB connection error", throwables);
            return BAD_CREATION_CODE;
        }
    }

    @Override
    public void update(Student entity) {
    }

    @Override
    public List<Student> findByGroup(int groupNum) throws DAOException {
        List<Student> students = read();
        students.removeIf(student -> student.getSubgroup().getGroup().getGroupNumber() != groupNum);
        return students;
    }

    @Override
    public Optional<Student> findByUserId(Integer id) throws DAOException {
        Student student = null;
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(SQL_SELECT_STUDENT_BY_USER_ID);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                student = new Student();
                fillStudent(student, resultSet);
                resultSet.close();
            }
        } catch (SQLException throwables) {
            LOGGER.error("DB connection error", throwables);
        } finally {
        }
        return Optional.ofNullable(student);
    }

    private void fillStudent(Student student, ResultSet resultSet) throws SQLException, DAOException {
        student.setId(resultSet.getInt(1));
        int subgroupID = resultSet.getInt(2);
        SubgroupDaoImpl subgroupDao = new SubgroupDaoImpl();
        subgroupDao.setConnection(connection);
        Subgroup subgroup = subgroupDao.read(subgroupID).orElse(null);
        student.setSubgroup(subgroup);
        int userID = resultSet.getInt(3);
        UserDaoImpl userDao = new UserDaoImpl();
        userDao.setConnection(connection);
        User user = userDao.read(userID).orElse(null);
        student.setUser(user);
    }
}
