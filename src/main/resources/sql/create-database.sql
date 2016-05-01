Create Database taskmanager
Go
Use taskmanager;

Create Table Developers (
	[id] int Primary Key Identity(1,1) not null,
	[username] nvarchar(10) not null,
	[password] binary(128) not null,
	[name] nvarchar(20) not null,
	[surname] nvarchar(20) not null,
	[patronymic] nvarchar(20) not null,
	[position] nvarchar(50) not null,
	[isAdmin] bit not null
)

Create Table Tasks (
	[id] int Primary Key Identity(1,1) not null,
	[number] int not null,
	[name] nvarchar(60) not null,
	[description] nvarchar(500),
	[deadline] date not null,
	[priority] int not null Check ([priority] In(1,2,3)),
	[state] bit not null,
	[developer_id] int Foreign Key References Developers(id)
)