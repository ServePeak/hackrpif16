import simplejson as json
import urllib
import os

lat = 42.33141
lng = -71.099396
key = ""
with open(os.path.join(os.path.dirname(__file__), './key.txt')) as data_file:
    key = data_file.readline()
url = "http://api.tripadvisor.com/api/partner/2.0/map/" + str(lat) + "," + str(lng) + "/restaurants?key=" + key
response = urllib.urlopen(url)
dump = json.loads(response.read())

info = []
for x in range(0, len(dump['data'])):
    specificInfo = {}
    specificInfo['name'] = dump['data'][x]['name']
    specificInfo['rating'] = dump['data'][x]['rating']
    specificInfo['location'] = dump['data'][x]['address_obj']['address_string']
    specificInfo['cost'] = dump['data'][x]['price_level']
    if dump['data'][x]['cuisine'] == []:
        specificInfo['description'] = ""
    else:
        specificInfo['description'] = dump['data'][x]['cuisine'][0]['localized_name']
    specificInfo['distance'] = dump['data'][x]['distance']
    info.append(specificInfo)
        
print info
