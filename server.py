from flask import Flask, request, render_template
import simplejson as json
import urllib
import os 
import random
from flaskext.mysql import MySQL

mysql = MySQL()
app = Flask(__name__)
app.config['MYSQL_DATABASE_USER'] = 'hackrpi'
app.config['MYSQL_DATABASE_PASSWORD'] = '2016'
app.config['MYSQL_DATABASE_DB'] = 'hackrpi'
app.config['MYSQL_DATABASE_HOST'] = 'localhost'
mysql.init_app(app)

def get_mysql(row, table, column, value):
    connection = mysql.get_db()
    cursor = connection.cursor()
    cursor.execute("SELECT " + row + " FROM " + table + " WHERE " + column + "='" + value + "'")
    return str(cursor.fetchone()[0])
    
@app.route('/login')
def login():
    ret = {}
    user = request.args.get('user')
    passwd = request.args.get('pass')
    cursor = mysql.connect().cursor()   
    cursor.execute("SELECT * FROM user WHERE username='" + user + "' AND password='" + passwd + "'")
    data = cursor.fetchone()
    if data is None:
        return "The username and password combination does not exist."
    else:
        cursor.execute("SELECT groupname, g.groupkey FROM groupmaster c, groupu g, user u WHERE g.groupkey = c.groupkey AND u.userid = c.userid")
        group = cursor.fetchall()
        for x in group:
            ret[x[0]] = x[1]
        cursor.execute("SELECT groupname, g.groupkey FROM groupjoined c, groupu g, user u WHERE g.groupkey = c.groupkey AND u.userid = c.userid")
        group = cursor.fetchall()
        for x in group:
            ret[x[0]] = x[1]
        return json.dumps(ret)
     
@app.route('/register')
def register():
    user = request.args.get('user')
    passwd = request.args.get('pass')
    connection = mysql.get_db()
    cursor = connection.cursor()
    cursor.execute("SELECT * FROM user WHERE username='" + user + "'")
    data = cursor.fetchone()
    if data is None:
        try:
            cursor.execute("INSERT INTO user (username, password) VALUES ('" + user + "', '" + passwd + "')")
            connection.commit()
            return "Success"
        except Exception as e:
            return ""
    else:
        return "The username already exists."

@app.route('/makegroup')
def makegroup():
    user = request.args.get('user')
    name = request.args.get('name')
    group = ''.join(random.choice('0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUWXYZ') for i in xrange(5))
    connection = mysql.get_db()
    cursor = connection.cursor()
    userid = get_mysql("userid", "user", "username", user)
    cursor.execute("INSERT INTO groupu (groupkey, groupname) VALUES ('" + group + "', '" + name + "')")
    cursor.execute("INSERT INTO groupmaster (groupkey, userid) VALUES ('" + group + "', '" + userid + "')")
    connection.commit()
    return group
    
@app.route('/joingroup')
def joingroup():
    ret = {}
    user = request.args.get('user')
    group = request.args.get('group')
    connection = mysql.get_db()
    cursor = connection.cursor()
    groupname = get_mysql("groupname", "groupu", "groupkey", group)
    ret[groupname] = []
    userid = get_mysql("userid", "user", "username", user)
    cursor.execute("SELECT username FROM groupjoined c, groupu g, user u WHERE g.groupkey = c.groupkey AND u.userid = c.userid AND g.groupkey ='" + group + "'")
    check = cursor.fetchall()
    cursor.execute("SELECT username FROM groupmaster c, groupu g, user u WHERE g.groupkey = c.groupkey AND u.userid = c.userid AND g.groupkey ='" + group + "'")
    check += cursor.fetchall()
    print check
    checkbool = True
    for x in check:
        if user in x:
            checkbool = False
            break
    if checkbool:
        cursor.execute("INSERT INTO groupjoined (groupkey, userid) VALUES ('" + group + "', '" + userid + "')")
    cursor.execute("SELECT username FROM groupjoined c, groupu g, user u WHERE g.groupkey = c.groupkey AND u.userid = c.userid AND g.groupkey ='" + group + "'")
    check = cursor.fetchall()
    cursor.execute("SELECT username FROM groupmaster c, groupu g, user u WHERE g.groupkey = c.groupkey AND u.userid = c.userid AND g.groupkey ='" + group + "'")
    check += cursor.fetchall()
    for x in check:
        ret[groupname].append(x[0])
    connection.commit()
    return json.dumps(ret) # Return all people within this group along with the groupname
    
@app.route('/location')
def location():
    lat = request.args.get('lat')
    lng = request.args.get('lng')
    key = ""
    with open(os.path.join(os.path.dirname(__file__), './key.txt')) as data_file:
        key = data_file.readline()
    url = "http://api.tripadvisor.com/api/partner/2.0/map/" + str(lat) + "," + str(lng) + "/restaurants?key=" + key
    response = urllib.urlopen(url)
    dump = json.loads(response.read())
    
    if lat == None or lng == None:# or len(lat) == 0 or len(lng) == 0:
        return "Failure"
    return json.dumps(dump)

if __name__ == '__main__':
	app.debug = True
	app.run(host='0.0.0.0', port=80)# makes server publicly available