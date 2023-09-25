import common as cm
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

# Dateinamen festlegen
sim_directory = 'src/python/'           ### Anpassen
sim_result_name = 'multi_sim_result1'   ### Anpassen
file_name = sim_directory + sim_result_name + '.txt'

# Lese die CSV-Datei ein
df = pd.read_csv(file_name, delimiter=cm.COLUMN_DELIMITER, skiprows=1)

# Umrechnung der Verfügbarkeiten in Werte zwischen 0 und 1
df[cm.AVAILABILITY] = df[cm.AVAILABILITY].str.rstrip('%').astype(float) / 100

# Gruppieren der Daten nach MTBF und MTTR und Berechnung des Produkts der Verfügbarkeiten
grouped_df = df.groupby([cm.MTBF, cm.MTTR])[cm.AVAILABILITY].prod().reset_index()

# Erstelle einen 2D-Plot
fig, ax = plt.subplots()

# Wähle den höchsten und kleinsten MTBF-Wert sowie Werte aus der Mitte
unique_mtbf_values = np.sort(grouped_df[cm.MTBF].unique())
n = 4  # Anzahl der Werte
indices = np.linspace(0, len(unique_mtbf_values) - 1, n).astype(int)
selected_mtbf_values = np.concatenate([unique_mtbf_values[indices]])

# Durchlaufe die ausgewählten MTBF-Werte
for mtbf in selected_mtbf_values:
    subset = grouped_df[grouped_df[cm.MTBF] == mtbf]
    
    # Extrahiere die Daten für den Plot
    x = subset[cm.MTTR].astype(float) / (60*60)
    y = 1 - subset[cm.AVAILABILITY].apply(lambda x: x if x < 1 else 0.999999)
    
    # Füge die Daten zum Plot hinzu
    ax.scatter(x, y, marker='.', label=f'MTBF [d] = {mtbf / (60*60*24):.2f}')

    # Füge eine Regressionslinie hinzu
    coeffs = np.polyfit(x, y, 1)
    y_fit = np.polyval(coeffs, x)
    ax.plot(x, y_fit, linestyle='dashdot')

# Achsenbeschriftungen
ax.set_xlabel('MTTR [h]')
ax.set_ylabel('Verfügbarkeit [%]')

# Setze die y-Achsenticks und -beschriftungen manuell
ax.set_yscale("log")
yticks = [90, 99, 99.9, 99.99, 99.999, 99.9999]
ax.set_yticks(1 - np.array(yticks) / 100)
ax.set_yticklabels([str(y) for y in yticks])

# Kehre die y-Achse um
ax.invert_yaxis()
ax.grid(visible=True, which='both')

# Zeige die Legende
ax.legend()

fig.set_figwidth(12)
fig.set_figheight(6)

plt.show()
