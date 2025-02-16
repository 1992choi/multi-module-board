package demo.board.articleread.service.event.handler;

import demo.board.common.event.Event;
import demo.board.common.event.EventPayload;

public interface EventHandler<T extends EventPayload> {
    void handle(Event<T> event);
    boolean supports(Event<T> event);
}
