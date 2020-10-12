package im.zhaojun.system.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
public class Equipment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备ID
     */
    
    @JsonProperty("equipmentID")
    private Integer equipmentID;

    /**
     * 所属用户
     */
    @JsonProperty("userID")
    private String userID;

    /**
     * 设备名称
     */
    @JsonProperty("equipmentName")
    private String equipmentName;

    /**
     * 设备ID
     */
    @JsonProperty("equipmentNO")
    private String equipmentNO;


    /**
     * 设备类型
     */
    @JsonProperty("equipmentType")
    private Integer equipmentType;

    /**
     * 设备安装地址
     */
    @JsonProperty("equipmentAddress")
    private String equipmentAddress;

    /**
     * 电流1
     */
    @JsonProperty("electricCurrentIA")
    private Double electricCurrentIA;

    /**
     * 电流2
     */
    @JsonProperty("electricCurrentIB")
    private Double electricCurrentIB;

    /**
     * 电流3
     */
    @JsonProperty("electricCurrentIC")
    private Double electricCurrentIC;

    /**
     * 设备状态
     */
    @JsonProperty("electricStatus")
    private Integer electricStatus;

    /**
     * 运行状态
     */
    @JsonProperty("runningState")
    private Integer runningState;

    /**
     * 运行模式
     */
    @JsonProperty("OperationMode")
    private Integer OperationMode;

    /**
     * 故障状态
     */
    @JsonProperty("FaultStatus")
    private Integer FaultStatus;

}
