package com.example.messanger.dao.generator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.UUID;

public class UuidGenerator implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        Object id = sharedSessionContractImplementor
                .getEntityPersister(null, o)
                .getClassMetadata()
                .getIdentifier(o, sharedSessionContractImplementor);

        return id != null ? id : generate();
    }

    public static UUID generate() {
        return UUID.randomUUID();
    }
}

