
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.StringTokenizer;



// Graph class: evaluate shortest paths.
//
// CONSTRUCTION: with no parameters.
//
// ******************PUBLIC OPERATIONS**********************
// void addEdge( String v, String w, double cvw )
//                              --> Add additional edge
// void printPath( String w )   --> Print path after alg is run
// void unweighted( String s )  --> Single-source unweighted
// void dijkstra( String s )    --> Single-source weighted
// void negative( String s )    --> Single-source negative weighted
// void acyclic( String s )     --> Single-source acyclic
// ******************ERRORS*********************************
// Some error checking is performed to make sure graph is ok,
// and to make sure graph satisfies properties needed by each
// algorithm.  Exceptions are thrown if errors are detected.

public class Graph
{
    public static final double INFINITY = Double.MAX_VALUE;
    public Map<String,Vertex> vertexMap = new HashMap<String,Vertex>( );

    /**
     * Add a new edge to the graph.
     */
    public void addEdge( String sourceName, String destName, double cost )
    {
        Vertex v = getVertex( sourceName );
        Vertex w = getVertex( destName );
        v.adj.add( new Edge( w, cost ) );
    }

    /**
     * Driver routine to handle unreachables and print total cost.
     * It calls recursive routine to print shortest path to
     * destNode after a shortest path algorithm has run.
     */
    public void printPath( String destName )
    {
        Vertex w = vertexMap.get( destName );
        if( w == null )
            throw new NoSuchElementException( "Client cannot be helped" );
        else if( w.dist == INFINITY )
            System.out.println( destName + "Client cannot be helped" );
        else if(w.dist ==0){
            System.out.println( destName + "Client cannot be helped" );
        }else{

            //System.out.print( "(Cost is: " + w.dist + ") " );
            printPath( w );
            System.out.println( );
        }
    }

    /**
     * If vertexName is not present, add it to vertexMap.
     * In either case, return the Vertex.
     */
    private Vertex getVertex( String vertexName )
    {
        Vertex v = vertexMap.get( vertexName );
        if( v == null )
        {
            v = new Vertex( vertexName );
            vertexMap.put( vertexName, v );
        }
        return v;
    }

    /**
     * Recursive routine to print shortest path to dest
     * after running shortest path algorithm. The path
     * is known to exist.
     */
    private void printPath( Vertex dest )
    {
    //  Path a = new Path(dest, dest.dist);
        if( dest.prev != null )
        {
            printPath( dest.prev );
            System.out.print( " " );
            //System.out.print(a.compareTo(dest.prev));
        }
        System.out.print( dest.name );
    }

    /**
     * Initializes the vertex output info prior to running
     * any shortest path algorithm.
     */
    private void clearAll( )
    {
        for( Vertex v : vertexMap.values( ) )
            v.reset( );
    }

    /**
     * Single-source unweighted shortest-path algorithm.
     */
    public void unweighted( String startName )
    {
        clearAll( );

        Vertex start = vertexMap.get( startName );
        if( start == null )
            throw new NoSuchElementException( "Start vertex not found" );

        Queue<Vertex> q = new LinkedList<Vertex>( );
        q.add( start ); start.dist = 0;

        while( !q.isEmpty( ) )
        {
            Vertex v = q.remove( );

            for( Edge e : v.adj )
            {
                Vertex w = e.dest;
                if( w.dist == INFINITY )
                {
                    w.dist = v.dist + 1;
                    w.prev = v;
                    q.add( w );
                }
            }
        }
    }

    /**
     * Single-source weighted shortest-path algorithm. (Dijkstra)
     * using priority queues based on the binary heap
     */
    public void dijkstra( String startName )
    {
        PriorityQueue<Path> pq = new PriorityQueue<Path>( );
        //Path x = new Path();

        Vertex start = vertexMap.get( startName );
        if( start == null )
            throw new NoSuchElementException( "Start vertex not found" );

        clearAll( );
        Path x =  new Path( start, 0 );
        pq.add( x ); start.dist = 0;
      //  x.toString(x);


        int nodesSeen = 0;
        while( !pq.isEmpty( ) && nodesSeen < vertexMap.size( ) )
        {
            Path vrec = pq.remove( );
            Vertex v = vrec.dest;
            if( v.scratch != 0 )  // already processed v
                continue;

            v.scratch = 1;
            nodesSeen++;

            for( Edge e : v.adj )
            {
                Vertex w = e.dest;
                double cvw = e.cost;

                if( cvw < 0 )
                    throw new GraphException( "Graph has negative edges" );

                if( w.dist > v.dist + cvw )
                {
                    w.dist = v.dist +cvw;
                    w.prev = v;
                    //System.out.println(w.dist + " " + w);
                    pq.add( new Path( w, w.dist ) );
                    x.compareTo(new Path( w, w.dist));
                }
            }
        }
    }

    /**
     * Single-source negative-weighted shortest-path algorithm.
     * Bellman-Ford Algorithm
     */
    public void negative( String startName )
    {
        clearAll( );

        Vertex start = vertexMap.get( startName );
        if( start == null )
            throw new NoSuchElementException( "Start vertex not found" );

        Queue<Vertex> q = new LinkedList<Vertex>( );
        q.add( start ); start.dist = 0; start.scratch++;

        while( !q.isEmpty( ) )
        {
            Vertex v = q.remove( );
            if( v.scratch++ > 2 * vertexMap.size( ) )
                throw new GraphException( "Negative cycle detected" );

            for( Edge e : v.adj )
            {
                Vertex w = e.dest;
                double cvw = e.cost;

                if( w.dist > v.dist + cvw )
                {
                    w.dist = v.dist + cvw;
                    w.prev = v;
                      // Enqueue only if not already on the queue
                    if( w.scratch++ % 2 == 0 )
                        q.add( w );
                    else
                        w.scratch--;  // undo the enqueue increment
                }
            }
        }
    }

    /**
     * Single-source negative-weighted acyclic-graph shortest-path algorithm.
     */
    public void acyclic( String startName )
    {
        Vertex start = vertexMap.get( startName );
        if( start == null )
            throw new NoSuchElementException( "Start vertex not found" );

        clearAll( );
        Queue<Vertex> q = new LinkedList<Vertex>( );
        start.dist = 0;

          // Compute the indegrees
		Collection<Vertex> vertexSet = vertexMap.values( );
        for( Vertex v : vertexSet )
            for( Edge e : v.adj )
                e.dest.scratch++;

          // Enqueue vertices of indegree zero
        for( Vertex v : vertexSet )
            if( v.scratch == 0 )
                q.add( v );

        int iterations;
        for( iterations = 0; !q.isEmpty( ); iterations++ )
        {
            Vertex v = q.remove( );

            for( Edge e : v.adj )
            {
                Vertex w = e.dest;
                double cvw = e.cost;

                if( --w.scratch == 0 )
                    q.add( w );

                if( v.dist == INFINITY )
                    continue;

                if( w.dist > v.dist + cvw )
                {
                    w.dist = v.dist + cvw;
                    w.prev = v;
                }
            }
        }

        if( iterations != vertexMap.size( ) )
            throw new GraphException( "Graph has a cycle!" );
    }

    /**
     * Process a request; return false if end of file.
     */
    public static boolean processRequest( Scanner in, Graph g )
    {
        try
        {
            System.out.print( "client " );
            String startName = in.nextLine( );

            System.out.print( "taxi " );
            String taxi = in.nextLine( );

            g.dijkstra( taxi );
            g.printPath( startName );

            System.out.print( "shop " );
            String shop = in.nextLine( );

            g.dijkstra( startName );
            g.printPath( shop );


        }
        catch( NoSuchElementException e )
          { return false; }
        catch( GraphException e )
          { System.err.println( e ); }
        return true;
    }

    /**
     * A main routine that:
     * 1. Reads a file containing edges (supplied as a command-line parameter);
     * 2. Forms the graph;
     * 3. Repeatedly prompts for two vertices and
     *    runs the shortest path algorithm.
     * The data file is a sequence of lines of the format
     *    source destination cost
     */

    public static void main( String [] args )
    {
        Graph g = new Graph( );
      //  Path a = new Path();

        try
        {
            //FileReader fin = new FileReader(args[0]);
        	FileReader fin = new FileReader("Graph3.txt");
            Scanner graphFile = new Scanner( fin );

            // Read the edges and insert
            String line;
            while( graphFile.hasNextLine( ) )
            {
                line = graphFile.nextLine( );
                String[] segment = line.split(" ");
                StringTokenizer st = new StringTokenizer( line );

                try
                {
                    if( st.countTokens( ) == 1 || st.countTokens( )%2 == 0)
                    {
                    //    System.err.println( "Skipping ill-formatted line " + line );
                        continue;
                    }else{
                         String source;
                         String dest;
                         int cost;
                        for(int t = 0;t < st.countTokens()-2;t+=2){
                             //ource  = st.tokenNumber(0);
                             if(t%2==0){
                               //source  = st.tokenNumber(0);
                               //dest = st.tokenNumber(t+1);
                               //cost = Integer.parseInt(st.tokenNumber(t+2) );
                               source = segment[0];
                               dest = segment[t+1];
                               cost = Integer.parseInt(segment[t+2]);
                               g.addEdge( source, dest, cost );

                             }else{

                             }
                        }

                             //g.addEdge( source, dest, cost );


                    }
                    //String source  = st.nextToken( );
                    //String dest    = st.nextToken( );
                    //int    cost    = Integer.parseInt( st.nextToken( ) );
                    //g.addEdge( source, dest, cost );
                }
                catch( NumberFormatException e )
                  { //System.err.println( "Skipping ill-formatted line " + line ); }
                }
             }
         }
         catch( IOException e )
           { System.err.println( e ); }

         //System.out.println( "File read..." );
      //   System.out.println( g.vertexMap.size( ) + " vertices" );

         Scanner in = new Scanner( System.in );
         while( processRequest( in, g ) )
             ;
    }
}
