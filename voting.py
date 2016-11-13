def voting(array):
    restaurants = []
    bordaScores = []
    
    for x in range(0,len(array[0])):
        num = len(array) - x - 1
        restaurants.append(array[0][x])
        bordaScores.append(0)

    for x in array: #goes through each user
        for y in x: #goes through the order the user inputted
            for z in restaurants: #goes through the order the user inputted
                if y == z:
                    bordaScores[restaurants.index(z)] = bordaScores[restaurants.index(z)] + len(array[0]) - x.index(z) - 1

    for x in restaurants:
         print x, bordaScores[restaurants.index(x)]

    max = bordaScores[0]
    maxIndex = 0
    for x in bordaScores:
        if x > max:
            max = x
            maxIndex = bordaScores.index(x)

    return restaurants[maxIndex]

testArray = [['Subways', 'McDonalds', 'Popeyes'],['Subways', 'Popeyes', 'McDonalds'],['Popeyes', 'Subways', 'McDonalds']]
result = voting(testArray)
print 'The highest ranked restaurant is', result
