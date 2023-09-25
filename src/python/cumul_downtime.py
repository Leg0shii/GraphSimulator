import common as cm
import pandas as pd
import matplotlib.pyplot as plt

# Dateinamen festlegen
sim_directory = 'src/python/'           ### Anpassen
sim_result_name = 'multi_sim_result1'   ### Anpassen
file_name = sim_directory + sim_result_name + '.txt'

# Simulationszeit eingeben
total_simulation_time = 1

# Lese die Datei in einen DataFrame
df = pd.read_csv(file_name, delimiter=cm.COLUMN_DELIMITER, skiprows=1)

# Berechne die Ausfallzeit für jeden Ausfall
df['downtime'] = (df['desend'] - df['desstart']) / (3600*24)

# Iteriere über alle eindeutigen Knotennamen
for node_name in df['name'].unique():
    node_df = df[df['name'] == node_name]
    
    # Erstelle eine kumulative Summe der Ausfallzeiten
    node_df['cumulative_downtime'] = node_df['downtime'].cumsum()
    
    # Plotte die kumulative Ausfallzeit über die Simulationszeit
    plt.plot(node_df['desend'] / (3600*24*365), node_df['cumulative_downtime'], label=node_name)

plt.xlabel('Simulationszeit [y]')
plt.ylabel('Kumulative Ausfallzeit [d]')

plt.xlim(0, total_simulation_time)

plt.legend()
plt.grid(True)

plt.gcf().set_figwidth(12)
plt.gcf().set_figheight(6)

plt.show()
