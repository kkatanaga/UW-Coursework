from math import floor
from timeit import default_timer as timer
from itertools import combinations
from statistics import median

import matplotlib.pyplot as plt
import numpy as np

def multihash_pcy(chunk, threshold, hash_f1, hash_f2):
    # Pass 1
    s_frequent, p_bitmap1, p_bitmap2 = multihash_pcy_p1(chunk, threshold, hash_f1, hash_f2)
    
    # Pass 2
    p_cand = multihash_pcy_p2(chunk, s_frequent, p_bitmap1, p_bitmap2, hash_f1, hash_f2)

    return p_cand

def multihash_pcy_p1(chunk, threshold, hash_f1, hash_f2):
    s_supp = dict()     # Item counts => Item : Support(Item)
    p_hsh1 = dict()     # First hash table for pairs; hash_f1 pair key : Sum of support of hashed pairs
    p_hsh2 = dict()     # Second hash table for pairs; hash_f2 pair key : Sum of support of hashed pairs

    s_freq = set()      # Frequent items
    p_bmp1 = dict()     # First bitmap; hash_f1 pair key : 1 (Frequent) or 0 (Infrequent)
    p_bmp2 = dict()     # Second bitmap; hash_f2 pair key : 1 (Frequent) or 0 (Infrequent)
    
    for b in chunk:
        for item in b:
            s_supp[item] = s_supp.get(item, 0) + 1

            if s_supp[item] >= threshold:
                s_freq.add(item)                                    # L1; non-frequent are pruned
            
        for pair in combinations(b, 2): # All 2-tuple combinations of b
            k1 = hash_f1(pair[0], pair[1])
            # p_hsh1[k1] = p_hsh1.get(k1, 0) + 1
            # p_bmp1[k1] = 1 if p_hsh1.get(k1, 0) >= threshold else 0

            # k2 = hash_f2(pair[0], pair[1])
            # p_hsh2[k2] = p_hsh2.get(k2, 0) + 1
            # p_bmp2[k2] = 1 if p_hsh2.get(k2, 0) >= threshold else 0

            if p_bmp1.get(k1, 0) == 0:
                # if k1 not in p_bmp1:
                #     p_bmp1[k1] = 0

                if p_hsh1.get(k1, 0) + 1 >= threshold:
                    p_hsh1.pop(k1)
                    p_bmp1[k1] = 1
                else:
                    p_hsh1[k1] = p_hsh1.get(k1, 0) + 1
            
            k2 = hash_f2(pair[0], pair[1])
            if p_bmp2.get(k2, 0) == 0:
                # if k2 not in p_bmp2:
                #     p_bmp2[k2] = 0

                if p_hsh2.get(k2, 0) + 1 >= threshold:
                    p_hsh2.pop(k2)
                    p_bmp2[k2] = 1
                else:
                    p_hsh2[k2] = p_hsh2.get(k2, 0) + 1
            
    return s_freq, p_bmp1, p_bmp2

def multihash_pcy_p2(chunk, s_freq, p_bmp1, p_bmp2, hash_f1, hash_f2):
    p_cand = set()      # Candidate pairs; i, j are frequent AND bitmap[hash_f1(i, j)] = 1 AND bitmap[hash_f2(i, j)] = 1

    for b in chunk:
        for pair in combinations(b, 2):     # All 2-tuple combinations of b
            k1 = hash_f1(pair[0], pair[1])
            k2 = hash_f2(pair[0], pair[1])

            if p_bmp1.get(k1, 0) == 1 and p_bmp2.get(k2, 0) == 1 and pair[0] in s_freq and pair[1] in s_freq:
                p_cand.add(pair)
    
    return p_cand

def multistage_pcy(chunk, threshold, hash_f1, hash_f2):
    # Pass 1
    s_frequent, p_bitmap1 = single_pcy_p1(chunk, threshold, hash_f1)
    
    # Pass 2
    p_bitmap2 = multistage_pcy_p2(chunk, threshold, s_frequent, p_bitmap1, hash_f1, hash_f2)

    # Pass 3
    p_cand = multistage_pcy_p3(chunk, s_frequent, p_bitmap1, p_bitmap2, hash_f1, hash_f2)

    return p_cand

def multistage_pcy_p2(chunk, threshold, s_freq, p_bmp1, hash_f1, hash_f2):
    p_hsh2 = dict()     # Second hash table for pairs => Hashed pair key : Sum of support of hashed pairs

    p_bmp2 = dict()     # Second bitmap; i, j are frequent AND bitmap[hash_f1(i, j)] = 1

    for b in chunk:
        for pair in combinations(b, 2):     # All 2-tuple combinations of b
            k1 = hash_f1(pair[0], pair[1])
            k2 = hash_f2(pair[0], pair[1])

            # p_hsh2[k2] = p_hsh2.get(k2, 0) + 1
            # p_bmp2[k2] = 1 if p_hsh2.get(k2, 0) >= threshold and pair[0] in s_freq and pair[1] in s_freq and p_bmp1[k1] == 1 else 0

            if p_bmp2.get(k2, 0) == 1 or p_bmp1.get(k1, 0) == 0 or pair[0] not in s_freq or pair[1] not in s_freq:
                continue

            # if k2 not in p_bmp2:
            #     p_bmp2[k2] = 0

            if p_hsh2.get(k2, 0) + 1 >= threshold:
                p_hsh2.pop(k2)
                p_bmp2[k2] = 1
            else:
                p_hsh2[k2] = p_hsh2.get(k2, 0) + 1
    
    return p_bmp2

def multistage_pcy_p3(chunk, s_freq, p_bmp1, p_bmp2, hash_f1, hash_f2):
    p_cand = set()      # Candidate pairs; i, j are frequent AND bitmap[hash_f1(i, j)] = 1 AND bitmap[hash_f2(i, j)] = 1

    for b in chunk:
        for pair in combinations(b, 2):     # All 2-tuple combinations of b
            k1 = hash_f1(pair[0], pair[1])
            k2 = hash_f2(pair[0], pair[1])
            
            if p_bmp1.get(k1, 0) == 1 and p_bmp2.get(k2, 0) == 1 and pair[0] in s_freq and pair[1] in s_freq:
                p_cand.add(pair)
    
    return p_cand

def single_pcy(chunk, threshold, hash_f):
    # Pass 1
    s_frequent, p_bitmap = single_pcy_p1(chunk, threshold, hash_f)
    
    # Pass 2
    p_cand = single_pcy_p2(chunk, s_frequent, p_bitmap, hash_f)

    return p_cand
    
def single_pcy_p1(chunk, threshold, hash_f):
    s_supp = dict()     # Item counts => Item : Support(Item)
    p_hash = dict()     # Hash table for pairs => Hashed pair key : Sum of support of hashed pairs

    s_freq = set()      # Frequent items
    p_bitm = dict()     # Bitmap; Hashed pair key : 1 (Frequent) or 0 (Infrequent)
    
    for b in chunk:
        for item in b:
            s_supp[item] = s_supp.get(item, 0) + 1

            if s_supp[item] >= threshold:
                s_freq.add(item)                                    # L1; non-frequent are pruned
            
        for pair in combinations(b, 2):     # All 2-tuple combinations of b
            k = hash_f(pair[0], pair[1])

            # p_hash[k] = p_hash.get(k, 0) + 1
            # p_bitm[k] = 1 if p_hash.get(k, 0) >= threshold else 0

            if p_bitm.get(k, 0) == 1:
                continue

            # if k not in p_bitm:
            #     p_bitm[k] = 0

            if p_hash.get(k, 0) + 1 >= threshold:
                p_hash.pop(k)
                p_bitm[k] = 1
            else:
                p_hash[k] = p_hash.get(k, 0) + 1
            
    return s_freq, p_bitm

def single_pcy_p2(chunk, s_freq, p_bitm, hash_f):
    p_cand = set()      # Candidate pairs; i, j are frequent AND bitmap[hash_f(i, j)] = 1

    for b in chunk:
        for pair in combinations(b, 2):     # All 2-tuple combinations of b
            k = hash_f(pair[0], pair[1])

            if p_bitm.get(k, 0) == 1 and pair[0] in s_freq and pair[1] in s_freq:
                p_cand.add(pair)
    
    return p_cand
        
def a_priori(chunk, threshold):
    # Pass 1
    s_frequent = a_priori_p1(chunk, threshold)

    # Pass 2
    p_frequent, p_cand = a_priori_p2(chunk, threshold, s_frequent)

    return p_frequent, p_cand

def a_priori_p1(chunk, threshold):
    s_supp = dict()     # Item counts

    s_freq = set()      # Frequent + Pruned items; no count

    for b in chunk:
        for item in b:
            s_supp[item] = s_supp.get(item, 0) + 1

            if s_supp[item] >= threshold:
                s_freq.add(item)                                    # L1; non-frequent are pruned
    
    return s_freq

def a_priori_p2(chunk, threshold, s_freq):
    p_supp = dict()     # Pair : Support(Pair)

    p_cand = set()      # Candidate pairs, aka C2 which is generated based on L1
    p_freq = set()      # Frequent pairs

    for b in chunk:
        for pair in combinations(b, 2): # All 2-tuple combinations of b
            if pair[0] in s_freq and pair[1] in s_freq: # If i, j are frequent, then (i, j) is a candidate. Count occurrences.
                p_cand.add(pair)
                p_supp[pair] = p_supp.get(pair, 0) + 1

                if p_supp[pair] >= threshold:
                    p_freq.add(pair)
                    
    return p_freq, p_cand

def get_chunk_set(chunk):
    # Set of all singletons (unique) in chunk
    return {i for b in chunk for i in b}

def get_next_prime(n):
    # Get next prime greater than hash_n
    # List of primes taken from https://www.freecodecamp.org/news/prime-numbers-list-chart-of-primes/
    next_prime = 1

    with open('primes.txt', 'r') as primes:
        for prime in primes:
            if not prime.strip().isdigit():
                continue

            p = int(prime.strip())
            if p < n:
                continue

            next_prime = p
            break
    
    return next_prime

def read_retail():
    with open('retail.txt', 'r') as f:
        _file = f.read().split('\n')
        return [[int(_item) for _item in _basket.strip().split(' ')] for _basket in _file]

def decimal_to_rate(decimal):
    return decimal / 100

def compute_percentage(base, decimal):
    return floor(base * decimal_to_rate(decimal))

SUPPORT_THRESHOLDS = [1, 5, 10]
DATASET_PERCENT_DECIMALS = [1, 5, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100]
ALGORITHM_NAMES = ['A-Priori', 'PCY', 'Multistage PCY', 'Multihash PCY']
RUNTIME_RESULTS = {t : {d : {a : 0 for a in ALGORITHM_NAMES} for d in DATASET_PERCENT_DECIMALS} for t in SUPPORT_THRESHOLDS}
CANDIDATE_COUNTS = {t : {d : {a : 0 for a in ALGORITHM_NAMES} for d in DATASET_PERCENT_DECIMALS} for t in SUPPORT_THRESHOLDS}
FREQUENCY_COUNTS = {t : {d : 0 for d in DATASET_PERCENT_DECIMALS} for t in SUPPORT_THRESHOLDS}

def main():
    hash_n = 1
    def hash_f1(i, j):
        return (i * j) % hash_n

    def hash_f2(i, j):
        return (i + j) % hash_n
    
    baskets = read_retail()
    basket_count = len(baskets)

    for t_d in SUPPORT_THRESHOLDS:
        # if t_d < 10:
        #     continue

        print(f'Mining with {t_d}% support threshold...', end='', flush=True)

        for p_d in DATASET_PERCENT_DECIMALS:
            chunk_size = compute_percentage(basket_count, p_d)
            chunk = baskets[:chunk_size]
            threshold = compute_percentage(chunk_size, t_d)
            chunk_set = get_chunk_set(chunk)
            
            for _ in range(3):
                # A-Priori
                alg_name = 'A-Priori'
                
                start_time = timer()
                freq_pairs, res_a_priori = a_priori(chunk, threshold)
                runtime_ms = (timer() - start_time) * 1000

                RUNTIME_RESULTS[t_d][p_d][alg_name] = min(RUNTIME_RESULTS[t_d][p_d][alg_name], runtime_ms) if RUNTIME_RESULTS[t_d][p_d][alg_name] != 0 else runtime_ms
                CANDIDATE_COUNTS[t_d][p_d][alg_name] = len(res_a_priori)
                FREQUENCY_COUNTS[t_d][p_d] = len(freq_pairs)

                # PCY
                alg_name = 'PCY'

                hash_n = get_next_prime(len(chunk_set))         # Next prime of total number of unique singletons
                # hash_n = get_next_prime(max(chunk_set))         # Next prime of largest unique singleton
                # hash_n = get_next_prime(len(chunk_set) // 4)    # Next prime of quarter of total number of unique singletons
                # hash_n = get_next_prime(median(chunk_set))      # Next prime of median of unique singletons

                start_time = timer()
                res_single_pcy = single_pcy(chunk, threshold, hash_f1)
                runtime_ms = (timer() - start_time) * 1000

                RUNTIME_RESULTS[t_d][p_d][alg_name] = min(RUNTIME_RESULTS[t_d][p_d][alg_name], runtime_ms) if RUNTIME_RESULTS[t_d][p_d][alg_name] != 0 else runtime_ms
                CANDIDATE_COUNTS[t_d][p_d][alg_name] = len(res_single_pcy)

                # Multistage PCY
                alg_name = 'Multistage PCY'

                start_time = timer()
                res_multistage_pcy = multistage_pcy(chunk, threshold, hash_f1, hash_f2)
                runtime_ms = (timer() - start_time) * 1000

                RUNTIME_RESULTS[t_d][p_d][alg_name] = min(RUNTIME_RESULTS[t_d][p_d][alg_name], runtime_ms) if RUNTIME_RESULTS[t_d][p_d][alg_name] != 0 else runtime_ms
                CANDIDATE_COUNTS[t_d][p_d][alg_name] = len(res_multistage_pcy)

                # Multihash PCY
                alg_name = 'Multihash PCY'

                hash_n = get_next_prime(len(chunk_set) // 2)        # Next prime of half of total number of unique singletons
                # hash_n = get_next_prime(max(chunk_set) // 2)        # Next prime of half of largest unique singleton
                # hash_n = get_next_prime(len(chunk_set) // (2 * 4))  # Next prime of half of quarter of total number of unique singletons
                # hash_n = get_next_prime(median(chunk_set) // 2)     # Next prime of median of unique singletons

                start_time = timer()
                res_multihash_pcy = multihash_pcy(chunk, threshold, hash_f1, hash_f2)
                runtime_ms = (timer() - start_time) * 1000

                RUNTIME_RESULTS[t_d][p_d][alg_name] = min(RUNTIME_RESULTS[t_d][p_d][alg_name], runtime_ms) if RUNTIME_RESULTS[t_d][p_d][alg_name] != 0 else runtime_ms
                CANDIDATE_COUNTS[t_d][p_d][alg_name] = len(res_multihash_pcy)

        print(' done!', flush=True)

    # for t_d in SUPPORT_THRESHOLDS:
        y_matrix = [[runtime_result[algorithm_name] for runtime_result in RUNTIME_RESULTS[t_d].values()] for algorithm_name in ALGORITHM_NAMES]
        # Same as:
        # y1 = [runtime_result['A-Priori']          for runtime_result in RUNTIME_RESULTS[t_d].values()]
        # y2 = [runtime_result['PCY']               for runtime_result in RUNTIME_RESULTS[t_d].values()]
        # y3 = [runtime_result['Multistage PCY']    for runtime_result in RUNTIME_RESULTS[t_d].values()]
        # y4 = [runtime_result['Multihash PCY']     for runtime_result in RUNTIME_RESULTS[t_d].values()]

        freq_result = list(FREQUENCY_COUNTS[t_d].values())
        z_matrix = [freq_result] + [ [length_result[algorithm_name] for length_result in CANDIDATE_COUNTS[t_d].values()] for algorithm_name in ALGORITHM_NAMES]

        x = [f'{d}%' for d in DATASET_PERCENT_DECIMALS]

        # Create a line chart
        fig = plt.figure(figsize=(8, 10))
        ax_main = fig.add_subplot(111)
        axs = [ax_main.twiny() for _ in range(5)]

        for y, a in zip(y_matrix, ALGORITHM_NAMES):
            ax_main.plot(x, y, marker='o', linestyle='-', label=a)

        pos = -0.125
        for m, (ax, z, title) in enumerate(zip(axs, z_matrix, ['Frequent Pairs'] + ALGORITHM_NAMES), 1):
            # Move twinned axis ticks and label from top to bottom
            ax.xaxis.set_ticks_position("bottom")
            ax.xaxis.set_label_position("bottom")

            # Offset the twin axis below the host
            ax.spines["bottom"].set_position(("axes", pos * m))

            # Turn on the frame for the twin axis, but then hide all 
            # but the bottom spine
            ax.set_frame_on(True)
            ax.patch.set_visible(False)

            for sp in ax.spines.values():
                sp.set_visible(False)
            ax.spines["bottom"].set_visible(True)

            ax.set_xticks([i for i, _ in enumerate(DATASET_PERCENT_DECIMALS)])
            ax.set_xticklabels(z)
            ax.set_xlabel(title if title == 'Frequent Pairs' else f'Candidate Pairs ({title})')
            ax.set_xlim(ax_main.get_xlim())
            
        # Add title and labels
        ax_main.set_title(f'Support Threshold: {t_d}%')
        ax_main.set_xlabel('Dataset Size')
        ax_main.set_ylabel('Run time (ms)')
        ax_main.legend()
        ax_main.grid(True)

        fig.tight_layout()
        fig.subplots_adjust(bottom=0.41, left=0.09)

        # Save the plot
        plt.savefig(f'./plots/{t_d}.png')

    return

main()