
import numpy as np



#1 номер

def y(x, a):
    if x < 0.2:
        return a * x * np.cos(3 * x)
    elif x == 0.2:
        return 13.5 * x ** 2
    else:
        return a * x ** 2 * np.sin(2 * x ** 2)


x_values = np.arange(-3, 1.2 + 0.4, 0.4)

results = [(x, y(x, 2)) for x in x_values]

for x, y_val in results:
    print(f"x = {x:.1f}, y = {y_val:.6f}")

#2 Найти максимальные значения

max_y = max(y_val for x, y_val in results)
print(f"Наибольшее значение y: {max_y:.6f}")

#3 Вычислить

from sympy import symbols, Sum, factorial, simplify

k, n = symbols('k n')
series = Sum(factorial(k + 1) / (k ** 5), (k, 1, n))
sum_value = simplify(series.doit())

# Подставляем n = 5
result = sum_value.subs(n, 5).evalf()
print(f"Сумма ряда для n = 5: {result:.6f}")

#4 Вычислите с точностью eps = 0.001
def calculate_series(epsilon=0.001):
    total = 0
    i = 5
    while True:
        term = 1 / (i * (i + 1) * (i + 2))
        total += term
        if term < epsilon:
            break
        i += 1
    return total, i


sum_result, terms = calculate_series()
print(f"Сумма ряда с точностью 0.001: {sum_result:.6f} (вычислено за {terms} шагов)")
