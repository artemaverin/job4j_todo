create table if not exists tasks
(
   id SERIAL PRIMARY KEY,
   title TEXT,
   description TEXT,
   created TIMESTAMP,
   done BOOLEAN
);