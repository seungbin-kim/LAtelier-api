INSERT INTO category (id, parent_id, category_name) -- 1
VALUES (nextval('category_seq'), null, '라이프 스타일');
INSERT INTO category (id, parent_id, category_name) -- 2
VALUES (nextval('category_seq'), 1, '인테리어');
INSERT INTO category (id, parent_id, category_name) -- 3
VALUES (nextval('category_seq'), 1, '생활용품');
INSERT INTO category (id, parent_id, category_name) -- 4
VALUES (nextval('category_seq'), 1, '가구');

INSERT INTO category (id, parent_id, category_name) -- 5
VALUES (nextval('category_seq'), null, '음료 / 요리');
INSERT INTO category (id, parent_id, category_name) -- 6
VALUES (nextval('category_seq'), 5, '에피타이저');
INSERT INTO category (id, parent_id, category_name) -- 7
VALUES (nextval('category_seq'), 5, '베이커리');
INSERT INTO category (id, parent_id, category_name) -- 8
VALUES (nextval('category_seq'), 5, '디저트');
INSERT INTO category (id, parent_id, category_name) -- 9
VALUES (nextval('category_seq'), 5, '커피');
INSERT INTO category (id, parent_id, category_name) -- 10
VALUES (nextval('category_seq'), 5, '한식');
INSERT INTO category (id, parent_id, category_name) -- 11
VALUES (nextval('category_seq'), 5, '중식');
INSERT INTO category (id, parent_id, category_name) -- 12
VALUES (nextval('category_seq'), 5, '양식');

INSERT INTO category (id, parent_id, category_name) -- 13
VALUES (nextval('category_seq'), null, '사진 / 영상');
INSERT INTO category (id, parent_id, category_name) -- 14
VALUES (nextval('category_seq'), 13, '편집');
INSERT INTO category (id, parent_id, category_name) -- 15
VALUES (nextval('category_seq'), 13, '촬영');

INSERT INTO category (id, parent_id, category_name) -- 16
VALUES (nextval('category_seq'), null, '운동 / 건강');
INSERT INTO category (id, parent_id, category_name) -- 17
VALUES (nextval('category_seq'), 16, '요가');
INSERT INTO category (id, parent_id, category_name) -- 18
VALUES (nextval('category_seq'), 16, '헬스');

INSERT INTO category (id, parent_id, category_name) -- 19
VALUES (nextval('category_seq'), null, '전기 / 전자');
INSERT INTO category (id, parent_id, category_name) -- 20
VALUES (nextval('category_seq'), 19, '로봇');
INSERT INTO category (id, parent_id, category_name) -- 21
VALUES (nextval('category_seq'), 19, 'IoT');

INSERT INTO category (id, parent_id, category_name) -- 22
VALUES (nextval('category_seq'), null, '공예');
INSERT INTO category (id, parent_id, category_name) -- 23
VALUES (nextval('category_seq'), 22, '조립 / 핸드메이드');
INSERT INTO category (id, parent_id, category_name) -- 24
VALUES (nextval('category_seq'), 22, '스케치 / 드로잉');
INSERT INTO category (id, parent_id, category_name) -- 25
VALUES (nextval('category_seq'), 22, '동양화 / 서양화');
INSERT INTO category (id, parent_id, category_name) -- 26
VALUES (nextval('category_seq'), 22, '바느질 / 뜨개질');
INSERT INTO category (id, parent_id, category_name) -- 27
VALUES (nextval('category_seq'), 22, '캐리커쳐');
INSERT INTO category (id, parent_id, category_name) -- 28
VALUES (nextval('category_seq'), 22, '프라모델');
INSERT INTO category (id, parent_id, category_name) -- 29
VALUES (nextval('category_seq'), 22, '악세사리');
INSERT INTO category (id, parent_id, category_name) -- 30
VALUES (nextval('category_seq'), 22, '미니어쳐');
INSERT INTO category (id, parent_id, category_name) -- 31
VALUES (nextval('category_seq'), 22, '수채화');
INSERT INTO category (id, parent_id, category_name) -- 32
VALUES (nextval('category_seq'), 22, '비즈');

INSERT INTO category (id, parent_id, category_name) -- 33
VALUES (nextval('category_seq'), null, '음악');
INSERT INTO category (id, parent_id, category_name) -- 34
VALUES (nextval('category_seq'), 33, '악기');
INSERT INTO category (id, parent_id, category_name) -- 35
VALUES (nextval('category_seq'), 33, '노래');

INSERT INTO category (id, parent_id, category_name) -- 36
VALUES (nextval('category_seq'), null, '언어');
INSERT INTO category (id, parent_id, category_name) -- 37
VALUES (nextval('category_seq'), 36, '제 2 외국어');
INSERT INTO category (id, parent_id, category_name) -- 38
VALUES (nextval('category_seq'), 36, '한국어');
INSERT INTO category (id, parent_id, category_name) -- 39
VALUES (nextval('category_seq'), 36, '영어');

INSERT INTO category (id, parent_id, category_name) -- 40
VALUES (nextval('category_seq'), null, '교육');
INSERT INTO category (id, parent_id, category_name) -- 41
VALUES (nextval('category_seq'), 40, '국 / 영 / 수');
INSERT INTO category (id, parent_id, category_name) -- 42
VALUES (nextval('category_seq'), 40, '개발 / 코딩');
INSERT INTO category (id, parent_id, category_name) -- 43
VALUES (nextval('category_seq'), 40, '사회 / 과학');
INSERT INTO category (id, parent_id, category_name) -- 44
VALUES (nextval('category_seq'), 40, '한국사');