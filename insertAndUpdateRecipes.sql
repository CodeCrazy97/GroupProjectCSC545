
create or replace procedure insertAndUpdateRecipes (oldTitle in recipes.title%type, newTitle in recipes.title%type)
as	

	t recipes.title%type;
	flag boolean := false;
	
	cursor recipesCursor is
		select title from recipes;
begin
	open recipesCursor;
	
	loop
		fetch recipesCursor into t;
		exit when recipesCursor%notfound;
		
		if t = oldTitle then
			flag := true;				-- an existing course with the same name AND credits has been found
			exit;
		end if;
		
	end loop;
	
	close recipesCursor;
	
	if !flag then
		dbms_output.put_line('Deletion of ' || t || ' failed');
	else
		delete from recipes where title = t;
	end if;
end;
/

/*
declare
	oldTitle recipes.title%type := 'recipe1';
	newTitle recipes.title%type := 'recipe2';
begin
	insertAndUpdateRecipes(oldTitle, newTitle);
end;
/
*/