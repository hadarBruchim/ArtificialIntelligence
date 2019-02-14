import java.util.Objects;

public class Pair2<T,S> {
private T key;
private S value;

public Pair2(T key1, S val){
    key=key1;
    value=val;
}

/**
 * @return the key
 */
public T getKey() {
    return key;
}

/**
 * @return the value
 */
public S getValue() {
    return value;
}

/**
 * @param key the key to set
 */
public void setKey(T key) {
    this.key = key;
}

/**
 * @param value the value to set
 */
public void setValue(S value) {
    this.value = value;
}

@Override
public int hashCode() {
    return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
}

@Override
public boolean equals(Object o) {
    if (!(o instanceof Pair2)) {
        return false;
    }
    Pair2<?, ?> p = (Pair2<?, ?>) o;
    return Objects.equals(p.key, key) && Objects.equals(p.value, value);
}
}
