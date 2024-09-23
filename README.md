# Graph Simulator

This tool helps you test and understand your network's reliability. You can simulate various failure scenarios and see how the network handles them.

## Features

### 1. **Create Nodes and Connections**
Place network nodes and set connections between them. You can define the redundancy level (n), which shows how many alternative paths are available if a connection fails.

<img src="https://github.com/user-attachments/assets/6a011be2-d97a-467d-bb39-4df70aa95d6b" width="400"/>

### 2. **Define Main Nodes**
Choose which nodes are critical (main nodes). These nodes always need to have a connection, no matter what happens to the rest of the network.

<img src="https://github.com/user-attachments/assets/0e48a097-2deb-4ba2-9485-f8106127e576" width="400"/>

### 3. **Configure Simulation Parameters**
Adjust settings like:
   - **Runtime**: How long the simulation runs.
   - **Average Connection Loss**: How often connections fail.
   - **Standard Deviation of Connection Loss**: Variability in connection failures.
   - **Average Fix Time**: How long repairs take.
   - **Standard Deviation of Fix Time**: Variability in repair times.
   - **Randomized Attack Count**: Simulate random attacks that reduce network redundancy.

<img src="https://github.com/user-attachments/assets/15151148-a356-4831-ba0a-61d070f3190b" width="400"/>


### 4. **Track Network Failures**
The simulator logs when nodes lose their connection to main nodes. You get detailed timestamps of each failure event.

### 5. **Save and Load Configurations**
Save your network setups and load them later. This way, you can easily test different configurations.

### 6. **Run Multiple Simulations**
Simulate different scenarios by setting a range and step size for parameters like failure rate and fix time. This helps you see how changes affect network stability.

<img src="https://github.com/user-attachments/assets/ee86eb46-8465-4c00-8c23-31ae55f88a0f" width="400"/>
<img src="https://github.com/user-attachments/assets/c9d01b67-5547-4f87-8d14-13488947abdd" width="400"/>

### 7. **Estimate Required Redundancy**
Find out how much redundancy you need to maintain a stable network with a specific reliability level (like 99.9%).

## Why Use It?

This tool is useful for checking how well your network can handle failures and for planning improvements. You can test different setups and see what happens when things go wrong. It’s a practical way to make sure your network can handle unexpected problems.

## How to Start

1. Clone the repository.
2. Set up your network nodes and connections.
3. Define your main nodes.
4. Configure your simulation parameters.
5. Run the simulation and check the results.

## Contributions

If you have improvements or find bugs, please open an issue or submit a pull request.

If you have questions, feel free to ask in the issues section.
