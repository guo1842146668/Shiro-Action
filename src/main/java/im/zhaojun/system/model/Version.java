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
public class Version implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 版本ID
     */
    @JsonProperty("version")
    private Integer version;

    /**
     * 版本名称
     */
    @JsonProperty("version_name")
    private String versionName;

    /**
     * 版本内容
     */
    @JsonProperty("version_content")
    private String versionContent;


}
