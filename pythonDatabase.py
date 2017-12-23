import MySQLdb

conn = MySQLdb.connect(host='localhost',user='root',passwd='abc123',db='hostel')
cursor=conn.cursor()

TABLES = {}
TABLES['test']= '''CREATE TABLE `test`(`name` varchar(50))'''
TABLES['users1']='''CREATE TABLE `users` (`FullName` varchar(50) , `Email` varchar(50) , `U_password` varchar(30) , `PhoneNumber` varchar(16) , `Users_type` varchar(25) , primary key (`Email`))'''

TABLES['foodmenu1']='''CREATE TABLE `foodmenu` (`food_id` int(11),`menuType` varchar(100),`menuName` varchar(100),`date` date,foreign key (`food_id`))'''

TABLES['food_record1']='''CREATE TABLE `food_record` (`user_id` varchar(50),`food_id` varchar(50),`status` boolean,)'''


for name, ddl in TABLES.iteritems():
    #try:
    print("Creating table {}:" , name)
    cursor.execute(ddl)
    #except err:
    #    print("some error . ", err.msg)
    #else:
    #    print("OK")

cursor.close()
conn.commit()
conn.close()
