package com.i2s.flawlog.service.impl;

import com.i2s.flawlog.dao.MetalMapper;
import com.i2s.flawlog.service.MetalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.i2s.flawlog.domain.bo.*;

import java.util.List;

/**
 * Created by wxy on 2017/11/26.
 */
@Service(value="metalService")
public class MetalServiceImpl implements MetalService {
    @Autowired
    MetalMapper metalMapper;

    @Override
    public int insertMetal(MetalBO metalBO)throws Exception{
        return metalMapper.insertMetal(metalBO);
    }

    @Override
    public int updateMetal(MetalBO metalBO) throws Exception {
        return metalMapper.updateMetal(metalBO);
    }

    @Override
    public List<MetalBO> queryMetal(MetalBO metalBO) throws Exception{
        return metalMapper.selectMetal(metalBO);
    }

    @Override
    public int deleteMetal(MetalBO metalBO)throws Exception {
        return metalMapper.deleteMetal(metalBO);
    }
}
