def DictToXML(d):
    global xml
    global counter
    global maxcounter
    for k, v in d.items():
        xml += "\t" * (counter + 1) + "<" + k + ">"
        if isinstance(v, list):
            for i in v:
                if xml[-1] != "\n":
                    xml += "\n"
                counter += 1
                maxcounter = counter
                DictToXML(i)
                counter -= 1
        elif isinstance(v, dict):
            if xml[-1] != "\n":
                xml += "\n"
            counter += 1
            maxcounter = counter
            DictToXML(v)
            counter -= 1
        else:
            maxcounter = counter
            xml += str(v)
        if counter == maxcounter:
            xml += "</" + k + ">\n"
        else:
            xml += "\t" * (counter + 1) + "</" + k + ">\n"


maxcounter = 0
counter = 0
xml = '<?xml version="1.0" encoding="UTF-8" ?>\n<main>\n'
with open("mon_shedule.json", "r", encoding="utf-8") as f:
    text = f.read()
true = True
New_text = eval(text)
DictToXML(New_text)
xml += "</main>"
print(xml)
with open("../Outputs/output1.xml", "w", encoding="utf-8") as f:
    f.write(xml)
