package com.i2s.flawlog.service;

import com.i2s.flawlog.domain.*;
import com.i2s.flawlog.domain.bo.*;

import java.util.List;

/**
 * Created by wxy on 2017/11/26.
 */
public interface MetalService {

    public int insertMetal(MetalBO metalBO)throws Exception;

    public int updateMetal(MetalBO metalBO)throws Exception;

    List<MetalBO> queryMetal(MetalBO metalBO)throws Exception;

    public int deleteMetal(MetalBO metalBO) throws Exception;
}
