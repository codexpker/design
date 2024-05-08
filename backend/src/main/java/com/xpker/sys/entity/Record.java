package com.xpker.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author xpker
 * @since 2024-05-07
 */
@TableName("x_record")
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer aId;

    private Integer aSx;

    private Integer aSy;

    private Integer bId;

    private Integer bSx;

    private Integer bSy;

    private String aSteps;

    private String bSteps;

    private String map;

    private String loser;

    private LocalDateTime createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getaId() {
        return aId;
    }

    public void setaId(Integer aId) {
        this.aId = aId;
    }
    public Integer getaSx() {
        return aSx;
    }

    public void setaSx(Integer aSx) {
        this.aSx = aSx;
    }
    public Integer getaSy() {
        return aSy;
    }

    public void setaSy(Integer aSy) {
        this.aSy = aSy;
    }
    public Integer getbId() {
        return bId;
    }

    public void setbId(Integer bId) {
        this.bId = bId;
    }
    public Integer getbSx() {
        return bSx;
    }

    public void setbSx(Integer bSx) {
        this.bSx = bSx;
    }
    public Integer getbSy() {
        return bSy;
    }

    public void setbSy(Integer bSy) {
        this.bSy = bSy;
    }
    public String getaSteps() {
        return aSteps;
    }

    public void setaSteps(String aSteps) {
        this.aSteps = aSteps;
    }
    public String getbSteps() {
        return bSteps;
    }

    public void setbSteps(String bSteps) {
        this.bSteps = bSteps;
    }
    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }
    public String getLoser() {
        return loser;
    }

    public void setLoser(String loser) {
        this.loser = loser;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Record{" +
            "id=" + id +
            ", aId=" + aId +
            ", aSx=" + aSx +
            ", aSy=" + aSy +
            ", bId=" + bId +
            ", bSx=" + bSx +
            ", bSy=" + bSy +
            ", aSteps=" + aSteps +
            ", bSteps=" + bSteps +
            ", map=" + map +
            ", loser=" + loser +
            ", createTime=" + createTime +
        "}";
    }
}
