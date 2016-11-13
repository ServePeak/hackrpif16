def voting(array):
    restaurants = []
    bordaScores = []
    
    for x in range(0,len(array[0])):
        restaurants.append(array[0][x])
        bordaScores.append(0)

    for x in array: #goes through each user
        for y in x: #goes through the order the user inputted
            for z in restaurants: #goes through the order the user inputted
                if y == z:
                    bordaScores[restaurants.index(z)] = bordaScores[restaurants.index(z)] + len(array[0]) - x.index(z)

    for x in restaurants:
         print x, bordaScores[restaurants.index(x)]

    max = bordaScores[0]
    maxIndex = 0
    for x in bordaScores:
        if x > max:
            max = x
            maxIndex = bordaScores.index(x)

    return restaurants[maxIndex]

def vote(array):
    pupper = {}
    for x in range(0,len(array)):
        for y in range(0, len(array[x])):
            print array[x][y]
            if array[x][y] in pupper.keys():
                pupper[array[x][y]] = pupper[array[x][y]] + len(array[y]) - y
            else:
                pupper[array[x][y]] = len(array[x]) - y
    return pupper



testArray = [['Subways', 'McDonalds', 'Popeyes'],['Subways', 'Popeyes', 'McDonalds'],['Popeyes', 'Subways', 'McDonalds']]
#result = voting(testArray)
#print 'The highest ranked restaurant is', result
result = vote(testArray)
print result
