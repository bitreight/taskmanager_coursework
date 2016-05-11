﻿Create Database taskmanager;
Go

Use taskmanager;

Create Table Developers (
	[id] int Primary Key Identity(1,1) not null,
	[username] nvarchar(10) not null,
	[password] binary(32) not null,
	[dev_name] nvarchar(20) not null,
	[surname] nvarchar(20) not null,
	[patronymic] nvarchar(20) not null,
	[position] nvarchar(50) not null,
	[isAdmin] bit not null
);

Create Table Tasks (
	[id] int Primary Key Identity(1,1) not null,
	[number] int not null,
	[task_name] nvarchar(60) not null,
	[description] nvarchar(500),
	[deadline] date not null,
	[priority] int not null Check ([priority] In(1,2,3)),
	[isCompleted] bit not null	
);

Create Table Developers_Tasks (
	[id] int Primary Key Identity(1,1) not null,
	[task_id] int Foreign Key References Tasks(id) not null,	
	[dev_id] int Foreign Key References Developers(id) not null
);
Go

Create Procedure createDeveloper
	@username nvarchar(10), 
	@password binary(32),
	@dev_name nvarchar(20),
	@surname nvarchar(20),
	@patronymic nvarchar(20),
	@position nvarchar(50),
	@isAdmin bit
As
Begin
	Set Nocount On;
	Insert Into Developers Values (@username, @password, @dev_name, @surname, @patronymic, @position, @isAdmin);
End
Go

Create Procedure updateDeveloper
	@dev_id int,
	@username nvarchar(10), 
	@password binary(32),
	@dev_name nvarchar(20),
	@surname nvarchar(20),
	@patronymic nvarchar(20),
	@position nvarchar(50),
	@isAdmin bit
As
Begin
	Set Nocount On;
	Update Developers
	Set username = @username, [password] = @password, dev_name = @dev_name, surname = @surname,
		patronymic = @patronymic, position = @position, isAdmin = @isAdmin
	Where id = @dev_id;	
End
Go	

Create Function getDevelopers()
Returns Table
As 
Return
(
	Select username, dev_name, surname, patronymic, position, isAdmin From Developers
)
Go

Create Procedure createTask
	@number int,
	@task_name nvarchar(60),
	@description nvarchar(500),
	@deadline date,
	@priority int = 3,
	@isCompleted bit
As
Begin
	Set Nocount On;
	Insert Into Tasks Values (@number, @task_name, @description, @deadline, @priority, @isCompleted);
End

Go

--Trigger checks if there are 2 tasks with the highest priority (1) in the table 'Tasks'.
--Trigger fires when inserting in the table or updating table 'Tasks'.
Create Trigger checkChangeTaskPriorityTrigger
	On Tasks
	For Insert, Update
As
Begin
	Set Nocount On;
	If((Select i.[priority] From inserted i) = 1) and
		((Select Count(*) From Tasks Where [priority] = 1) = 3)
		Begin
			Rollback Transaction
			Print 'В списке уже имеется две задачи с наивысшим приоритетом.'
		End
End
Go

Create Procedure updateTask
	@task_id int,
	@number int,
	@task_name nvarchar(60),
	@description nvarchar(500),
	@deadline date,
	@priority int,
	@isCompleted bit
As
Begin
	Set Nocount On;
		Update Tasks 
		Set number = @number, task_name = @task_name, [description] = @description, deadline = @deadline,
			[priority] = @priority, isCompleted = @isCompleted
		Where id = @task_id;	
End

Go

--Create Function getTasks()
--Returns
--@Tasks Table
--(
--	[id] int, [number] int, [task_name] nvarchar(60), [description] nvarchar(500),
--	[deadline] date, [priority] int, [isCompleted] bit
--)
--As
--Begin
--	Declare @devs nvarchar(100) = '';
--	Select task_id From Developers_Tasks
--	Group By task_id

--	Select Tasks.*, Developers.dev_name From Tasks 
--	Join Developers_Tasks On Tasks.id = Developers_Tasks.task_id
--	Join Developers On Developers.id = Developers_Tasks.dev_id
--	--Where 
--	--Group By Tasks.id, number, Tasks.task_name, [description], deadline, [priority], isCompleted, Developers.dev_name
--	Order By Tasks.id
	
--	Return
--End
--Go

--This function finds developers having the selected task and concat their full names into one row.
--It is to be called from getTasks() procedure.
Create Function selectConcat(@task_id int)
Returns nvarchar(MAX)
As
Begin
	Declare @result nvarchar(MAX) = '';
	Select @result = @result + task_dev.dev_name + ' ' + 
					 Left(task_dev.surname, 1) + '.' +
					 Left(task_dev.patronymic, 1) + '. '
	From (Select Developers_Tasks.task_id, Developers.dev_name, Developers.surname, Developers.patronymic 
		  From Developers_Tasks Join Developers On Developers_Tasks.dev_id = Developers.id) task_dev
	Where task_dev.task_id = @task_id;
	Return @result;
End
Go

Create Function getTasksByDeveloper(@dev_id int)
Returns Table
As 
Return
(
	Select Top 100 percent Tasks.*, Developers.dev_name From Tasks 
	Join Developers_Tasks On Tasks.id = Developers_Tasks.task_id
	Join Developers On Developers.id = Developers_Tasks.dev_id
	Where Developers.id = @dev_id
	Order By Tasks.id
)
Go

Create Procedure assignTask
	@dev_id int,
	@task_id int
	--@result bit = 0 OUTPUT
As	
Begin	
	Set Nocount On;
	If Not Exists(Select * From Developers_Tasks Where 
					Developers_Tasks.task_id = @task_id and 
					Developers_Tasks.dev_id = @dev_id)
		Begin			
			Insert Into Developers_Tasks Values (@task_id, @dev_id);
		End	
End
Go

--Trigger checks if the chosen task is assigned to two developers or if the chosen developer has 3 tasks.
--Trigger fires when inserting in the table 'Developers_Tasks'.
Create Trigger checkAssignTaskTrigger
	On Developers_Tasks 
	For Insert
As
Begin
	Set Nocount On;
	If (Select Count(*) From Developers_Tasks Where 
			Developers_Tasks.task_id = (Select i.task_id From (Select * From inserted) i)) = 3
		Begin
			Rollback Transaction
			Print 'Данная задача уже назначена двум разработчикам.'
		End
	Else if (Select Count(*) From Developers_Tasks Where 
			Developers_Tasks.dev_id = (Select i.dev_id From (Select * From inserted) i)) = 4
		Begin
			Rollback Transaction
			Print 'Выбранному разработчику уже назначено 3 задачи.'
		End
End
Go

Create Procedure deassignTask
	@dev_id int,
	@task_id int
As	
Begin	
	Set Nocount On;
	Delete From Developers_Tasks 
	Where task_id = @task_id and dev_id = @dev_id;		
End
Go

Create Procedure setTaskCompletionByUser
	@task_id int,
	@isCompleted bit
As
Begin
	Set Nocount On;
	Update Tasks
	Set isCompleted = @isCompleted
	Where id = @task_id;
End

--Execute addDeveloper 'test2',123,'name','surname','patronymic','test',0;
--Delete From Developers;

--Execute addTask 100,'task2','testtask','01-01-2016', 3, 0;
--Delete From Tasks;

--Execute assignTask 1, 1; 
--Delete From Developers_Tasks;

--Create Procedure assignTask
--	@oldDeveloper_id int = 0,
--	@newDeveloper_id int = 0,
--	@task_id int,
--	@result bit = 0 OUTPUT
--As	
--Begin
--	Declare @relation_id int;
--	Select @relation_id = (Select id From Developers_Tasks 
--					Where Developers_Tasks.task_id = @task_id and 
--						  Developers_Tasks.developer_id = @oldDeveloper_id or
--						  Developers_Tasks.developer_id = @newDeveloper_id);
--	If @relation_id is NULL
--		Begin
--			Insert Into Developers_Tasks Values (@newDeveloper_id, @task_id, 3);
--			Select @result = 1;
--		End
--	Else If @newDeveloper_id = 0
--		Begin
--			Delete From Developers_Tasks Where id = @relation_id;
--			Select @result = 1;
--		End
--	Else
--		Begin
--			Update Developers_Tasks Set developer_id = @newDeveloper_id
--			Where id = @relation_id;
--			Select @result = 1;
--		End
--End
--Execute assignTask 0,5,8;