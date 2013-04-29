fromNetID = 0.67
rejectMin = 0.99 * fromNetID
rejectLimit = rejectMin + 0.01

edgeFile = open("edges.txt")
output = open("filteredEdges.txt", "w")
removeEdges = open("removedEdges.txt" , "w")

edges = edgeFile.readlines()

def selectInputLine(x):
	if (x >= rejectMin) and (x < rejectLimit):
		return False
	else:
		return True

for edge in edges:
	fields = edge.split(" ")
	fields = filter(None, fields)
	edgeFilter = fields[2][:-1]
	if selectInputLine(float(edgeFilter)) == True:
		output.write(edge)
	else:
		removeEdges.write(edge)
