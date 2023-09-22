import pandas as pd
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
import numpy as np

# Lese die CSV-Datei ein
dataframe = pd.read_csv('src/python/simulation-results1.csv', delimiter=';')

# Entferne die erste Zeile, die die Simulationsparameter enthält
#dataframe = dataframe.iloc[1:]

# Umrechnung der Verfügbarkeiten in Werte zwischen 0 und 1
dataframe['available'] = dataframe['available'].str.rstrip('%').astype(float) / 100

# Gruppieren der Daten nach MTBF und MTTR und Berechnung des Produkts der Verfügbarkeiten
grouped_df = dataframe.groupby(['meandes', 'meanrep'])['available'].prod().reset_index()

# Extrahiere die Daten für den Plot
x = grouped_df['meandes'].astype(float) / (60*60*24)
y = grouped_df['meanrep'].astype(float) / (60*60)
z = grouped_df['available'].apply(lambda z: z if z < 1 else 99.9999/100)
z = np.log10(1 - z)

# Erstelle einen 3D-Plot
fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')

# Füge die Daten zum Plot hinzu
ax.scatter(x, y, z)

# Achsenbeschriftungen
ax.set_xlabel('MTBF [d]')
ax.set_ylabel('MTTR [h]')
ax.set_zlabel('Verfügbarkeit [%]')

# Setze die y-Achsenticks und -beschriftungen manuell
zticks = [90, 99, 99.9, 99.99, 99.999, 99.9999]
ax.set_zticks(np.log10(1 - np.array(zticks) / 100))
ax.set_zticklabels([str(z) for z in zticks])
ax.invert_zaxis()

# Zeige die Legende
ax.legend()

# Zeige den Plot
plt.show()
