import common as cm
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

# Dateinamen festlegen
sim_directory = 'src/python/'           ### Anpassen
sim_result_name = 'multi_sim_result1'   ### Anpassen
file_name = sim_directory + sim_result_name + '.txt'

# Lese die CSV-Datei ein
dataframe = pd.read_csv(file_name, delimiter=cm.COLUMN_DELIMITER, skiprows=1)

# Erstelle einen 3D-Plot
fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')

# Durchlaufe alle einzigartigen Knotennamen
for name in dataframe[cm.NODE_NAME].unique():
    subset = dataframe[dataframe[cm.NODE_NAME] == name]
    
    # Extrahiere die Daten für den Plot
    x = subset[cm.MTBF].astype(float) / (60*60*24)
    y = subset[cm.MTTR].astype(float) / (60*60)

    z = subset[cm.AVAILABILITY].str.rstrip('%').astype(float)
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

fig.set_figwidth(6)
fig.set_figheight(6)

# Zeige den Plot
plt.show()
