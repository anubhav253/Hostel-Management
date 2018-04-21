Hostel Mess Management
===================
Hostel Mess Management android app supported by a python backend with flask, MySQL, redis. Basic features implemented such as login, signup, fetching menu, posting comments, selecting menu etc. Used third party services like to trigger SMS for OTP generation and to trigger email for general notification.  

Clone Hostel Mess Management:
```bash
git clone https://github.com/anubhav253/Hostel-Management 
```
Change directory and switch to the develop branch:
```bash
cd Hostel-Management

git checkout -b change origin/develop
```

Install requirements using:
```bash
pip install .
```
Config file added:
```bash
{
	"gmailCred": {
		"email": "",
		"passWord": ""
	},
	"dbCred":{
		"host": "localhost",
		"user": "root",
		"passwd": "",
		"db" : ""
	},
	"redisCred":{
		"host": "localhost",
		"port": "",
		"db" : 0
	},
	"otpConstant":{
		"otp_authkey" : "",
		"sender" : "",
		"route" : "default",
		"sendUrl" : "",
		"resendUrl" : "",
		"verifyUrl" : ""
	}
}