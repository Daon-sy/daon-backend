package com.daon.backend.member.infrastructure;

import com.daon.backend.member.domain.MemberCreatedEvent;
import com.daon.backend.task.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MemberCreatedEventHandler {

    private final WorkspaceService workspaceService;

    @TransactionalEventListener(
            classes = MemberCreatedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(MemberCreatedEvent event) {
        workspaceService.createPersonalWorkspace(event.getMemberId(), event.getMemberName(), event.getMemberEmail());
    }
}
