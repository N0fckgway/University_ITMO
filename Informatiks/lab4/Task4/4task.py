from lark import Lark, Transformer

# Грамматика JSON
json_grammar = r"""
    start: value

    value: object
         | array
         | string
         | number
         | "true"  -> true
         | "false" -> false
         | "null"  -> null

    object : "{" [members] "}"
    members: pair ("," pair)*
    pair   : string ":" value

    array  : "[" [elements] "]"
    elements: value ("," value)*

    string : ESCAPED_STRING

    number : SIGNED_NUMBER

    %import common.ESCAPED_STRING
    %import common.SIGNED_NUMBER
    %import common.WS_INLINE

    %ignore WS_INLINE
"""


class JsonToXmlTransformer(Transformer):
    def __init__(self):
        self.counter = 0
        self.max_counter = 0
        self.xml = ""

    def start(self, items):
        return self.xml

    def object(self, items):
        self.counter += 1
        self.max_counter = max(self.counter, self.max_counter)
        self.xml += "\t" * self.counter + "<object>\n"
        for item in items:
            # Преобразуем Tree в строку
            item_str = ''.join(str(child) for child in item.children)
            self.xml += item_str
        self.counter -= 1
        self.xml += "\t" * self.counter + "</object>\n"
        return self.xml

    def pair(self, items):
        key, value = items
        # Преобразуем Tree в строку
        value_str = ''.join(str(child) for child in value.children)
        self.xml += "\t" * (self.counter + 1) + "<" + key + ">" + value_str + "</" + key + ">\n"
        return self.xml

    def array(self, items):
        self.counter += 1
        self.max_counter = max(self.counter, self.max_counter)
        self.xml += "\t" * self.counter + "<array>\n"
        for item in items:
            # Преобразуем Tree в строку
            item_str = ''.join(str(child) for child in item.children)
            self.xml += item_str
        self.counter -= 1
        self.xml += "\t" * self.counter + "</array>\n"
        return self.xml

    def string(self, items):
        return items[0]

    def number(self, items):
        return items[0]

    def true(self, _):
        return "true"

    def false(self, _):
        return "false"

    def null(self, _):
        return "null"


if __name__ == "__main__":
    # Чтение файла JSON
    with open("mon_shedule.json", "r", encoding="utf-8") as f:
        json_data = f.read()

    # Парсинг JSON
    parser = Lark(json_grammar, parser='lalr', transformer=JsonToXmlTransformer())
    tree = parser.parse(json_data)

    # Генерация XML
    xml = '<?xml version="1.0" encoding="UTF-8"?>\n' + tree + '\n</main>'

    # Запись результата в файл
    with open("../Outputs/output4.xml", "w", encoding="utf-8") as f:
        f.write(xml)
