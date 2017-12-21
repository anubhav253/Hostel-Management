import MySQLdb

conn = MySQLdb.connect(host='localhost',user='root',passwd='Devil253',db='hostel')
a=conn.cursor()

sql = "SELECT * from `signup`";
a.execute(sql)

countrow = a.execute(sql)
print("Number of rows:",countrow)
data=a.fetchall()
print (data)