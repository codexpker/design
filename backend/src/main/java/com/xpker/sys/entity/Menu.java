package com.xpker.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 *
 * @author xpker
 * @since 2023-07-06
 */
@Data
@TableName("x_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "menu_id", type = IdType.AUTO)
    private Integer menuId;

    private String component;

    private String path;

    private String redirect;

    private String name;

    private String title;

    private String icon;

    private Integer parentId;

    private String isLeaf;

    private Boolean hidden;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private List<Menu> children;

    @TableField(exist = false)
    private Map<String, Object> meta;

    public Map<String, Object> getMeta(){
        meta = new HashMap<>();
        meta.put("title", title);
        meta.put("icon", icon);
        return meta;
    }

    @Override
    public String toString() {
        return "Menu{" +
            "menuId=" + menuId +
            ", component=" + component +
            ", path=" + path +
            ", redirect=" + redirect +
            ", name=" + name +
            ", title=" + title +
            ", icon=" + icon +
            ", parentId=" + parentId +
            ", isLeaf=" + isLeaf +
            ", hidden=" + hidden +
        "}";
    }
}
