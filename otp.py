import urllib
import urllib2 
from flask import Flask, jsonify, request, json
import random
import redis
 

data = json.load(open('config.json'))
otp_authkey = data["otpConstant"]["otp_authkey"]
sender = data["otpConstant"]["sender"]
route = data["otpConstant"]["route"]
sendUrl = data["otpConstant"]["sendUrl"]
resendUrl = data["otpConstant"]["resendUrl"]
verifyUrl = data["otpConstant"]["verifyUrl"]
redisCred = data["redisCred"]
app = Flask(__name__)
redisConn = redis.StrictRedis(host=redisCred["host"], port=redisCred["port"], db=redisCred["db"])

def generateOtp(mobileNumber):
	generated_otp = int(1000 + random.random()*10000)
	print generated_otp 
	otpKey = "otp--"+ str(mobileNumber)
	redisConn.setex(otpKey, 1800, generated_otp);
	return generated_otp
	


def sendOtp(mobileNumber,message):
	otp = generateOtp(mobileNumber)
	paylod = {
          'authkey' : otp_authkey,
          'mobiles' : mobileNumber,
          'message' : message + str(otp),
          'sender' : sender,
          'route' : route,
          'otp' :otp
          }
	postdata = urllib.urlencode(paylod) # URL encoding the data here.
	req = urllib2.Request(sendUrl, postdata)
	response = urllib2.urlopen(req)
	output = response.read() 
	print output
	return response.read()

def resendOtp(mobileNumber,message):
	otp = redisConn.get("otp--" + str(mobileNumber))
	print otp, mobileNumber
	paylod = {
          'authkey' : otp_authkey,
          'mobile' : mobileNumber,
          'retrytype' : "text/voice",
          'message': message + str(otp),
          'otp': otp,
          'route': route
          }
	postdata = urllib.urlencode(paylod) # URL encoding the data here.
	req = urllib2.Request(resendUrl, postdata)
	print postdata
	response = urllib2.urlopen(req)
	output = response.read() 
	print output
	return response.read()

def verifyOtp(mobileNumber, otp):
	otp1 = redisConn.get("otp--" + str(mobileNumber))
	response = "";
	if(otp1 == None):
		response = 404
		print response

	elif(otp1 == otp):
		response = 200 
		print response

	else:
		response = 400 
		print response
	return response

#sendOtp("9999999999","Your otp is ")
#resendOtp("9999999999","your otp is")
verifyOtp("9999999999", "otp")