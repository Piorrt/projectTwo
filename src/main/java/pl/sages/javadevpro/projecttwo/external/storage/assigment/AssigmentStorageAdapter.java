package pl.sages.javadevpro.projecttwo.external.storage.assigment;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.dao.DuplicateKeyException;
import pl.sages.javadevpro.projecttwo.domain.assigment.Assigment;
import pl.sages.javadevpro.projecttwo.domain.assigment.AssigmentRepository;

import java.util.Optional;

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
            throw new AssigmentAlreadyExistsException();
        }
    }

    @Override
    public Optional<Assigment> find(String userId, String taskId) {
        Optional<AssigmentEntity> byTaskId = assigmentRepository.findByTaskId(taskId);
        // TODO jeden if - done
        if (byTaskId.isPresent() && byTaskId.get().getUserId().equals(userId)){
            return Optional.of(mapper.toDomain(byTaskId.get()));
        }
        return Optional.empty();
    }
}
