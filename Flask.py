from flask import Flask, jsonify, request, json
import MySQLdb
import smtplib
import datetime
import redis
import uuid

data = json.load(open('config.json'))
senderEmail = data["gmailCred"]["email"]
senderPassword = data["gmailCred"]["passWord"]
dbCred = data["dbCred"]
redisCred = data["redisCred"]

redisConn = redis.StrictRedis(host=redisCred["host"], port=redisCred["port"], db=redisCred["db"])
conn = MySQLdb.connect(host=dbCred["host"], user=dbCred["user"], passwd=dbCred["passwd"], db=dbCred["db"])
a=conn.cursor()

emailClient = smtplib.SMTP('smtp.gmail.com', 587)
emailClient.starttls()


app = Flask(__name__) #define app using flask

def sendEmail(Email, message):
	emailClient.login(senderEmail, senderPassword)
	print ("Sending Message to " + Email)
	emailClient.sendmail(senderEmail, Email, message)
	emailClient.quit()

@app.route('/', methods=['GET'])
def test():
	sql = "SELECT * from `users`"
	a.execute(sql)
	data = a.fetchall()
	#print (data)
	return jsonify(data)

@app.route('/login', methods=['POST'])
def checkLogin():
	#print ("request loggingg" , request.body);
	#print (request.get_json() );
	body_json = request.get_json()
	body = body_json["nameValuePairs"]
	Email = body['Email']
	U_password = body['U_password']
	#print ("Email " + Email);
	sql = "SELECT * from `users` where Email='%s' and U_password='%s'"% (Email,U_password)
	a.execute(sql)
	data = a.fetchall()
	response = ""
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
	Email = request.json['nameValuePairs']['Email']
	U_password = request.json['nameValuePairs']['password']
	fullname = request.json['nameValuePairs']['fullname']
	mobile = request.json['nameValuePairs']['mobile']
	print ("Email " + Email)
	#sql = "INSERT INTO products(name, category_id, sub_category_id) VALUES ('%s' ,%d, %d)" % (name + ' (' + sku + ')',i+1,i+1)
	sql = "INSERT into users(Email,FullName,PhoneNumber,U_password) VALUES ('%s', '%s', '%s', '%s')" % (Email, fullname, mobile, U_password)
	data = a.execute(sql)
	conn.commit()
	print ("data", data)
	response = ""

	if(data):
		#valid 
		sendEmail(Email, "You are registered Successfully")
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
	email = request.json['email']
	foodId = request.json['foodId']
	selected = request.json['selected']
	sql = "INSERT into food_record(user_id, food_id, status) VALUES ('%s', '%s', '%s')" % (email, foodId, selected)
	data=a.execute(sql)
	conn.commit()
	print ("data", data)
	response = ""
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
	
	getdate = request.args.get("date")

	timedelta = datetime.timedelta(days=int(getdate))
	now = datetime.datetime.now()

	currenDate = now.strftime("%Y-%m-%d")
	nextDate = now + timedelta
	nextDate = nextDate.strftime("%Y-%m-%d")

	sql = "SELECT * from foodmenu where date = '%s'" % nextDate
	a.execute(sql)
	data = a.fetchall()
	response = ""
	if(len(data)):
		response = app.response_class(
	        response=json.dumps({
                        "data":data
                }),
	        status=200,
	        mimetype='application/json'
	    )
	else:
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
	FullName = request.json['nameValuePairs']['FullName']
	RoomNumber = request.json['nameValuePairs']['RoomNumber']
	complaint_type = request.json['nameValuePairs']['complaint_type']
	complain = request.json['nameValuePairs']['complain']
	print ("Room Number " + RoomNumber)
	#sql = "INSERT INTO products(name, category_id, sub_category_id) VALUES ('%s' ,%d, %d)" % (name + ' (' + sku + ')',i+1,i+1)
	sql = "INSERT into complaint(FullName,RoomNumber,complaint_type,complain) VALUES ('%s', '%s', '%s', '%s')" % (FullName,RoomNumber, complaint_type, complain)
	data = a.execute(sql)
	conn.commit()
	print ("data", data)
	response = ""
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

@app.route('/forget-password', methods=['POST'])
def forgetPassword():
	body_json = request.get_json()
	body = body_json["nameValuePairs"]
	Email = body['Email']
	sql = "SELECT * from `users` where Email='%s'"% (Email)
	a.execute(sql)
	data = a.fetchall()
	response = ""
	if(len(data)):
		redisValue = uuid.uuid4().hex
		timeLimitKey = "email--"+Email
		limitCount = int (redisConn.get(timeLimitKey))
		print("limit count    ",limitCount)
		if(limitCount > 5):
			response = app.response_class(
		        response=json.dumps({
		        	"message": "Retry Limit exceeded"
		        	}),
		        status=400,
		        mimetype='application/json'
		    )
			return response
		elif(limitCount):
			redisConn.setex(timeLimitKey, 600, limitCount + 1)
		else:
			redisConn.setex(timeLimitKey, 600, 1)
		redisConn.setex(Email, 600 , redisValue)
		message = 'Subject: {}\n\n{}'.format("Reset Email","Please reset your email use this code with passWord ")
		sendEmail(Email, message + redisValue)
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

@app.route('/reset-password', methods=['POST'])
def resetPassword():
	response = ""
	body_json = request.get_json()
	body = body_json["nameValuePairs"]
	Email = body['Email']
	New_password = body['New_password']
	user_id	= body['uuid']
	redisValue = redisConn.get(Email)
	if(redisValue != user_id):
		response = app.response_class(
	        response=json.dumps({
	        	"message": "Invalid User"
	        	}),
	        status=401,
	        mimetype='application/json'
	    )
		return response
	sql = "UPDATE `users` set U_password='%s' where Email='%s' " % (New_password,Email)
	data = a.execute(sql)
	conn.commit()
	if(data):
		redisConn.delete(Email)
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

if __name__ == '__main__':
	app.run(host="0.0.0.0",debug=True,port=8080)

