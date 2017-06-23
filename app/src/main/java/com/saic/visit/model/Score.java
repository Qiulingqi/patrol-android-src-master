package com.saic.visit.model;

/**
 * Created by Administrator on 2016/6/8.
 */
public class Score {
    @TreeNodeId
    private int _id;
    @TreeNodePid
    private int parentId;
    @TreeNodeLabel
    private String name;
    private int length;
    @TreeNodeDesc
    private String desc;
    @TreeNodeType
    private int type;
    @TreeNodeCheckPoint
    private CheckPointVo checkPointVo ;

    public Score(int _id, int parentId, String name, int type) {
        super();
        this._id = _id;
        this.parentId = parentId;
        this.name = name;
        this.type = type;
    }

    public Score(int _id, int parentId, String name, int type, String desc) {
        this(_id, parentId, name, type);
        this.desc = desc;
    }

    public Score(int _id, int parentId, String name, int type, String desc,CheckPointVo checkPointVo) {
        this(_id, parentId, name, type,desc);
        this.checkPointVo = checkPointVo;
    }

}
