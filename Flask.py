from flask import Flask, jsonify, request, json
import MySQLdb
import datetime

conn = MySQLdb.connect(host='localhost',user='root',passwd='abc123',db='hostel')
a=conn.cursor()


app = Flask(__name__) #define app using flask

@app.route('/', methods=['GET'])
def test():
	sql = "SELECT * from `users`";
	a.execute(sql)
	data=a.fetchall()
	#print (data)
	return jsonify(data)

@app.route('/login', methods=['POST'])
def checkLogin():
	#print ("request loggingg" , request.body);
	#print (request.get_json() );
	body_json = request.get_json();
	body = body_json["nameValuePairs"];
	Email = body['Email'];
	U_password = body['U_password'];
	#print ("Email " + Email);
	sql = "SELECT * from `users` where Email='%s' and U_password='%s'"% (Email,U_password);
	a.execute(sql);
	data = a.fetchall();
	response = "";
	if(len(data)):
		#valid User
		#print (jsonify(data))
		response = app.response_class(
	        response=json.dumps({}),
	        status=200,
	        mimetype='application/json'
	    )
	else:
		response = app.response_class(
	        response=json.dumps({
	        	"message": "something went wrong please try again"
	        	}),
	        status=400,
	        mimetype='application/json'
	    )
	return response

@app.route('/signup', methods=['POST'])
def checkSignUp():
	#print ("request loggingg" , request.body);
	Email = request.json['nameValuePairs']['Email'];
	U_password = request.json['nameValuePairs']['password'];
	fullname = request.json['nameValuePairs']['fullname'];
	mobile = request.json['nameValuePairs']['mobile'];
	print ("Email " + Email);
	#sql = "INSERT INTO products(name, category_id, sub_category_id) VALUES ('%s' ,%d, %d)" % (name + ' (' + sku + ')',i+1,i+1)
	sql = "INSERT into users(Email,FullName,PhoneNumber,U_password) VALUES ('%s', '%s', '%s', '%s')" % (Email,fullname, mobile, U_password)
	data = a.execute(sql);
	conn.commit();
	print ("data", data)
	response = "";
	if(data):
		#valid User
		response = app.response_class(
	        response=json.dumps(data),
	        status=200,
	        mimetype='application/json'
	    )
	else:
		#invalid user since we cannot find any one with matching name
		response = app.response_class(
	        response=json.dumps({
	        	"message" :"Something Went Wrong please try again later"
	        	}),
	        status=400,
	        mimetype='application/json'
	    )
	return response

@app.route('/fooddata', methods=['POST'])
def postFoodData():
	#print ("request loggingg" , request.body);
	email = request.json['email'];
	foodId = request.json['foodId'];
	selected = request.json['selected'];
	sql = "INSERT into food_record(user_id,food_id,status) VALUES ('%s', '%s', '%s')" % (email,foodId,selected)
	data=a.execute(sql);
	conn.commit();
	print ("data", data)
	response = "";
	if(data):
		#valid User
		response = app.response_class(
	        response=json.dumps({
	        	"message" : "Your request has been submitted"
	        	}),
	        status=200,
	        mimetype='application/json'
	    )
	else:
		#invalid user since we cannot find any one with matching name
		response = app.response_class(
	        response=json.dumps({
	        	"message" : "Something went wrong while processing you request"
	        	}),
	        status=400,
	        mimetype='application/json'
	    )
	return response

@app.route('/fooddata', methods=['GET'])
def getFoodData():
	
	getdate = request.args.get("date");

	timedelta = datetime.timedelta(days=int(getdate));
	now = datetime.datetime.now()

	currenDate = now.strftime("%Y-%m-%d")
	nextDate = now + timedelta
	nextDate = nextDate.strftime("%Y-%m-%d")

	sql = "SELECT * from foodmenu where date = '%s'" % nextDate
	a.execute(sql);
	data = a.fetchall();
	response = "";
	if(len(data)):
		#valid User
		response = app.response_class(
	        response=json.dumps({
                        "data":data
                }),
	        status=200,
	        mimetype='application/json'
	    )
	else:
		#invalid user since we cannot find any one with matching name
		response = app.response_class(
	        response=json.dumps({
	        		"message" : "Something Went wrong not datat found"
	        	}),
	        status=400,
	        mimetype='application/json'
	    )
	return response


@app.route('/complaint', methods=['POST'])
def complaint():
	#print ("request loggingg" , request.body);
	FullName = request.json['nameValuePairs']['FullName'];
	RoomNumber = request.json['nameValuePairs']['RoomNumber'];
	complaint_type = request.json['nameValuePairs']['complaint_type'];
	complain = request.json['nameValuePairs']['complain'];
	print ("Room Number " + RoomNumber);
	#sql = "INSERT INTO products(name, category_id, sub_category_id) VALUES ('%s' ,%d, %d)" % (name + ' (' + sku + ')',i+1,i+1)
	sql = "INSERT into complaint(FullName,RoomNumber,complaint_type,complain) VALUES ('%s', '%s', '%s', '%s')" % (FullName,RoomNumber, complaint_type, complain)
	data = a.execute(sql);
	conn.commit();
	print ("data", data)
	response = "";
	if(data):
		#valid User
		response = app.response_class(
	        response=json.dumps({"data":data}),
	        status=200,
	        mimetype='application/json'
	    )
	else:
		#invalid user since we cannot find any one with matching name
		response = app.response_class(
	        response=json.dumps({
	        	"message" :"Something Went Wrong please try again later"
	        	}),
	        status=400,
	        mimetype='application/json'
	    )
	return response

if __name__ == '__main__':
	app.run(host="0.0.0.0",debug=True,port=8080)
