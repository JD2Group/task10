package org.example.dao.impl;

import org.example.dao.SolutionDao;
import org.example.pojo.Solution;

public class SolutionDaoImpl extends DaoImpl<Solution, Long> implements SolutionDao {


    public SolutionDaoImpl() {

        super(Solution.class);
    }
}
