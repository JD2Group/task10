CREATE DATABASE university;
USE university;


create table professors
(
    id      bigint auto_increment
        primary key,
    name    varchar(255) not null,
    surname varchar(255) not null
);

create table courses
(
    id           bigint auto_increment
        primary key,
    createdDate  datetime(6)  null,
    title        varchar(255) null,
    professor_id bigint       null,
    constraint FKsj4okul9jc8m3p4tsnuobqjpb
        foreign key (professor_id) references professors (id)
);

create table students
(
    id      bigint auto_increment
        primary key,
    name    varchar(255) not null,
    surname varchar(255) not null
);

insert into students (id, name, surname, email)
    value (1,'Deleted','Deleted','Deleted');

create table students_courses
(
    student_id bigint not null,
    course_id  bigint not null,
    primary key (student_id, course_id),
    constraint FKcc42107lsifo5rjyjlhdu6i6u
        foreign key (course_id) references courses (id),
    constraint FKr16q8s9m6kr7xupi4fw9iqpgl
        foreign key (student_id) references students (id)
);

create table tasks
(
    id          bigint auto_increment
        primary key,
    createdDate datetime(6)  null,
    description longtext     null,
    title       varchar(255) null,
    course_id   bigint       null,
    constraint FKopldg47bgaarlampi2f6wees3
        foreign key (course_id) references courses (id)
);

create table solutions
(
    id             bigint auto_increment
        primary key,
    createdDate    datetime(6) null,
    mark           int         null,
    readyForReview bit         null,
    response       longtext    null,
    review         longtext    null,
    updatedDate    datetime(6) null,
    student_id     bigint      null,
    task_id        bigint      null,
    constraint FK1pcfy9sr41qrjfjnryxw7ld1o
        foreign key (student_id) references students (id),
    constraint FKbqabgk1lnu9xg0rbmno60q2tw
        foreign key (task_id) references tasks (id)
);

insert into tasks ( description, title)
 value ('The task has been deleted','Canceled task');

