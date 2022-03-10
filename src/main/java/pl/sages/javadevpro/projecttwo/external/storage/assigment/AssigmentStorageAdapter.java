package pl.sages.javadevpro.projecttwo.external.storage.assigment;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.dao.DuplicateKeyException;
import pl.sages.javadevpro.projecttwo.domain.assigment.Assigment;
import pl.sages.javadevpro.projecttwo.domain.assigment.AssigmentRepository;

@Log
@RequiredArgsConstructor
public class AssigmentStorageAdapter implements AssigmentRepository {

    private final AssigmentMongoRepository assigmentRepository;
    private final AssigmentEntityMapper mapper;

    @Override
    public Assigment save(Assigment assigment) {
        try{
            AssigmentEntity saved = assigmentRepository.insert(mapper.toEntity(assigment));
            log.info("Saved assigment " + saved);
            return mapper.toDomain(saved);
        }catch (DuplicateKeyException ex){
            log.warning("Assigment " +  assigment.getId() + " already exits");
            throw new AssigmentAlreadyExistsException("Assigment already exists");
        }
    }
}
