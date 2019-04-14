/*
Group project - SQL Script
CSC 545, Spring 2019 

Group 3:
Evan Wright, Chris Ward, Corey Robinson, Ethan Vaughan
*/

drop table INGREDIENTS cascade constraints;
drop table RECIPES cascade constraints;
drop table CALLSFOR cascade constraints;
drop table MEALS cascade constraints;
drop table SERVEDDURINGMEAL cascade constraints;
drop table MEALPLAN cascade constraints;
drop table MEALDAY cascade constraints;
 
create table ingredients(
name varchar(100) primary key,
foodGroup varchar(3000) not null,
inStock varchar2(1) check (inStock in ('Y', 'N')),					-- This determines if the ingredient is in stock ('Y' means it is; 'N' means it isn't)
nutritionFacts varchar(3000) not null
);

create table recipes(
title varchar(100) primary key,
instructions varchar(3000) not null,
category varchar(100) not null
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
nextOccurrence date default to_date('1900-01-01', 'YYYY-MM-DD')   -- Date when the meal plan is scheduled to occur next (default date is in the past, so it is not scheduled to occur unless the user says so)
);

create table mealDay(
dayOfWeek varchar(9), 
mealTitle varchar(100),
mealName varchar(100),
mealPlanTitle varchar(100),
primary key (dayOfWeek, mealTitle, mealName, mealPlanTitle),
constraint fk_mealDay_mealname foreign key (mealName) references meals(name) on delete cascade,
constraint fk_mealDay_mealplantitle foreign key (mealPlanTitle) references mealPlan(title) on delete cascade
);






