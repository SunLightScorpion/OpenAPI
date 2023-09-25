import os

mysql_link = "https://sunlightdev.org/api/sunlightscorpion/libary/3323cccf-068f-4ef4-97c7-9aec1cb3a521/mysql-connector-java-8.0.29.jar"
targetFolder = "API"

if not os.path.exists(targetFolder):
    os.makedirs(targetFolder)
