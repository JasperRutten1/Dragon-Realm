package dscp.dragon_realm.utils;

import lombok.Getter;
import org.apache.commons.lang.Validate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A GroupedList represents a list of lists, split every X indexes.
 * Often used to create pages in command or containers.
 */
public class GroupedList<T> implements Iterable<List<T>> {

    @Getter private List<List<T>> pages;

    /**
     * Construct a new GroupedList from the given list, and specified split
     *
     * @param list Source list
     * @param split Split amount
     */
    public GroupedList(List<T> list, int split) {
        Validate.isTrue(split > 0, "Split cannot be less than 1");

        this.pages = new ArrayList<>();

        for(int i = 0; i < list.size(); i++) {
            T obj = list.get(i);

            if(i % split == 0) {
                this.pages.add(new ArrayList<>());
            }

            //noinspection IntegerDivisionInFloatingPointContext
            int index = (int) (Math.ceil(i / split));

            pages.get(index).add(obj);
        }
    }

    /**
     * Returns the page count
     *
     * @return int
     */
    public int getGroupCount() {
        return pages.size();
    }

    /**
     * Return the amount of total entries
     *
     * @return int
     */
    public int getEntryCount() {
        return pages.stream().mapToInt(List::size).sum();

    }

    /**
     * Returns the page at the given index
     *
     * @param index The page
     * @return List
     */
    public List<T> getGroup(int index) {
        try {
            return pages.get(index);
        } catch(IndexOutOfBoundsException ex) {
            return null;
        }
    }

    /**
     * Get the iterator for this object
     *
     */
    @Override
    public Iterator<List<T>> iterator() {
        return pages.iterator();
    }
}
