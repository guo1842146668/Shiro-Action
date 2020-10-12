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
 * @since 2020-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Scheduled implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 定时ID
     */
    @JsonProperty("cron_id")
    private Integer cronId;

    /**
     * 定时名称
     */
    @JsonProperty("cronName")
    private String cronName;

    /**
     * 定时开始
     */
    @JsonProperty("cronStartTime")
    private String cronStartTime;

    /**
     * 定时结束
     */
    @JsonProperty("cronEndTime")
    private String cronEndTime;

    /**
     * 用户ID
     */
    @JsonProperty("userID")
    private String equipmentNO;

    /**
     * 定时器状态
     */
    @JsonProperty("cronStatus")
    private Integer cronStatus;

}
