package org.example.dao.impl.entityImpl;


import org.example.dao.SolutionDao;
import org.example.dao.impl.DaoImpl;
import org.example.pojo.Solution;

public class SolutionDaoImpl extends DaoImpl<Solution> implements SolutionDao {

    public SolutionDaoImpl() {

        super(Solution.class);
    }
}
