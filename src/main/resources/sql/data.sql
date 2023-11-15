INSERT INTO member (member_id, created_at, modified_at, username, name, password)
VALUES ('78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8', '2023-11-11 17:38:13', '2023-11-11 17:38:13', 'user1', 'USER1', '$2a$10$Y9j.WBTfgOrytIlLyApHiuxdWp9V3PV4fRuYcgV9Mljk6nH3fB0OW'),
       ('2831ccac-aef9-4359-abbb-1d432b1b8078', '2023-11-11 17:38:13', '2023-11-11 17:38:13', 'user2', 'USER2', '$2a$10$Y9j.WBTfgOrytIlLyApHiuxdWp9V3PV4fRuYcgV9Mljk6nH3fB0OW'),
       ('4c624615-7123-4a63-9ade-0fd5889452cd', '2023-11-11 17:38:13', '2023-11-11 17:38:13', 'user3', 'USER3', '$2a$10$Y9j.WBTfgOrytIlLyApHiuxdWp9V3PV4fRuYcgV9Mljk6nH3fB0OW'),
       ('c152ed2e-1978-4100-bf26-b0014af642b4', '2023-11-11 17:38:13', '2023-11-11 17:38:13', 'user4', 'USER4', '$2a$10$Y9j.WBTfgOrytIlLyApHiuxdWp9V3PV4fRuYcgV9Mljk6nH3fB0OW');

insert into member_email (created_at, modified_at, email, member_id)
values ('2023-11-11 17:38:13', '2023-11-11 17:38:13', 'test1@email.com', '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8'),
       ('2023-11-11 17:38:13', '2023-11-11 17:38:13', 'test2@email.com', '2831ccac-aef9-4359-abbb-1d432b1b8078'),
       ('2023-11-11 17:38:13', '2023-11-11 17:38:13', 'test3@email.com', '4c624615-7123-4a63-9ade-0fd5889452cd'),
       ('2023-11-11 17:38:13', '2023-11-11 17:38:13', 'test4@email.com', 'c152ed2e-1978-4100-bf26-b0014af642b4'),
       ('2023-11-11 17:38:13', '2023-11-11 17:38:13', 'test5@email.com', '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8'),
       ('2023-11-11 17:38:13', '2023-11-11 17:38:13', 'test6@email.com', '2831ccac-aef9-4359-abbb-1d432b1b8078'),
       ('2023-11-11 17:38:13', '2023-11-11 17:38:13', 'test7@email.com', '4c624615-7123-4a63-9ade-0fd5889452cd'),
       ('2023-11-11 17:38:13', '2023-11-11 17:38:13', 'test8@email.com', 'c152ed2e-1978-4100-bf26-b0014af642b4');


INSERT INTO workspace (created_at, modified_at, description, division, image_url, subject, title)
VALUES ('2023-11-11 18:16:21.000000', '2023-11-11 18:16:21.000000', null, 'PERSONAL', null, '개인용도', 'USER1 개인워크스페이스'),
       ('2023-11-11 18:16:21.000000', '2023-11-11 18:16:21.000000', null, 'PERSONAL', null, '개인용도', 'USER2 개인워크스페이스'),
       ('2023-11-11 18:16:21.000000', '2023-11-11 18:16:21.000000', null, 'PERSONAL', null, '개인용도', 'USER3 개인워크스페이스'),
       ('2023-11-11 18:16:21.000000', '2023-11-11 18:16:21.000000', null, 'PERSONAL', null, '개인용도', 'USER4 개인워크스페이스'),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'GROUP', null, '그룹 테스트', '그룹 스페이스1'),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'GROUP', null, '그룹 테스트', '그룹 스페이스2'),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'GROUP', null, '그룹 테스트', '그룹 스페이스3'),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'GROUP', null, '그룹 테스트', '그룹 스페이스4');


insert into workspace_participant(created_at, modified_at, member_id, email, image_url, name, role, workspace_id)
VALUES
    -- 개인 워크스페이스
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8', 'user1@email.com', null, 'USER1', 'WORKSPACE_ADMIN', 1),
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078', 'user2@email.com', null, 'USER2', 'WORKSPACE_ADMIN', 2),
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4c624615-7123-4a63-9ade-0fd5889452cd', 'user3@email.com', null, 'USER3', 'WORKSPACE_ADMIN', 3),
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', 'c152ed2e-1978-4100-bf26-b0014af642b4', 'user4@email.com', null, 'USER4', 'WORKSPACE_ADMIN', 4),
    -- 그룹 스페이스1
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8', 'user1@email.com', null, 'WS5_USER1', 'WORKSPACE_ADMIN', 5),
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078', 'user2@email.com', null, 'WS5_USER2', 'PROJECT_ADMIN', 5),
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4c624615-7123-4a63-9ade-0fd5889452cd', 'user3@email.com', null, 'WS5_USER3', 'PROJECT_ADMIN', 5),
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', 'c152ed2e-1978-4100-bf26-b0014af642b4', 'user4@email.com', null, 'WS5_USER4', 'BASIC_PARTICIPANT', 5),
    -- 그룹 스페이스2
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8', 'user1@email.com', null, 'WS6_USER1', 'BASIC_PARTICIPANT', 6),
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078', 'user2@email.com', null, 'WS6_USER2', 'WORKSPACE_ADMIN', 6),
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4c624615-7123-4a63-9ade-0fd5889452cd', 'user3@email.com', null, 'WS6_USER3', 'PROJECT_ADMIN', 6),
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', 'c152ed2e-1978-4100-bf26-b0014af642b4', 'user4@email.com', null, 'WS6_USER4', 'PROJECT_ADMIN', 6),
    -- 그룹 스페이스3
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8', 'user1@email.com', null, 'WS7_USER1', 'BASIC_PARTICIPANT', 7),
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078', 'user2@email.com', null, 'WS7_USER2', 'BASIC_PARTICIPANT', 7),
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4c624615-7123-4a63-9ade-0fd5889452cd', 'user3@email.com', null, 'WS7_USER3', 'WORKSPACE_ADMIN', 7),
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', 'c152ed2e-1978-4100-bf26-b0014af642b4', 'user4@email.com', null, 'WS7_USER4', 'PROJECT_ADMIN', 7),
    -- 그룹 스페이스4
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8', 'user1@email.com', null, 'WS8_USER1', 'PROJECT_ADMIN', 8),
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078', 'user2@email.com', null, 'WS8_USER2', 'BASIC_PARTICIPANT', 8),
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4c624615-7123-4a63-9ade-0fd5889452cd', 'user3@email.com', null, 'WS8_USER3', 'BASIC_PARTICIPANT', 8),
    ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', 'c152ed2e-1978-4100-bf26-b0014af642b4', 'user4@email.com', null, 'WS8_USER4', 'WORKSPACE_ADMIN', 8);


insert into project(created_at, modified_at, description, title, workspace_id)
values ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project1', 1),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project2', 1),

       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project1', 2),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project2', 2),

       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project1', 3),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project2', 3),

       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project1', 4),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project2', 4),

       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project1', 5), -- 9
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project2', 5),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project3', 5),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project4', 5),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project5', 5),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project6', 5),

       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project1', 6), -- 15
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project2', 6),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project3', 6),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project4', 6),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project5', 6),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project6', 6),

       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project1', 7), -- 21
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project2', 7),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project3', 7),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project4', 7),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project5', 7),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project6', 7),

       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project1', 8), -- 27
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project2', 8),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project3', 8),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project4', 8),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project5', 8),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', null, 'project6', 8);


insert into project_participant(created_at, modified_at, member_id, project_id, workspace_partipant_id)
-- '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8'
-- '2831ccac-aef9-4359-abbb-1d432b1b8078'
-- '4c624615-7123-4a63-9ade-0fd5889452cd'
-- 'c152ed2e-1978-4100-bf26-b0014af642b4'
values ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8', 1, 1),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8', 2, 1),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078', 3, 2),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078', 4, 2),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4c624615-7123-4a63-9ade-0fd5889452cd', 5, 3),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4c624615-7123-4a63-9ade-0fd5889452cd', 6, 3),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', 'c152ed2e-1978-4100-bf26-b0014af642b4', 7, 4),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', 'c152ed2e-1978-4100-bf26-b0014af642b4', 8, 4),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8', 9, 5),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078', 9, 6),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4c624615-7123-4a63-9ade-0fd5889452cd', 9, 7),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', 'c152ed2e-1978-4100-bf26-b0014af642b4', 9, 8),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078', 10, 6),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4c624615-7123-4a63-9ade-0fd5889452cd', 10, 7),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', 'c152ed2e-1978-4100-bf26-b0014af642b4', 10, 8),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8', 11, 5),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', 'c152ed2e-1978-4100-bf26-b0014af642b4', 11, 8),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078', 12, 6),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4c624615-7123-4a63-9ade-0fd5889452cd', 12, 7),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078', 13, 6),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4c624615-7123-4a63-9ade-0fd5889452cd', 14, 7),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078', 15, 10),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4c624615-7123-4a63-9ade-0fd5889452cd', 15, 11),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', 'c152ed2e-1978-4100-bf26-b0014af642b4', 15, 12),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8', 16, 9),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078', 16, 10),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4c624615-7123-4a63-9ade-0fd5889452cd', 16, 11),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8', 17, 9),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', 'c152ed2e-1978-4100-bf26-b0014af642b4', 17, 12),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4c624615-7123-4a63-9ade-0fd5889452cd', 18, 11),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', 'c152ed2e-1978-4100-bf26-b0014af642b4', 18, 12),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078', 19, 10),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4c624615-7123-4a63-9ade-0fd5889452cd', 19, 11),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8', 20, 9),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', 'c152ed2e-1978-4100-bf26-b0014af642b4', 20, 12),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8', 21, 13),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078', 21, 14),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4c624615-7123-4a63-9ade-0fd5889452cd', 21, 15),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', 'c152ed2e-1978-4100-bf26-b0014af642b4', 21, 16),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078', 22, 14),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8', 22, 13),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078', 23, 14),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4c624615-7123-4a63-9ade-0fd5889452cd', 23, 15),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', 'c152ed2e-1978-4100-bf26-b0014af642b4', 23, 16),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4c624615-7123-4a63-9ade-0fd5889452cd', 24, 15),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078', 24, 14),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8', 24, 13),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078', 25, 14),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4c624615-7123-4a63-9ade-0fd5889452cd', 25, 15),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', 'c152ed2e-1978-4100-bf26-b0014af642b4', 26, 16),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8', 27, 17),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078', 27, 18),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4c624615-7123-4a63-9ade-0fd5889452cd', 27, 19),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', 'c152ed2e-1978-4100-bf26-b0014af642b4', 27, 20),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', 'c152ed2e-1978-4100-bf26-b0014af642b4', 28, 20),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4c624615-7123-4a63-9ade-0fd5889452cd', 28, 19),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', 'c152ed2e-1978-4100-bf26-b0014af642b4', 29, 20),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8', 30, 17),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078', 30, 18),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8', 31, 17),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2831ccac-aef9-4359-abbb-1d432b1b8078', 32, 18),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4c624615-7123-4a63-9ade-0fd5889452cd', 32, 19),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', 'c152ed2e-1978-4100-bf26-b0014af642b4', 32, 20);


insert into board(created_at, modified_at, title, project_id)
values ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 1),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2번 보드', 1),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '3번 보드', 1),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 2),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2번 보드', 2),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 3),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2번 보드', 3),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 4),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 5),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 6),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 8),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 9),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2번 보드', 9),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '3번 보드', 9),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4번 보드', 9),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 10),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2번 보드', 10),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 12),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 13),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2번 보드', 13),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '3번 보드', 13),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 14),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2번 보드', 14),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 15),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2번 보드', 15),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 16),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2번 보드', 16),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '3번 보드', 16),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4번 보드', 16),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 19),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 20),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2번 보드', 20),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 21),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2번 보드', 21),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '3번 보드', 21),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 22),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2번 보드', 22),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 23),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2번 보드', 23),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 24),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2번 보드', 24),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 25),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2번 보드', 25),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '3번 보드', 25),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '4번 보드', 25),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '5번 보드', 25),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 26),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2번 보드', 26),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 28),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2번 보드', 28),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 29),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2번 보드', 29),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '3번 보드', 29),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 30),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '2번 보드', 30),
       ('2023-11-11 18:18:13.000000', '2023-11-11 18:18:13.000000', '1번 보드', 32);


insert into task (created_at, modified_at, project_id, board_id, title, content, emergency, start_date, end_date, progress_status, creator_id, task_manager_id)
values ('2023-11-11 18:18:13', '2023-11-11 18:18:13', 9, 12, 'Task1', null, false, '2023-11-01 00:00:00', '2023-11-30 00:00:00', 'TODO', 9, 9),
       ('2023-11-11 18:18:13', '2023-11-11 18:18:13', 9, 12, 'Task2', '내용 내용 내용 내용 내용 내용 내용 내용 내용 내용 내용 내용 내용 내용 내용 내용 내용 내용 내용 내용 ', false, '2023-11-01 00:00:00', '2023-11-30 00:00:00', 'TODO', 9, 9),
       ('2023-11-11 18:18:13', '2023-11-11 18:18:13', 9, 12, 'Task3', null, true, '2023-11-03 00:00:00', '2023-11-30 00:00:00', 'TODO', 9, 10),
       ('2023-11-11 18:18:13', '2023-11-11 18:18:13', 9, 12, 'Task4', null, false, '2023-11-01 00:00:00', '2023-11-02 00:00:00', 'TODO', 9, 11),
       ('2023-11-11 18:18:13', '2023-11-11 18:18:13', 9, 12, 'Task5', null, false, '2023-11-01 00:00:00', '2023-11-10 00:00:00', 'TODO', 10, null),
       ('2023-11-11 18:18:13', '2023-11-11 18:18:13', 9, 14, 'Task6', null, false, '2023-11-05 00:00:00', '2023-11-11 00:00:00', 'TODO', 9, 9),
       ('2023-11-11 18:18:13', '2023-11-11 18:18:13', 9, 15, 'Task7', null, true, '2023-11-01 00:00:00', '2023-11-01 00:00:00', 'TODO', 11, 9),
       ('2023-11-11 18:18:13', '2023-11-11 18:18:13', 9, 12, 'Task8', null, false, '2023-11-11 00:00:00', '2023-11-23 00:00:00', 'TODO', 9, 12),
       ('2023-11-11 18:18:13', '2023-11-11 18:18:13', 9, 15, 'Task9', null, false, '2023-11-21 00:00:00', '2023-11-22 00:00:00', 'TODO', 10, null),
       ('2023-11-11 18:18:13', '2023-11-11 18:18:13', 9, 15, 'Task10', null, false, '2023-11-01 00:00:00', '2023-11-30 00:00:00', 'TODO', 12, 10),

       ('2023-11-11 18:18:13', '2023-11-11 18:18:13', 15, 24, 'Task1', null, false, '2023-11-01 00:00:00', '2023-11-30 00:00:00', 'TODO', 22, 22),
       ('2023-11-11 18:18:13', '2023-11-11 18:18:13', 15, 25, 'Task2', '내용 내용 내용 내용 내용 내용 내용 내용 내용 내용 내용 내용 내용 내용 내용 내용 내용 내용 내용 내용 ', false, '2023-11-01 00:00:00', '2023-11-30 00:00:00', 'TODO', 22, 23),
       ('2023-11-11 18:18:13', '2023-11-11 18:18:13', 15, 25, 'Task3', null, true, '2023-11-03 00:00:00', '2023-11-30 00:00:00', 'TODO', 23, 23),
       ('2023-11-11 18:18:13', '2023-11-11 18:18:13', 15, 25, 'Task4', null, false, '2023-11-01 00:00:00', '2023-11-02 00:00:00', 'TODO', 24, 24),
       ('2023-11-11 18:18:13', '2023-11-11 18:18:13', 15, 25, 'Task5', null, false, '2023-11-01 00:00:00', '2023-11-10 00:00:00', 'TODO', 22, null),
       ('2023-11-11 18:18:13', '2023-11-11 18:18:13', 15, 24, 'Task6', null, false, '2023-11-05 00:00:00', '2023-11-11 00:00:00', 'TODO', 22, null),
       ('2023-11-11 18:18:13', '2023-11-11 18:18:13', 15, 24, 'Task7', null, true, '2023-11-01 00:00:00', '2023-11-01 00:00:00', 'TODO', 24, 22),
       ('2023-11-11 18:18:13', '2023-11-11 18:18:13', 15, 24, 'Task8', null, false, '2023-11-11 00:00:00', '2023-11-23 00:00:00', 'TODO', 24, 24),
       ('2023-11-11 18:18:13', '2023-11-11 18:18:13', 15, 24, 'Task9', null, false, '2023-11-21 00:00:00', '2023-11-22 00:00:00', 'TODO', 24, null),
       ('2023-11-11 18:18:13', '2023-11-11 18:18:13', 15, 25, 'Task10', null, false, '2023-11-01 00:00:00', '2023-11-30 00:00:00', 'TODO', 24, 24);

