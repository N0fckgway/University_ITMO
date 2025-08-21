# #
# # import numpy as np
# #
# #
# #
# # #1 номер
# #
# # def y(x, a):
# #     if x < 0.2:
# #         return a * x * np.cos(3 * x)
# #     elif x == 0.2:
# #         return 13.5 * x ** 2
# #     else:
# #         return a * x ** 2 * np.sin(2 * x ** 2)
# #
# #
# # x_values = np.arange(-3, 1.2 + 0.4, 0.4)
# #
# # results = [(x, y(x, 2)) for x in x_values]
# #
# # for x, y_val in results:
# #     print(f"x = {x:.1f}, y = {y_val:.6f}")
# #
# # #2 Найти максимальные значения
# #
# # max_y = max(y_val for x, y_val in results)
# # print(f"Наибольшее значение y: {max_y:.6f}")
# #
# # #3 Вычислить
# #
# # from sympy import symbols, Sum, factorial, simplify
# #
# # k, n = symbols('k n')
# # series = Sum(factorial(k + 1) / (k ** 5), (k, 1, n))
# # sum_value = simplify(series.doit())
# #
# # # Подставляем n = 5
# # result = sum_value.subs(n, 5).evalf()
# # print(f"Сумма ряда для n = 5: {result:.6f}")
# #
# # #4 Вычислите с точностью eps = 0.001
# # def calculate_series(epsilon=0.001):
# #     total = 0
# #     i = 5
# #     while True:
# #         term = 1 / (i * (i + 1) * (i + 2))
# #         total += term
# #         if term < epsilon:
# #             break
# #         i += 1
# #     return total, i
# #
# #
# # sum_result, terms = calculate_series()
# # print(f"Сумма ряда с точностью 0.001: {sum_result:.6f} (вычислено за {terms} шагов)")
#
#
# # import numpy as np
# #
# # G = np.array([
# #     [6, 5, 9, -7],
# #     [5, 6, 7, -3],
# #     [9, 7, 14, -11],
# #     [-7, -3, -11, 13]
# # ])
# #
# # S = np.array([
# #     [3, 2, 6, -8],
# #     [0, 1, 0, -2],
# #     [-2, 0, -3, 4],
# #     [0, 2, 2, -1]
# # ])
# #
# # G_new = S.T @ G @ S
# # print(G_new)
#
#
# # import random
# # def generateList() -> list:
# #     return [random.randint(-10000, 10000) for _ in range(random.randint(1, 10))]
# #
# #
# # def A() -> int:
# #     tmp = []
# #     penis = generateList()
# #     for i in penis:
# #         if i >= 0:
# #             tmp.append(i)
# #     print(penis)
# #     return min(tmp)
# #
# # print(A())
#
#
# import numpy as np
#
# # Векторы
# x = np.array([4, -2, -4])
# y = np.array([-2, 0, 2])
#
# # Матрица Грама
# G = np.array([
#     [2, 3, -3],
#     [3, 6, -7],
#     [-3, -7, 9]
# ])
#
# # Скалярное произведение с матрицей Грама
# def gram_dot(a, b, G):
#     return a @ G @ b
#
# # Нормы
# norm_x = np.sqrt(gram_dot(x, x, G))
# norm_y = np.sqrt(gram_dot(y, y, G))
#
# # Скалярное произведение
# dot_xy = gram_dot(x, y, G)
#
# # Косинус угла
# cos_theta = dot_xy / (norm_x * norm_y)
#
# # Угол в радианах
# theta_rad = np.arccos(cos_theta)
#
# # Угол в градусах
# theta_deg = np.degrees(theta_rad)
#
# print(f"Косинус угла: {cos_theta:.3f}")
# print(f"Угол (радианы): {theta_rad:.2f}")
# print(f"Угол (градусы): {theta_deg:.1f}")
#
#
#


# for x in '0123456789ABCDEFGHIJKLMNOPQRS':
#     n = int(f'463{x}7921', 29) + int(f'8241{x}153', 29)
#     if n % 28 == 0:
#         print(n)


# def f(x, y):
#     return (2 * x + y != 70) or (x < y) or (A < x)
#
# i = []
# for A in range(100000):
#     if all(f(x, y) == 1 for x in range(1000) for y in range(1000)):
#         i.append(A)
# print(max(i))


# def f(s, m):
#     if s <= 29: return m % 2 == 0
#     if m == 0: return 0
#     h = [f(s - 3, m - 1), f(s - 6, m - 1), f(s // 3, m - 1)]
#     return any(h) if (m - 1) % 2 == 0 else all(h)
#
#
# print(19, [s for s in range(30, 1000) if not f(s, 2) and f(s, 4)])
