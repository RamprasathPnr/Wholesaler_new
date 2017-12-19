package com.omneAgate.wholeSaler.DTO;


import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class TalukDto implements Serializable {

    Long id;        //Unique id

    String name;    //Name of the taluk

    String code;    //two digit taluk code

    Long districtId;

    DistrictDto districtDto;

    List<VillageDto> villages;

}
