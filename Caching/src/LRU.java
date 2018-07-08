import java.util.HashMap;
import java.util.Iterator;

public class LRU<K, V> implements Iterable<K>
{
    private class Node
    {
        Node pre;
        Node next;
        K k;
        V v;

        public Node(K k, V v)
        {
            this.k = k;
            this.v = v;
        }
    }

    private Node head;
    private Node tail;

    private HashMap<K, Node> map;

    private int maxSize;

    public LRU(int maxSize)
    {
        this.maxSize = maxSize;
        this.map = new HashMap<>(maxSize * 4 / 3);
        head = new Node(null, null);
        tail = new Node(null, null);
        head.next = tail;
        tail.pre = head;
    }

    public V get(K key)
    {
        if (!map.containsKey(key)) {
            return null;
        }

        Node node = map.get(key);
        unlink(node);
        appendHead(node);

        return node.v;
    }

    public void put(K key, V value)
    {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            unlink(node);
        }

        Node node = new Node(key, value);
        map.put(key, node);
        appendHead(node);

        if (map.size() > maxSize) {
            Node toRemove = removeTail();
            map.remove(toRemove);
        }
    }

    private void unlink(Node node)
    {
        Node pre = node.pre;
        node.pre = node.next;
        node.next = pre;
    }

    private void appendHead(Node node)
    {
        node.next = head.next;
        head.next = node;
    }

    private Node removeTail()
    {
        Node node = tail.pre;
        node.pre = tail;
        return node;
    }

    @Override
    public Iterator<K> iterator()
    {
        return new Iterator<K>()
        {
            private Node cur = head.next;

            @Override
            public boolean hasNext()
            {
                return cur != tail;
            }

            @Override
            public K next()
            {
                Node node = cur;
                cur = cur.next;
                return node.k;
            }
        };
    }

}
