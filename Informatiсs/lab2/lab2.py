import numpy as np

a = np.array([(1, -1), (-2, 3)])
b = np.array([-4, -2])

res = np.kron(a, b)
print(res)
