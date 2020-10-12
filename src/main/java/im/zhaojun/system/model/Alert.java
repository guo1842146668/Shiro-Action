package im.zhaojun.system.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;


/**
 * <p>
 * 
 * </p>
 *
 * @author jobob
 * @since 2020-09-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Alert implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 警报ID
     */
    @JsonProperty("alertID")
    private Integer alertID;

    /**
     * 警报时间
     */
    @JsonProperty("alertTime")
    private Date alertTime;

    /**
     * 警报的设备
     */
    @JsonProperty("equipmentNO")
    private String equipmentNO;


}
