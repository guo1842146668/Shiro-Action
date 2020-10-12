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
public class Dict implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字典ID
     */
    @JsonProperty("dictID")
    private Integer dictID;

    /**
     * 父级名称
     */
    @JsonProperty("dictPrent")
    private Integer dictPrent;

    /**
     * 字典名称
     */
    @JsonProperty("dictName")
    private String dictName;


}
