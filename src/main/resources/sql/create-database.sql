--*******************************************************************--
--This SQL script is for Microsoft SQL Server.
--It creates database 'taskmanager' with all the tables,
--procedures, functions, triggers and users for application connection.
--*******************************************************************--

--TODO: Add function which gets list of developers of the selected task.

Create Database taskmanager;
Go

Use taskmanager;

-----------Tables creation-----------

Create Table Developers (
    [id] int Primary Key Identity(1,1) not null,
    [username] nvarchar(10) not null,
    [password] binary(32) not null,
    [dev_name] nvarchar(20) not null,
    [surname] nvarchar(20) not null,
    [patronymic] nvarchar(20) not null,
    [position] nvarchar(50) not null,
    [is_admin] bit not null
);

Create Table Tasks (
    [id] int Primary Key Identity(1,1) not null,
    [number] int not null,
    [task_name] nvarchar(60) not null,
    [description] nvarchar(500),
    [deadline] date not null,
    [priority] int not null Check ([priority] In(1,2,3)),
    [is_completed] bit not null	
);

Create Table Developers_Tasks (
    [id] int Primary Key Identity(1,1) not null,
    [task_id] int Foreign Key References Tasks(id) not null,	
    [dev_id] int Foreign Key References Developers(id) not null
);
Go

------------Login and user creation--------------
If not Exists (select name from master.dbo.syslogins 
               where name = 'TaskmanagerUser')
Begin
    Create Login TaskmanagerUser With Password = 'Qwerty123';
End
Create User TaskmanagerUser For Login TaskmanagerUser;
Go

--------Programmability creation--------
Create Procedure createDeveloper
    @username nvarchar(10), 
    @password binary(32),
    @dev_name nvarchar(20),
    @surname nvarchar(20),
    @patronymic nvarchar(20),
    @position nvarchar(50),
    @is_admin bit,
    @identity int OUTPUT
As
Begin
    Set Nocount On;
    Insert Into Developers Values (@username, @password, @dev_name, @surname, @patronymic, @position, @is_admin);
    Select @identity = @@IDENTITY
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
    @is_admin bit
As
Begin
    Set Nocount On;
    Update Developers
    Set username = @username, [password] = @password, dev_name = @dev_name, surname = @surname,
        patronymic = @patronymic, position = @position, is_admin = @is_admin
    Where id = @dev_id;	
End
Go	

Create Function getDevelopers()
Returns Table
As 
Return
(
    Select id, username, dev_name, surname, patronymic, position, is_admin From Developers
)
Go

--Create Function getDevelopersByTask(@task_id int)
--Returns Table
--As
--Return
--(
--    Select Developers.id, Developers.dev_name, Developers.surname, Developers.patronymic
--    From Developers Join Developers_Tasks On Developers.id = Developers_Tasks.dev_id
--    Where Developers_Tasks.task_id = @task_id
--)
--Go

Create Procedure deleteDeveloper
    @dev_id int
As
Begin
    Set Nocount On;
    Delete From Developers Where id = @dev_id;  
End
Go

Create Procedure createTask
    @number int,
    @task_name nvarchar(60),
    @description nvarchar(500),
    @deadline date,
    @priority int = 3,
    @is_completed bit = 0,
    @identity int OUTPUT
As
Begin
    Set Nocount On;
    Insert Into Tasks Values (@number, @task_name, @description, @deadline, @priority, @is_completed);
    Select @identity = @@IDENTITY
End
Go

Create Procedure updateTask
    @task_id int,
    @number int,
    @task_name nvarchar(60),
    @description nvarchar(500),
    @deadline date,
    @priority int,
    @is_completed bit
As
Begin
    Set Nocount On;
    Update Tasks 
    Set number = @number, task_name = @task_name, [description] = @description, deadline = @deadline,
        [priority] = @priority, is_completed = @is_completed
    Where id = @task_id;	
End

Go

--This function finds developers having the selected task and concat their full names into one row.
--Create Function selectConcat(@task_id int)
--Returns nvarchar(MAX)
--As
--Begin
--    Declare @result nvarchar(MAX) = '';
--    Select @result = @result + task_dev.dev_name + ' ' + 
--                               Left(task_dev.surname, 1) + '.' +
--                               Left(task_dev.patronymic, 1) + '. '
--    From (Select Developers_Tasks.task_id, Developers.dev_name, Developers.surname, Developers.patronymic 
--            From Developers_Tasks Join Developers On Developers_Tasks.dev_id = Developers.id) task_dev
--    Where task_dev.task_id = @task_id;
--    Return @result;
--End
--Go

Create Function getAllTasks()
Returns Table
As 
Return
(
    Select Tasks.*, Developers.id as dev_id, Developers.dev_name, Developers.surname, Developers.patronymic
    From Tasks Left Join Developers_Tasks On Tasks.id = Developers_Tasks.task_id
               Left Join Developers On Developers.id = Developers_Tasks.dev_id   
)
Go

Create Function getIncompletedTasks()
Returns Table
As
Return
(
    Select * From getAllTasks() tasks
    Where tasks.is_completed = 0
)
Go

Create Function getTasksByDeveloper(@dev_id int)
Returns Table
As 
Return
(
    Select Tasks.*, Developers.id as dev_id, Developers.dev_name, Developers.surname, Developers.patronymic
    From Tasks Join Developers_Tasks On Tasks.id = Developers_Tasks.task_id
               Join Developers On Developers.id = Developers_Tasks.dev_id
    Where Developers.id = @dev_id    
)
Go

Create Function getIncompletedTasksByDeveloper(@dev_id int)
Returns Table
As 
Return
(
    Select * From getTasksByDeveloper(@dev_id) tasks
    Where tasks.is_completed = 0
)
Go

Create Procedure deleteTask
    @task_id int
As
Begin
    Set Nocount On;
    Delete From Tasks Where id = @task_id;
End
Go

Create Procedure assignTask
    @dev_id int,
    @task_id int   
As	
Begin	
    Set Nocount On;
    Insert Into Developers_Tasks Values (@task_id, @dev_id);
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
    @is_completed bit
As
Begin
    Set Nocount On;
    Update Tasks
    Set is_completed = @is_completed
    Where id = @task_id;
End
Go

--This procedure checks if entered user credentials are valid and if user is admin.
Create Procedure checkCredentials
    @username nvarchar(10),
    @password binary(32),
    @is_valid bit OUTPUT,
    @is_admin bit OUTPUT
As
Begin
    Set Nocount On;
    Select @is_admin = Developers.is_admin From Developers 
    Where Developers.username = @username and Developers.[password] = @password;
    If @is_admin is NULL
        Begin
            Select @is_valid = 0;
            Select @is_admin = 0;
        End
    Else 
        Select @is_valid = 1;
End
Go

--Trigger checks if there is a username in the table 'Developers' as the username is to be added.
--Trigger fires when inserting in the table or updating table 'Developers'.
Create Trigger checkCreateOrUpdateDeveloperTrigger
    On Developers
    For Insert, Update
As
Begin
    Set Nocount On;
    If (Select Count(*) From Developers 
        Where Developers.username = (Select i.username From inserted i)) = 2
        Begin
            Rollback Transaction
            Print 'Указанное имя входа существует в базе данных.'
        End                
End
Go

--Trigger fires when deleting from table 'Developers'.
--It deletes relations between developer and its tasks from 'Developers_Tasks' first, then 
--deletes developer from 'Developers'.
Create Trigger checkDeleteDeveloperTrigger
    On Developers
    Instead OF Delete
As
Begin
    Set Nocount On;
    Delete Developers_Tasks
    From Deleted
    Where Deleted.id = Developers_Tasks.dev_id

    Delete Developers
    From Deleted
    Where Deleted.id = Developers.id
End
Go

--Trigger fires when deleting from table 'Tasks'.
--It deletes relations between task and its developers from 'Developers_Tasks' first, then 
--deletes task from 'Tasks'.
Create Trigger checkDeleteTaskTrigger
    On Tasks
    Instead Of Delete
As
Begin
    Set Nocount On;
    Delete Developers_Tasks
    From Deleted
    Where Deleted.id = Developers_Tasks.task_id

    Delete Tasks
    From Deleted
    Where Deleted.id = Tasks.id
End
Go

--At first trigger checks if there is a task number in the table 'Tasks' as the task number is to be added.
--At second trigger checks if there are 2 tasks with the highest priority (1) in the table 'Tasks'.
--At third trigger checks if deadline is later than current date.
--Trigger fires when inserting in the table or updating table 'Tasks'.
Create Trigger checkCreateOrUpdateTaskTrigger
    On Tasks
    For Insert, Update
As
Begin
    Set Nocount On;
    If (Select Count(*) From Tasks 
        Where Tasks.number = (Select i.number From inserted i)) = 2
        Begin
            Rollback Transaction
            Print 'Задача с таким номером существует в базе данных.'
        End
    Else If((Select i.[priority] From inserted i) = 1) and
        ((Select Count(*) From Tasks Where [priority] = 1) = 3)
        Begin
            Rollback Transaction
            Print 'В списке уже имеется две задачи с наивысшим приоритетом.'
        End
    Else If((Select i.deadline From inserted i) < Convert(date, GETDATE()))
        Begin
            Rollback Transaction
            Print 'Срок выполнения задачи не может быть раньше текущей даты.'
        End
End
Go

--Trigger checks if the chosen task is assigned to chosen developer or
--if the chosen task is assigned to two developers or if the chosen developer has 3 tasks.
--Trigger fires when inserting in the table 'Developers_Tasks'.
Create Trigger checkAssignTaskTrigger
    On Developers_Tasks 
    For Insert, Update
As
Begin
    Set Nocount On;
    If (Select Count(*) From Developers_Tasks 
        Where Developers_Tasks.task_id = (Select i.task_id From inserted i) and 
              Developers_Tasks.dev_id = (Select i.dev_id From inserted i)) = 2
        Begin
            Rollback Transaction
            Print 'Данная задача уже назначена этому разработчику.'
        End
    Else If (Select Count(*) From Developers_Tasks 
             Where Developers_Tasks.task_id = (Select i.task_id From inserted i)) = 3
        Begin
            Rollback Transaction
            Print 'Данная задача уже назначена двум разработчикам.'
        End
    Else if (Select Count(*) From Developers_Tasks 
             Where Developers_Tasks.dev_id = (Select i.dev_id From inserted i)) = 4
        Begin
            Rollback Transaction
            Print 'Выбранному разработчику уже назначено 3 задачи.'
        End
End
Go

--------Grant permissions to created user---------
Grant Insert, Select, Update, Delete On Developers To TaskmanagerUser;
Grant Insert, Select, Update, Delete On Tasks To TaskmanagerUser;
Grant Insert, Select, Update, Delete On Developers_tasks To TaskmanagerUser;
Grant Select On dbo.getDevelopers To TaskmanagerUser;
Grant Select On dbo.getAllTasks To TaskmanagerUser;
Grant Select On dbo.getIncompletedTasks To TaskmanagerUser;
Grant Select On dbo.getTasksByDeveloper To TaskmanagerUser;
Grant Select On dbo.getIncompletedTasksByDeveloper To TaskmanagerUser;
Grant Execute To TaskmanagerUser;
Go

--Add default admin of the project 
Insert Into Developers Values('admin', HASHBYTES('SHA2_256', 'password'), 'Курчевский', 'Алексей', 'Александрович', 'Project manager', 1);

--Declare @test int;
--Execute createDeveloper 'test4',123,'name','surname','patronymic','test',0, @test output;

--Delete From Developers;
--Execute deleteDeveloper 4;

--Declare @test int;
--Execute createTask 100,'task2','testtask','05-28-2016', 3, 0, @test output;
--Delete From Tasks;
--Select @test;