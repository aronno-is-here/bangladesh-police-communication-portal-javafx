-- Seed minimal data for demo
insert into users(id, username, police_id, password_hash, role, name, photo_url, active) values
  (1, 'admin', null, '', 'ADMIN', 'System Administrator', null, true);
insert into users(id, username, police_id, password_hash, role, name, photo_url, active) values
  (2, 'rahman', 'BD001', '', 'OFFICER', 'Officer Rahman', null, true),
  (3, 'khan',   'BD002', '', 'OFFICER', 'Officer Khan',   null, true),
  (4, 'ali',    'BD003', '', 'OFFICER', 'Officer Ali',    null, true);

insert into chat_rooms(id, name, status, created_at) values
  (1, 'Operation Sunrise', 'ACTIVE', CURRENT_TIMESTAMP());

-- Room officers
insert into room_users(room_id, user_id) values (1,2), (1,3);

-- Criminal records sample
insert into criminal_records(id, name, age, gender, hometown, national_id, birth_date, photo_url, description)
values (1,'Ahmed Hassan',32,'Male','Dhaka','1234567890', PARSEDATETIME('1992-03-15','yyyy-MM-dd'), null,'Repeat offender');

insert into crimes(id, record_id, type, date, location, status)
values (1,1,'Theft', CURRENT_TIMESTAMP(), 'Gulshan, Dhaka', 'Under Investigation');
