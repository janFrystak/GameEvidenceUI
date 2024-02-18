import java.util.Comparator;

class SortByName implements Comparator<BoardGame>
{

    public int compare(BoardGame a, BoardGame b)
    {
        return a.getName().compareTo(b.getName());
    }
}
