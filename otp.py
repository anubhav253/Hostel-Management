import urllib
import urllib2 
from flask import Flask, jsonify, request, json
import random
 

data = json.load(open('config.json'))
otp_authkey = data["otpConstant"]["otp_authkey"]
sender = data["otpConstant"]["sender"]
route = data["otpConstant"]["route"]
sendUrl = data["otpConstant"]["sendUrl"]

def generateOtp(mobileNumber):
	generated_otp = int(1000 + random.random()*10000)
	print generated_otp #to be removed after redis usage
	#use mobile to store otp in redis
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

sendOtp("9999999999","Your otp is ")