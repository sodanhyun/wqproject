package com.codehows.wqproject.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLecture is a Querydsl query type for Lecture
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLecture extends EntityPathBase<Lecture> {

    private static final long serialVersionUID = 1712365203L;

    public static final QLecture lecture = new QLecture("lecture");

    public final com.codehows.wqproject.auditing.QBaseEntity _super = new com.codehows.wqproject.auditing.QBaseEntity(this);

    public final BooleanPath active = createBoolean("active");

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final DateTimePath<java.time.LocalDateTime> edate = createDateTime("edate", java.time.LocalDateTime.class);

    public final StringPath etc = createString("etc");

    public final StringPath lCode = createString("lCode");

    public final NumberPath<Integer> limitMin = createNumber("limitMin", Integer.class);

    public final StringPath location = createString("location");

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regTime = _super.regTime;

    public final DateTimePath<java.time.LocalDateTime> sdate = createDateTime("sdate", java.time.LocalDateTime.class);

    public final StringPath speaker = createString("speaker");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateTime = _super.updateTime;

    public QLecture(String variable) {
        super(Lecture.class, forVariable(variable));
    }

    public QLecture(Path<? extends Lecture> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLecture(PathMetadata metadata) {
        super(Lecture.class, metadata);
    }

}

