import MySQLdb

DB_NAME = hostel

TABLES = {}
TABLES['users']=(
	"CREATE TABLE `users` ("
    "  `FullName` varchar(50),"
    "  `Email` varchar(50),"
    "  `U_password` varchar(30),"
    "  `PhoneNumber` varchar(16),"
    "  `Users_type`varchar(25) ,"
    "  Unique key (`Email`)")

TABLES['foodmenu']=(
	"CREATE TABLE `foodmenu` ("
    "  `food_id` int(11),"
    "  `menuType` varchar(100),"
    "  `menuName` varchar(100),"
    "  `date` date,"
    "  foreign key (`food_id`)")

TABLES['food_record']=(
	"CREATE TABLE `food_record` ("
    "  `user_id` varchar(50),"
    "  `food_id` varchar(50),"
    "  `status` boolean,")