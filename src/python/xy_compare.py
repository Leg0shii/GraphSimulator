import common as cm
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

# Dateinamen festlegen
sim_directory = 'src/python/'           ### Anpassen
sim_result_1_name = 'multi_sim_result1'   ### Anpassen
sim_result_2_name = 'multi_sim_result2'   ### Anpassen
file_name_1 = sim_directory + sim_result_1_name + '.txt'
file_name_2 = sim_directory + sim_result_2_name + '.txt'

# Dateien in DataFrames einlesen (ohne Konfiguration)
# Verfügbarkeit umwandeln
df1 = pd.read_csv(file_name_1, delimiter=cm.COLUMN_DELIMITER, skiprows=1)
df1[cm.AVAILABILITY] = df1[cm.AVAILABILITY].str.rstrip('%').astype(float) / 100

df2 = pd.read_csv(file_name_2, delimiter=cm.COLUMN_DELIMITER, skiprows=1)
df2[cm.AVAILABILITY] = df2[cm.AVAILABILITY].str.rstrip('%').astype(float) / 100

# Daten nach MTBF und MTTR gruppieren und Produkt der Verfügbarkeiten berechnen
grouped_df1 = df1.groupby([cm.MTBF, cm.MTTR])[cm.AVAILABILITY].prod().reset_index()
grouped_df1[cm.AVAILABILITY] = grouped_df1[cm.AVAILABILITY].apply(lambda x: x if x < 1 else 0.999999)

grouped_df2 = df2.groupby([cm.MTBF, cm.MTTR])[cm.AVAILABILITY].prod().reset_index()
grouped_df2[cm.AVAILABILITY] = grouped_df2[cm.AVAILABILITY].apply(lambda x: x if x < 1 else 0.999999)

# Führe einen Merge der beiden DataFrames basierend auf MTBF und MTTR durch
merged_df = pd.merge(grouped_df1, grouped_df2, on=[cm.MTBF, cm.MTTR], suffixes=('_1', '_2'))

# Dividiere die Verfügbarkeitswerte der beiden DataFrames miteinander
merged_df['availability_ratio'] = merged_df['available_2'] / merged_df['available_1']

# Erstelle eine Matrix für den Tile-Plot
unique_mtbf = sorted(merged_df[cm.MTBF].unique())
unique_mttr = sorted(merged_df[cm.MTTR].unique())
matrix = np.zeros((len(unique_mtbf), len(unique_mttr)))

for i, mtbf in enumerate(unique_mtbf):
    for j, mttr in enumerate(unique_mttr):
        value = merged_df[(merged_df[cm.MTBF] == mtbf) & (merged_df[cm.MTTR] == mttr)]['availability_ratio'].values
        if value:
            matrix[i, j] = value[0]

# Erstelle den Tile-Plot
plt.imshow(matrix, cmap='viridis', origin='lower', aspect='auto', extent=[unique_mtbf[0], unique_mtbf[-1], unique_mttr[0], unique_mttr[-1]])
plt.colorbar(label='Verhältnis der Verfügbarkeiten')

plt.xlabel('MTBF [s]')
plt.ylabel('MTTR [s]')

plt.gcf().set_figwidth(12)
plt.gcf().set_figheight(6)

plt.show()
