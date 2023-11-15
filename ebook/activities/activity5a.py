import numpy as np
import matplotlib.pyplot as plt

# glider pattern
glider = np.array([[0,1,0],[0,0,1],[1,1,1]], dtype=np.uint8)
plt.imshow(glider, cmap='Greys', interpolation='nearest')
plt.show()

import dask.array as da

# put the glider in the top corner of a 16x16 grid
grid = np.zeros((16,16), dtype=np.uint8)
grid[0:glider.shape[0],0:glider.shape[1]] = glider

# build a dask array of 4 8x8 chunks
dagrid = da.from_array(grid, chunks=(8, 8))

# print the contents: see the glider
plt.imshow(dagrid, cmap='Greys', interpolation='nearest')
plt.show()

# 64 total iterations to repeat
for i in range(16):
    # glider shape repeats every 4 steps --
    for i in range(4):

        # define overlappings region for stencil computations
        # TODO ...

        # update the regions in parallel
        # TODO ...

        # trim the chunks and rebuild the overlap
        # TODO ...

    # print every fourth cycle -- should still look like a glider
    plt.imshow(dagrid, cmap='Greys', interpolation='nearest')
    plt.show()