# s = str(int(input("Введите число в Фибоначчиевой системе счисления: ")))[::-1]
# fib = [1, 2]
# sum = 0
# des = 0
# for i in range(0, len(s)):
#     sum = fib[i] + fib[i + 1]
#     fib.append(sum)
#
# for p in range(len(s)):
#     if s[p] == '1':
#         des += fib[p]
# des = str(des)
# print("Ответ в десятичной системе счисления: " + des)

BookPages = int(input())  
Days = [int(x) for x in input().split()]


current_day = 0
while BookPages > 0:
    BookPages -= Days[current_day]
    if BookPages <= 0:
        print(current_day + 1)
        break
    current_day = (current_day + 1) % 7
