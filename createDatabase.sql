
/*
Group project - SQL Script
CSC 545, Spring 2019 

Group 3:
Evan Wright, Chris Ward, Corey Robinson, Ethan Vaughan
*/

set define off;

drop trigger ingredients_update_t2;
drop trigger recipes_update_t1;
drop trigger recipes_update_t2;
drop trigger meals_update_t1;
drop trigger meals_update_t2;
drop trigger mealplan_update_t1;

drop table INGREDIENTS cascade constraints;
drop table RECIPES cascade constraints;
drop table CALLSFOR cascade constraints;
drop table MEALS cascade constraints;
drop table SERVEDDURINGMEAL cascade constraints;
drop table MEALPLAN cascade constraints;
drop table MEALDAY cascade constraints;
 
create table ingredients(
name varchar(100) primary key,
foodGroup varchar(100),
inStock varchar2(1) check (inStock in ('Y', 'N')),					-- This determines if the ingredient is in stock ('Y' means it is; 'N' means it isn't)
nutritionFacts varchar(3000)
);

create table recipes(
title varchar(100) primary key,
instructions varchar(3000),
category varchar(100)
);

create table callsFor(
ingredientName varchar(100),
recipeTitle varchar(100),
primary key (ingredientName, recipeTitle),
constraint fk_callsFor_ingredientname foreign key (ingredientName) references ingredients(name) on delete cascade,
constraint fk_callsFor_recipetitle foreign key (recipeTitle) references recipes(title) on delete cascade
);

create table meals(
name varchar(100) primary key
);

create table servedDuringMeal(
recipeTitle varchar(100),
mealName varchar(100),
primary key (recipeTitle, mealName),
constraint fk_servedDuringMeal_recipetitle foreign key (recipeTitle) references recipes(title) on delete cascade,
constraint fk_servedDuringMeal_mealname foreign key (mealName) references meals(name) on delete cascade
);


create table mealPlan (
title varchar(100) primary key,
nextOccurrence date,   -- Date when the meal plan is scheduled to occur next (default date is in the past, so it is not scheduled to occur unless the user says so)
constraint unique_nextOccurrence unique (nextOccurrence)
);

create table mealDay(
dayOfWeek varchar(9) check (dayOfWeek in ('Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday')), 
mealTitle varchar(100),
mealName varchar(100),
mealPlanTitle varchar(100),
primary key (dayOfWeek, mealPlanTitle, mealTitle),
constraint fk_mealDay_mealname foreign key (mealName) references meals(name) on delete cascade,
constraint fk_mealDay_mealplantitle foreign key (mealPlanTitle) references mealPlan(title) on delete cascade
);

-- ++++++++++++++++++++++++++++++++++++++++++++
-- Below are the triggers. ++++++++++++++++++++
-- ++++++++++++++++++++++++++++++++++++++++++++


-- trigger to update CALLSFOR when ingredient name is changed.
create or replace trigger ingredients_update_t2
after update on ingredients					-- event
referencing 
new as newrow
old as oldrow
for each row
begin									-- action
	update callsFor set ingredientName = :newrow.name where ingredientName = :oldrow.name;	
end;
/

-- trigger to update CALLSFOR when recipes title is changed.
create or replace trigger recipes_update_t1
after update on recipes					-- event
referencing 
new as newrow
old as oldrow
for each row
begin									-- action
	update callsFor set recipeTitle = :newrow.title where recipeTitle = :oldrow.title;	
end;
/


-- trigger to update SERVEDDURINGMEAL when recipe title is changed.
create or replace trigger recipes_update_t2
after update on recipes					-- event
referencing 
new as newrow
old as oldrow
for each row
begin									-- action
	update servedDuringMeal set recipeTitle = :newrow.title where recipeTitle = :oldrow.title;	
end;
/

-- trigger to update SERVEDDURINGMEAL when meal name is changed/deleted.
create or replace trigger meals_update_t1
after update on meals					-- event
referencing 
new as newrow
old as oldrow
for each row
begin									-- action
	update servedDuringMeal set mealName = :newrow.name where mealName = :oldrow.name;	
end;
/

-- trigger to update MEALDAY when meal name is changed/deleted
create or replace trigger meals_update_t2
after update on meals					-- event
referencing 
new as newrow
old as oldrow
for each row
begin									-- action
	update mealDay set mealName = :newrow.name where mealName = :oldrow.name;	
end;
/

-- trigger to update MEALDAY when meal plan title is changed/deleted
create or replace trigger mealplan_update_t1
after update on mealPlan					-- event
referencing 
new as newrow
old as oldrow
for each row
begin									-- action
	update mealDay set mealPlanTitle = :newrow.title where mealPlanTitle = :oldrow.title;	
end;
/




insert into ingredients values('broccoli','vegetables','Y','high in fiber, very high in vitamin C and has potassium, B6 and vitamin A'); 
insert into ingredients values('tuna','fish','N','calories per 8oz: 116 calories');
insert into ingredients values('hamburger','meat','N','Calories per single patty: 266');  
insert into ingredients values('red delicious apple','fruit','Y','Calories per apple: 68');
insert into ingredients values('turkey burger','meat','N','Caloriesper patty: 209');                           
insert into ingredients values('asparagus','vegetables','N','Calories per stick: 7');
insert into ingredients values('2% milk','dairy','Y','Calories per gallon: 1024');
insert into ingredients values('almond milk','dairy','N','Calories per half gallon: 178');
insert into ingredients values('heavy whipping cream','dairy','Y','Calories per ounce: 40');
insert into ingredients values('angel hair','pasta','Y','Calories per 8 oz: 369');                           
insert into ingredients values('wheat bread','grain','Y','Calories per loaf: 700');
insert into ingredients values('salt','seasoning','Y','Calories per tablespoon: 15');
insert into ingredients values('pepper','seasoning','N','Calories per tablespoon: 9');
insert into ingredients values('texas pete hot sauce','seasoning','N','Calories per table spoon: 17');
insert into ingredients values('alfredo sauce','seasoning','Y','Calories per 8oz: 296');                           
insert into ingredients values('spaghetti noodles','pasta','N','Calories per 8oz: 166');
insert into ingredients values('mahi mahi','fish','Y','Calories per 8oz: 320');
insert into ingredients values('sour cream','dairy','N','Calories per 8oz: 154');
insert into ingredients values('salmon','fish','N','Calories per 8oz: 460');
insert into ingredients values('pineapple','fruit','Y','Calories per whole pineapple: 1266');                           
insert into ingredients values('carrots','vegetables','Y','Calories per 8oz: 38');
insert into ingredients values('green beans','vegetables','Y','Calories: 266');
insert into ingredients values('orange','fruit','N','Calories per fruit: 97');
insert into ingredients values('porkchop','meat','Y','Calories per chop: 250');
insert into ingredients values('venision','meat','Y','Calories per 8oz: 300');                           
insert into ingredients values('pear','fruit','Y','Calories per pear: 50');
insert into ingredients values('watermelon','fruit','Y','Calories per watermelon: 686');
insert into ingredients values('cocoa powder','seasoning','N','Calories per 8 oz: 196'); 
insert into ingredients values('colby cheese','dairy','N','Calories per 8oz: 122');
insert into ingredients values('pepperjack cheese','dairy','Y','Calories per 8oz: 85');                           
insert into ingredients values('lemon pepper seasoing','seasoning','Y','Calories per 8oz: 18');

insert into recipes values('Milky Asparagus','mix ingredients','Healthy life foods');
insert into recipes values ('Salty pepper', 'salt pepper & enjoy!', 'Mediterranean food');


insert into callsFor values ('asparagus', 'Milky Asparagus');
insert into callsFor values ('2% milk', 'Milky Asparagus');

insert into callsFor values ('salt', 'Salty pepper');
insert into callsFor values ('pepper', 'Salty pepper');


commit;
