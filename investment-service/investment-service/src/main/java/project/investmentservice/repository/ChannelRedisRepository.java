package project.investmentservice.repository;

import org.springframework.data.repository.CrudRepository;
import project.investmentservice.domain.Channel;

public interface ChannelRedisRepository extends CrudRepository<Channel, Long> {
}
