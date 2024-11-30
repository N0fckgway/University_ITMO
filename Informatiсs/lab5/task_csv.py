# import csv
# import pandas as pd
# import seaborn as sns
# import matplotlib.pyplot as plt
#
# with open("data.csv", encoding='utf-8') as f:
#     data = csv.reader(f, delimiter="	")
#     arr = [[], [], [], []]
#     col = ["Open", "High", "Low", "Close"]
#     dates = ['19/9/18', '10/10/18', '7/11/18', '17/12/18']
#     count = 0
#     for row in data:
#         print(row)
#         if count == 0:
#             pass
#
#         elif row[0] == '19/9/18':
#             arr[0].append([int(row[1]), int(row[2]), int(row[3]), int(row[4])])
#         elif row[0] == '10/10/18':
#             arr[1].append([int(row[1]), int(row[2]), int(row[3]), int(row[4])])
#         elif row[0] == '7/11/18':
#             arr[2].append([int(row[1]), int(row[2]), int(row[3]), int(row[4])])
#         elif row[0] == '17/12/18':
#             arr[3].append([int(row[1]), int(row[2]), int(row[3]), int(row[4])])
#         count += 1
#
#     plt.figure(figsize=(10, 7))
#     for i in range(4):
#         data = pd.DataFrame(arr[i], columns=col)
#         plt.subplot(2, 2, i + 1)
#         sns.boxplot(data=data)
#         plt.title(dates[i])
#     plt.subplots_adjust(wspace=0.5, hspace=0.4)
#     plt.show()

for i in range(0, 8):
    n = bin(i)[2:]
    print(n)