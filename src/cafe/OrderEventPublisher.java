package cafe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Synchronously forwards order placements to subscribers in registration order. */
public final class OrderEventPublisher implements CafeFacade.OrderEvents {
    private final List<CafeFacade.OrderEvents> listeners = new ArrayList<>();

    public void subscribe(CafeFacade.OrderEvents listener) {
        listeners.add(Objects.requireNonNull(listener, "Listener is required."));
    }

    @Override
    public void placed(Order order, int total) {
        // Subscriptions made during notification take effect on the next event.
        for (CafeFacade.OrderEvents listener : List.copyOf(listeners)) {
            listener.placed(order, total);
        }
    }
}
