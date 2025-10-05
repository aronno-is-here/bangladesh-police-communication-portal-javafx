package bdpolice.server.scheduler;

import bdpolice.server.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class CleanupScheduler {
  private final MessageRepo messages;
  private final long ttlSeconds;

  public CleanupScheduler(MessageRepo messages, @Value("${app.message-ttl-seconds}") long ttlSeconds) {
    this.messages = messages; this.ttlSeconds = ttlSeconds;
  }

  @Scheduled(fixedDelay = 60_000)
  public void deleteExpiredMessages() {
    Instant cutoff = Instant.now().minusSeconds(ttlSeconds);
    messages.deleteByTimestampBefore(cutoff);
  }
}
