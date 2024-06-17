package dataaccess;

import dataaccess.dao.IntAuthDAO;
import dataaccess.dao.IntGameDAO;
import dataaccess.dao.IntUserDAO;

public record DataAccess(IntAuthDAO authDAO, IntGameDAO gameDAO, IntUserDAO userDAO) {
}
