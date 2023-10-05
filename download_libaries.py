import os
import urllib.request

mysql_link = "http://45.85.218.127/api/libary/3323cccf-068f-4ef4-97c7-9aec1cb3a521/mysql-connector-j-8.1.0.jar"
target_mysql_file = "API/mysql-connector-j-8.1.0.jar"

targetFolder = "API"

if not os.path.exists(targetFolder):
    os.makedirs(targetFolder)

urllib.request.urlretrieve(mysql_link, target_mysql_file)
