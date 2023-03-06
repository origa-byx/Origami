package vtb.mashiro.kanon.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @by: origami
 * @date: {2022/5/16}
 * @info:
 **/
public class DefMap<K, V> extends ConcurrentHashMap<K, V> {

    private final V nullV;

    public DefMap() {
        this.nullV = null;
    }

    public DefMap(V nullV) {
        this.nullV = nullV;
    }

    @Override
    public V get(Object key) {
        V v = super.get(key);
        return v == null ? nullV : v;
    }
}
