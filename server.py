from flask import Flask, request, render_template
import simplejson as json
import urllib
import os   

app = Flask(__name__)

@app.route('/api')
def api():
    user = request.args.get('user')
    passwd = request.args.get('pass')
    if user == None or passwd == None or len(user) == 0 or len(passwd) == 0:
        return "Failure"
    return "Success!"
    
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