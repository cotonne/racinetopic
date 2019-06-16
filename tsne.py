import numpy as np
from sklearn.manifold import TSNE
import matplotlib.pyplot as plt
from numpy import genfromtxt, savetxt

labels = genfromtxt('termsId.txt', dtype='|U25').reshape((1505, 1))
print(labels)
X = genfromtxt('test.csv', delimiter=',')
X = X[:, :-1]

# X_embedded = TSNE(n_components=2).fit_transform(X)
# print(X_embedded.shape)
# plt.scatter(X_embedded[:, 0], X_embedded[:, 1], c="r")
# plt.show()

X_embedded = TSNE(n_components=2).fit_transform(np.transpose(X))

fig,ax = plt.subplots()
sc = plt.scatter(X_embedded[:, 0], X_embedded[:, 1], c="r")

annot = ax.annotate("", xy=(0,0), xytext=(20,20),textcoords="offset points",
                    bbox=dict(boxstyle="round", fc="w"),
                    arrowprops=dict(arrowstyle="->"))
annot.set_visible(False)
l = labels.tolist()

def update_annot(ind):
    index = ind["ind"].tolist()
    pos = sc.get_offsets()[ind["ind"][0]]
    annot.xy = pos
    text = "\n".join([l[n][0] for n in index])
    annot.set_text(text)

def hover(event):
    vis = annot.get_visible()
    if event.inaxes == ax:
        cont, ind = sc.contains(event)
        if cont:
            update_annot(ind)
            annot.set_visible(True)
            fig.canvas.draw_idle()
        else:
            if vis:
                annot.set_visible(False)
                fig.canvas.draw_idle()

fig.canvas.mpl_connect("motion_notify_event", hover)


plt.show()

X_embedded = np.concatenate((labels, X_embedded), axis= 1)
savetxt("tsne-terms.csv", X_embedded, fmt='%s,%s,%s')
