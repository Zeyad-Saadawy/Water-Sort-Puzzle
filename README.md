# Water Sort Puzzle Solver

## Project Overview
This project implements multiple search algorithms to solve the Water Sort Puzzle, focusing on six main strategies:
- **BF (Breadth-First Search)**
- **DF (Depth-First Search)**
- **UC (Uniform Cost Search)**
- **GR1, GR2 (Greedy Search with two heuristics)**
- **AS1, AS2 (A* Search with two heuristics)**

Each strategy uses a specific approach or heuristic to guide the search towards the goal, where each bottle contains only one color.

## Implementation Details

### Classes

- **Node**: Represents each state in the search space. Each `Node` instance holds information about its parent node, operator (action taken), path cost, depth, and heuristic value, which is initialized to `0`. The `Node` class calculates heuristic values based on the selected strategy.

- **Bottle**: Models each bottle in the puzzle, storing the capacity and layers (colors) as a stack. It includes functionality to check if a bottle is uniform (i.e., all layers are the same color) and to manage layers of colors, including filling with empty spaces when necessary.

- **NodeComparator**: Used to compare nodes within a priority queue based on the search strategy. It ensures nodes are ordered by path cost for UC, heuristic value (`h(n)`) for Greedy, and total cost (`f(n) = g(n) + h(n)`) for A* search.

- **GenericSearch**: Defines the framework for different search strategies, managing the expansion of nodes in the frontier based on a queue, stack, or priority queue. The `addToFrontier` method is key to adding nodes based on the chosen search strategy.

- **WaterSortSearch**: Extends `GenericSearch` and implements the specific search strategies (BF, DF, UC, GR1, GR2, AS1, AS2). It calculates and applies the appropriate heuristics for Greedy and A* searches, expanding nodes based on path cost and heuristic values as required.

### Search Strategies
1. **Breadth-First Search (BF)**:
   - Expands nodes in the order they are added to a queue, exploring the shallowest nodes first without considering path cost or heuristics.

2. **Depth-First Search (DF)**:
   - Uses a stack to explore the deepest node in the frontier, allowing the search to follow paths until the end before backtracking.

3. **Uniform Cost Search (UC)**:
   - Expands nodes based solely on the path cost (`g(n)`), using the priority queue to always choose the node with the lowest cumulative path cost.

4. **Greedy Search**:
   - **GR1**: Expands nodes based on `calculateH1()`, prioritizing states with fewer non-uniform bottles.
   - **GR2**: Expands nodes based on `calculateH2()`, focusing on states with fewer misplaced layers.

5. **A* Search**:
   - **AS1**: Expands nodes using the total cost `f(n) = g(n) + h(n)` with `calculateH1()`, combining path cost and the number of non-uniform bottles.
   - **AS2**: Expands nodes using `f(n) = g(n) + h(n)` with `calculateH2()`, factoring in path cost and the number of misplaced layers.

The algorithms use a priority queue for UC, Greedy, and A* strategies, ensuring that nodes with the lowest costs or heuristic values are expanded first.

## Heuristic Functions and Admissibility

1. **Heuristic 1 (H1): Number of Non-Uniform Bottles**
   - Measures how many bottles still contain mixed colors.
   - Used in `GR1` and `AS1` to focus on bottle uniformity.
   - *Admissibility*: H1 is admissible as it provides a lower bound on steps required to make all bottles uniform.

2. **Heuristic 2 (H2): Number of Misplaced Layers**
   - Counts misplaced layers in all bottles, where each misplaced layer contributes to the complexity of reaching the goal.
   - Used in `GR2` and `AS2` to prioritize states with fewer misplaced layers.
   - *Admissibility*: H2 is admissible as it does not overestimate the actions required to organize all layers.

## Summary
- **H1** (Non-Uniform Bottles) provides a coarse estimate of proximity to the goal, guiding GR1 and AS1 strategies.
- **H2** (Misplaced Layers) offers a finer measure of disorganization, guiding GR2 and AS2 strategies.
Both heuristics are admissible, making them suitable for use in Greedy and A* search algorithms and ensuring optimal solutions.
