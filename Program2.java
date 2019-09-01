//Hyder Shad
//hs25796
//Program2.java
//EE360C - Julien
//4/2/2019
//tested on JavaSE 1.8

public class Program2 {

    public int constructIntensityGraph(int[][] image){	//returns sum of all edges in the image
    	
    	int rowmax = image.length;		//get row colum lengths for index boundaries
    	int colmax = image[0].length;
    	int row = 0;					//row/col indexes
    	int col = 0;
    	int edge_sum = 0;				//return value for sum of all the edges
    	int temp = 0;
    	if((rowmax<2)&&(colmax<2)) {return edge_sum;} //single pixel or empty
    	
    	for(row = 0; row<rowmax; row++) {
    		for(col = 0; col<colmax;col++) {
    			if(row>0) {							//if row>0, add the edge length to the vertex/pixel directly above
    				temp = abs(image[row][col], image[row-1][col]); 
    				edge_sum = edge_sum+temp;
    			}
    			if(col>0) {			//if col>0, add the edge length to the vertex/pixel directly to the left
    				temp = abs(image[row][col], image[row][col-1]); 
    				edge_sum = edge_sum+temp;
    			}
    		}
    		
    	}
        return edge_sum;	//return the sum of all edge weights in the image
    }

   
    public int constructPrunedGraph(int[][] image){		//returns sum of all edges in minimum spanning tree for the image
    	int row = 0;				//row/col indexes
    	int col = 0;
    	int edge_sum = 0;			//sum of all edge weights in the MST
    	int up, dw, lf, rt = 0;		//edge weight in each direction from the current vertex to the adjacent ones
    	int rowmax = image.length;	//get row colum lengths for index boundaries
    	int colmax = image[0].length;
    	
    	int [][] mstSet = new int[rowmax][colmax];	//create a MST matrix to keep track of all the pixels/vertices that have been added to the minimum spanning tree

    	for(row = 0; row<rowmax; row++) {
    		for(col = 0; col<colmax;col++) {
    			mstSet[row][col]=0;		//initialize MST so that no vertices have been included/checked yet
    		}
    	}
    	mstSet[0][0]=1;	//add the origin as the first pixel to check the minimum edges for
    	
    	row = 0;	    		//start at (0,0)
    	col = 0;
    	int done=0;				//when MST contains all vertices (every pixel is in the tree), done will be set to 1 and while loop will exit
    	int direction = 0;		//direction used to determine which non-MST neighboring pixel had the minimum edge weight
    	int minedge = 888888;	//initialize minedge to large value but smaller than the invalid edge values
    	int minrow = 0;			//row/col position of the vertex with the minimum edge so the MST can be updated to include it
    	int mincol = 0;	

    	while(done==0) {		//while MST incomplete/not all vertexes added
    		minedge = 888888;	//initialize minedge for each search of a new vertex to add
    		done = 1;			//done is set to zero if a vertex is found that is still not in the MST
        	int analyze = 0;	//when a new minedge out is found, update the min edge and MST of the new vertex to be added
    		
        	//start a search for the next vertex to add to MST that has the smallest edge weight
        	for(row = 0; row<rowmax; row++) {	//index through entire MST matrix
				for(col=0; col<colmax; col++) {
					if(mstSet[row][col]==0) {	//if vertex is not in MST, do not do edge analysis, reset done variable because the MST is not complete yet
						done = 0;
					}
					else {		//if vertex is in current MST
	    				//find all neighboring edges to vertices that are not in the MST
	    				
	    				if( (!(row==rowmax-1)) && (mstSet[row+1][col] ==0) ) {dw = abs(image[row][col], image[row+1][col]);} //check if there is an adjacent vertex in this direction and if the vertex is in MST or not
						else {dw = 999999;}	//neighbor already in MST, set to an invalid edge value, aka super large
						
						if( (!(row==0)) && (mstSet[row-1][col] ==0) ) {up = abs(image[row][col], image[row-1][col]);}
						else {up = 999999;} 
	
						if( (!(col==colmax-1)) && (mstSet[row][col+1] ==0) ) {rt = abs(image[row][col], image[row][col+1]);}	
						else {rt = 999999;} 
	
						if( (!(col==0)) && (mstSet[row][col-1] ==0) ) {lf = abs(image[row][col], image[row][col-1]);}
						else {lf = 999999;} 
						
						if( !((dw==999999) && (up==999999) && (rt==999999) && (lf==999999))) {	//if there is at least one adjacent vertex not in MST
							
							analyze = 1;	//new vertex was found with an edge, see if it is the minimum edge weight of all the verticies not in MST so far in this search
							direction = smallestdge(dw, up, rt, lf);	//1= dw, 2=up, 3=rt, 4=lf
							
							switch(direction) {	//based on direction, update minedge with the correct edge weight and location of new vertex to be added to the MST
								case 1: 
									if(minedge>dw) {
										minedge = dw;		//edge weight
										minrow = row+1;		//position of new vertex to add relative to the current MST vertex
										mincol = col;
									}
									break;
								case 2:
									if(minedge>up) {
										minedge = up;		
										minrow = row-1;		
										mincol = col;
									}
									break;
								case 3:
									if(minedge>rt) {
										minedge = rt;
										minrow = row;
										mincol = col+1;
									}
									break;
								case 4:
									if(minedge>lf) {
										minedge = lf;
										minrow = row;
										mincol = col-1;
									}
									break;
								default: break;
							}
							
						}
	    			}
				}
    		}
    		if(analyze !=0) {	//DO NOT UPDATE IF ALL VERTEXES ARE IN MST, otherwise a vertex was added 
    							
    			edge_sum = edge_sum + minedge;  //add the new edge length and put the vertex in the MST
    			mstSet[minrow][mincol] = 1;		//set this location in MST matrix to 1 to indicate it is included in the minimum spanning tree
    		}
    		//return to start a new search if MST matrix not completely filled yet
    	}
    	
        return edge_sum;		//return the sum of the edge weights used to construct the minimum spanning tree
    }
    
    private int abs(int a, int b) {	//helper function to return absolute value difference between two pixel intensities aka edge length
    	int diff = a - b;
    	if(diff<0) {diff = -diff;} 	//flip sign of result if negative
    	return diff;				//return the edge length
    }

    private int smallestdge(int dw, int up, int rt, int lf) {		//helper function to return the location of the adjacent pixel not in the MST that has the 
    															//shortest edge to the current pixel/vertex in the MST
    	
    	//if lengths are equal just pick any (hence the '<=')
	    if(dw<=up) {
	    	if(dw<=rt) {
	    		if(dw<=lf) {return 1;}		//return the direction value for down
	    	}
	    }
	    if(up<=dw) {
	    	if(up<=rt) {
	    		if(up<=lf) {return 2;}		//up is shortest edge length
	    	}
	    }
	    if(rt<=up) {
	    	if(rt<=dw) {
	    		if(rt<=lf) {return 3;}		//right
	    	}
	    }
	    if(lf<=up) {
	    	if(lf<=rt) {
	    		if(lf<=dw) {return 4;}		//left
	    	}
	    }
	    return 0;		//default
    }
}


