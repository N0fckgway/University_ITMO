B = 198
A = 95
def f(s):
    AC = s
    if AC < 0 or AC == 0:
        AC *= 4
        AC -= s
        AC += B
        s = AC
        return s
    if AC - A == 0 or AC < A:
        AC = A
        s = AC
        return s
    AC *= 4
    AC -= s
    AC += B
    s = AC
    return s


print(f(8))

