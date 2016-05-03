Create Database taskmanager;
Go

Use taskmanager;

Create Table Developers (
	[id] int Primary Key Identity(1,1) not null,
	[username] nvarchar(10) not null,
	[password] binary(32) not null,
	[name] nvarchar(20) not null,
	[surname] nvarchar(20) not null,
	[patronymic] nvarchar(20) not null,
	[position] nvarchar(50) not null,
	[isAdmin] bit not null
);

Create Table Tasks (
	[id] int Primary Key Identity(1,1) not null,
	[number] int not null,
	[name] nvarchar(60) not null,
	[description] nvarchar(500),
	[deadline] date not null,
	[isCompleted] bit not null	
);

Create Table Developers_Tasks (
	[id] int Primary Key Identity(1,1) not null,
	[developer_id] int Foreign Key References Developers(id) not null,
	[task_id] int Foreign Key References Tasks(id) not null,
	[priority] int not null Check ([priority] In(1,2,3))
);
Go

Create Procedure addDeveloper
	@username nvarchar(10), 
	@password binary(32),
	@name nvarchar(20),
	@surname nvarchar(20),
	@patronymic nvarchar(20),
	@position nvarchar(50),
	@isAdmin bit
As
Begin
	Set Nocount On;
	Insert Into Developers Values (@username, @password, @name, @surname, @patronymic, @position, @isAdmin);
End
Go
--Execute addDeveloper 'test',123,name,surname,patronymic,test,0;
--Delete From Developers;
Go

Create Procedure addTask
	@number int,
	@name nvarchar(60),
	@description nvarchar(500),
	@deadline date,
	@isCompleted bit
As
Begin
	Set Nocount On;
	Insert Into Tasks Values (@number, @name, @description, @deadline, @isCompleted);
End

Go
--Execute addTask 100,task,testtask,'01-01-2016',0;
--Delete From Tasks;
Go

Create Procedure assignTask
	@oldDeveloper_id int = 0,
	@newDeveloper_id int = 0,
	@task_id int,
	@result bit = 0 OUTPUT
As	
Begin
	Declare @relation_id int;
	Select @relation_id = (Select id From Developers_Tasks 
					Where Developers_Tasks.task_id = @task_id and 
						  Developers_Tasks.developer_id = @oldDeveloper_id);
	If @relation_id = NULL
		Begin
			Insert Into Developers_Tasks Values (@newDeveloper_id, @task_id, 3);
			Select @result = 1;
		End
	Else If @newDeveloper_id = 0
		Begin
			Delete From Developers_Tasks Where id = @relation_id;
			Select @result = 1;
		End
	Else
		Begin
			Update Developers_Tasks Set developer_id = @newDeveloper_id
			Where id = @relation_id;
			Select @result = 1;
		End
End
