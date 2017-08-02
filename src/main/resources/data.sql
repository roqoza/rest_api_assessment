insert into app_user(id, username, phone, password, login_date, token) values (1,'admin','12345','admin', null, null);
insert into app_user(id, username, phone, password, login_date, token) values (2,'user','654321','user', null, null);

insert into app_user_roles(app_user_id, roles) values (1,'ADMIN');
insert into app_user_roles(app_user_id, roles) values (1,'USER');
insert into app_user_roles(app_user_id, roles) values (2,'USER');
