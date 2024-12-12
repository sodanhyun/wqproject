package com.goormthon.halmang.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSendEmoji is a Querydsl query type for SendEmoji
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSendEmoji extends EntityPathBase<SendEmoji> {

    private static final long serialVersionUID = 947993095L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSendEmoji sendEmoji = new QSendEmoji("sendEmoji");

    public final com.goormthon.halmang.auditing.QBaseTimeEntity _super = new com.goormthon.halmang.auditing.QBaseTimeEntity(this);

    public final StringPath eId = createString("eId");

    public final BooleanPath readFlag = createBoolean("readFlag");

    public final QUser receiver;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regTime = _super.regTime;

    public final QUser sender;

    public final NumberPath<Long> sendSeq = createNumber("sendSeq", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateTime = _super.updateTime;

    public QSendEmoji(String variable) {
        this(SendEmoji.class, forVariable(variable), INITS);
    }

    public QSendEmoji(Path<? extends SendEmoji> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSendEmoji(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSendEmoji(PathMetadata metadata, PathInits inits) {
        this(SendEmoji.class, metadata, inits);
    }

    public QSendEmoji(Class<? extends SendEmoji> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.receiver = inits.isInitialized("receiver") ? new QUser(forProperty("receiver")) : null;
        this.sender = inits.isInitialized("sender") ? new QUser(forProperty("sender")) : null;
    }

}

