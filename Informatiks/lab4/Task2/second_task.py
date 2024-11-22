from json2xml import json2xml
from json2xml.utils import readfromjson

json = readfromjson("sat_shedule.json")
xml = json2xml.Json2xml(json).to_xml()
print(xml)
with open("../Outputs/output2.xml", "w", encoding="utf-8") as f:
    f.write(xml)
