package com.i2s.flawlog.dao;

import com.i2s.flawlog.domain.bo.MetalBO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by wxy on 2017/11/26.
 */
@Mapper
public interface MetalMapper {

    List<MetalBO> selectMetal(MetalBO metalBO);

    int insertMetal(MetalBO metalBO);

    int updateMetal(MetalBO metalBO);

    int deleteMetal(MetalBO metalBO);
}
