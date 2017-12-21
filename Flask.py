from flask import Flask, jsonify, request, json
import MySQLdb

conn = MySQLdb.connect(host='localhost',user='root',passwd='Devil253',db='hostel')
a=conn.cursor()


app = Flask(__name__) #define app using flask

@app.route('/', methods=['GET'])
def test():
	sql = "SELECT * from `signup`";
	a.execute(sql)
	data=a.fetchall()
	#print (data)
	return jsonify(data)

@app.route('/login', methods=['POST'])
def checkLogin():
	#print ("request loggingg" , request.body);
	FullName = request.json['FullName'];
	U_password = request.json['U_password'];
	print ("FullName " + FullName); 
	sql = "SELECT * from `signup` where FullName='Anubhav Kumar' and U_password='anubhav'";
	a.execute(sql);
	data = a.fetchall();
	response = "";
	if(len(data)):
		#valid User
		response = app.response_class(
	        response=json.dumps(data),
	        status=200,
	        mimetype='application/json'
	    )
	else:
		#invalid user since we cannot find any one with matching name
		response = app.response_class(
	        response=json.dumps(""),
	        status=400,
	        mimetype='application/json'
	    )
	return response



if __name__ == '__main__':
	app.run(debug=True,port=8080)