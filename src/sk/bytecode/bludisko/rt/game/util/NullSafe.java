package sk.bytecode.bludisko.rt.game.util;

import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.function.Consumer;

public final class NullSafe {

    public static <T> void accept(@Nullable T nullable, Consumer<T> consumer) {
        if(nullable != null) {
            consumer.accept(nullable);
        }
    }

    public static <T> void acceptWeak(@Nullable WeakReference<T> weakNullable, Consumer<T> consumer) {
        if(weakNullable != null) {
            var nullable = weakNullable.get();
            if(nullable != null) {
                consumer.accept(nullable);
            }
        }
    }

}
