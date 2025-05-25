ALTER TABLE review_entity
DROP
FOREIGN KEY FKb94lawr3tr901b9powev3hlhi;

ALTER TABLE review_entity
DROP
FOREIGN KEY FKjvsqk3tup6xvy163jkw6v79t2;

CREATE TABLE tbl_genre
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    status     INT NULL,
    is_display INT NULL,
    is_active  INT NULL,
    new_data   VARCHAR(4000) NULL,
    name       VARCHAR(255) NULL COMMENT 'tên thể loại ngắn gọn (ví dụ: "Action", "Science Fiction")',
    title      VARCHAR(2000) NULL,
    slug       VARCHAR(255) NULL,
    `rank`     INT NULL,
    CONSTRAINT pk_tbl_genre PRIMARY KEY (id)
);

DROP TABLE episodes;

DROP TABLE movies;

DROP TABLE review_entity;

DROP TABLE review_entity_seq;

DROP TABLE table_country;

DROP TABLE tbl_reviews_seq;

DROP TABLE tbl_users_seq;

DROP TABLE users_entity;

DROP TABLE users_entity_seq;