import axelrod as axl
from axelrod.action import Action, actions_to_str
from axelrod.player import Player
from axelrod.strategy_transformers import (
    FinalTransformer,
    TrackHistoryTransformer,
)
import random
import secrets
import pandas as pd
import openpyxl
import time

def main():
    C, D = Action.C, Action.D

    ITERATIONS = [1000, 10000]  # Use ITERATIONS[0] for now; ITERATIONS is the number of times we cycle between search method and playing against TFT
    TURNS = 100                 # Number of turns for the match between search method strategy vs TFT
    DEPTH = [3, 4, 5]           # Use DEPTH[0] for now
    
    GENERATION = ITERATIONS[0]
    POPULATION_SIZE = [100, 1000, 10000]

    # ga = GeneticAlgorithm(population_size=POPULATION_SIZE[0], memory=DEPTH[0], sorted=False, reincarnate=False, eliminate=0, versus_TFT=False, versus_Defector=False)

    # print("--------------------")
    # print(f"Size: {ga.population_size}")
    # ga.evaluate()
    # print(ga.leaderboard)
    # print(ga.population[ga.leaderboard[0][0]].moveProbabilities())
    # print(ga.results.ranking)
    # print(ga.getFirst().name)
    # print(ga.getSecond().name)
    # print(ga.getThird().name)
    # print("--------------------")
    # for gen in range(GENERATION-1):
    #     if not ga.evaluate():
    #         break

    # print("--------------------")
    # print(ga.leaderboard)
    # print(ga.population[ga.leaderboard[0][0]].moveProbabilities())
    # print(ga.results.ranking)
    # print(ga.getFirst().name)
    # print(ga.getSecond().name)
    # print(ga.getThird().name)
    # print("--------------------")

    individual_count = 5
    
    ga_list1 = list(GeneticAlgorithm(population_size=POPULATION_SIZE[0], memory=DEPTH[0], sorted=True, reincarnate=False, eliminate=1, versus_TFT=False, versus_Defector=False) for i in range(individual_count))
    ga_list2 = list(GeneticAlgorithm(population_size=POPULATION_SIZE[0], memory=DEPTH[0], sorted=True, reincarnate=False, eliminate=1, versus_TFT=True, versus_Defector=False) for i in range(individual_count))
    ga_list3 = list(GeneticAlgorithm(population_size=POPULATION_SIZE[0], memory=DEPTH[0], sorted=True, reincarnate=False, eliminate=1, versus_TFT=True, versus_Defector=True) for i in range(individual_count))

    start1 = time.time()
    for i, ga in enumerate(ga_list1):
        print(f"Eliminate {i}")
        for gen in range(GENERATION):
            if not ga.evaluate():
                break
    end1 = time.time()

    start2 = time.time()
    for i, ga in enumerate(ga_list2):
        print(f"TFT {i}")
        for gen in range(GENERATION):
            if not ga.evaluate():
                break
    end2 = time.time()

    start3 = time.time()
    for i, ga in enumerate(ga_list3):
        print(f"Def {i}")
        for gen in range(GENERATION):
            if not ga.evaluate():
                break
    end3 = time.time()
    # data = ga_1.leaderboard.copy()
    # for x in data:
    #     x.extend(ga_1.population[x[0]].moveCount())
    # df_1 = pd.DataFrame(data, columns=["Index", "Score", "Cooperates", "Defects", "C/D Ratio"])

    individuals_list1 = list(ga.population[0] for ga in ga_list1)
    individuals_list2 = list(ga.population[0] for ga in ga_list2)
    individuals_list3 = list(ga.population[0] for ga in ga_list3)

    print("Eliminate Only Strategies")
    for i in individuals_list1:
        print(i.strategy_list)
    
    print("Eliminate & Versus TFT Strategies")
    for i in individuals_list2:
        print(i.strategy_list)

    print("Eliminate & Versus TFT & Versus Defector Strategies")
    for i in individuals_list3:
        print(i.strategy_list)

    vs_Ran_list1 = list(axl.Match((i, axl.Random()), turns=100) for i in individuals_list1)
    vs_Ran_list2 = list(axl.Match((i, axl.Random()), turns=100) for i in individuals_list2)
    vs_Ran_list3 = list(axl.Match((i, axl.Random()), turns=100) for i in individuals_list3)

    for match in vs_Ran_list1:
        match.play()

    for match in vs_Ran_list2:
        match.play()

    for match in vs_Ran_list3:
        match.play()

    #vs_Def_list = list(axl.Match((i, axl.Defector()), turns=100) for i in individuals_list)

    #for match in vs_Def_list:
    #    match.play()

    move_counts1 = list(individual.moveCount() for individual in individuals_list1)
    move_counts2 = list(individual.moveCount() for individual in individuals_list2)
    move_counts3 = list(individual.moveCount() for individual in individuals_list3)

    data_Ran1 = list(list(vs_Ran_list1[i].final_score()) + move_counts1[i] for i in range(individual_count))
    data_Ran2 = list(list(vs_Ran_list2[i].final_score()) + move_counts2[i] for i in range(individual_count))
    data_Ran3 = list(list(vs_Ran_list3[i].final_score()) + move_counts3[i] for i in range(individual_count))
    #data_Def = list(list(vs_Def_list[i].final_score()) + move_counts[i] for i in range(individual_count))

    df_1 = pd.DataFrame(data_Ran1, columns=["Player Score", "Random Score", "Cooperates", "Defects", "C/D Ratio"])
    df_2 = pd.DataFrame(data_Ran2, columns=["Player Score", "Random Score", "Cooperates", "Defects", "C/D Ratio"])
    df_3 = pd.DataFrame(data_Ran3, columns=["Player Score", "Random Score", "Cooperates", "Defects", "C/D Ratio"])
    #df_2 = pd.DataFrame(data_Def, columns=["Player Score", "Defector Score", "Cooperates", "Defects", "C/D Ratio"])


    # for gen in range(GENERATION):
    #     if not ga_2.evaluate():
    #         break
    # df_2 = pd.DataFrame(ga_2.leaderboard, columns=["Index", "Score"])

    # for gen in range(GENERATION):
    #     if not ga_3.evaluate():
    #         break
    # df_3 = pd.DataFrame(ga_3.leaderboard, columns=["Index", "Score"])


    path = "Multiple Versus Random.xlsx"
    with pd.ExcelWriter(path, mode='a') as writer:
        df_1.to_excel(writer, sheet_name="Eliminate 4")
        df_2.to_excel(writer, sheet_name="Versus TFT 4")
        df_3.to_excel(writer, sheet_name="Versus Defector 4")

    print("Eliminate Time")
    print(time.ctime(start1))
    print(time.ctime(end1))
    print()
    print("Versus TFT Time")
    print(time.ctime(start2))
    print(time.ctime(end2))
    print()
    print("Versus Defector Time")
    print(time.ctime(start3))
    print(time.ctime(end3))
    # path = "Random.xlsx"
    # with pd.ExcelWriter(path, mode='a') as writer:
    #     df_1.to_excel(writer, sheet_name="Round 3")
    #     df_2.to_excel(writer, sheet_name="Round 4")
    #     df_3.to_excel(writer, sheet_name="Round 5")
        # df_4.to_excel(writer, sheet_name="Round 5")

    # future work:  Take first three best strategies from n different instances of GeneticAlgorithm() and run their respective evaluation and crossover
    #               Mix and combine GA with other search methods

# @misc{axelrodproject,
#   author       = {{ {The Axelrod project developers} }},
#   title        = {Axelrod: <RELEASE TITLE>},
#   month        = apr,
#   year         = 2016,
#   doi          = {<DOI INFORMATION>},
#   url          = {http://dx.doi.org/10.5281/zenodo.<DOI NUMBER>}
# }

class Individual(Player):
    name = "Individual"
    classifier = {
        "memory_depth": 3,
        "stochastic": False,
        "long_run_time": False,
        "inspects_source": False,
        "manipulates_source": False,
        "manipulates_state": False,
    }

    def __init__(self, memory, id):
        super().__init__()
        self.name = f"Individual {id}"
        self.id = id
        self.memory = memory
        self.strategy_size = 4 ** memory
        self.strategy_list = list(secrets.choice([Action.C, Action.D]) for i in range(self.strategy_size))          # initial strategy is random
        # self.strategy_list = list(Action.C if i % 2 == 0 else Action.D for i in range(self.strategy_size))        # TFT as a strategy array

    def moveCount(self):
        c_count, d_count = 0.0, 0.0
        for i in self.strategy_list:
            if i == Action.C:
                c_count += 1 
            else:
                d_count += 1
        
        return [c_count, d_count, f"{c_count / self.strategy_size} : {d_count / self.strategy_size}"]

    def strategyTable(self):                                                    # print the table like in the slides
        offset = self.memory * 2
        i_header = "Index"
        b_header = format("Binary", '^' + str(offset))
        h_header = format("History", '^' + str(offset))
        n_m_header = "Next Move"
        header = f"| {i_header} | {b_header} | {h_header} | {n_m_header} |"
        print(header)
        print("-" * len(header))

        for index,move in enumerate(self.strategy_list):
            binary = format(index, '0' + str(offset) + 'b')                     # convert the index to a string of binary with leading zeros
            history = "".join("C" if i == '0' else "D" for i in binary)         # convert the binary into its history counterpart
            print(f"| {index:>5} | {binary} | {format(history, '>' + str(len(h_header)))} | {move:^9} |")    # print row

    def resetStrategy(self):
        self.strategy_list = list(secrets.choice([Action.C, Action.D]) for i in range(self.strategy_size))
        
    def nextMove(self, opponent):
        if len(self.history) < self.memory:
            return secrets.choice([Action.C, Action.D])

        fullHist = self.history[-self.memory:] + opponent.history[-self.memory:]      # combine together the last 3 moves of self and opponent
        decimal = 0                                                             # index of the array
        place = 0                                                               # place of the binary value starting from the right
        for i in fullHist[::-1]:                                                # iterates the full history backwards (from right to left)
            if i == Action.D:
                decimal += (2 ** place)                                         # D = 1, C = 0, so we only look at digits with D and add its decimal equivalent
            place += 1                                                          # moves to the next digit (right to left)

        return self.strategy_list[decimal]                                      # returns the next move

    def strategy(self, opponent: Player) -> Action:
        return self.nextMove(opponent)


class GeneticAlgorithm():
    def __init__(self, population_size, memory, sorted=False, reincarnate=False, eliminate=0, versus_TFT=False, versus_Defector=False):
        self.population_size = population_size
        self.chromosomes = 4 ** memory
        self.mutation_prob = 1.0 / self.chromosomes
        self.population = list(Individual(memory, i) for i in range(population_size))

        self.sorted = sorted
        self.leaderboard_initialized = False
        self.reincarnate = reincarnate
        self.eliminate = eliminate

        self.tournament = axl.Tournament(self.population, turns=1, repetitions=1)
        self.versus_TFT = versus_TFT
        self.versus_Defector = versus_Defector

        if versus_TFT:
            self.tft_list = list(axl.Match((p,axl.TitForTat()),turns=1) for p in self.population)
            self.tft_results = [0] * self.population_size

        if versus_Defector:
            self.defector_list = list(axl.Match((p,axl.Defector()),turns=1) for p in self.population)
            self.defector_results = [0] * self.population_size

    def reRank(self, result_tuple):
        index = result_tuple[0]
        for i in self.leaderboard:
            if i[0] > index:
                i[0] -= 1
        
    def evaluate(self):
        # Play matches
        self.results = self.tournament.play()
    
        if self.versus_TFT:
            for i,m in enumerate(self.tft_list):
                m.play()
                self.tft_results[i] = m.final_score()[0]

        if self.versus_Defector:
            for i,m in enumerate(self.defector_list):
                m.play()
                self.defector_results[i] = m.final_score()[0]

        # Save leaderboard
        if not self.leaderboard_initialized:
            if not self.sorted:                         # if the population is unsorted, only set up the leaderboard once as the leaderboard will get randomized
                self.leaderboard_initialized = True

            temp_scores = list(s[0] for s in self.results.scores)
            if self.versus_TFT and self.versus_Defector:                # both tft and defector
                initial_leaderboard = list(zip(list(i for i in range(self.population_size)), temp_scores, self.tft_results, self.defector_results))
            elif self.versus_TFT:                                       # only tft
                initial_leaderboard = list(zip(list(i for i in range(self.population_size)), temp_scores, self.tft_results))
            elif self.versus_Defector:                                  # only defector
                initial_leaderboard = list(zip(list(i for i in range(self.population_size)), temp_scores, self.defector_results))
            else:                                                       # neither
                initial_leaderboard = list(zip(list(i for i in range(self.population_size)), temp_scores))

            self.leaderboard = list(list(initial_leaderboard[i]) for i in range(self.population_size))   # convert tuples into lists

        # Sort or unsort data
        if self.population_size == 1 or (self.reincarnate and self.eliminate <= 0):
            return False               # leave at least 1 individual, or reincarnate but no count given
        
        if self.versus_TFT and self.versus_Defector:
            self.leaderboard.sort(reverse=True, key=lambda x:x[3])
            self.leaderboard.sort(reverse=True, key=lambda x:x[2])
            self.leaderboard.sort(reverse=True, key=lambda x:x[1])
        elif self.versus_TFT or self.versus_Defector:
            self.leaderboard.sort(reverse=True, key=lambda x:x[2])
            self.leaderboard.sort(reverse=True, key=lambda x:x[1])
        else:
            self.leaderboard.sort(reverse=True, key=lambda x:x[1])

        
        if self.reincarnate:     # reincarnate worst individuals
            for i in range(self.eliminate):
                self.population[self.leaderboard[-1][0]].resetStrategy()
        elif self.eliminate > 0:                        # no reincarnation, but eliminate worst individuals
            if self.population_size - self.eliminate < 1:
                for i in range(self.eliminate-1, 0, -1):
                    if self.population_size - i == 1:
                        self.eliminate = i
                        break
            
            self.population_size -= self.eliminate

            for i in range(self.eliminate):
                self.population.remove(self.population[self.leaderboard[-1][0]])
                remove_ranking = self.leaderboard[-1]
                self.leaderboard.remove(remove_ranking)
                self.reRank(remove_ranking)

                if self.versus_TFT:
                    self.tft_list.remove(self.tft_list[remove_ranking[0]])
                    self.tft_results.remove(self.tft_results[remove_ranking[0]])

                if self.versus_Defector:
                    self.defector_list.remove(self.defector_list[remove_ranking[0]])
                    self.defector_results.remove(self.defector_results[remove_ranking[0]])

        # Crossover & Mutate
        size = self.population_size
        if size % 2 != 0:                                   # if population size is odd, the last remaining will not cross over with anyone
            size -= 1

        if not self.sorted:                                 # randomize the index list for random pairing
            random.shuffle(self.results.ranking)

            for i in range(0, size, 2):                     # pair every 2 individuals in sequence (using the randomized list)
                individual_A, individual_B = self.population[self.results.ranking[i]], self.population[self.results.ranking[i+1]]
                self.crossOver(individual_A.strategy_list, individual_B.strategy_list)
        else:
            for i in range(0, size, 2):                     # pair every 2 individuals in sequence (using the sorted list)
                individual_A, individual_B = self.population[self.leaderboard[i][0]], self.population[self.leaderboard[i+1][0]]
                self.crossOver(individual_A.strategy_list, individual_B.strategy_list)

        return True
    
    def mutate(self):                                   # a chance of 1/strategy size of mutating
        return random.choices((True, False), (self.mutation_prob, 1-self.mutation_prob))[0]

    def crossOver(self, strategy_A, strategy_B):
        for i in range(self.chromosomes):
            if secrets.choice((True, False)):                                   # 50% chance to swap/crossover an element
                strategy_A[i], strategy_B[i] = strategy_B[i], strategy_A[i]
        
        if self.mutate():                                                       # see if strategy_A wants to mutate
            random_index = random.randint(0, self.chromosomes-1)
            strategy_A[random_index].flip()

        if self.mutate():                                                       # see if strategy_B wants to mutate
            random_index = random.randint(0, self.chromosomes-1)
            strategy_B[random_index].flip()
    
    def getFirst(self):
        return self.population[self.leaderboard[0][0]]
    
    def getSecond(self):
        return self.population[self.leaderboard[1][0]]
    
    def getThird(self):
        return self.population[self.leaderboard[2][0]]

main()