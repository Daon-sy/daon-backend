INSERT INTO member (member_id, created_at, modified_at, username, name, password, removed, notified)
VALUES ('78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8', '2023-11-11 17:38:13', '2023-11-11 17:38:13', 'user1', 'USER1', '$2a$10$Y9j.WBTfgOrytIlLyApHiuxdWp9V3PV4fRuYcgV9Mljk6nH3fB0OW', false, true),
       ('2831ccac-aef9-4359-abbb-1d432b1b8078', '2023-11-11 17:38:13', '2023-11-11 17:38:13', 'user2', 'USER2',
        '$2a$10$Y9j.WBTfgOrytIlLyApHiuxdWp9V3PV4fRuYcgV9Mljk6nH3fB0OW', false, true),
       ('4c624615-7123-4a63-9ade-0fd5889452cd', '2023-11-11 17:38:13', '2023-11-11 17:38:13', 'user3', 'USER3',
        '$2a$10$Y9j.WBTfgOrytIlLyApHiuxdWp9V3PV4fRuYcgV9Mljk6nH3fB0OW', false, true),
       ('c152ed2e-1978-4100-bf26-b0014af642b4', '2023-11-11 17:38:13', '2023-11-11 17:38:13', 'user4', 'USER4',
        '$2a$10$Y9j.WBTfgOrytIlLyApHiuxdWp9V3PV4fRuYcgV9Mljk6nH3fB0OW', false, true);

INSERT INTO member_email (created_at, modified_at, email, member_id)
VALUES ('2023-11-11 17:38:13', '2023-11-11 17:38:13', 'test1@email.com', '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8'),
       ('2023-11-11 17:38:13', '2023-11-11 17:38:13', 'test2@email.com', '2831ccac-aef9-4359-abbb-1d432b1b8078'),
       ('2023-11-11 17:38:13', '2023-11-11 17:38:13', 'test3@email.com', '4c624615-7123-4a63-9ade-0fd5889452cd');

INSERT INTO workspace (created_at, modified_at, description, division, image_url, subject, title, removed)
VALUES ('2023-11-11 18:16:21.000000', '2023-11-11 18:16:21.000000', null, 'PERSONAL', null, '개인용도', 'USER1 개인워크스페이스',
        false),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'PERSONAL', null, '개인용도', 'USER2 개인워크스페이스',
        false),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'GROUP', null, '그룹 테스트', '그룹 스페이스1', false);

INSERT INTO workspace_invitation(created_at, modified_at, member_id, role, workspace_id)
VALUES ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078',
        'PROJECT_ADMIN', 3);

INSERT INTO workspace_participant(created_at, modified_at, member_id, email, image_url, name, role, workspace_id)
VALUES
    -- 개인 워크스페이스
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8',
     'user1@email.com', null, 'USER1', 'WORKSPACE_ADMIN', 1),
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078',
     'user2@email.com', null, 'USER2', 'WORKSPACE_ADMIN', 2),
    -- 그룹 스페이스1
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8',
     'user1@email.com', null, 'WS_USER1', 'WORKSPACE_ADMIN', 3),
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078',
     'user2@email.com', null, 'WS_USER2', 'PROJECT_ADMIN', 3),
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4c624615-7123-4a63-9ade-0fd5889452cd',
     'user3@email.com', null, 'WS_USER3', 'BASIC_PARTICIPANT', 3);

INSERT INTO message(created_at, modified_at, title, content, readed, receiver_id, receiver_name, sender_id, sender_name,
                    workspace_id)
VALUES ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', 'message title1', 'message content1', false, 3,
        'WS_USER1', 4, 'WS_USER2', 3),
       ('2023-11-11 18:19:13.000000', '2023-11-11 18:19:13.000000', 'message title2', 'message content2', false, 3,
        'WS_USER1', 4, 'WS_USER2', 3);

INSERT INTO workspace_notice(created_at, modified_at, title, content, workspace_id, workspace_participant_id)
VALUES ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', 'notice title', 'notice content', 3, 3);

INSERT INTO project(created_at, modified_at, description, title, workspace_id, removed)
VALUES ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project1', 3, false);

INSERT INTO project_participant(created_at, modified_at, member_id, project_id, workspace_partipant_id)
VALUES ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8', 1, 3),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4c624615-7123-4a63-9ade-0fd5889452cd', 1, 5);

INSERT INTO board(created_at, modified_at, title, project_id, removed)
VALUES ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 1, false),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2번 보드', 1, false);

INSERT INTO task (created_at, modified_at, project_id, board_id, title, content, emergency, start_date, end_date,
                  progress_status, creator_id, task_manager_id, removed)
VALUES ('2023-11-11 18:18:13', '2023-11-11 18:18:13', 1, 1, 'Task', null, false, '2023-11-01 00:00:00',
        '2023-11-30 00:00:00', 'TODO', 3, null, false);

INSERT INTO task_reply (created_at, modified_at, content, task_id, writer_id)
VALUES ('2023-11-11 18:18:13', '2023-11-11 18:18:13', 'reply', 1, 1);
