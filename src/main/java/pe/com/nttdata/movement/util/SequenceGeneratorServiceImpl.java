package pe.com.nttdata.movement.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

@Service
public class SequenceGeneratorServiceImpl implements SequenceGeneratorService {

    private static final Logger log = LoggerFactory.getLogger(SequenceGeneratorServiceImpl.class);


    private ReactiveMongoOperations mongoOperations;

    @Autowired
    public SequenceGeneratorServiceImpl(ReactiveMongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public long generateSequence(final String sequenceName) throws ExecutionException, InterruptedException {
        return mongoOperations.findAndModify(new Query(Criteria.where("_id").is(sequenceName)),
                new Update().inc("sequence", 1), options().returnNew(true).upsert(true), DatabaseSequence.class)
                .doOnSuccess(object -> log.debug("databaseSequence is evaluated: {}", object))
                .toFuture().get().getSequence();
    }

}
