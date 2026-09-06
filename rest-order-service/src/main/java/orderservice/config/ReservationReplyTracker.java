package orderservice.config;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;
import orderservice.controller.ReservationController;
import orderservice.entity.ReservationResponse;

@Component
public class ReservationReplyTracker {

  private final Map<String, DeferredResult<ResponseEntity<EntityModel<ReservationResponse>>>> pending =
      new ConcurrentHashMap<>();

  public void register(String correlationId,
      DeferredResult<ResponseEntity<EntityModel<ReservationResponse>>> result) {
    pending.put(correlationId, result);
  }

  public void complete(String correlationId, ReservationResponse response) {
    DeferredResult<ResponseEntity<EntityModel<ReservationResponse>>> result =
        pending.remove(correlationId);
    if (result != null) {
      EntityModel<ReservationResponse> model = EntityModel.of(response);
      model.add(linkTo(methodOn(ReservationController.class).confirmReservation(response.getId()))
          .withSelfRel());
      result.setResult(ResponseEntity.ok(model));
    }
  }
}
