import pandas as pd
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
import numpy as np

# Lese die CSV-Datei ein
dataframe = pd.read_csv('src/python/simulation-results1.csv', delimiter=';')

# Entferne die erste Zeile, die die Simulationsparameter enthält
#dataframe = dataframe.iloc[1:]

# Erstelle einen 3D-Plot
fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')

# Durchlaufe alle einzigartigen Knotennamen
for name in dataframe['name'].unique():
    subset = dataframe[dataframe['name'] == name]
    
    # Extrahiere die Daten für den Plot
    x = subset['meandes'].astype(float) / (60*60*24)
    y = subset['meanrep'].astype(float) / (60*60)

    z = subset['available'].str.rstrip('%').astype(float)
    z = z.apply(lambda z: z if z < 100 else 99.9999) / 100
    z = np.log10(1 - z)
    
    # Füge die Daten zum Plot hinzu
    ax.scatter(x, y, z, label=name)

# Achsenbeschriftungen
ax.set_xlabel('MTBF [d]')
ax.set_ylabel('MTTR [h]')
ax.set_zlabel('Verfügbarkeit (%)')

# Setze die y-Achsenticks und -beschriftungen manuell
zticks = [90, 99, 99.9, 99.99, 99.999, 99.9999]
#ax.set_zscale("log")
ax.set_zticks(np.log10(1 - np.array(zticks) / 100))
ax.set_zticklabels([str(z) for z in zticks])
ax.invert_zaxis()

# Zeige die Legende
ax.legend()

# Zeige den Plot
plt.show()
