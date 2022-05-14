package sk.bytecode.bludisko.rt.game.util;

import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
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

    public static <T, U> void accept(Optional<T> optionalT, Optional<U> optionalU, BiConsumer<T, U> biConsumer) {
        if(optionalT.isPresent() && optionalU.isPresent()) {
            biConsumer.accept(optionalT.get(), optionalU.get());
        }
    }

    public static <T, U, R> Optional<R> apply(Optional<T> optionalT, Optional<U> optionalU, BiFunction<T, U, R> biFunction) {
        if(optionalT.isPresent() && optionalU.isPresent()) {
            return Optional.of(biFunction.apply(optionalT.get(), optionalU.get()));
        }
        return Optional.empty();
    }

}
