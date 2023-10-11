# Count Unaligned Pairs of Bytes

In this problem you are given a memory blob and a target byte, and you need to return how many times there are a pair of bytes next to each other with the value of the target byte. The difficult thing with this problem is that the pair of bytes do not need to be aligned.

```
uint64_t count_pairs(uint8_t *data, uint64_t size, uint8_t target) {
  uint64_t total = 0;
  for (uint64_t i = 0; i < size - 1; i++) {
    if (data[i] == target && data[i + 1] == target) {
      total += 1;
    }
  }
  return total;
}
```

The goal is to speed this up.

Note: size might not be a even number
## Hints
### Vectorization

You can use SIMD instructions to make comparisons across many bytes at one. A list of all simd instructions for intel processors can be seen https://www.intel.com/content/www/us/en/docs/intrinsics-guide/index.html.

A few specific ones that may be useful are

`__m256i _mm256_set1_epi8 (char a)`
```
FOR j := 0 to 31
  i := j*8
  dst[i+7:i] := a[7:0]
ENDFOR
dst[MAX:256] := 0
```

`__m256i _mm256_setzero_si256 (void)`
```
dst[MAX:0] := 0
```
`__m256i _mm256_loadu_si256 (__m256i const * mem_addr)`
```
dst[255:0] := MEM[mem_addr+255:mem_addr]
dst[MAX:256] := 0
```
`__m256i _mm256_cmpeq_epi8 (__m256i a, __m256i b)`
```
FOR j := 0 to 31
    i := j*8
    dst[i+7:i] := ( a[i+7:i] == b[i+7:i] ) ? 0xFF : 0
ENDFOR
dst[MAX:256] := 0
```

`__m256i _mm256_add_epi8 (__m256i a, __m256i b)`
```
FOR j := 0 to 31
    i := j*8
    dst[i+7:i] := a[i+7:i] + b[i+7:i]
ENDFOR
dst[MAX:256] := 0
```
`__m256i _mm256_sub_epi8 (__m256i a, __m256i b)`
```
FOR j := 0 to 31
    i := j*8
    dst[i+7:i] := a[i+7:i] - b[i+7:i]
ENDFOR
dst[MAX:256] := 0
```
`int _mm256_extract_epi8 (__m256i a, const int index)`
```
dst[7:0] := (a[255:0] >> (index[4:0] * 8))[7:0]
```
`int _mm256_movemask_epi8 (__m256i a)`
```
FOR j := 0 to 31
    i := j*8
    dst[j] := a[i+7]
ENDFOR
```
`int _mm_popcnt_u32 (unsigned int a)`
```
dst := 0
FOR i := 0 to 31
    IF a[i]
        dst := dst + 1
    FI
ENDFOR
```

### Parallelization

The iterations of the loop are independant, so they can be parallelized.

You may want to use a reducer so that you can update the sum from different threads in parallel without races.

An example of a sum reducer of type long can be seen below
```
#include <cilk/cilk.h>

void zero_i(void *v) { *(long *)v = 0; }
void plus_i(void *l, void *r) { *(long *)l += *(long *)r; }

long sum_array(long *array, size_t n) {
  long cilk_reducer(zero_i, plus_i) sum = 0;
  cilk_for (size_t i = 0; i < n; ++i)
    sum += array[i];
  return sum;
}  
```
