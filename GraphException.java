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


class GraphException extends RuntimeException
{
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public GraphException( String name )
    {
        super( name );
    }
}
