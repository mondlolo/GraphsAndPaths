/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package Graphs;

import java.io.FileReader;
import java.io.IOException;
import java.util.Queue;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;
//import sun.security.provider.certpath.Vertex;

public class Unweighted {

// Represents an entry in the priority queue for Dijkstra's algorithm.
    public static final double INFINITY = Double.MAX_VALUE;
    private Map<String,Vertex> vertexMap = new HashMap<String,Vertex>( );

    /**
     * Add a new edge to the graph.
     */
    public void addEdge( String sourceName, String destName, double cost)
    {
        Vertex v = getVertex( sourceName );
        Vertex w = getVertex( destName );
        v.adj.add( new Edge(w,cost));
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
            throw new NoSuchElementException( "Destination vertex not found" );
        else if( w.dist == INFINITY )
            System.out.println( destName + " is unreachable" );
        else
        {
            System.out.print( "(Cost is: " + w.dist + ") " );
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
        if( dest.prev != null )
        {
            printPath( dest.prev );
            System.out.print( " to " );
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
     * Single-source weighted shortest-path algorithm.
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
    public static void main( String [ ] args )
    {
        Unweighted g = new Unweighted( );
        try
        {
        	FileReader fin1 = new FileReader("Graph4.txt");
            Scanner graphFile = new Scanner( fin1 );

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
                  { System.err.println( "Skipping ill-formatted line " + line ); }
             }
         }
         catch( IOException e )
           { System.err.println( e ); }

         System.out.println( "File read..." );
         Scanner in = new Scanner( System.in );
         System.out.print( "Enter start node:" );
         String startName = in.nextLine( );
         System.out.print( "Enter destination node:" );
         String destName = in.nextLine( );
         g.unweighted(startName);
         g.printPath(destName);
    }
}
