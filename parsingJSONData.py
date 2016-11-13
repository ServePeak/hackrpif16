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

specificInfo = {}
for x in dump['data']:
    id = x['location_id']
    specificInfo[id] = {}
    specificInfo[id]['name'] = x['name']
    specificInfo[id]['rating'] = x['rating']
    specificInfo[id]['location'] = x['address_obj']['address_string']
    specificInfo[id]['cost'] = x['price_level']
    if x['cuisine'] == []:
        specificInfo[id]['description'] = ""
    else:
        specificInfo[id]['description'] = x['cuisine'][0]['localized_name']
    specificInfo[id]['distance'] = x['distance']
        
print specificInfo
