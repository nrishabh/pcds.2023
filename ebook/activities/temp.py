import dask.array as da
import numpy as np
import matplotlib.pyplot as plt

# glider pattern
glider = np.array([[0,1,0],[0,0,1],[1,1,1]], dtype=np.uint8)

# put the glider in the top corner of a 16x16 grid  
grid = np.zeros((16,16), dtype=np.uint8)
grid[0:glider.shape[0],0:glider.shape[1]] = glider

# build a dask array of 4 8x8 chunks with 1 cell overlap
chunks = (8,8)
slices = [[slice(0,-1),slice(0,-1)], 
          [slice(1,None),slice(0,-1)],
          [slice(0,-1),slice(1,None)],
          [slice(1,None),slice(1,None)]]
dags = [da.from_array(grid[s], chunks=chunks) for s in slices]
dagrid = da.block(dags)

# iterate 64 times
for i in range(64):
    # apply rules every 4 iterations 
    if i % 4 == 0:
        # compute neighbor counts with overlaps    
        rolls = [d.roll(1,0) for d in dags] 
        rolls += [d.roll(0,1) for d in dags]
        rolls += [d.roll(-1,0) for d in dags] 
        rolls += [d.roll(0,-1) for d in dags]
        
        # apply rules        
        dagrid = (dagrid.sum(axis=0) == 3) | (dagrid & (rolls.sum(axis=0) == 2))

    # trim chunks        
    dagrid = da.map_blocks(lambda x: x[1:-1,1:-1], dagrid, chunks=chunks, drop_axis=[0,1])
    
    if i % 4 == 0:
        print("After %d iterations:" % (i+1))
        plt.imshow(dagrid.compute(), cmap='Greys', interpolation='nearest')
        plt.show()