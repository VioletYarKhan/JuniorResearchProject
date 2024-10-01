import matplotlib.pyplot as plt
import pandas as pd

# Load data from CSV file
data = pd.read_csv('/Users/nicholas/Desktop/Junior_Research_Project/app/simulation_output.csv')

# Plotting the data
plt.figure(figsize=(10, 6))

plt.subplot(4, 1, 1)
plt.plot(data['Timestep'], data['Living Population'], label='Living Population', color='blue')
plt.xlabel('Timestep')
plt.ylabel('Living Population')
plt.title('Living Population Over Time')
plt.grid(True)

plt.subplot(4, 1, 2)
plt.plot(data['Timestep'], data['Active Infections'], label='Active Infections', color='red')
plt.xlabel('Timestep')
plt.ylabel('Active Infections')
plt.title('Active Infections Over Time')
plt.grid(True)

plt.subplot(4, 1, 3)
plt.plot(data['Timestep'], data['Economic Productivity'], label='Economic Productivity', color='green')
plt.xlabel('Timestep')
plt.ylabel('Economic Productivity')
plt.title('Economic Productivity Over Time')
plt.grid(True)

plt.subplot(4, 1, 4)
plt.plot(data['Timestep'], data['Population Change'].rolling(window=5).mean(), label='Population Change', color='black')
plt.xlabel('Timestep')
plt.ylabel('Population Change')
plt.title('Population Change Over Time')
plt.grid(True)

plt.tight_layout()
plt.show()
